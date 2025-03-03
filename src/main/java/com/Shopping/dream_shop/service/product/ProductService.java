package com.Shopping.dream_shop.service.product;

import com.Shopping.dream_shop.dto.ImageDto;
import com.Shopping.dream_shop.dto.ProductDto;
import com.Shopping.dream_shop.exception.ProductNotFoundException;
import com.Shopping.dream_shop.exception.ResourceNotFoundException;
import com.Shopping.dream_shop.model.Category;
import com.Shopping.dream_shop.model.Image;
import com.Shopping.dream_shop.model.Product;
import com.Shopping.dream_shop.repository.CategoryRepository;
import com.Shopping.dream_shop.repository.ImageRepository;
import com.Shopping.dream_shop.repository.ProductRepository;
import com.Shopping.dream_shop.request.AddProductRequest;
import com.Shopping.dream_shop.request.UpdateProductRequest;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service

public class ProductService implements IProductService {

    @Autowired
    private final ProductRepository productRepository;
    @Autowired
    private final CategoryRepository categoryRepository;
    @Autowired
    private final ImageRepository imageRepository;
    private final ModelMapper modelMapper;

    public ProductService(ProductRepository productRepository, CategoryRepository categoryRepository, ImageRepository imageRepository, ModelMapper modelMapper) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
        this.imageRepository = imageRepository;
        this.modelMapper = modelMapper;
    }


    @Override
    public Product addProduct(AddProductRequest request) {
        Category category = Optional.ofNullable(categoryRepository.findByName(request.getCategory().getName()))
                .orElseGet(() -> {
                    Category newcategory = new Category(request.getCategory().getName());
                    return categoryRepository.save(newcategory);
                });
        request.setCategory(category);
        return productRepository.save(createProduct(request, category));
    }

    public Product createProduct(AddProductRequest request, Category category) {
        return new Product(
                request.getName(),
                request.getInventory(),
                request.getPrice(),
                request.getBrand(),
                request.getDescription(),
                category
        );
    }


    @Override
    public Product getProductById(Long id) {
        return productRepository.findById(id).
                orElseThrow(() -> new ProductNotFoundException("product not found"));
    }

    @Override
    public void deleteProductById(Long id) {
        productRepository.findById(id).ifPresentOrElse(productRepository::delete, () -> {
            throw new ResourceNotFoundException(" product not found");
        });

    }

    @Override
    public Product updateProductById(UpdateProductRequest request, Long productId) {
        Category category = categoryRepository.findFirstByName(request.getCategory().getName());
        if (category == null) {
            // If no category exists, create a new one
            category = new Category();
            category.setName(request.getCategory().getName());
            category = categoryRepository.save(category);
            request.setCategory(category);
        }
        return productRepository.findById(productId)
                .map(existingproduct ->
                        updateExistingProduct(existingproduct, request))
                .map(productRepository::save)
                .orElseThrow(() -> new ProductNotFoundException("product not found"));
    }

    public Product updateExistingProduct(Product existingProduct, UpdateProductRequest request) {
        existingProduct.setName(request.getName());
        existingProduct.setBrand(request.getBrand());
        existingProduct.setPrice(request.getPrice());
        existingProduct.setDescription(request.getDescription());
        existingProduct.setInventory(request.getInventory());
        Category category = categoryRepository.findByName(request.getCategory().getName());
        existingProduct.setCategory(category);
        return existingProduct;
    }

    @Override
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    @Override
    public List<Product> getProductsByCategory(String category) {
        return productRepository.findByCategoryName(category);
    }

    @Override
    public List<Product> getProductByBrand(String brand) {
        return productRepository.findByBrand(brand);
    }

    @Override
    public List<Product> getProductByCategoryAndBrand(String category, String brand) {
        return productRepository.findByCategoryNameAndBrand(category, brand);
    }

    @Override
    public List<Product> getProductByName(String name) {
        return productRepository.findByName(name);
    }

    @Override
    public List<Product> getProductByBrandAndName(String brand, String name) {
        return productRepository.findByBrandAndName(brand, name);
    }

    @Override
    public Long countProductsByBrandAndName(String brand, String name) {
        return productRepository.countByBrandAndName(brand, name);
    }

    @Override
    public List<ProductDto> getConvertedProducts(List<Product> products) {
        return products.stream().map(this::convertToDto).toList();
    }

    @Override
    public ProductDto convertToDto(Product product) {
        ProductDto productDto = modelMapper.map(product, ProductDto.class);
        List<Image> images = imageRepository.findByProductId(product.getId());
        List<ImageDto> imageDtos = images.stream()
                .map(image -> modelMapper.map(image, ImageDto.class))
                .toList();
        productDto.setImages(imageDtos);
        return productDto;
    }
}
