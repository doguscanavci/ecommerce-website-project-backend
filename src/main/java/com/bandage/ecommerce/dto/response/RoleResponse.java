package com.bandage.ecommerce.dto.response;

import com.bandage.ecommerce.entity.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class RoleResponse {
    private Long id;
    private String name;
    private String code;

    public static RoleResponse from(Role role) {
        return new RoleResponse(role.getId(), role.getName(), role.getCode());
    }
}
