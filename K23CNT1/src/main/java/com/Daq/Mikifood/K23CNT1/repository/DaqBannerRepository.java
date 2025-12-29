package com.Daq.Mikifood.K23CNT1.repository;

import com.Daq.Mikifood.K23CNT1.entity.DaqBanner;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DaqBannerRepository extends JpaRepository<DaqBanner, Integer> {
    List<DaqBanner> findByIsActiveTrueOrderByDisplayOrderAsc();
}
