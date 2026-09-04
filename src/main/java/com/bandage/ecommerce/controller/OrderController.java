package com.bandage.ecommerce.controller;

import com.bandage.ecommerce.dto.request.CreateOrderRequest;
import com.bandage.ecommerce.dto.response.OrderResponse;
import com.bandage.ecommerce.security.UserPrincipal;
import com.bandage.ecommerce.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/order")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @GetMapping
    public List<OrderResponse> getOrders(@AuthenticationPrincipal UserPrincipal principal) {
        return orderService.getUserOrders(principal.getId());
    }

    @PostMapping
    public ResponseEntity<OrderResponse> createOrder(
            @AuthenticationPrincipal UserPrincipal principal,
            @Valid @RequestBody CreateOrderRequest request
    ) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(orderService.createOrder(principal.getId(), request));
    }
}
