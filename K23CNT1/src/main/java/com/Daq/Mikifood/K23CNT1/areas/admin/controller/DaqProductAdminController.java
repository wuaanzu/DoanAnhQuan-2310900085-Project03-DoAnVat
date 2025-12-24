package com.Daq.Mikifood.K23CNT1.areas.admin.controller;

import com.Daq.Mikifood.K23CNT1.entity.DaqCategory;
import com.Daq.Mikifood.K23CNT1.entity.DaqProduct;
import com.Daq.Mikifood.K23CNT1.areas.admin.service.DaqCategoryService;
import com.Daq.Mikifood.K23CNT1.areas.admin.service.DaqProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/admin/daqProducts")
public class DaqProductAdminController {
    
    @Autowired
    private DaqProductService productService;
    
    @Autowired
    private DaqCategoryService categoryService;
    
    private static final String UPLOAD_DIR = "src/main/resources/static/uploads/";
    
    // Hiển thị danh sách sản phẩm
    @GetMapping
    public String index(Model model) {
        List<DaqProduct> products = productService.getAllProducts();
        model.addAttribute("products", products);
        model.addAttribute("pageTitle", "Quản lý Sản phẩm");
        return "areas/admin/DaqSanPham/index";
    }
    
    // Hiển thị form thêm mới
    @GetMapping("/create")
    public String create(Model model) {
        model.addAttribute("product", new DaqProduct());
        model.addAttribute("categories", categoryService.getAllCategories());
        model.addAttribute("pageTitle", "Thêm Sản phẩm mới");
        return "areas/admin/DaqSanPham/create";
    }
    
    // Xử lý thêm mới
    @PostMapping("/create")
    public String store(@ModelAttribute DaqProduct product,
                       @RequestParam("imageFile") MultipartFile imageFile,
                       @RequestParam("categoryId") Integer categoryId,
                       RedirectAttributes redirectAttributes) {
        try {
            // Xử lý upload ảnh
            if (!imageFile.isEmpty()) {
                String fileName = saveImage(imageFile);
                product.setImage(fileName);
            }
            
            // Set category
            DaqCategory category = categoryService.getCategoryById(categoryId)
                    .orElseThrow(() -> new RuntimeException("Category not found"));
            product.setCategory(category);
            
            productService.saveProduct(product);
            redirectAttributes.addFlashAttribute("success", "Thêm sản phẩm thành công!");
            return "redirect:/admin/daqProducts";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Có lỗi xảy ra: " + e.getMessage());
            return "redirect:/admin/daqProducts/create";
        }
    }
    
    // Hiển thị form chỉnh sửa
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable Integer id, Model model, RedirectAttributes redirectAttributes) {
        try {
            DaqProduct product = productService.getProductById(id)
                    .orElseThrow(() -> new RuntimeException("Không tìm thấy sản phẩm"));
            model.addAttribute("product", product);
            model.addAttribute("categories", categoryService.getAllCategories());
            model.addAttribute("pageTitle", "Chỉnh sửa Sản phẩm");
            return "areas/admin/DaqSanPham/edit";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Không tìm thấy sản phẩm!");
            return "redirect:/admin/daqProducts";
        }
    }
    
    // Xử lý cập nhật
    @PostMapping("/edit/{id}")
    public String update(@PathVariable Integer id,
                        @ModelAttribute DaqProduct product,
                        @RequestParam(value = "imageFile", required = false) MultipartFile imageFile,
                        @RequestParam("categoryId") Integer categoryId,
                        RedirectAttributes redirectAttributes) {
        try {
            DaqProduct existingProduct = productService.getProductById(id)
                    .orElseThrow(() -> new RuntimeException("Product not found"));
            
            // Xử lý upload ảnh mới nếu có
            if (imageFile != null && !imageFile.isEmpty()) {
                String fileName = saveImage(imageFile);
                product.setImage(fileName);
            } else {
                product.setImage(existingProduct.getImage());
            }
            
            // Set category
            DaqCategory category = categoryService.getCategoryById(categoryId)
                    .orElseThrow(() -> new RuntimeException("Category not found"));
            product.setCategory(category);
            
            productService.updateProduct(id, product);
            redirectAttributes.addFlashAttribute("success", "Cập nhật sản phẩm thành công!");
            return "redirect:/admin/daqProducts";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Có lỗi xảy ra: " + e.getMessage());
            return "redirect:/admin/daqProducts/edit/" + id;
        }
    }
    
    // Xóa sản phẩm
    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Integer id, RedirectAttributes redirectAttributes) {
        try {
            productService.deleteProduct(id);
            redirectAttributes.addFlashAttribute("success", "Xóa sản phẩm thành công!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Không thể xóa sản phẩm: " + e.getMessage());
        }
        return "redirect:/admin/daqProducts";
    }
    
    // Helper method để lưu ảnh
    private String saveImage(MultipartFile file) throws IOException {
        // Tạo thư mục nếu chưa tồn tại
        File uploadDir = new File(UPLOAD_DIR);
        if (!uploadDir.exists()) {
            uploadDir.mkdirs();
        }
        
        // Tạo tên file unique
        String originalFilename = file.getOriginalFilename();
        String extension = originalFilename.substring(originalFilename.lastIndexOf("."));
        String fileName = UUID.randomUUID().toString() + extension;
        
        // Lưu file
        Path filePath = Paths.get(UPLOAD_DIR + fileName);
        Files.write(filePath, file.getBytes());
        
        return fileName;
    }
}
