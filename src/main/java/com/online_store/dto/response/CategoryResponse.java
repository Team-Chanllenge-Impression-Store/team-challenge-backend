package com.online_store.dto.response;

import com.online_store.entity.Product;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class CategoryResponse {
    private Long id;
    private String description;
    private String name;
    private List<Product> products;
}
