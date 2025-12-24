package com.Daq.Mikifood.K23CNT1.controller;

import com.Daq.Mikifood.K23CNT1.entity.DaqCategory;
import com.Daq.Mikifood.K23CNT1.areas.admin.service.DaqCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
public class DaqCategoryController {
    
    @Autowired
    private DaqCategoryService categoryService;
    
    @GetMapping
    public List<DaqCategory> getAllCategories() {
        return categoryService.getAllCategories();
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<DaqCategory> getCategoryById(@PathVariable Integer id) {
        return categoryService.getCategoryById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping("/search")
    public ResponseEntity<DaqCategory> getCategoryByName(@RequestParam String name) {
        return categoryService.getCategoryByName(name)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    @PostMapping
    public DaqCategory createCategory(@RequestBody DaqCategory category) {
        return categoryService.saveCategory(category);
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<DaqCategory> updateCategory(@PathVariable Integer id, @RequestBody DaqCategory category) {
        try {
            DaqCategory updatedCategory = categoryService.updateCategory(id, category);
            return ResponseEntity.ok(updatedCategory);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCategory(@PathVariable Integer id) {
        categoryService.deleteCategory(id);
        return ResponseEntity.noContent().build();
    }
}
