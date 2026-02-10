package com.Blog_Application.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.Blog_Application.BlogServices.CategoryService;
import com.Blog_Application.Payload.CategoryDto;
import com.Blog_Application.Payload.JwtAuthResponse;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @PostMapping
    public ResponseEntity<CategoryDto> createCategory(
            @Valid @RequestBody CategoryDto categoryDto) {

        return new ResponseEntity<>(
                categoryService.createCategory(categoryDto),
                HttpStatus.CREATED
        );
    }

    @PutMapping("/{categoryId}")
    public ResponseEntity<CategoryDto> updateCategory(
            @Valid @RequestBody CategoryDto categoryDto,
            @PathVariable int categoryId) {

        return ResponseEntity.ok(
                categoryService.updateCategory(categoryDto, categoryId)
        );
    }

    @GetMapping("/{categoryId}")
    public ResponseEntity<CategoryDto> getCategoryById(
            @PathVariable int categoryId) {

        return ResponseEntity.ok(
                categoryService.getCategoryById(categoryId)
        );
    }

    @GetMapping
    public ResponseEntity<List<CategoryDto>> getAllCategories() {
        return ResponseEntity.ok(categoryService.getAllCategory());
    }

    @DeleteMapping("/{categoryId}")
    public ResponseEntity<JwtAuthResponse> deleteCategory(
            @PathVariable int categoryId) {

        categoryService.deleteCategory(categoryId);
        return ResponseEntity.ok(
                new JwtAuthResponse()
        );
    }
}
