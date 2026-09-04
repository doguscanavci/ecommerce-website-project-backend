package com.bandage.ecommerce.repository;

import com.bandage.ecommerce.entity.CreditCard;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CreditCardRepository extends JpaRepository<CreditCard, Long> {
    List<CreditCard> findByUserId(Long userId);
    boolean existsByIdAndUserId(Long id, Long userId);
}
