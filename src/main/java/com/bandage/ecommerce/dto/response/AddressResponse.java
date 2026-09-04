package com.bandage.ecommerce.dto.response;

import com.bandage.ecommerce.entity.Address;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class AddressResponse {
    private Long id;
    private String title;
    private String name;
    private String surname;
    private String phone;
    private String city;
    private String district;
    private String neighborhood;
    private String address;

    public static AddressResponse from(Address a) {
        return new AddressResponse(a.getId(), a.getTitle(), a.getName(), a.getSurname(),
                a.getPhone(), a.getCity(), a.getDistrict(), a.getNeighborhood(), a.getAddress());
    }
}
