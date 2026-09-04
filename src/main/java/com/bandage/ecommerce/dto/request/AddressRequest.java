package com.bandage.ecommerce.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AddressRequest {
    private Long id; // used for PUT (update)

    @NotBlank(message = "title is required")
    private String title;

    @NotBlank(message = "name is required")
    private String name;

    @NotBlank(message = "surname is required")
    private String surname;

    @NotBlank(message = "phone is required")
    private String phone;

    @NotBlank(message = "city is required")
    private String city;

    @NotBlank(message = "district is required")
    private String district;

    @NotBlank(message = "neighborhood is required")
    private String neighborhood;

    private String address;
}
