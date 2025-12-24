package com.Daq.Mikifood.K23CNT1.repository;

import com.Daq.Mikifood.K23CNT1.entity.DaqCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DaqCategoryRepository extends JpaRepository<DaqCategory, Integer> {
    Optional<DaqCategory> findByCategoryName(String categoryName);
}
