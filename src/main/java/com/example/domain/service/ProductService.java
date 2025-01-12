package com.example.domain.service;

import com.example.domain.model.Category;
import com.example.domain.model.Product;
import io.quarkus.cache.CacheInvalidate;
import io.quarkus.cache.CacheResult;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import jakarta.inject.Inject;
import com.example.infrastructure.CategoryRepository;
import com.example.infrastructure.ProductRepository;

import java.util.List;

@ApplicationScoped
public class ProductService {



    @Inject
    ProductRepository productRepository;

    @Inject
    private CategoryRepository categoryRepository;



    @CacheResult(cacheName = "productCache")
    public Product getProductById(Long id) {
        return  productRepository.findById(id);
    }

    @Transactional
    public Product createProduct(Product product) {
        Category category = categoryRepository.findByIdOptional(product.getCategoryId())
                .orElseThrow(() -> new IllegalArgumentException("Category not found"));
        productRepository.persist(product);
        return product;
    }

    @Transactional
    @CacheInvalidate(cacheName = "productCache")
    public Product updateProduct(Long id, Product updatedProduct) {
        Product existingProduct = productRepository.findById(id);
        if (existingProduct != null) {
            existingProduct.setName(updatedProduct.getName());
            existingProduct.setPrice(updatedProduct.getPrice());
            existingProduct.setCategoryId(updatedProduct.getCategoryId());
            productRepository.persist(existingProduct);
        }
        return existingProduct;
    }

    @Transactional
    @CacheInvalidate(cacheName = "productCache")
    public void deleteProduct(Long id) {
        productRepository.deleteById(id);
    }

    public List<Product> getAllProducts() {
        return productRepository.listAll();
    }
}