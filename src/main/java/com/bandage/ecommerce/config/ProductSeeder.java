package com.bandage.ecommerce.config;

import com.bandage.ecommerce.entity.Category;
import com.bandage.ecommerce.entity.Product;
import com.bandage.ecommerce.entity.ProductImage;
import com.bandage.ecommerce.repository.CategoryRepository;
import com.bandage.ecommerce.repository.ProductRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
@Order(2)
public class ProductSeeder implements CommandLineRunner {

    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void run(String... args) {
        System.out.println(">>> ProductSeeder: starting run()");

        long existingCategories = categoryRepository.count();
        System.out.println(">>> ProductSeeder: existing category count = " + existingCategories);

        if (existingCategories > 0) {
            System.out.println(">>> ProductSeeder: categories already exist, skipping seed.");
            return;
        }

        try {
            JsonNode root;
            try (InputStream is = new ClassPathResource("seed-data/seed-data.json").getInputStream()) {
                System.out.println(">>> ProductSeeder: seed-data.json found, reading...");
                root = objectMapper.readTree(is);
            }

            JsonNode categoriesNode = root.get("categories");
            JsonNode productsNode = root.get("products");

            System.out.println(">>> ProductSeeder: categories in JSON = " +
                    (categoriesNode == null ? "NULL" : categoriesNode.size()));
            System.out.println(">>> ProductSeeder: products in JSON = " +
                    (productsNode == null ? "NULL" : productsNode.size()));

            if (categoriesNode == null || productsNode == null) {
                System.out.println(">>> ProductSeeder: ABORTING - JSON structure missing 'categories' or 'products' key.");
                return;
            }

            // ---- Categories ----
            Map<Integer, Category> savedCategories = new HashMap<>();
            int index = 1;
            for (JsonNode c : categoriesNode) {
                Category saved = categoryRepository.save(Category.builder()
                        .code(c.get("code").asText())
                        .title(c.get("title").asText())
                        .img(c.get("img").asText())
                        .rating(c.get("rating").asDouble())
                        .gender(c.get("gender").asText())
                        .build());
                savedCategories.put(index, saved);
                index++;
            }
            System.out.println(">>> ProductSeeder: inserted " + savedCategories.size() + " categories.");

            // ---- Products ----
            int insertedProducts = 0;
            int skippedProducts = 0;
            for (JsonNode p : productsNode) {
                int categoryId = p.get("category_id").asInt();
                Category category = savedCategories.get(categoryId);
                if (category == null) {
                    skippedProducts++;
                    continue;
                }

                Product product = Product.builder()
                        .name(p.get("name").asText())
                        .description(p.get("description").asText())
                        .price(new BigDecimal(p.get("price").asText()))
                        .stock(p.get("stock").asInt())
                        .rating(p.get("rating").asDouble())
                        .sellCount(p.get("sell_count").asInt())
                        .category(category)
                        .build();

                List<ProductImage> images = new java.util.ArrayList<>();
                for (JsonNode img : p.get("images")) {
                    images.add(ProductImage.builder()
                            .url(img.get("url").asText())
                            .index(img.get("index").asInt())
                            .product(product)
                            .build());
                }
                product.setImages(images);

                productRepository.save(product);
                insertedProducts++;
            }

            System.out.println(">>> ProductSeeder: inserted " + insertedProducts + " products, skipped " + skippedProducts);
            System.out.println(">>> ProductSeeder: DONE.");

        } catch (Exception e) {
            System.out.println(">>> ProductSeeder: EXCEPTION occurred: " + e.getClass().getName() + " - " + e.getMessage());
            e.printStackTrace();
        }
    }
}