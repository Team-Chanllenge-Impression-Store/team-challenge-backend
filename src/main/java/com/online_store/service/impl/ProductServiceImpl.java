package com.online_store.service.impl;

import com.online_store.constants.ErrorMessage;
import com.online_store.dto.request.ProductRequest;
import com.online_store.dto.request.SearchRequest;
import com.online_store.entity.Product;
import com.online_store.repository.ProductRepository;
import com.online_store.repository.specification.ProductSpecification;
import com.online_store.service.CategoryService;
import com.online_store.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;
    private final CategoryService categoryService;
    private final ModelMapper modelMapper;


    @Override
    @Transactional
    public Product createOrUpdateProduct(ProductRequest request) {
        if (request.getName() == null || request.getName().trim().isEmpty()) {
            log.error("Product name is null or empty");
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Product name is empty");
        }

        Product product;
        if (request.getId() != null) {
            // Update existing product
            product = getProductById(request.getId());
        } else {
            // Create new product
            product = modelMapper.map(request, Product.class);
        }
        // Update fields
        product.setName(request.getName());
        product.setDescription(request.getDescription());
        product.setPrice(request.getPrice());
        product.setDate(request.getDate());
        product.setCity(request.getCity());
        product.setParticipantCount(request.getParticipantCount());
        product.setAvailable(request.getAvailable());
        product.setCategory(categoryService.getCategoryById(request.getCategoryId()));
        productRepository.save(product);
        log.info("Saved product: {}", product);
        return product;
    }

    @Override
    public Product getProductById(Long id) {
        Product product = productRepository.getProductById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, ErrorMessage.PRODUCT_NOT_FOUND));
        return product;
    }

    @Override
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    @Override
    public List<Product> searchProducts(SearchRequest searchRequest) {
        return productRepository.findAll(ProductSpecification.searchProducts(
                searchRequest.getCity(),
                searchRequest.getDate(),
                searchRequest.getParticipantCount()
        ));
    }
}
