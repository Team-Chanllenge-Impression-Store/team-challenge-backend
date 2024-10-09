package com.online_store.web.generator.controller;

import com.online_store.utils.MessageResponse;
import com.online_store.utils.constant.Path;
import com.online_store.web.generator.service.ProductGeneratorService;
import com.online_store.web.generator.service.UserGeneratorService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequiredArgsConstructor
@RequestMapping(Path.GENERATOR)
@Tag(name = "Generator", description = "Generates random Users and Products")
public class GeneratorController {
    private final UserGeneratorService userGeneratorService;
    private final ProductGeneratorService productGeneratorService;

    @PostMapping("/users")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<MessageResponse> generateUsers(
            @RequestParam(name = "amount", defaultValue = "10", required = false) int amount) {
        userGeneratorService.generateUsers(amount);
        return ResponseEntity.ok(new MessageResponse(
                String.valueOf(HttpStatus.OK), amount + " users have been generated successfully"));
    }

    @PostMapping("/products")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<MessageResponse> generateProducts(
            @RequestParam(name = "amount", defaultValue = "10", required = false) int amount) {
        productGeneratorService.generateProducts(amount);
        return ResponseEntity.ok(new MessageResponse(
                String.valueOf(HttpStatus.OK), amount + " products have been generated successfully"));
    }
}

