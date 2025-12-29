package com.Daq.Mikifood.K23CNT1.controller;

import com.Daq.Mikifood.K23CNT1.entity.DaqProduct;
import com.Daq.Mikifood.K23CNT1.model.CartItem;
import com.Daq.Mikifood.K23CNT1.repository.DaqProductRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/cart")
public class CartController {

    @Autowired
    private DaqProductRepository productRepository;

    // Lấy giỏ hàng từ session
    private Map<Integer, CartItem> getCart(HttpSession session) {
        Map<Integer, CartItem> cart = (Map<Integer, CartItem>) session.getAttribute("cart");
        if (cart == null) {
            cart = new HashMap<>();
            session.setAttribute("cart", cart);
        }
        return cart;
    }

    // Thêm sản phẩm vào giỏ hàng (hỗ trợ cả POST và GET)
    @PostMapping("/add")
    public String addToCartPost(@RequestParam("productId") Integer productId,
            @RequestParam(value = "quantity", defaultValue = "1") Integer quantity,
            HttpSession session,
            RedirectAttributes redirectAttributes) {
        return addToCart(productId, quantity, session, redirectAttributes);
    }

    @GetMapping("/add")
    public String addToCartGet(@RequestParam("productId") Integer productId,
            @RequestParam(value = "quantity", defaultValue = "1") Integer quantity,
            HttpSession session,
            RedirectAttributes redirectAttributes) {
        return addToCart(productId, quantity, session, redirectAttributes);
    }

    // Logic chung cho thêm vào giỏ hàng
    private String addToCart(Integer productId, Integer quantity,
            HttpSession session, RedirectAttributes redirectAttributes) {
        try {
            DaqProduct product = productRepository.findById(productId).orElse(null);

            if (product == null) {
                redirectAttributes.addFlashAttribute("error", "Không tìm thấy sản phẩm!");
                return "redirect:/products";
            }

            // Kiểm tra tồn kho
            if (product.getQuantity() < quantity) {
                redirectAttributes.addFlashAttribute("error", "Sản phẩm không đủ số lượng!");
                return "redirect:/products";
            }

            Map<Integer, CartItem> cart = getCart(session);

            // Nếu sản phẩm đã có trong giỏ, tăng số lượng
            if (cart.containsKey(productId)) {
                CartItem item = cart.get(productId);
                item.setQuantity(item.getQuantity() + quantity);
            } else {
                // Thêm sản phẩm mới vào giỏ
                CartItem item = new CartItem(
                        product.getProductId(),
                        product.getProductName(),
                        product.getPrice(),
                        quantity,
                        product.getImage());
                cart.put(productId, item);
            }

            redirectAttributes.addFlashAttribute("success", "Đã thêm sản phẩm vào giỏ hàng!");
            return "redirect:/cart";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Lỗi: " + e.getMessage());
            return "redirect:/products";
        }
    }

    // Xem giỏ hàng
    @GetMapping({ "", "/" })
    public String viewCart(HttpSession session, Model model) {
        Map<Integer, CartItem> cart = getCart(session);

        // Tính tổng tiền
        BigDecimal total = cart.values().stream()
                .map(CartItem::getSubtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        model.addAttribute("cartItems", cart.values());
        model.addAttribute("total", total);
        model.addAttribute("cartSize", cart.size());

        return "cart";
    }

    // Cập nhật số lượng
    @PostMapping("/update")
    public String updateQuantity(@RequestParam("productId") Integer productId,
            @RequestParam("quantity") Integer quantity,
            HttpSession session,
            RedirectAttributes redirectAttributes) {
        try {
            if (quantity <= 0) {
                redirectAttributes.addFlashAttribute("error", "Số lượng phải lớn hơn 0!");
                return "redirect:/cart";
            }

            Map<Integer, CartItem> cart = getCart(session);

            if (cart.containsKey(productId)) {
                // Kiểm tra tồn kho
                DaqProduct product = productRepository.findById(productId).orElse(null);
                if (product != null && product.getQuantity() >= quantity) {
                    cart.get(productId).setQuantity(quantity);
                    redirectAttributes.addFlashAttribute("success", "Đã cập nhật số lượng!");
                } else {
                    redirectAttributes.addFlashAttribute("error", "Sản phẩm không đủ số lượng!");
                }
            }

            return "redirect:/cart";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Lỗi: " + e.getMessage());
            return "redirect:/cart";
        }
    }

    // Xóa sản phẩm khỏi giỏ hàng
    @GetMapping("/remove/{productId}")
    public String removeFromCart(@PathVariable("productId") Integer productId,
            HttpSession session,
            RedirectAttributes redirectAttributes) {
        Map<Integer, CartItem> cart = getCart(session);
        cart.remove(productId);

        redirectAttributes.addFlashAttribute("success", "Đã xóa sản phẩm khỏi giỏ hàng!");
        return "redirect:/cart";
    }

    // Xóa toàn bộ giỏ hàng
    @GetMapping("/clear")
    public String clearCart(HttpSession session, RedirectAttributes redirectAttributes) {
        session.removeAttribute("cart");
        redirectAttributes.addFlashAttribute("success", "Đã xóa toàn bộ giỏ hàng!");
        return "redirect:/cart";
    }
}
