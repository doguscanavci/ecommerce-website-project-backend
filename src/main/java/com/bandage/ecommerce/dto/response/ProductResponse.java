package com.bandage.ecommerce.dto.response;

import com.bandage.ecommerce.entity.Product;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@AllArgsConstructor
public class ProductResponse {
    private Long id;
    private String name;
    private String description;
    private BigDecimal price;
    private Integer stock;
    private Double rating;
    private Integer sell_count;
    private Long category_id;
    private Long store_id;
    private List<ProductImageResponse> images;

    public static ProductResponse from(Product p) {
        List<ProductImageResponse> imgs = p.getImages() == null ? List.of() :
                p.getImages().stream().map(ProductImageResponse::from).collect(Collectors.toList());

        return new ProductResponse(
                p.getId(),
                p.getName(),
                p.getDescription(),
                p.getPrice(),
                p.getStock(),
                p.getRating(),
                p.getSellCount(),
                p.getCategory() != null ? p.getCategory().getId() : null,
                p.getStore() != null ? p.getStore().getId() : null,
                imgs
        );
    }
}
