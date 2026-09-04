package com.bandage.ecommerce.dto.response;

import com.bandage.ecommerce.entity.Category;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class CategoryResponse {
    private Long id;
    private String title;
    private String img;
    private String gender;
    private Double rating;
    private String code;

    public static CategoryResponse from(Category c) {
        return new CategoryResponse(c.getId(), c.getTitle(), c.getImg(), c.getGender(), c.getRating(), c.getCode());
    }
}
