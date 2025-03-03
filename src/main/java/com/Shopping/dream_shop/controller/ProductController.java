package com.Shopping.dream_shop.controller;

import com.Shopping.dream_shop.dto.ProductDto;
import com.Shopping.dream_shop.exception.ProductNotFoundException;
import com.Shopping.dream_shop.exception.ResourceNotFoundException;
import com.Shopping.dream_shop.model.Product;
import com.Shopping.dream_shop.request.AddProductRequest;
import com.Shopping.dream_shop.request.UpdateProductRequest;
import com.Shopping.dream_shop.response.ApiResponse;
import com.Shopping.dream_shop.service.product.IProductService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.HttpStatus.*;


@RestController
@RequestMapping("${api.prefix}/products")
public class ProductController {

    @Autowired
    private final IProductService productService;

    public ProductController(IProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/all")
    public ResponseEntity<ApiResponse> getAllProducts() {
        List<Product> products = productService.getAllProducts();
        List<ProductDto> convertedProducts = productService.getConvertedProducts(products);
        return ResponseEntity.ok(new ApiResponse("success", convertedProducts));
    }

    @GetMapping("/product/{productId}/product")
    public ResponseEntity<ApiResponse> getProductById(@PathVariable Long productId) {
        try {
            Product product = productService.getProductById(productId);
            ProductDto productDto = productService.convertToDto(product);
            return ResponseEntity.ok(new ApiResponse("success", productDto));
        } catch (ProductNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        }
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/add")
    public ResponseEntity<ApiResponse> addProduct(@RequestBody AddProductRequest product) {
        try {
            Product theProduct = productService.addProduct(product);
            ProductDto productDto = productService.convertToDto(theProduct);
            return ResponseEntity.ok(new ApiResponse("success", productDto));
        } catch (Exception e) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse(e.getMessage(), null));
        }
    }


    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping("/product/{productId}/update")
    public ResponseEntity<ApiResponse> updateProduct(@RequestBody UpdateProductRequest request , @PathVariable Long productId)
    {
        try {
            Product theProduct= productService.updateProductById(request , productId);
            ProductDto productDto = productService.convertToDto(theProduct);
            return ResponseEntity.ok(new ApiResponse("success" , productDto));
        } catch (ProductNotFoundException e) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse(e.getMessage() , null));
        }
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping("/product/{productId}/delete")
    public ResponseEntity<ApiResponse> deleteProduct(@PathVariable Long productId)
    {
        try {
            productService.deleteProductById(productId);
            return ResponseEntity.ok(new ApiResponse("delete success" , null));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage() , null));
        }
    }

    @GetMapping("/products/by/brand-and-name")
    public ResponseEntity<ApiResponse> getProductByBrandAndName(@RequestParam String brandName , @RequestParam String productName)
    {
        try {
            List<Product> products = productService.getProductByBrandAndName(brandName,productName);
            List<ProductDto> convertedProducts = productService.getConvertedProducts(products);
            if (products.isEmpty())
            {
                return ResponseEntity.status(NOT_FOUND).body(new ApiResponse("no product Found " , null));
            }
            return ResponseEntity.ok(new ApiResponse("success" , convertedProducts));
        } catch (Exception e) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse(e.getMessage() , null));
        }

    }

    @GetMapping("/products/by/category-and-brand")
    public ResponseEntity<ApiResponse> getProductsByCategoryAndBrand(@RequestParam String category , @RequestParam String brand)
    {
        try {
            List<Product> products = productService.getProductByCategoryAndBrand(category,brand);
            if (products.isEmpty())
            {
                return ResponseEntity.status(NOT_FOUND).body(new ApiResponse("no product Found " , null));
            }
            List<ProductDto> productDtos = productService.getConvertedProducts(products);
            return ResponseEntity.ok(new ApiResponse("success" , productDtos));
        } catch (Exception e) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse(e.getMessage() , null));
        }
    }

    @GetMapping("/products/{name}/products")
    public ResponseEntity<ApiResponse> getProductByName(@PathVariable String name){
        try {
            List<Product> products = productService.getProductByName(name);
            List<ProductDto> productDtos = productService.getConvertedProducts(products);
            return ResponseEntity.ok(new ApiResponse("success" , productDtos));
        } catch (Exception e) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse( "error", e.getMessage()));
        }
    }

    @GetMapping("/product/by-brand")
    public ResponseEntity<ApiResponse> findProductByBrand(@RequestParam String brand){
        try {
            List<Product> products = productService.getProductByBrand(brand);
            List<ProductDto> productDtos = productService.getConvertedProducts(products);
            if (products.isEmpty())
            {
                return ResponseEntity.status(NOT_FOUND).body(new ApiResponse("no product Found " , null));
            }
            return ResponseEntity.ok(new ApiResponse("success" , productDtos));
        } catch (Exception e) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse( "error", e.getMessage()));
        }
    }

    @GetMapping("/product/{category}/all/products")
    public ResponseEntity<ApiResponse> findProductsByCategory(@PathVariable String category){
        try {
            List<Product> products = productService.getProductsByCategory(category);
            List<ProductDto> productDtos = productService.getConvertedProducts(products);
            if (products.isEmpty())
            {
                return ResponseEntity.status(NOT_FOUND).body(new ApiResponse("no product Found " , null));
            }
            return ResponseEntity.ok(new ApiResponse("success" , productDtos));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse( "error", e.getMessage()));
        }
    }

    @GetMapping("/product/count/by-brand/and-name")
    public  ResponseEntity<ApiResponse> CountProductsByBrandAndName(@RequestParam String brand, @RequestParam String name){
        try {
            var productCount=productService.countProductsByBrandAndName(brand,name);
            return ResponseEntity.ok(new ApiResponse("success" , productCount));
        } catch (Exception e) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse( "error", e.getMessage()));
        }
    }
}
