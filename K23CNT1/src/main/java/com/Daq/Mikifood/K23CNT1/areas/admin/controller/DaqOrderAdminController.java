package com.Daq.Mikifood.K23CNT1.areas.admin.controller;

import com.Daq.Mikifood.K23CNT1.entity.DaqOrder;
import com.Daq.Mikifood.K23CNT1.entity.DaqOrderDetail;
import com.Daq.Mikifood.K23CNT1.repository.DaqOrderRepository;
import com.Daq.Mikifood.K23CNT1.repository.DaqOrderDetailRepository;
import com.Daq.Mikifood.K23CNT1.repository.DaqStaffRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/admin/daqOrders")
public class DaqOrderAdminController {

    @Autowired
    private DaqOrderRepository orderRepository;

    @Autowired
    private DaqOrderDetailRepository orderDetailRepository;

    @Autowired
    private DaqStaffRepository staffRepository;

    // Hiển thị danh sách đơn hàng
    @GetMapping({ "", "/" })
    public String index(Model model) {
        List<DaqOrder> orders = orderRepository.findAll();
        model.addAttribute("orders", orders);
        return "areas/admin/DaqDonHang/index";
    }

    // Hiển thị form thêm mới đơn hàng
    @GetMapping("/create")
    public String create(Model model) {
        DaqOrder order = new DaqOrder();
        model.addAttribute("order", order);
        model.addAttribute("staffList", staffRepository.findAll());
        return "areas/admin/DaqDonHang/create";
    }

    // Xử lý thêm mới đơn hàng
    @PostMapping("/store")
    public String store(@ModelAttribute DaqOrder order,
            @RequestParam(value = "staffId", required = false) Integer staffId,
            RedirectAttributes redirectAttributes) {
        try {
            // Set staff nếu có
            if (staffId != null) {
                order.setStaff(staffRepository.findById(staffId).orElse(null));
            }

            // Set ngày đặt hàng hiện tại nếu chưa có
            if (order.getOrderDate() == null) {
                order.setOrderDate(java.time.LocalDateTime.now());
            }

            orderRepository.save(order);

            redirectAttributes.addFlashAttribute("success", "Thêm đơn hàng thành công!");
            return "redirect:/admin/daqOrders";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Lỗi: " + e.getMessage());
            return "redirect:/admin/daqOrders/create";
        }
    }

    // Xem chi tiết đơn hàng
    @GetMapping("/view/{id}")
    public String view(@PathVariable("id") Integer id, Model model, RedirectAttributes redirectAttributes) {
        DaqOrder order = orderRepository.findById(id).orElse(null);

        if (order == null) {
            redirectAttributes.addFlashAttribute("error", "Không tìm thấy đơn hàng!");
            return "redirect:/admin/daqOrders";
        }

        // Lấy danh sách chi tiết đơn hàng
        List<DaqOrderDetail> orderDetails = order.getOrderDetails();

        model.addAttribute("order", order);
        model.addAttribute("orderDetails", orderDetails);
        return "areas/admin/DaqDonHang/view";
    }

    // Hiển thị form cập nhật trạng thái
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") Integer id, Model model, RedirectAttributes redirectAttributes) {
        DaqOrder order = orderRepository.findById(id).orElse(null);

        if (order == null) {
            redirectAttributes.addFlashAttribute("error", "Không tìm thấy đơn hàng!");
            return "redirect:/admin/daqOrders";
        }

        model.addAttribute("order", order);
        return "areas/admin/DaqDonHang/edit";
    }

    // Xử lý cập nhật trạng thái
    @PostMapping("/update/{id}")
    public String update(@PathVariable("id") Integer id,
            @RequestParam("status") String status,
            RedirectAttributes redirectAttributes) {
        try {
            DaqOrder order = orderRepository.findById(id).orElse(null);

            if (order == null) {
                redirectAttributes.addFlashAttribute("error", "Không tìm thấy đơn hàng!");
                return "redirect:/admin/daqOrders";
            }

            // Cập nhật trạng thái
            order.setStatus(status);
            orderRepository.save(order);

            redirectAttributes.addFlashAttribute("success", "Cập nhật trạng thái đơn hàng thành công!");
            return "redirect:/admin/daqOrders/view/" + id;
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Lỗi: " + e.getMessage());
            return "redirect:/admin/daqOrders";
        }
    }

    // Xóa đơn hàng
    @GetMapping("/delete/{id}")
    public String delete(@PathVariable("id") Integer id, RedirectAttributes redirectAttributes) {
        try {
            DaqOrder order = orderRepository.findById(id).orElse(null);

            if (order == null) {
                redirectAttributes.addFlashAttribute("error", "Không tìm thấy đơn hàng!");
                return "redirect:/admin/daqOrders";
            }

            // Xóa đơn hàng (cascade sẽ tự động xóa order_detail)
            orderRepository.delete(order);

            redirectAttributes.addFlashAttribute("success", "Xóa đơn hàng thành công!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Lỗi khi xóa: " + e.getMessage());
        }

        return "redirect:/admin/daqOrders";
    }
}
