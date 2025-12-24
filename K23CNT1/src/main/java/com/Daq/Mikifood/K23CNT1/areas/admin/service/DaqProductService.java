package com.Daq.Mikifood.K23CNT1.areas.admin.service;

import com.Daq.Mikifood.K23CNT1.entity.DaqProduct;
import com.Daq.Mikifood.K23CNT1.repository.DaqProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DaqProductService {
    
    @Autowired
    private DaqProductRepository productRepository;
    
    public List<DaqProduct> getAllProducts() {
        return productRepository.findAll();
    }
    
    public Optional<DaqProduct> getProductById(Integer id) {
        return productRepository.findById(id);
    }
    
    public List<DaqProduct> getProductsByCategory(Integer categoryId) {
        return productRepository.findByCategory_CategoryId(categoryId);
    }
    
    public List<DaqProduct> searchProductsByName(String name) {
        return productRepository.findByProductNameContaining(name);
    }
    
    public DaqProduct saveProduct(DaqProduct product) {
        return productRepository.save(product);
    }
    
    public void deleteProduct(Integer id) {
        productRepository.deleteById(id);
    }
    
    public DaqProduct updateProduct(Integer id, DaqProduct productDetails) {
        DaqProduct product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));
        
        product.setProductName(productDetails.getProductName());
        product.setPrice(productDetails.getPrice());
        product.setQuantity(productDetails.getQuantity());
        product.setImage(productDetails.getImage());
        product.setDescription(productDetails.getDescription());
        product.setCategory(productDetails.getCategory());
        
        return productRepository.save(product);
    }
}
