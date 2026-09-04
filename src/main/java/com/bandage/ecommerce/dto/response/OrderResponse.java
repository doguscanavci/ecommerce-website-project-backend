package com.bandage.ecommerce.dto.response;

import com.bandage.ecommerce.entity.Order;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Matches PreviousOrdersPage.jsx field usage:
 * order.id, order.order_date, order.products[], order.price,
 * order.card_no, order.card_name, order.card_expire_month, order.card_expire_year
 */
@Getter
@Setter
@AllArgsConstructor
public class OrderResponse {
    private Long id;
    private LocalDateTime order_date;
    private String card_no; // masked-friendly: full digits, frontend masks it with maskCardNo()
    private String card_name;
    private Integer card_expire_month;
    private Integer card_expire_year;
    private BigDecimal price;
    private List<OrderProductResponse> products;

    public static OrderResponse from(Order order) {
        List<OrderProductResponse> products = order.getItems().stream()
                .map(OrderProductResponse::from)
                .collect(Collectors.toList());

        return new OrderResponse(
                order.getId(),
                order.getOrderDate(),
                order.getCardNo(),
                order.getCardName(),
                order.getCardExpireMonth(),
                order.getCardExpireYear(),
                order.getPrice(),
                products
        );
    }
}
