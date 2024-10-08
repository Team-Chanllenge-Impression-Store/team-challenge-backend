package com.online_store.web.product.controller;

import com.online_store.utils.MessageResponse;
import com.online_store.utils.constant.ErrorMessage;
import com.online_store.utils.constant.Path;
import com.online_store.utils.constant.SuccessMessage;
import com.online_store.web.product.dto.ProductDTO;
import com.online_store.web.product.dto.SearchRequest;
import com.online_store.web.product.model.Product;
import com.online_store.web.product.service.ProductService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller class for handling {@link Product}-related HTTP requests.
 */
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping(Path.PRODUCT)
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Product", description = "API to work with products")
public class ProductController {

    private final ProductService productService;

    /**
     * Creates or updates a product.
     * This endpoint is accessible only to users with ADMIN authority.
     *
     * @param request The {@link ProductDTO} object containing product details
     * @return ResponseEntity with a success message and HTTP status 200 (OK)
     */
    @PostMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<?> addProduct(@Valid @RequestBody ProductDTO request) {
        Product product = productService.createOrUpdateProduct(request);
        return ResponseEntity.ok().body(new MessageResponse(String.valueOf(HttpStatus.OK),
                request.getId() == null ? SuccessMessage.PRODUCT_CREATED : SuccessMessage.PRODUCT_UPDATED));
    }

    /**
     * Retrieves a specific product by its ID.
     *
     * @param productId The ID of the product to retrieve
     * @return ResponseEntity with the product if found, or a not found message with HTTP status 400 (Bad Request) if not found
     */
    @GetMapping("/{productId}")
    public ResponseEntity<?> getProduct(@PathVariable Long productId) {
        Product product = productService.getProductById(productId);
        if (product == null) {
            log.warn("Product with id {} doesn't exist", productId);
            return ResponseEntity.badRequest().body(new MessageResponse(String.valueOf(HttpStatus.NOT_FOUND), ErrorMessage.PRODUCT_NOT_FOUND));
        }
        return ResponseEntity.ok().body(product);
    }

    /**
     * Retrieves a list of all products.
     *
     * @return ResponseEntity with a list of all products and HTTP status 200 (OK)
     */
    @GetMapping("/list")
    public ResponseEntity<?> getProducts() {
        List<Product> products = productService.getAllProducts();
        return ResponseEntity.ok().body(products);
    }

    /**
     * Searches for products based on the given search criteria.
     *
     * @param searchRequest The {@link SearchRequest} object containing search criteria
     * @return ResponseEntity with a list of products matching the search criteria and HTTP status 200 (OK)
     */
    @PostMapping("/search")
    public ResponseEntity<?> searchProducts(@Valid @RequestBody SearchRequest searchRequest) {
        List<Product> products = productService.searchProducts(searchRequest);
        return ResponseEntity.ok().body(products);
    }
}
