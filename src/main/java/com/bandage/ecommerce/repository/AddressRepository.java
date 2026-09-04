package com.bandage.ecommerce.repository;

import com.bandage.ecommerce.entity.Address;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AddressRepository extends JpaRepository<Address, Long> {
    List<Address> findByUserId(Long userId);
    boolean existsByIdAndUserId(Long id, Long userId);
}
