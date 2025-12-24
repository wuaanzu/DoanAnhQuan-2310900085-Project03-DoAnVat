package com.Daq.Mikifood.K23CNT1.repository;

import com.Daq.Mikifood.K23CNT1.entity.DaqOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DaqOrderRepository extends JpaRepository<DaqOrder, Integer> {
    List<DaqOrder> findByStaff_StaffId(Integer staffId);

    List<DaqOrder> findByStatus(String status);
}
