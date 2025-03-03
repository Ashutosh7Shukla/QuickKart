package com.Shopping.dream_shop.service.product;

import com.Shopping.dream_shop.dto.ProductDto;
import com.Shopping.dream_shop.model.Product;
import com.Shopping.dream_shop.request.AddProductRequest;
import com.Shopping.dream_shop.request.UpdateProductRequest;

import java.util.List;

public interface IProductService {

    Product addProduct(AddProductRequest request);

    Product getProductById(Long id);

    void deleteProductById(Long id);

    Product updateProductById(UpdateProductRequest request, Long productId);

    List<Product> getAllProducts();

    List<Product> getProductsByCategory(String category);

    List<Product> getProductByBrand(String brand);

    List<Product> getProductByCategoryAndBrand(String category, String brand);

    List<Product> getProductByName(String name);

    List<Product> getProductByBrandAndName(String brand, String name);

    Long countProductsByBrandAndName(String brand, String name);


    List<ProductDto> getConvertedProducts(List<Product> products);

    ProductDto convertToDto(Product product);
}
