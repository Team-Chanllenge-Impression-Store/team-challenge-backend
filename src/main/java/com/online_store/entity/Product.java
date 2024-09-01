package com.online_store.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "products")
@Getter
@Setter
public class Product {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "product_seq")
    @SequenceGenerator(name = "product_seq", sequenceName = "product_seq", allocationSize = 1)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "price")
    private Double price;

    @Column(name = "date", columnDefinition = "timestamp default current_timestamp")
    private LocalDateTime date;

    @Column(name = "city")
    private String city;

    @Column(name = "participant_count")
    private Integer participantCount;

    @Column(name = "available")
    private Boolean available;

    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;
}

