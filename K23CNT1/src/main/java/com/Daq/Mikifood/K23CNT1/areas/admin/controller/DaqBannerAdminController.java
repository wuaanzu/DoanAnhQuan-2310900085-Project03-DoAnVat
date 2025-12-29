package com.Daq.Mikifood.K23CNT1.areas.admin.controller;

import com.Daq.Mikifood.K23CNT1.entity.DaqBanner;
import com.Daq.Mikifood.K23CNT1.entity.DaqCategory;
import com.Daq.Mikifood.K23CNT1.repository.DaqBannerRepository;
import com.Daq.Mikifood.K23CNT1.repository.DaqCategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@Controller
@RequestMapping("/admin/daqBanner")
public class DaqBannerAdminController {

    @Autowired
    private DaqBannerRepository bannerRepository;

    @Autowired
    private DaqCategoryRepository categoryRepository;

    private static final String UPLOAD_DIR = "src/main/resources/static/uploads/";

    // Hiển thị danh sách banner
    @GetMapping({ "", "/" })
    public String index(Model model) {
        List<DaqBanner> banners = bannerRepository.findAll();
        model.addAttribute("banners", banners);
        return "areas/admin/DaqBanner/index";
    }

    // Hiển thị form thêm mới
    @GetMapping("/create")
    public String create(Model model) {
        DaqBanner banner = new DaqBanner();
        List<DaqCategory> categories = categoryRepository.findAll();
        model.addAttribute("banner", banner);
        model.addAttribute("categories", categories);
        return "areas/admin/DaqBanner/create";
    }

    // Xử lý thêm mới
    @PostMapping("/store")
    public String store(@ModelAttribute DaqBanner banner,
            @RequestParam("imageFile") MultipartFile file,
            RedirectAttributes redirectAttributes) {
        try {
            // Upload ảnh
            if (!file.isEmpty()) {
                String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
                Path path = Paths.get(UPLOAD_DIR + fileName);
                Files.createDirectories(path.getParent());
                Files.write(path, file.getBytes());
                banner.setImage(fileName);
            }

            bannerRepository.save(banner);
            redirectAttributes.addFlashAttribute("success", "Thêm banner thành công!");
            return "redirect:/admin/daqBanner";
        } catch (IOException e) {
            redirectAttributes.addFlashAttribute("error", "Lỗi upload ảnh: " + e.getMessage());
            return "redirect:/admin/daqBanner/create";
        }
    }

    // Hiển thị form chỉnh sửa
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") Integer id, Model model) {
        DaqBanner banner = bannerRepository.findById(id).orElse(null);
        List<DaqCategory> categories = categoryRepository.findAll();

        if (banner == null) {
            return "redirect:/admin/daqBanner";
        }

        model.addAttribute("banner", banner);
        model.addAttribute("categories", categories);
        return "areas/admin/DaqBanner/edit";
    }

    // Xử lý cập nhật
    @PostMapping("/update/{id}")
    public String update(@PathVariable("id") Integer id,
            @ModelAttribute DaqBanner banner,
            @RequestParam("imageFile") MultipartFile file,
            RedirectAttributes redirectAttributes) {
        try {
            DaqBanner existingBanner = bannerRepository.findById(id).orElse(null);

            if (existingBanner == null) {
                redirectAttributes.addFlashAttribute("error", "Không tìm thấy banner!");
                return "redirect:/admin/daqBanner";
            }

            // Cập nhật thông tin
            existingBanner.setTitle(banner.getTitle());
            existingBanner.setDescription(banner.getDescription());
            existingBanner.setLinkUrl(banner.getLinkUrl());
            existingBanner.setCategory(banner.getCategory());
            existingBanner.setDisplayOrder(banner.getDisplayOrder());
            existingBanner.setIsActive(banner.getIsActive());

            // Upload ảnh mới nếu có
            if (!file.isEmpty()) {
                String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
                Path path = Paths.get(UPLOAD_DIR + fileName);
                Files.createDirectories(path.getParent());
                Files.write(path, file.getBytes());
                existingBanner.setImage(fileName);
            }

            bannerRepository.save(existingBanner);
            redirectAttributes.addFlashAttribute("success", "Cập nhật banner thành công!");
            return "redirect:/admin/daqBanner";
        } catch (IOException e) {
            redirectAttributes.addFlashAttribute("error", "Lỗi upload ảnh: " + e.getMessage());
            return "redirect:/admin/daqBanner/edit/" + id;
        }
    }

    // Xóa banner
    @GetMapping("/delete/{id}")
    public String delete(@PathVariable("id") Integer id, RedirectAttributes redirectAttributes) {
        try {
            bannerRepository.deleteById(id);
            redirectAttributes.addFlashAttribute("success", "Xóa banner thành công!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Lỗi: " + e.getMessage());
        }
        return "redirect:/admin/daqBanner";
    }
}
