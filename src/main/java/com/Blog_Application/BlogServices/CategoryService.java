package com.Blog_Application.BlogServices;

import java.util.List;
import com.Blog_Application.Payload.CategoryDto;

public interface CategoryService {

    CategoryDto createCategory(CategoryDto categoryDto);

    CategoryDto updateCategory(CategoryDto categoryDto, int categoryId);

    CategoryDto getCategoryById(int categoryId);

    List<CategoryDto> getAllCategory();

    void deleteCategory(int categoryId);
}
