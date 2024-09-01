package com.online_store.dto.request;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Data
public class ProductRequest {
    private Long id;
    private String name;
    private String description;
    private Double price;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime date;
    private String city;
    private Integer participantCount;
    private Boolean available;
    private Long categoryId;
}
