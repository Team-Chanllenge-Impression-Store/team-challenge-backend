package com.online_store.dto.request;

import lombok.Data;

@Data
public class ProductRequest {
    private Long id;
    private String name;
    private String description;
    private Double price;
    private String city;
    private Integer participantCount;
    private Boolean available;
    private Long categoryId;
}
