package com.Daq.Mikifood.K23CNT1.areas.admin.service;

import com.Daq.Mikifood.K23CNT1.entity.DaqOrder;
import com.Daq.Mikifood.K23CNT1.repository.DaqOrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DaqOrderService {

    @Autowired
    private DaqOrderRepository orderRepository;

    public List<DaqOrder> getAllOrders() {
        return orderRepository.findAll();
    }

    public Optional<DaqOrder> getOrderById(Integer id) {
        return orderRepository.findById(id);
    }

    public List<DaqOrder> getOrdersByStaff(Integer staffId) {
        return orderRepository.findByStaff_StaffId(staffId);
    }

    public List<DaqOrder> getOrdersByStatus(String status) {
        return orderRepository.findByStatus(status);
    }

    public DaqOrder saveOrder(DaqOrder order) {
        return orderRepository.save(order);
    }

    public void deleteOrder(Integer id) {
        orderRepository.deleteById(id);
    }

    public DaqOrder updateOrder(Integer id, DaqOrder orderDetails) {
        DaqOrder order = orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        order.setStaff(orderDetails.getStaff());
        order.setOrderDate(orderDetails.getOrderDate());
        order.setTotalMoney(orderDetails.getTotalMoney());
        order.setStatus(orderDetails.getStatus());

        return orderRepository.save(order);
    }

    public DaqOrder updateOrderStatus(Integer id, String status) {
        DaqOrder order = orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        order.setStatus(status);
        return orderRepository.save(order);
    }
}
