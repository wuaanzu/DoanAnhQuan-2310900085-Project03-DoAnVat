package com.Daq.Mikifood.K23CNT1.controller;

import com.Daq.Mikifood.K23CNT1.entity.DaqStaff;
import com.Daq.Mikifood.K23CNT1.areas.admin.service.DaqStaffService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/staff")
public class DaqStaffController {
    
    @Autowired
    private DaqStaffService staffService;
    
    @GetMapping
    public List<DaqStaff> getAllStaff() {
        return staffService.getAllStaff();
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<DaqStaff> getStaffById(@PathVariable Integer id) {
        return staffService.getStaffById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping("/phone/{phone}")
    public ResponseEntity<DaqStaff> getStaffByPhone(@PathVariable String phone) {
        return staffService.getStaffByPhone(phone)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping("/role/{role}")
    public List<DaqStaff> getStaffByRole(@PathVariable String role) {
        return staffService.getStaffByRole(role);
    }
    
    @PostMapping
    public DaqStaff createStaff(@RequestBody DaqStaff staff) {
        return staffService.saveStaff(staff);
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<DaqStaff> updateStaff(@PathVariable Integer id, @RequestBody DaqStaff staff) {
        try {
            DaqStaff updatedStaff = staffService.updateStaff(id, staff);
            return ResponseEntity.ok(updatedStaff);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStaff(@PathVariable Integer id) {
        staffService.deleteStaff(id);
        return ResponseEntity.noContent().build();
    }
}
