package com.bandage.ecommerce.service;

import com.bandage.ecommerce.dto.response.ProductListResponse;
import com.bandage.ecommerce.dto.response.ProductResponse;
import com.bandage.ecommerce.entity.Product;
import com.bandage.ecommerce.exception.ApiException;
import com.bandage.ecommerce.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    /**
     * Matches ShopPage.jsx / productActions.js query params:
     *   category  -> filter by category id
     *   filter    -> free-text search on product name
     *   sort      -> "price:asc" | "price:desc" | "rating:asc" | "rating:desc"
     *   limit     -> page size (default 25)
     *   offset    -> zero-based row offset
     *
     * @Transactional(readOnly = true) keeps the Hibernate session open while
     * we map entities to DTOs below — without it, accessing the lazy
     * `images` collection on Product throws LazyInitializationException
     * once open-in-view is disabled.
     */
    @Transactional(readOnly = true)
    public ProductListResponse getProducts(Long categoryId, String filterText, String sort, Integer limit, Integer offset) {
        int pageSize = (limit != null && limit > 0) ? limit : 25;
        int pageOffset = (offset != null && offset >= 0) ? offset : 0;
        int pageNumber = pageOffset / pageSize;

        Sort sortSpec = buildSort(sort);
        PageRequest pageRequest = PageRequest.of(pageNumber, pageSize, sortSpec);

        Specification<Product> spec = Specification.where(null);

        if (categoryId != null) {
            spec = spec.and((root, query, cb) -> cb.equal(root.get("category").get("id"), categoryId));
        }

        if (filterText != null && !filterText.isBlank()) {
            String likePattern = "%" + filterText.toLowerCase() + "%";
            spec = spec.and((root, query, cb) -> cb.like(cb.lower(root.get("name")), likePattern));
        }

        Page<Product> page = productRepository.findAll(spec, pageRequest);

        List<ProductResponse> products = page.getContent().stream()
                .map(ProductResponse::from)
                .collect(Collectors.toList());

        return new ProductListResponse(products, page.getTotalElements());
    }

    @Transactional(readOnly = true)
    public ProductResponse getProductById(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ApiException("Ürün bulunamadı", HttpStatus.NOT_FOUND));
        return ProductResponse.from(product);
    }

    private Sort buildSort(String sort) {
        if (sort == null || sort.isBlank()) {
            return Sort.by(Sort.Direction.DESC, "sellCount"); // "Popularity" default
        }

        String[] parts = sort.split(":");
        String field = parts[0];
        Sort.Direction direction = (parts.length > 1 && "desc".equalsIgnoreCase(parts[1]))
                ? Sort.Direction.DESC : Sort.Direction.ASC;

        String mappedField = switch (field) {
            case "price" -> "price";
            case "rating" -> "rating";
            default -> "sellCount";
        };

        return Sort.by(direction, mappedField);
    }
}