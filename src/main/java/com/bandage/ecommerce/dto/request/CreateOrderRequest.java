package com.bandage.ecommerce.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
public class CreateOrderRequest {

    @NotNull(message = "address_id is required")
    private Long address_id;

    @NotNull(message = "card_no is required")
    private Long card_no;

    @NotNull(message = "card_name is required")
    private String card_name;

    @NotNull(message = "card_expire_month is required")
    private Integer card_expire_month;

    @NotNull(message = "card_expire_year is required")
    private Integer card_expire_year;

    @NotNull(message = "card_ccv is required")
    private Integer card_ccv;

    @NotNull(message = "price is required")
    private BigDecimal price;

    @NotEmpty(message = "products list cannot be empty")
    @Valid
    private List<OrderProductItem> products;

    @Getter
    @Setter
    public static class OrderProductItem {
        @NotNull
        private Long product_id;
        @NotNull
        private Integer count;
        private String detail;
    }
}
