package com.online_store.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.online_store.configuration.WebSecurityTestConfig;
import com.online_store.constants.Path;
import com.online_store.constants.SuccessMessage;
import com.online_store.dto.request.CategoryRequest;
import com.online_store.entity.Category;
import com.online_store.security.jwt.JwtUtils;
import com.online_store.security.services.UserDetailsServiceImpl;
import com.online_store.service.CategoryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CategoryController.class)
@Import(WebSecurityTestConfig.class)
@ActiveProfiles("test")
@TestPropertySource("/application-test.properties")
class CategoryControllerTest {

    @Value("${com.example.demo.mockJwtCookieName}")
    private String mockJwtCookie;
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private CategoryService categoryService;
    @MockBean
    private JwtUtils jwtUtils;
    @MockBean
    private UserDetailsServiceImpl userDetailsService;
    @Autowired
    private ObjectMapper objectMapper;

    private Category testCategory;
    private CategoryRequest testCategoryRequest;

    @BeforeEach
    void setUp() {
        testCategory = new Category();
        testCategory.setId(1L);
        testCategory.setName("Test Category");
        testCategory.setDescription("Test Description");

        testCategoryRequest = new CategoryRequest();
        testCategoryRequest.setName("Test Category");
        testCategoryRequest.setDescription("Test Description");
    }

    @Test
    @WithMockUser(authorities = "ADMIN")
    void addCategory_ShouldReturnOkResponse() throws Exception {
        when(categoryService.createOrUpdateCategory(any(CategoryRequest.class))).thenReturn(testCategory);
        mockMvc.perform(post(Path.CATEGORY)
                        .header(HttpHeaders.AUTHORIZATION, mockJwtCookie)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testCategoryRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value(SuccessMessage.CATEGORY_CREATED));
    }

    @Test
    void getAllCategories_ShouldReturnListOfCategories() throws Exception {
        List<Category> categories = Collections.singletonList(testCategory);
        when(categoryService.getAllCategories()).thenReturn(categories);

        mockMvc.perform(get(Path.CATEGORY + "/list"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(testCategory.getId()))
                .andExpect(jsonPath("$[0].name").value(testCategory.getName()));
    }

    @Test
    void getCategory_ShouldReturnCategory() throws Exception {
        when(categoryService.getCategoryById(1L)).thenReturn(testCategory);

        mockMvc.perform(get(Path.CATEGORY + "/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(testCategory.getId()))
                .andExpect(jsonPath("$.name").value(testCategory.getName()));
    }
}