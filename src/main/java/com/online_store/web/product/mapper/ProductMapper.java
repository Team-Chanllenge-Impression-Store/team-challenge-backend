package com.online_store.web.product.mapper;

import com.online_store.web.product.dto.ProductDTO;
import com.online_store.web.product.model.Product;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ProductMapper {

    ProductMapper INSTANCE = org.mapstruct.factory.Mappers.getMapper(ProductMapper.class);

    Product toProductRequest(ProductDTO productDTO);

    ProductDTO toProduct(Product product);
}
