package com.Daq.Mikifood.K23CNT1.areas.admin.service;

import com.Daq.Mikifood.K23CNT1.entity.DaqStaff;
import com.Daq.Mikifood.K23CNT1.repository.DaqStaffRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DaqStaffService {
    
    @Autowired
    private DaqStaffRepository staffRepository;
    
    public List<DaqStaff> getAllStaff() {
        return staffRepository.findAll();
    }
    
    public Optional<DaqStaff> getStaffById(Integer id) {
        return staffRepository.findById(id);
    }
    
    public Optional<DaqStaff> getStaffByPhone(String phone) {
        return staffRepository.findByPhone(phone);
    }
    
    public List<DaqStaff> getStaffByRole(String role) {
        return staffRepository.findByRole(role);
    }
    
    public DaqStaff saveStaff(DaqStaff staff) {
        return staffRepository.save(staff);
    }
    
    public void deleteStaff(Integer id) {
        staffRepository.deleteById(id);
    }
    
    public DaqStaff updateStaff(Integer id, DaqStaff staffDetails) {
        DaqStaff staff = staffRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Staff not found"));
        
        staff.setStaffName(staffDetails.getStaffName());
        staff.setPhone(staffDetails.getPhone());
        staff.setRole(staffDetails.getRole());
        
        return staffRepository.save(staff);
    }
}
