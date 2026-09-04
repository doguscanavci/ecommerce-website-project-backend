package com.bandage.ecommerce.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreditCardRequest {
    private Long id; // used for PUT (update)

    @NotBlank(message = "card_no is required")
    @Pattern(regexp = "^\\d{16}$", message = "card_no must be exactly 16 digits")
    private String card_no;

    @NotNull(message = "expire_month is required")
    private Integer expire_month;

    @NotNull(message = "expire_year is required")
    private Integer expire_year;

    @NotBlank(message = "name_on_card is required")
    private String name_on_card;
}
