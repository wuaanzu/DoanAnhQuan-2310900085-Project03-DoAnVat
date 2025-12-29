package com.Daq.Mikifood.K23CNT1.repository;

import com.Daq.Mikifood.K23CNT1.entity.DaqCategory;
import com.Daq.Mikifood.K23CNT1.entity.DaqProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DaqProductRepository extends JpaRepository<DaqProduct, Integer> {
    List<DaqProduct> findByCategory_CategoryId(Integer categoryId);

    List<DaqProduct> findByProductNameContaining(String productName);

    List<DaqProduct> findByCategory(DaqCategory category);

    // Tìm kiếm sản phẩm theo keyword trong tên hoặc mô tả
    @Query("SELECT p FROM DaqProduct p WHERE " +
            "LOWER(p.productName) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(p.description) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<DaqProduct> searchByKeyword(@Param("keyword") String keyword);
}
