package com.bandage.ecommerce.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StoreRequest {
    private String name;
    private String phone;
    private String tax_no;
    private String bank_account;
}
