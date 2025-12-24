package com.Daq.Mikifood.K23CNT1.areas.admin.service;

import com.Daq.Mikifood.K23CNT1.entity.DaqOrderDetail;
import com.Daq.Mikifood.K23CNT1.repository.DaqOrderDetailRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DaqOrderDetailService {
    
    @Autowired
    private DaqOrderDetailRepository orderDetailRepository;
    
    public List<DaqOrderDetail> getAllOrderDetails() {
        return orderDetailRepository.findAll();
    }
    
    public Optional<DaqOrderDetail> getOrderDetailById(Integer id) {
        return orderDetailRepository.findById(id);
    }
    
    public List<DaqOrderDetail> getOrderDetailsByOrder(Integer orderId) {
        return orderDetailRepository.findByOrder_OrderId(orderId);
    }
    
    public List<DaqOrderDetail> getOrderDetailsByProduct(Integer productId) {
        return orderDetailRepository.findByProduct_ProductId(productId);
    }
    
    public DaqOrderDetail saveOrderDetail(DaqOrderDetail orderDetail) {
        return orderDetailRepository.save(orderDetail);
    }
    
    public void deleteOrderDetail(Integer id) {
        orderDetailRepository.deleteById(id);
    }
    
    public DaqOrderDetail updateOrderDetail(Integer id, DaqOrderDetail orderDetailDetails) {
        DaqOrderDetail orderDetail = orderDetailRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order detail not found"));
        
        orderDetail.setOrder(orderDetailDetails.getOrder());
        orderDetail.setProduct(orderDetailDetails.getProduct());
        orderDetail.setQuantity(orderDetailDetails.getQuantity());
        orderDetail.setPrice(orderDetailDetails.getPrice());
        
        return orderDetailRepository.save(orderDetail);
    }
}
