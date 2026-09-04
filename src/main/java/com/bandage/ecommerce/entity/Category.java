package com.bandage.ecommerce.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "categories")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String img;

    // 'k' = kadın, 'e' = erkek — matches frontend genderToSlug()
    @Column(nullable = false, length = 1)
    private String gender;

    @Column(nullable = false)
    @Builder.Default
    private Double rating = 0.0;

    private String code;
}
