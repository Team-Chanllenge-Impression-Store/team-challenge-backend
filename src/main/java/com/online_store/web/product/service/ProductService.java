package com.online_store.web.product.service;

import com.online_store.web.product.dto.ProductDTO;
import com.online_store.web.product.dto.SearchRequest;
import com.online_store.web.product.model.Product;

import java.util.List;

public interface ProductService {
    Product createOrUpdateProduct(ProductDTO request);

    Product getProductById(Long id);

    List<Product> getAllProducts();

    List<Product> searchProducts(SearchRequest searchRequest);
}
