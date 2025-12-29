package com.Daq.Mikifood.K23CNT1.areas.admin.controller;

import com.Daq.Mikifood.K23CNT1.entity.DaqOrder;
import com.Daq.Mikifood.K23CNT1.entity.DaqOrderDetail;
import com.Daq.Mikifood.K23CNT1.entity.DaqProduct;
import com.Daq.Mikifood.K23CNT1.repository.DaqOrderRepository;
import com.Daq.Mikifood.K23CNT1.repository.DaqOrderDetailRepository;
import com.Daq.Mikifood.K23CNT1.repository.DaqProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/admin/daqOrderDetails")
public class DaqOrderDetailAdminController {

    @Autowired
    private DaqOrderDetailRepository orderDetailRepository;

    @Autowired
    private DaqOrderRepository orderRepository;

    @Autowired
    private DaqProductRepository productRepository;

    // Hiển thị danh sách chi tiết đơn hàng
    @GetMapping({ "", "/" })
    public String index(Model model) {
        List<DaqOrderDetail> orderDetails = orderDetailRepository.findAll();
        model.addAttribute("orderDetails", orderDetails);
        return "areas/admin/DaqChiTietDonHang/index";
    }

    // Hiển thị form thêm mới
    @GetMapping("/create")
    public String create(Model model) {
        DaqOrderDetail orderDetail = new DaqOrderDetail();

        // Load danh sách orders và products
        List<DaqOrder> orders = orderRepository.findAll();
        List<DaqProduct> products = productRepository.findAll();

        model.addAttribute("orderDetail", orderDetail);
        model.addAttribute("orders", orders);
        model.addAttribute("products", products);

        return "areas/admin/DaqChiTietDonHang/create";
    }

    // Xử lý thêm mới
    @PostMapping("/store")
    public String store(@ModelAttribute DaqOrderDetail orderDetail,
            @RequestParam("orderId") Integer orderId,
            @RequestParam("productId") Integer productId,
            RedirectAttributes redirectAttributes) {
        try {
            // Set order và product
            DaqOrder order = orderRepository.findById(orderId).orElse(null);
            DaqProduct product = productRepository.findById(productId).orElse(null);

            if (order == null || product == null) {
                redirectAttributes.addFlashAttribute("error", "Đơn hàng hoặc sản phẩm không tồn tại!");
                return "redirect:/admin/daqOrderDetails/create";
            }

            orderDetail.setOrder(order);
            orderDetail.setProduct(product);

            orderDetailRepository.save(orderDetail);

            redirectAttributes.addFlashAttribute("success", "Thêm chi tiết đơn hàng thành công!");
            return "redirect:/admin/daqOrderDetails";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Lỗi: " + e.getMessage());
            return "redirect:/admin/daqOrderDetails/create";
        }
    }

    // Hiển thị form chỉnh sửa
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") Integer id, Model model, RedirectAttributes redirectAttributes) {
        DaqOrderDetail orderDetail = orderDetailRepository.findById(id).orElse(null);

        if (orderDetail == null) {
            redirectAttributes.addFlashAttribute("error", "Không tìm thấy chi tiết đơn hàng!");
            return "redirect:/admin/daqOrderDetails";
        }

        // Load danh sách orders và products
        List<DaqOrder> orders = orderRepository.findAll();
        List<DaqProduct> products = productRepository.findAll();

        model.addAttribute("orderDetail", orderDetail);
        model.addAttribute("orders", orders);
        model.addAttribute("products", products);

        return "areas/admin/DaqChiTietDonHang/edit";
    }

    // Xử lý cập nhật
    @PostMapping("/update/{id}")
    public String update(@PathVariable("id") Integer id,
            @ModelAttribute DaqOrderDetail orderDetail,
            @RequestParam("orderId") Integer orderId,
            @RequestParam("productId") Integer productId,
            RedirectAttributes redirectAttributes) {
        try {
            DaqOrderDetail existingDetail = orderDetailRepository.findById(id).orElse(null);

            if (existingDetail == null) {
                redirectAttributes.addFlashAttribute("error", "Không tìm thấy chi tiết đơn hàng!");
                return "redirect:/admin/daqOrderDetails";
            }

            // Set order và product
            DaqOrder order = orderRepository.findById(orderId).orElse(null);
            DaqProduct product = productRepository.findById(productId).orElse(null);

            if (order == null || product == null) {
                redirectAttributes.addFlashAttribute("error", "Đơn hàng hoặc sản phẩm không tồn tại!");
                return "redirect:/admin/daqOrderDetails/edit/" + id;
            }

            existingDetail.setOrder(order);
            existingDetail.setProduct(product);
            existingDetail.setQuantity(orderDetail.getQuantity());
            existingDetail.setPrice(orderDetail.getPrice());

            orderDetailRepository.save(existingDetail);

            redirectAttributes.addFlashAttribute("success", "Cập nhật chi tiết đơn hàng thành công!");
            return "redirect:/admin/daqOrderDetails";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Lỗi: " + e.getMessage());
            return "redirect:/admin/daqOrderDetails/edit/" + id;
        }
    }

    // Xóa chi tiết đơn hàng
    @GetMapping("/delete/{id}")
    public String delete(@PathVariable("id") Integer id, RedirectAttributes redirectAttributes) {
        try {
            DaqOrderDetail orderDetail = orderDetailRepository.findById(id).orElse(null);

            if (orderDetail == null) {
                redirectAttributes.addFlashAttribute("error", "Không tìm thấy chi tiết đơn hàng!");
                return "redirect:/admin/daqOrderDetails";
            }

            orderDetailRepository.delete(orderDetail);
            redirectAttributes.addFlashAttribute("success", "Xóa chi tiết đơn hàng thành công!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Lỗi khi xóa: " + e.getMessage());
        }

        return "redirect:/admin/daqOrderDetails";
    }
}
