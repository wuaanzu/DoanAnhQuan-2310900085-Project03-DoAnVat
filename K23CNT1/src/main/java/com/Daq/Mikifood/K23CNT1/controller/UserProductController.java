package com.Daq.Mikifood.K23CNT1.controller;

import com.Daq.Mikifood.K23CNT1.entity.DaqProduct;
import com.Daq.Mikifood.K23CNT1.entity.DaqBanner;
import com.Daq.Mikifood.K23CNT1.repository.DaqProductRepository;
import com.Daq.Mikifood.K23CNT1.repository.DaqBannerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/products")
public class UserProductController {

    @Autowired
    private DaqProductRepository productRepository;

    @Autowired
    private DaqBannerRepository bannerRepository;

    // Hiển thị danh sách sản phẩm cho người dùng
    @GetMapping({ "", "/" })
    public String index(Model model) {
        List<DaqProduct> products = productRepository.findAll();
        List<DaqBanner> banners = bannerRepository.findByIsActiveTrueOrderByDisplayOrderAsc();

        model.addAttribute("products", products);
        model.addAttribute("banners", banners);
        return "areas/user/products/index";
    }

    // Tìm kiếm sản phẩm theo keyword
    @GetMapping("/search")
    public String search(@RequestParam(value = "keyword", required = false) String keyword, Model model) {
        List<DaqProduct> products;
        List<DaqBanner> banners = bannerRepository.findByIsActiveTrueOrderByDisplayOrderAsc();

        if (keyword != null && !keyword.trim().isEmpty()) {
            products = productRepository.searchByKeyword(keyword.trim());
            model.addAttribute("keyword", keyword);
        } else {
            products = productRepository.findAll();
        }

        model.addAttribute("products", products);
        model.addAttribute("banners", banners);
        model.addAttribute("searchResultCount", products.size());

        return "areas/user/products/index";
    }

    // Hiển thị chi tiết sản phẩm
    @GetMapping("/detail/{id}")
    public String detail(@PathVariable("id") Integer id, Model model) {
        DaqProduct product = productRepository.findById(id).orElse(null);

        if (product == null) {
            return "redirect:/products";
        }

        model.addAttribute("product", product);

        // Lấy sản phẩm liên quan (cùng danh mục)
        if (product.getCategory() != null) {
            List<DaqProduct> relatedProducts = productRepository
                    .findByCategory(product.getCategory())
                    .stream()
                    .filter(p -> !p.getProductId().equals(id))
                    .limit(4)
                    .toList();
            model.addAttribute("relatedProducts", relatedProducts);
        }

        return "areas/user/products/detail";
    }
}
