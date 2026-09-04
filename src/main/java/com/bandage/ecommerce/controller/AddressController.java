package com.bandage.ecommerce.controller;

import com.bandage.ecommerce.dto.request.AddressRequest;
import com.bandage.ecommerce.dto.response.AddressResponse;
import com.bandage.ecommerce.security.UserPrincipal;
import com.bandage.ecommerce.service.AddressService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/user/address")
@RequiredArgsConstructor
public class AddressController {

    private final AddressService addressService;

    @GetMapping
    public List<AddressResponse> getAddresses(@AuthenticationPrincipal UserPrincipal principal) {
        return addressService.getUserAddresses(principal.getId());
    }

    @PostMapping
    public ResponseEntity<AddressResponse> createAddress(
            @AuthenticationPrincipal UserPrincipal principal,
            @Valid @RequestBody AddressRequest request
    ) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(addressService.createAddress(principal.getId(), request));
    }

    @PutMapping
    public AddressResponse updateAddress(
            @AuthenticationPrincipal UserPrincipal principal,
            @Valid @RequestBody AddressRequest request
    ) {
        return addressService.updateAddress(principal.getId(), request);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> deleteAddress(
            @AuthenticationPrincipal UserPrincipal principal,
            @PathVariable Long id
    ) {
        addressService.deleteAddress(principal.getId(), id);
        return ResponseEntity.ok(Map.of("message", "Adres silindi"));
    }
}
