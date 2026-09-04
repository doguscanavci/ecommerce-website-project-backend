package com.bandage.ecommerce.service;

import com.bandage.ecommerce.dto.request.CreateOrderRequest;
import com.bandage.ecommerce.dto.response.OrderResponse;
import com.bandage.ecommerce.entity.*;
import com.bandage.ecommerce.exception.ApiException;
import com.bandage.ecommerce.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final AddressRepository addressRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    public List<OrderResponse> getUserOrders(Long userId) {
        return orderRepository.findByUserIdOrderByOrderDateDesc(userId).stream()
                .map(OrderResponse::from)
                .collect(Collectors.toList());
    }

    @Transactional
    public OrderResponse createOrder(Long userId, CreateOrderRequest request) {
        User user = userRepository.getReferenceById(userId);

        Address address = addressRepository.findById(request.getAddress_id())
                .orElseThrow(() -> new ApiException("Adres bulunamadı", HttpStatus.NOT_FOUND));

        if (!address.getUser().getId().equals(userId)) {
            throw new ApiException("Bu adres size ait değil", HttpStatus.FORBIDDEN);
        }

        // Note: card_ccv is intentionally validated (present & well-formed) but never persisted.
        if (String.valueOf(request.getCard_ccv()).length() < 3) {
            throw new ApiException("Geçersiz CVV", HttpStatus.BAD_REQUEST);
        }

        Order order = Order.builder()
                .user(user)
                .address(address)
                .cardNo(String.valueOf(request.getCard_no()))
                .cardName(request.getCard_name())
                .cardExpireMonth(request.getCard_expire_month())
                .cardExpireYear(request.getCard_expire_year())
                .price(request.getPrice())
                .build();

        List<OrderItem> items = new ArrayList<>();

        for (CreateOrderRequest.OrderProductItem item : request.getProducts()) {
            Product product = productRepository.findById(item.getProduct_id())
                    .orElseThrow(() -> new ApiException(
                            "Ürün bulunamadı: " + item.getProduct_id(), HttpStatus.NOT_FOUND));

            if (product.getStock() < item.getCount()) {
                throw new ApiException(
                        "\"" + product.getName() + "\" için yeterli stok yok (mevcut: " + product.getStock() + ")",
                        HttpStatus.BAD_REQUEST);
            }

            // Decrement stock and bump sell count
            product.setStock(product.getStock() - item.getCount());
            product.setSellCount(product.getSellCount() + item.getCount());
            productRepository.save(product);

            OrderItem orderItem = OrderItem.builder()
                    .order(order)
                    .product(product)
                    .count(item.getCount())
                    .unitPrice(product.getPrice())
                    .detail(item.getDetail())
                    .build();

            items.add(orderItem);
        }

        order.setItems(items);

        Order saved = orderRepository.save(order);
        return OrderResponse.from(saved);
    }
}