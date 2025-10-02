package com.foodorder.service;

import com.foodorder.entity.Order;
import com.foodorder.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class OrderService {
    
    @Autowired
    private OrderRepository orderRepository;
    
    // Create a new order
    public Order createOrder(Order order) {
        order.setOrderDate(LocalDateTime.now());
        return orderRepository.save(order);
    }
    
    // Get all orders
    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }
    
    // Get order by ID
    public Optional<Order> getOrderById(Long id) {
        return orderRepository.findById(id);
    }
    
    // Update order
    public Order updateOrder(Order order) {
        return orderRepository.save(order);
    }
    
    // Delete order
    public void deleteOrder(Long id) {
        orderRepository.deleteById(id);
    }
    
    // Search orders by customer name
    public List<Order> searchOrdersByCustomerName(String customerName) {
        return orderRepository.findByCustomerNameContainingIgnoreCase(customerName);
    }
    
    // Search orders by customer email
    public List<Order> searchOrdersByCustomerEmail(String customerEmail) {
        return orderRepository.findByCustomerEmailIgnoreCase(customerEmail);
    }
    
    // Get orders by status
    public List<Order> getOrdersByStatus(Order.OrderStatus status) {
        return orderRepository.findByStatus(status);
    }
    
    // Search orders with multiple criteria
    public List<Order> searchOrders(String customerName, String customerEmail, 
                                   Order.OrderStatus status, LocalDateTime startDate, 
                                   LocalDateTime endDate) {
        return orderRepository.searchOrders(customerName, customerEmail, status, startDate, endDate);
    }
    
    // Get orders by date range
    public List<Order> getOrdersByDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        return orderRepository.findByOrderDateBetween(startDate, endDate);
    }
    
    // Get recent orders (last 30 days)
    public List<Order> getRecentOrders() {
        LocalDateTime thirtyDaysAgo = LocalDateTime.now().minusDays(30);
        return orderRepository.findRecentOrders(thirtyDaysAgo);
    }
    
    // Count orders by status
    public long countOrdersByStatus(Order.OrderStatus status) {
        return orderRepository.countByStatus(status);
    }
    
    // Get top customers by order count
    public List<Object[]> getTopCustomersByOrderCount() {
        return orderRepository.findTopCustomersByOrderCount();
    }
    
    // Update order status
    public Order updateOrderStatus(Long orderId, Order.OrderStatus status) {
        Optional<Order> orderOpt = orderRepository.findById(orderId);
        if (orderOpt.isPresent()) {
            Order order = orderOpt.get();
            order.setStatus(status);
            return orderRepository.save(order);
        }
        throw new RuntimeException("Order not found with id: " + orderId);
    }
}
