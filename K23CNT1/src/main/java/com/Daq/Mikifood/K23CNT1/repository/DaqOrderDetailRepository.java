package com.Daq.Mikifood.K23CNT1.repository;

import com.Daq.Mikifood.K23CNT1.entity.DaqOrderDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DaqOrderDetailRepository extends JpaRepository<DaqOrderDetail, Integer> {
    List<DaqOrderDetail> findByOrder_OrderId(Integer orderId);
    List<DaqOrderDetail> findByProduct_ProductId(Integer productId);
}
