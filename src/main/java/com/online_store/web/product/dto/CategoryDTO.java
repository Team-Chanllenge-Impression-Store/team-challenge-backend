package com.online_store.web.product.dto;

import com.online_store.web.product.model.Product;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CategoryDTO {
    private Long id;
    private List<Product> products;
    private String name;
    private String description;
}
