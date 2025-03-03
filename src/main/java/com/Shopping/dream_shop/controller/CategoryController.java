package com.Shopping.dream_shop.controller;

import com.Shopping.dream_shop.exception.AlreadyExistsException;
import com.Shopping.dream_shop.exception.ResourceNotFoundException;
import com.Shopping.dream_shop.model.Category;
import com.Shopping.dream_shop.response.ApiResponse;
import com.Shopping.dream_shop.service.category.ICategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.HttpStatus.*;

@RestController
@RequestMapping("${api.prefix}/categories")
public class CategoryController {
    @Autowired
    private final ICategoryService categoryService;

    public CategoryController(ICategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping("/all")
    public ResponseEntity<ApiResponse> getAllCategories(){
        try {
            List<Category> categories = categoryService.getAllCategory();
            return ResponseEntity.ok(new ApiResponse("all categories" , categories));
        } catch (Exception e) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse(e.getMessage(),null));
        }
    }

    @PostMapping("/add")
    public ResponseEntity<ApiResponse> addCategory(@RequestBody Category category){
        try {
            Category theCategory= categoryService.addCategory(category);
            return ResponseEntity.ok(new ApiResponse("category added" , theCategory));
        } catch (AlreadyExistsException e) {
            return ResponseEntity.status(CONFLICT).body(new ApiResponse(e.getMessage(),null));
        }
    }

    @GetMapping("/Category/{id}/category")
    public ResponseEntity<ApiResponse> getCategoryById(Long id){
        try {
            Category theCategory= categoryService.getCategoryById(id);
            return ResponseEntity.ok(new ApiResponse("category found" , theCategory));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(),null));
        }
    }

    @GetMapping("/category/{name}/category")
    public ResponseEntity<ApiResponse> getCategoryByName(@PathVariable String name){
        try {
            Category theCategory= categoryService.getCategoryByName(name);
            return ResponseEntity.ok(new ApiResponse("category found" , theCategory));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(),null));
        }
    }

    @DeleteMapping("/Category/{id}/delete")
    public ResponseEntity<ApiResponse> deleteCategory(@PathVariable  Long id){
        try {
            categoryService.deleteCategory(id);
            return ResponseEntity.ok(new ApiResponse("category found and deleted" , null));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(),null));
        }
    }

    @PutMapping("/category/{id}/update")
    public ResponseEntity<ApiResponse> updateCategory(@PathVariable Long id , @RequestBody Category category)
    {
        try {
            Category theCategory = categoryService.updateCategory(category , id);
            return ResponseEntity.ok(new ApiResponse("update Success" , theCategory));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(),null));
        }

    }
}
