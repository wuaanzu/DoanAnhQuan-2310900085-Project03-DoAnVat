package com.Daq.Mikifood.K23CNT1.controller;

import com.Daq.Mikifood.K23CNT1.entity.DaqProduct;
import com.Daq.Mikifood.K23CNT1.repository.DaqProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class HomeController {

    @Autowired
    private DaqProductRepository productRepository;

    @GetMapping({ "/", "/home" })
    public String home(Model model) {
        // Lấy sản phẩm mới nhất (giới hạn 8 sản phẩm)
        List<DaqProduct> featuredProducts = productRepository.findAll()
                .stream()
                .limit(8)
                .toList();

        model.addAttribute("featuredProducts", featuredProducts);
        return "home";
    }
}
