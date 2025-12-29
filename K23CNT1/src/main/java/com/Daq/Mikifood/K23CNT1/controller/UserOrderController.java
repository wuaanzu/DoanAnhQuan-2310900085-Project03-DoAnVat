package com.Daq.Mikifood.K23CNT1.controller;

import com.Daq.Mikifood.K23CNT1.entity.DaqOrder;
import com.Daq.Mikifood.K23CNT1.entity.DaqOrderDetail;
import com.Daq.Mikifood.K23CNT1.entity.DaqProduct;
import com.Daq.Mikifood.K23CNT1.entity.DaqStaff;
import com.Daq.Mikifood.K23CNT1.model.CartItem;
import com.Daq.Mikifood.K23CNT1.repository.DaqOrderRepository;
import com.Daq.Mikifood.K23CNT1.repository.DaqOrderDetailRepository;
import com.Daq.Mikifood.K23CNT1.repository.DaqProductRepository;
import com.Daq.Mikifood.K23CNT1.repository.DaqStaffRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/orders")
public class UserOrderController {

    @Autowired
    private DaqOrderRepository orderRepository;

    @Autowired
    private DaqOrderDetailRepository orderDetailRepository;

    @Autowired
    private DaqProductRepository productRepository;

    @Autowired
    private DaqStaffRepository staffRepository;

    // Lấy giỏ hàng từ session
    private Map<Integer, CartItem> getCart(HttpSession session) {
        return (Map<Integer, CartItem>) session.getAttribute("cart");
    }

    // Hiển thị trang checkout
    @GetMapping("/checkout")
    public String checkout(HttpSession session, Model model, RedirectAttributes redirectAttributes) {
        Map<Integer, CartItem> cart = getCart(session);

        if (cart == null || cart.isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "Giỏ hàng trống!");
            return "redirect:/cart";
        }

        // Tính tổng tiền
        BigDecimal total = cart.values().stream()
                .map(CartItem::getSubtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        model.addAttribute("cartItems", cart.values());
        model.addAttribute("total", total);

        return "checkout";
    }

    // Xử lý đặt hàng
    @PostMapping("/place")
    public String placeOrder(@RequestParam(value = "staffId", required = false) Integer staffId,
            HttpSession session,
            RedirectAttributes redirectAttributes) {
        try {
            Map<Integer, CartItem> cart = getCart(session);

            if (cart == null || cart.isEmpty()) {
                redirectAttributes.addFlashAttribute("error", "Giỏ hàng trống!");
                return "redirect:/cart";
            }

            // Tính tổng tiền
            BigDecimal totalMoney = cart.values().stream()
                    .map(CartItem::getSubtotal)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            // Tạo đơn hàng mới
            DaqOrder order = new DaqOrder();
            order.setOrderDate(LocalDateTime.now());
            order.setTotalMoney(totalMoney);
            order.setStatus("Chờ xử lý");

            // Set staff nếu có (có thể null cho khách tự đặt)
            if (staffId != null) {
                DaqStaff staff = staffRepository.findById(staffId).orElse(null);
                order.setStaff(staff);
            }

            // Lưu order
            order = orderRepository.save(order);

            // Tạo order details từ giỏ hàng (TỰ ĐỘNG)
            for (CartItem item : cart.values()) {
                DaqProduct product = productRepository.findById(item.getProductId()).orElse(null);

                if (product != null) {
                    DaqOrderDetail detail = new DaqOrderDetail();
                    detail.setOrder(order);
                    detail.setProduct(product);
                    detail.setQuantity(item.getQuantity());
                    detail.setPrice(item.getPrice());

                    orderDetailRepository.save(detail);

                    // Cập nhật số lượng tồn kho
                    product.setQuantity(product.getQuantity() - item.getQuantity());
                    productRepository.save(product);
                }
            }

            // Xóa giỏ hàng sau khi đặt hàng thành công
            session.removeAttribute("cart");

            redirectAttributes.addFlashAttribute("success", "Đặt hàng thành công!");
            redirectAttributes.addFlashAttribute("orderId", order.getOrderId());
            return "redirect:/orders/success";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Lỗi khi đặt hàng: " + e.getMessage());
            return "redirect:/cart";
        }
    }

    // Trang đặt hàng thành công
    @GetMapping("/success")
    public String orderSuccess(Model model) {
        return "order-success";
    }

    // Xem đơn hàng của tôi (cần authentication - tạm thời hiển thị tất cả)
    @GetMapping("/my-orders")
    public String myOrders(Model model) {
        List<DaqOrder> orders = orderRepository.findAll();
        model.addAttribute("orders", orders);
        return "my-orders";
    }

    // Xem chi tiết đơn hàng
    @GetMapping("/detail/{id}")
    public String orderDetail(@PathVariable("id") Integer id, Model model, RedirectAttributes redirectAttributes) {
        DaqOrder order = orderRepository.findById(id).orElse(null);

        if (order == null) {
            redirectAttributes.addFlashAttribute("error", "Không tìm thấy đơn hàng!");
            return "redirect:/orders/my-orders";
        }

        List<DaqOrderDetail> orderDetails = order.getOrderDetails();

        model.addAttribute("order", order);
        model.addAttribute("orderDetails", orderDetails);
        return "order-detail";
    }
}
