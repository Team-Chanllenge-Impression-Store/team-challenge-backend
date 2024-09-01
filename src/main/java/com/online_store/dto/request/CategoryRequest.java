package com.online_store.dto.request;

import com.online_store.entity.Product;
import lombok.Data;

import java.util.List;

@Data
public class CategoryRequest {
    private Long id;
    private List<Product> products;
    private String name;
    private String description;
}
