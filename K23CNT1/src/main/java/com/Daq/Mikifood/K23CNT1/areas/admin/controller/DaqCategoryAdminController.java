package com.Daq.Mikifood.K23CNT1.areas.admin.controller;

import com.Daq.Mikifood.K23CNT1.entity.DaqCategory;
import com.Daq.Mikifood.K23CNT1.areas.admin.service.DaqCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/admin/daqCategories")
public class DaqCategoryAdminController {
    
    @Autowired
    private DaqCategoryService categoryService;
    
    // Hiển thị danh sách danh mục
    @GetMapping
    public String index(Model model) {
        List<DaqCategory> categories = categoryService.getAllCategories();
        model.addAttribute("categories", categories);
        model.addAttribute("pageTitle", "Quản lý Danh mục");
        return "areas/admin/DaqDanhMuc/index";
    }
    
    // Hiển thị form thêm mới
    @GetMapping("/create")
    public String create(Model model) {
        model.addAttribute("category", new DaqCategory());
        model.addAttribute("pageTitle", "Thêm Danh mục mới");
        return "areas/admin/DaqDanhMuc/create";
    }
    
    // Xử lý thêm mới
    @PostMapping("/create")
    public String store(@ModelAttribute DaqCategory category, RedirectAttributes redirectAttributes) {
        try {
            categoryService.saveCategory(category);
            redirectAttributes.addFlashAttribute("success", "Thêm danh mục thành công!");
            return "redirect:/admin/daqCategories";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Có lỗi xảy ra: " + e.getMessage());
            return "redirect:/admin/daqCategories/create";
        }
    }
    
    // Hiển thị form chỉnh sửa
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable Integer id, Model model, RedirectAttributes redirectAttributes) {
        try {
            DaqCategory category = categoryService.getCategoryById(id)
                    .orElseThrow(() -> new RuntimeException("Không tìm thấy danh mục"));
            model.addAttribute("category", category);
            model.addAttribute("pageTitle", "Chỉnh sửa Danh mục");
            return "areas/admin/DaqDanhMuc/edit";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Không tìm thấy danh mục!");
            return "redirect:/admin/daqCategories";
        }
    }
    
    // Xử lý cập nhật
    @PostMapping("/edit/{id}")
    public String update(@PathVariable Integer id, @ModelAttribute DaqCategory category, 
                        RedirectAttributes redirectAttributes) {
        try {
            categoryService.updateCategory(id, category);
            redirectAttributes.addFlashAttribute("success", "Cập nhật danh mục thành công!");
            return "redirect:/admin/daqCategories";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Có lỗi xảy ra: " + e.getMessage());
            return "redirect:/admin/daqCategories/edit/" + id;
        }
    }
    
    // Xóa danh mục
    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Integer id, RedirectAttributes redirectAttributes) {
        try {
            categoryService.deleteCategory(id);
            redirectAttributes.addFlashAttribute("success", "Xóa danh mục thành công!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Không thể xóa danh mục: " + e.getMessage());
        }
        return "redirect:/admin/daqCategories";
    }
}
