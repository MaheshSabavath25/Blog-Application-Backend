package com.Blog_Application.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import com.Blog_Application.Entities.Category;

public interface CategoryRepo extends JpaRepository<Category, Integer> {

    // ✅ REQUIRED for dynamic category creation
    Optional<Category> findByCategoryTitleIgnoreCase(String categoryTitle);
}
