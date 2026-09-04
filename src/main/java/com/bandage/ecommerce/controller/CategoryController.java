package com.bandage.ecommerce.controller;

import com.bandage.ecommerce.dto.response.CategoryResponse;
import com.bandage.ecommerce.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    @GetMapping("/categories")
    public List<CategoryResponse> getCategories() {
        return categoryService.getAllCategories();
    }
}
