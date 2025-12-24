package com.Daq.Mikifood.K23CNT1.areas.admin.service;

import com.Daq.Mikifood.K23CNT1.entity.DaqCategory;
import com.Daq.Mikifood.K23CNT1.repository.DaqCategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DaqCategoryService {
    
    @Autowired
    private DaqCategoryRepository categoryRepository;
    
    public List<DaqCategory> getAllCategories() {
        return categoryRepository.findAll();
    }
    
    public Optional<DaqCategory> getCategoryById(Integer id) {
        return categoryRepository.findById(id);
    }
    
    public Optional<DaqCategory> getCategoryByName(String name) {
        return categoryRepository.findByCategoryName(name);
    }
    
    public DaqCategory saveCategory(DaqCategory category) {
        return categoryRepository.save(category);
    }
    
    public void deleteCategory(Integer id) {
        categoryRepository.deleteById(id);
    }
    
    public DaqCategory updateCategory(Integer id, DaqCategory categoryDetails) {
        DaqCategory category = categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Category not found"));
        
        category.setCategoryName(categoryDetails.getCategoryName());
        return categoryRepository.save(category);
    }
}
