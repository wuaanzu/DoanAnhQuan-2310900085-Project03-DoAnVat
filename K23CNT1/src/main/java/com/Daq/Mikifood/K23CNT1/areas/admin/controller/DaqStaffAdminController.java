package com.Daq.Mikifood.K23CNT1.areas.admin.controller;

import com.Daq.Mikifood.K23CNT1.entity.DaqStaff;
import com.Daq.Mikifood.K23CNT1.repository.DaqStaffRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/admin/daqStaff")
public class DaqStaffAdminController {

    @Autowired
    private DaqStaffRepository staffRepository;

    @Autowired
    private org.springframework.security.crypto.password.PasswordEncoder passwordEncoder;

    // Hiển thị danh sách quản trị viên
    @GetMapping({ "", "/" })
    public String index(Model model) {
        List<DaqStaff> staffList = staffRepository.findAll();
        model.addAttribute("staffList", staffList);
        return "areas/admin/DaqQuanTriVien/index";
    }

    // Hiển thị form thêm mới
    @GetMapping("/create")
    public String create(Model model) {
        DaqStaff staff = new DaqStaff();
        model.addAttribute("staff", staff);
        return "areas/admin/DaqQuanTriVien/create";
    }

    // Xử lý thêm mới
    @PostMapping("/store")
    public String store(@ModelAttribute DaqStaff staff, RedirectAttributes redirectAttributes) {
        try {
            // Hash password trước khi lưu
            if (staff.getPassword() != null && !staff.getPassword().isEmpty()) {
                staff.setPassword(passwordEncoder.encode(staff.getPassword()));
            }

            staffRepository.save(staff);
            redirectAttributes.addFlashAttribute("success", "Thêm quản trị viên thành công!");
            return "redirect:/admin/daqStaff";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Lỗi: " + e.getMessage());
            return "redirect:/admin/daqStaff/create";
        }
    }

    // Hiển thị form chỉnh sửa
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") Integer id, Model model, RedirectAttributes redirectAttributes) {
        DaqStaff staff = staffRepository.findById(id).orElse(null);

        if (staff == null) {
            redirectAttributes.addFlashAttribute("error", "Không tìm thấy quản trị viên!");
            return "redirect:/admin/daqStaff";
        }

        model.addAttribute("staff", staff);
        return "areas/admin/DaqQuanTriVien/edit";
    }

    // Xử lý cập nhật
    @PostMapping("/update/{id}")
    public String update(@PathVariable("id") Integer id,
            @ModelAttribute DaqStaff staff,
            RedirectAttributes redirectAttributes) {
        try {
            DaqStaff existingStaff = staffRepository.findById(id).orElse(null);

            if (existingStaff == null) {
                redirectAttributes.addFlashAttribute("error", "Không tìm thấy quản trị viên!");
                return "redirect:/admin/daqStaff";
            }

            // Cập nhật thông tin
            existingStaff.setStaffName(staff.getStaffName());
            existingStaff.setPhone(staff.getPhone());
            existingStaff.setEmail(staff.getEmail());
            existingStaff.setAddress(staff.getAddress());
            existingStaff.setRole(staff.getRole());
            existingStaff.setUsername(staff.getUsername());

            // Chỉ hash password nếu có thay đổi (không rỗng)
            if (staff.getPassword() != null && !staff.getPassword().isEmpty()) {
                existingStaff.setPassword(passwordEncoder.encode(staff.getPassword()));
            }

            staffRepository.save(existingStaff);

            redirectAttributes.addFlashAttribute("success", "Cập nhật quản trị viên thành công!");
            return "redirect:/admin/daqStaff";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Lỗi: " + e.getMessage());
            return "redirect:/admin/daqStaff/edit/" + id;
        }
    }

    // Xóa quản trị viên
    @GetMapping("/delete/{id}")
    public String delete(@PathVariable("id") Integer id, RedirectAttributes redirectAttributes) {
        try {
            DaqStaff staff = staffRepository.findById(id).orElse(null);

            if (staff == null) {
                redirectAttributes.addFlashAttribute("error", "Không tìm thấy quản trị viên!");
                return "redirect:/admin/daqStaff";
            }

            staffRepository.delete(staff);
            redirectAttributes.addFlashAttribute("success", "Xóa quản trị viên thành công!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Lỗi khi xóa: " + e.getMessage());
        }

        return "redirect:/admin/daqStaff";
    }
}
