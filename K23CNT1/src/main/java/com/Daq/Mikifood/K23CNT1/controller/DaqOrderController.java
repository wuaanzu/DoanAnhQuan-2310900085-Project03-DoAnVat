package com.Daq.Mikifood.K23CNT1.controller;

import com.Daq.Mikifood.K23CNT1.entity.DaqOrder;
import com.Daq.Mikifood.K23CNT1.areas.admin.service.DaqOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class DaqOrderController {

    @Autowired
    private DaqOrderService orderService;

    @GetMapping
    public List<DaqOrder> getAllOrders() {
        return orderService.getAllOrders();
    }

    @GetMapping("/{id}")
    public ResponseEntity<DaqOrder> getOrderById(@PathVariable Integer id) {
        return orderService.getOrderById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/staff/{staffId}")
    public List<DaqOrder> getOrdersByStaff(@PathVariable Integer staffId) {
        return orderService.getOrdersByStaff(staffId);
    }

    @GetMapping("/status/{status}")
    public List<DaqOrder> getOrdersByStatus(@PathVariable String status) {
        return orderService.getOrdersByStatus(status);
    }

    @PostMapping
    public DaqOrder createOrder(@RequestBody DaqOrder order) {
        return orderService.saveOrder(order);
    }

    @PutMapping("/{id}")
    public ResponseEntity<DaqOrder> updateOrder(@PathVariable Integer id, @RequestBody DaqOrder order) {
        try {
            DaqOrder updatedOrder = orderService.updateOrder(id, order);
            return ResponseEntity.ok(updatedOrder);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<DaqOrder> updateOrderStatus(@PathVariable Integer id, @RequestParam String status) {
        try {
            DaqOrder updatedOrder = orderService.updateOrderStatus(id, status);
            return ResponseEntity.ok(updatedOrder);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOrder(@PathVariable Integer id) {
        orderService.deleteOrder(id);
        return ResponseEntity.noContent().build();
    }
}
