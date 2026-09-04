package com.bandage.ecommerce.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * Matches productActions.js:
 * dispatch(setProductList(response.data.products || response.data));
 * dispatch(setTotal(response.data.total || response.data.length));
 */
@Getter
@Setter
@AllArgsConstructor
public class ProductListResponse {
    private List<ProductResponse> products;
    private Long total;
}
