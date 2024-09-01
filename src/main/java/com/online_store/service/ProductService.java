package com.online_store.service;

import com.online_store.dto.request.ProductRequest;
import com.online_store.dto.request.SearchRequest;
import com.online_store.entity.Product;

import java.util.List;

public interface ProductService {
    Product createOrUpdateProduct(ProductRequest request);

    Product getProductById(Long id);

    List<Product> getAllProducts();

    List<Product> searchProducts(SearchRequest searchRequest);
}
