package com.bandage.ecommerce.controller;

import com.bandage.ecommerce.dto.request.CreditCardRequest;
import com.bandage.ecommerce.dto.response.CreditCardResponse;
import com.bandage.ecommerce.security.UserPrincipal;
import com.bandage.ecommerce.service.CreditCardService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/user/card")
@RequiredArgsConstructor
public class CreditCardController {

    private final CreditCardService creditCardService;

    @GetMapping
    public List<CreditCardResponse> getCards(@AuthenticationPrincipal UserPrincipal principal) {
        return creditCardService.getUserCards(principal.getId());
    }

    @PostMapping
    public ResponseEntity<CreditCardResponse> createCard(
            @AuthenticationPrincipal UserPrincipal principal,
            @Valid @RequestBody CreditCardRequest request
    ) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(creditCardService.createCard(principal.getId(), request));
    }

    @PutMapping
    public CreditCardResponse updateCard(
            @AuthenticationPrincipal UserPrincipal principal,
            @Valid @RequestBody CreditCardRequest request
    ) {
        return creditCardService.updateCard(principal.getId(), request);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> deleteCard(
            @AuthenticationPrincipal UserPrincipal principal,
            @PathVariable Long id
    ) {
        creditCardService.deleteCard(principal.getId(), id);
        return ResponseEntity.ok(Map.of("message", "Kart silindi"));
    }
}
