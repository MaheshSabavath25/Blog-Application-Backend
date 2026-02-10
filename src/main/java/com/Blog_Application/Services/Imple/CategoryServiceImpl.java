package com.Blog_Application.Services.Imple;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.Blog_Application.BlogServices.CategoryService;
import com.Blog_Application.Entities.Category;
import com.Blog_Application.Payload.CategoryDto;
import com.Blog_Application.Repository.CategoryRepo;   // ✅ FIXED IMPORT
import com.Blog_Application.Exception.ResourceNotFoundException;

@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryRepo categoryRepo;

    @Override
    public CategoryDto createCategory(CategoryDto dto) {
        Category category = new Category();
        category.setCategoryTitle(dto.getCategoryTitle());
        category.setCategoryDescription(dto.getCategoryDescription());

        return mapToDto(categoryRepo.save(category));
    }

    @Override
    public CategoryDto updateCategory(CategoryDto dto, int categoryId) {
        Category category = categoryRepo.findById(categoryId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Category", "id", categoryId));

        category.setCategoryTitle(dto.getCategoryTitle());
        category.setCategoryDescription(dto.getCategoryDescription());

        return mapToDto(categoryRepo.save(category));
    }

    @Override
    public CategoryDto getCategoryById(int categoryId) {
        Category category = categoryRepo.findById(categoryId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Category", "id", categoryId));

        return mapToDto(category);
    }

    @Override
    public List<CategoryDto> getAllCategory() {
        return categoryRepo.findAll()
                .stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteCategory(int categoryId) {
        Category category = categoryRepo.findById(categoryId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Category", "id", categoryId));

        categoryRepo.delete(category);
    }

    private CategoryDto mapToDto(Category category) {
        CategoryDto dto = new CategoryDto();
        dto.setId(category.getId());
        dto.setCategoryTitle(category.getCategoryTitle());
        dto.setCategoryDescription(category.getCategoryDescription());
        return dto;
    }
}
