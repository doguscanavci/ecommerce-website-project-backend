package com.bandage.ecommerce.dto.response;

import com.bandage.ecommerce.entity.ProductImage;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ProductImageResponse {
    private String url;
    private Integer index;

    public static ProductImageResponse from(ProductImage img) {
        return new ProductImageResponse(img.getUrl(), img.getIndex());
    }
}
