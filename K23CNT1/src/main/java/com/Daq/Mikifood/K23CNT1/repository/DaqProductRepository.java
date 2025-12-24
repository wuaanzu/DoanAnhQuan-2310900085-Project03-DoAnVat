package com.Daq.Mikifood.K23CNT1.repository;

import com.Daq.Mikifood.K23CNT1.entity.DaqProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DaqProductRepository extends JpaRepository<DaqProduct, Integer> {
    List<DaqProduct> findByCategory_CategoryId(Integer categoryId);
    List<DaqProduct> findByProductNameContaining(String productName);
}
