package com.bandage.ecommerce.dto.response;

import com.bandage.ecommerce.entity.CreditCard;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class CreditCardResponse {
    private Long id;
    private String card_no;
    private Integer expire_month;
    private Integer expire_year;
    private String name_on_card;

    public static CreditCardResponse from(CreditCard c) {
        return new CreditCardResponse(c.getId(), c.getCardNo(), c.getExpireMonth(), c.getExpireYear(), c.getNameOnCard());
    }
}
