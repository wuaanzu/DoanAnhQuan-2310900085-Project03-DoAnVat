package com.Daq.Mikifood.K23CNT1.controller;

import com.Daq.Mikifood.K23CNT1.entity.DaqStaff;
import com.Daq.Mikifood.K23CNT1.repository.DaqStaffRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class AuthController {

    @Autowired
    private DaqStaffRepository staffRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // Hiển thị trang đăng nhập
    @GetMapping("/login")
    public String login(@RequestParam(value = "error", required = false) String error,
            @RequestParam(value = "logout", required = false) String logout,
            Model model) {
        if (error != null) {
            model.addAttribute("error", "Tên đăng nhập hoặc mật khẩu không đúng!");
        }
        if (logout != null) {
            model.addAttribute("message", "Đăng xuất thành công!");
        }
        return "login";
    }

    // Hiển thị trang đăng ký
    @GetMapping("/register")
    public String register(Model model) {
        model.addAttribute("staff", new DaqStaff());
        return "register";
    }

    // Xử lý đăng ký
    @PostMapping("/register")
    public String processRegister(@ModelAttribute DaqStaff staff,
            @RequestParam("confirmPassword") String confirmPassword,
            RedirectAttributes redirectAttributes) {
        try {
            // Kiểm tra username đã tồn tại
            if (staffRepository.findByUsername(staff.getUsername()).isPresent()) {
                redirectAttributes.addFlashAttribute("error", "Tên đăng nhập đã tồn tại!");
                return "redirect:/register";
            }

            // Kiểm tra password khớp
            if (!staff.getPassword().equals(confirmPassword)) {
                redirectAttributes.addFlashAttribute("error", "Mật khẩu xác nhận không khớp!");
                return "redirect:/register";
            }

            // Mặc định role là USER khi đăng ký
            staff.setRole("USER");

            // Hash password
            staff.setPassword(passwordEncoder.encode(staff.getPassword()));

            // Lưu vào database
            staffRepository.save(staff);

            redirectAttributes.addFlashAttribute("success", "Đăng ký thành công! Vui lòng đăng nhập.");
            return "redirect:/login";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Lỗi: " + e.getMessage());
            return "redirect:/register";
        }
    }

    // Trang access denied
    @GetMapping("/access-denied")
    public String accessDenied() {
        return "access-denied";
    }
}
