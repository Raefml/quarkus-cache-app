package com.example.domain.service;

import com.example.domain.model.Category;
import com.example.infrastructure.CategoryRepository;
import io.quarkus.cache.CacheInvalidate;
import io.quarkus.cache.CacheInvalidateAll;
import io.quarkus.cache.CacheResult;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import java.util.List;

@ApplicationScoped
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @CacheResult(cacheName = "categoryCache")
    public List<Category> getAllCategories() {
        return categoryRepository.listAll();
    }

    @CacheResult(cacheName = "categoryCache")
    public Category getCategoryById(Long id) {
        return categoryRepository.findByIdOptional(id)
                .orElseThrow(() -> new IllegalArgumentException("Category with ID " + id + " not found"));
    }

    @Transactional
    @CacheInvalidateAll(cacheName = "categoryCache")
    public Category createCategory(Category category) {
        categoryRepository.persist(category);
        return category;
    }

    @Transactional
    @CacheInvalidate(cacheName = "categoryCache")
    @CacheInvalidateAll(cacheName = "categoryCache")
    public Category updateCategory(Long id, Category updatedCategory) {
        Category existingCategory = categoryRepository.findByIdOptional(id)
                .orElseThrow(() -> new IllegalArgumentException("Category with ID " + id + " not found"));

        existingCategory.setName(updatedCategory.getName());
        categoryRepository.persist(existingCategory);
        return existingCategory;
    }

    @Transactional
    @CacheInvalidate(cacheName = "categoryCache")
    @CacheInvalidateAll(cacheName = "categoryCache")
    public void deleteCategory(Long id) {
        Category existingCategory = categoryRepository.findByIdOptional(id)
                .orElseThrow(() -> new IllegalArgumentException("Category with ID " + id + " not found"));

        categoryRepository.delete(existingCategory);
    }
}
