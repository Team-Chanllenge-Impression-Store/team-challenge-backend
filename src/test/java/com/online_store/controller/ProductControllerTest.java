package com.online_store.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.online_store.configuration.WebSecurityTestConfig;
import com.online_store.constants.ErrorMessage;
import com.online_store.constants.Path;
import com.online_store.constants.SuccessMessage;
import com.online_store.dto.request.ProductRequest;
import com.online_store.dto.request.SearchRequest;
import com.online_store.entity.Product;
import com.online_store.service.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ProductController.class)
@Import(WebSecurityTestConfig.class)
@ActiveProfiles("test")
@TestPropertySource("/application-test.properties")
class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductService productService;

    @Autowired
    private ObjectMapper objectMapper;

    private Product testProduct;
    private ProductRequest testProductRequest;
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");

    @BeforeEach
    void setUp() {
        testProduct = new Product();
        testProduct.setId(1L);
        testProduct.setName("Test Product");
        testProduct.setCity("Test City");
        testProduct.setDate(LocalDateTime.parse("2024-01-01T00:00:00", DATE_TIME_FORMATTER));
        testProduct.setAvailable(true);
        testProduct.setParticipantCount(10);
        testProduct.setDescription("Test Description");
        testProduct.setPrice(10.0);

        testProductRequest = new ProductRequest();
        testProductRequest.setName("Test Product");
        testProductRequest.setDescription("Test Description");
        testProductRequest.setPrice(10.0);
    }

    @Test
    @WithMockUser(authorities = "ADMIN")
    void addProduct_ShouldReturnOkResponse() throws Exception {
        when(productService.createOrUpdateProduct(any(ProductRequest.class))).thenReturn(testProduct);

        mockMvc.perform(post(Path.PRODUCT)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testProductRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.response").value(String.valueOf(HttpStatus.OK)))
                .andExpect(jsonPath("$.message").value(SuccessMessage.PRODUCT_CREATED));
    }

    @Test
    void getProduct_ShouldReturnProduct() throws Exception {
        when(productService.getProductById(1L)).thenReturn(testProduct);

        mockMvc.perform(get(Path.PRODUCT + "/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(testProduct.getId()))
                .andExpect(jsonPath("$.name").value(testProduct.getName()))
                .andExpect(jsonPath("$.description").value(testProduct.getDescription()))
                .andExpect(jsonPath("$.price").value(testProduct.getPrice()))
                .andExpect(jsonPath("$.date").value(testProduct.getDate().format(DATE_TIME_FORMATTER)))
                .andExpect(jsonPath("$.city").value(testProduct.getCity()))
                .andExpect(jsonPath("$.participantCount").value(testProduct.getParticipantCount()))
                .andExpect(jsonPath("$.available").value(testProduct.getAvailable()));
    }

    @Test
    void getProduct_ShouldReturnNotFoundForNonExistentProduct() throws Exception {
        when(productService.getProductById(1L)).thenReturn(null);

        mockMvc.perform(get(Path.PRODUCT + "/1"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.response").value(String.valueOf(HttpStatus.NOT_FOUND)))
                .andExpect(jsonPath("$.message").value(ErrorMessage.PRODUCT_NOT_FOUND));
    }

    @Test
    void getAllProducts_ShouldReturnListOfProducts() throws Exception {
        List<Product> products = Collections.singletonList(testProduct);
        when(productService.getAllProducts()).thenReturn(products);

        mockMvc.perform(get(Path.PRODUCT + "/list"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(testProduct.getId()))
                .andExpect(jsonPath("$[0].name").value(testProduct.getName()))
                .andExpect(jsonPath("$[0].description").value(testProduct.getDescription()))
                .andExpect(jsonPath("$[0].price").value(testProduct.getPrice()))
                .andExpect(jsonPath("$[0].date").value(testProduct.getDate().format(DATE_TIME_FORMATTER)))
                .andExpect(jsonPath("$[0].city").value(testProduct.getCity()))
                .andExpect(jsonPath("$[0].participantCount").value(testProduct.getParticipantCount()))
                .andExpect(jsonPath("$[0].available").value(testProduct.getAvailable()));
    }

    @Test
    void searchProducts_ShouldReturnListOfProducts() throws Exception {
        List<Product> products = Collections.singletonList(testProduct);
        when(productService.searchProducts(any(SearchRequest.class))).thenReturn(products);

        SearchRequest searchRequest = new SearchRequest();
        searchRequest.setCity(testProduct.getCity());
        searchRequest.setDate(testProduct.getDate());
        searchRequest.setParticipantCount(testProduct.getParticipantCount());

        mockMvc.perform(post(Path.PRODUCT + "/search")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(searchRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(testProduct.getId()))
                .andExpect(jsonPath("$[0].name").value(testProduct.getName()))
                .andExpect(jsonPath("$[0].description").value(testProduct.getDescription()))
                .andExpect(jsonPath("$[0].price").value(testProduct.getPrice()))
                .andExpect(jsonPath("$[0].date").value(testProduct.getDate().format(DATE_TIME_FORMATTER)))
                .andExpect(jsonPath("$[0].city").value(testProduct.getCity()))
                .andExpect(jsonPath("$[0].participantCount").value(testProduct.getParticipantCount()))
                .andExpect(jsonPath("$[0].available").value(testProduct.getAvailable()));
    }

    @Test
    void searchProducts_WithDateGreaterThanAvailableProducts_ShouldReturnEmptyList() throws Exception {
        List<Product> products = Collections.emptyList();
        when(productService.searchProducts(any(SearchRequest.class))).thenReturn(products);

        SearchRequest searchRequest = new SearchRequest();
        searchRequest.setDate(LocalDateTime.now().plusDays(1));

        mockMvc.perform(post(Path.PRODUCT + "/search")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(searchRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isEmpty());
    }
}