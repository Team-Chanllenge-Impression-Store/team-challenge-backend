package com.online_store.web.generator.controller;

import com.online_store.utils.MessageResponse;
import com.online_store.utils.constant.Path;
import com.online_store.web.generator.service.ProductGeneratorService;
import com.online_store.web.generator.service.UserGeneratorService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequiredArgsConstructor
@RequestMapping(Path.GENERATOR)
@Tag(name = "Generator", description = "Generates Users, Products and Orders")
public class GeneratorController {
    private final UserGeneratorService userGeneratorService;
    private final ProductGeneratorService productGeneratorService;

    @GetMapping("/users")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<MessageResponse> generateUsers(@RequestParam("amount") int amount) {
        userGeneratorService.generateUsers(amount);
        return ResponseEntity.ok(new MessageResponse("200", "Users generated successfully"));
    }

    @GetMapping("/products")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<MessageResponse> generateProducts(@RequestParam("amount") int amount) {
        productGeneratorService.generateProducts(amount);
        return ResponseEntity.ok(new MessageResponse("200", "Products generated successfully"));
    }
// TODO: implement service method when OrderService is ready
//    @GetMapping("/orders")
//    public ResponseEntity<MessageResponse> generateOrders() {
//        generatorService.generateOrders();
//        return ResponseEntity.ok(new MessageResponse("200", "Orders generated successfully"));
//    }
}

