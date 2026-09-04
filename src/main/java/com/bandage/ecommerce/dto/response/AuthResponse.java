package com.bandage.ecommerce.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * Matches what clientActions.js expects from /login and /verify:
 * const { token, name, email: userEmail, role_id } = response.data;
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
public class AuthResponse {
    private String token;
    private String name;
    private String email;
    private Long role_id;
}
