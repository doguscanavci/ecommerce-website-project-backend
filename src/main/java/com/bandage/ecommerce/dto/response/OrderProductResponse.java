package com.bandage.ecommerce.dto.response;

import com.bandage.ecommerce.entity.OrderItem;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Matches what PreviousOrdersPage.jsx reads from order.products[]:
 * product.images[0].url, product.name, product.description, product.price, product.count
 */
@Getter
@Setter
@AllArgsConstructor
public class OrderProductResponse {
    private Long id;
    private String name;
    private String description;
    private BigDecimal price; // unit price snapshot at purchase time
    private Integer count;
    private List<ProductImageResponse> images;

    public static OrderProductResponse from(OrderItem item) {
        var product = item.getProduct();
        List<ProductImageResponse> imgs = (product == null || product.getImages() == null) ? List.of() :
                product.getImages().stream().map(ProductImageResponse::from).collect(Collectors.toList());

        return new OrderProductResponse(
                product != null ? product.getId() : null,
                product != null ? product.getName() : item.getDetail(),
                product != null ? product.getDescription() : null,
                item.getUnitPrice(),
                item.getCount(),
                imgs
        );
    }
}
