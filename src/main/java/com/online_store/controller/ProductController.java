package com.online_store.controller;

import com.online_store.constants.ErrorMessage;
import com.online_store.constants.Path;
import com.online_store.constants.SuccessMessage;
import com.online_store.dto.request.ProductRequest;
import com.online_store.dto.response.MessageResponse;
import com.online_store.entity.Product;
import com.online_store.service.ProductService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
public class ProductController {
    private static final Logger logger = LoggerFactory.getLogger(ProductController.class);
    @Autowired
    private ProductService productService;

    /**
     * Creates or updates a product.
     * This endpoint is accessible only to users with ADMIN authority.
     *
     * @param request The {@link ProductRequest} object containing product details
     * @return ResponseEntity with a success message and HTTP status 200 (OK)
     */
    @PostMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<?> addProduct(@Valid @RequestBody ProductRequest request) {
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
            logger.warn("Product with id {} doesn't exist", productId);
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
}
