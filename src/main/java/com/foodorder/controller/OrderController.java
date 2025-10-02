package com.foodorder.controller;

import com.foodorder.entity.Order;
import com.foodorder.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/orders")
public class OrderController {
    
    @Autowired
    private OrderService orderService;
    
    @GetMapping
    public String listOrders(@RequestParam(required = false) String customerName,
                           @RequestParam(required = false) String customerEmail,
                           @RequestParam(required = false) Order.OrderStatus status,
                           @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
                           @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate,
                           Model model) {
        
        List<Order> orders;
        
        if (customerName != null || customerEmail != null || status != null || startDate != null || endDate != null) {
            orders = orderService.searchOrders(customerName, customerEmail, status, startDate, endDate);
        } else {
            orders = orderService.getAllOrders();
        }
        
        model.addAttribute("orders", orders);
        model.addAttribute("customerName", customerName);
        model.addAttribute("customerEmail", customerEmail);
        model.addAttribute("status", status);
        model.addAttribute("startDate", startDate);
        model.addAttribute("endDate", endDate);
        model.addAttribute("orderStatuses", Order.OrderStatus.values());
        
        return "orders/list";
    }
    
    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("order", new Order());
        model.addAttribute("orderStatuses", Order.OrderStatus.values());
        return "orders/form";
    }
    
    @PostMapping
    public String createOrder(@Valid @ModelAttribute Order order, 
                            BindingResult result, 
                            RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            return "orders/form";
        }
        
        try {
            orderService.createOrder(order);
            redirectAttributes.addFlashAttribute("successMessage", "Order created successfully!");
            return "redirect:/orders";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error creating order: " + e.getMessage());
            return "redirect:/orders/new";
        }
    }
    
    @GetMapping("/{id}")
    public String viewOrder(@PathVariable Long id, Model model) {
        Optional<Order> order = orderService.getOrderById(id);
        if (order.isPresent()) {
            model.addAttribute("order", order.get());
            return "orders/view";
        } else {
            return "redirect:/orders?error=Order not found";
        }
    }
    
    @GetMapping("/{id}/edit")
    public String showEditForm(@PathVariable Long id, Model model) {
        Optional<Order> order = orderService.getOrderById(id);
        if (order.isPresent()) {
            model.addAttribute("order", order.get());
            model.addAttribute("orderStatuses", Order.OrderStatus.values());
            return "orders/form";
        } else {
            return "redirect:/orders?error=Order not found";
        }
    }
    
    @PostMapping("/{id}")
    public String updateOrder(@PathVariable Long id, 
                            @Valid @ModelAttribute Order order, 
                            BindingResult result, 
                            RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            return "orders/form";
        }
        
        try {
            order.setId(id);
            orderService.updateOrder(order);
            redirectAttributes.addFlashAttribute("successMessage", "Order updated successfully!");
            return "redirect:/orders/" + id;
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error updating order: " + e.getMessage());
            return "redirect:/orders/" + id + "/edit";
        }
    }
    
    @PostMapping("/{id}/delete")
    public String deleteOrder(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            orderService.deleteOrder(id);
            redirectAttributes.addFlashAttribute("successMessage", "Order deleted successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error deleting order: " + e.getMessage());
        }
        return "redirect:/orders";
    }
    
    @PostMapping("/{id}/status")
    public String updateOrderStatus(@PathVariable Long id, 
                                  @RequestParam Order.OrderStatus status,
                                  RedirectAttributes redirectAttributes) {
        try {
            orderService.updateOrderStatus(id, status);
            redirectAttributes.addFlashAttribute("successMessage", "Order status updated successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error updating order status: " + e.getMessage());
        }
        return "redirect:/orders/" + id;
    }
}
