package com.online_store.web.product.dao;

import com.online_store.web.product.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long>, JpaSpecificationExecutor<Product> {
    Optional<Product> getProductById(Long id);

//    @Transactional(readOnly = true)
//    @Cacheable("products")
//    @Query("SELECT p FROM Product p order by p.id desc")
//    List<Product> findAll() throws DataAccessException;
}
