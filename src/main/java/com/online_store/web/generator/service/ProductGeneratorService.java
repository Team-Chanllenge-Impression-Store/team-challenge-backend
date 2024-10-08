package com.online_store.web.generator.service;

import com.online_store.web.product.dao.ProductRepository;
import com.online_store.web.product.model.Product;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.datafaker.Faker;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductGeneratorService {
    private final ProductRepository productRepository;
    private final List<Product> products = new ArrayList<>(); // <1>

    public void generateProducts(int amount) {
        // start time
        long time = System.currentTimeMillis();
        Faker faker = new Faker();
        for (int i = 0; i < amount; i++) {
            products.add(createProduct(faker));
        }
        productRepository.saveAll(products);
        long time2 = System.currentTimeMillis();
        long duration = time2 - time;
        log.info("Products generated in {} ms", duration);

    }

    protected Product createProduct(Faker faker) {
        Product product = new Product();
        product.setName("Product #" + faker.number().numberBetween(0, 1000));
        product.setDescription("Description #" + faker.number().numberBetween(0, 1000));
        product.setPrice((double) faker.number().numberBetween(0, 1000));
        product.setDate(LocalDateTime.now());
        product.setCity(faker.address().cityName());
        product.setParticipantCount(faker.number().numberBetween(0, 100));
        product.setAvailable(true);
        product.setCategory(null);
        return product;
    }
}
