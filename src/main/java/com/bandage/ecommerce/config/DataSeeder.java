package com.bandage.ecommerce.config;

import com.bandage.ecommerce.entity.Role;
import com.bandage.ecommerce.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataSeeder implements CommandLineRunner {

    private final RoleRepository roleRepository;

    @Override
    public void run(String... args) {
        seedRole("customer", "Müşteri");
        seedRole("store", "Mağaza");
        seedRole("admin", "Admin");
    }

    private void seedRole(String code, String name) {
        roleRepository.findByCode(code).orElseGet(() -> {
            Role role = new Role();
            role.setCode(code);
            role.setName(name);
            return roleRepository.save(role);
        });
    }
}
