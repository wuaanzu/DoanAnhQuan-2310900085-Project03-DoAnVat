package com.Daq.Mikifood.K23CNT1.controller;

import com.Daq.Mikifood.K23CNT1.entity.DaqProduct;
import com.Daq.Mikifood.K23CNT1.areas.admin.service.DaqProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
public class DaqProductController {
    
    @Autowired
    private DaqProductService productService;
    
    @GetMapping
    public List<DaqProduct> getAllProducts() {
        return productService.getAllProducts();
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<DaqProduct> getProductById(@PathVariable Integer id) {
        return productService.getProductById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping("/category/{categoryId}")
    public List<DaqProduct> getProductsByCategory(@PathVariable Integer categoryId) {
        return productService.getProductsByCategory(categoryId);
    }
    
    @GetMapping("/search")
    public List<DaqProduct> searchProducts(@RequestParam String name) {
        return productService.searchProductsByName(name);
    }
    
    @PostMapping
    public DaqProduct createProduct(@RequestBody DaqProduct product) {
        return productService.saveProduct(product);
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<DaqProduct> updateProduct(@PathVariable Integer id, @RequestBody DaqProduct product) {
        try {
            DaqProduct updatedProduct = productService.updateProduct(id, product);
            return ResponseEntity.ok(updatedProduct);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Integer id) {
        productService.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }
}
