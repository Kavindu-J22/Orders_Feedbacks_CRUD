package com.foodorder.service;

import com.foodorder.entity.Order;
import com.foodorder.entity.Ticket;
import com.foodorder.repository.OrderRepository;
import com.foodorder.repository.TicketRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class StatisticsService {
    
    @Autowired
    private TicketRepository ticketRepository;
    
    @Autowired
    private OrderRepository orderRepository;
    
    // Get comprehensive dashboard statistics
    public Map<String, Object> getDashboardStatistics() {
        Map<String, Object> stats = new HashMap<>();
        
        // Ticket statistics
        stats.put("totalTickets", ticketRepository.count());
        stats.put("openTickets", ticketRepository.countByStatus(Ticket.TicketStatus.OPEN));
        stats.put("inProgressTickets", ticketRepository.countByStatus(Ticket.TicketStatus.IN_PROGRESS));
        stats.put("resolvedTickets", ticketRepository.countByStatus(Ticket.TicketStatus.RESOLVED));
        stats.put("closedTickets", ticketRepository.countByStatus(Ticket.TicketStatus.CLOSED));
        
        // Priority statistics
        stats.put("urgentTickets", ticketRepository.countByPriority(Ticket.Priority.URGENT));
        stats.put("highPriorityTickets", ticketRepository.countByPriority(Ticket.Priority.HIGH));
        stats.put("mediumPriorityTickets", ticketRepository.countByPriority(Ticket.Priority.MEDIUM));
        stats.put("lowPriorityTickets", ticketRepository.countByPriority(Ticket.Priority.LOW));
        
        // Category statistics
        List<Object[]> categoryStats = ticketRepository.countTicketsByCategory();
        stats.put("ticketsByCategory", categoryStats);
        
        // Top customers by ticket count
        List<Object[]> topCustomersByTickets = ticketRepository.findTopCustomersByTicketCount();
        stats.put("topCustomersByTickets", topCustomersByTickets);
        
        // Order statistics
        stats.put("totalOrders", orderRepository.count());
        stats.put("pendingOrders", orderRepository.countByStatus(Order.OrderStatus.PENDING));
        stats.put("confirmedOrders", orderRepository.countByStatus(Order.OrderStatus.CONFIRMED));
        stats.put("preparingOrders", orderRepository.countByStatus(Order.OrderStatus.PREPARING));
        stats.put("outForDeliveryOrders", orderRepository.countByStatus(Order.OrderStatus.OUT_FOR_DELIVERY));
        stats.put("deliveredOrders", orderRepository.countByStatus(Order.OrderStatus.DELIVERED));
        stats.put("cancelledOrders", orderRepository.countByStatus(Order.OrderStatus.CANCELLED));
        
        // Top customers by order count
        List<Object[]> topCustomersByOrders = orderRepository.findTopCustomersByOrderCount();
        stats.put("topCustomersByOrders", topCustomersByOrders);
        
        return stats;
    }
    
    // Get ticket status distribution
    public Map<String, Long> getTicketStatusDistribution() {
        Map<String, Long> distribution = new HashMap<>();
        distribution.put("OPEN", ticketRepository.countByStatus(Ticket.TicketStatus.OPEN));
        distribution.put("IN_PROGRESS", ticketRepository.countByStatus(Ticket.TicketStatus.IN_PROGRESS));
        distribution.put("RESOLVED", ticketRepository.countByStatus(Ticket.TicketStatus.RESOLVED));
        distribution.put("CLOSED", ticketRepository.countByStatus(Ticket.TicketStatus.CLOSED));
        return distribution;
    }
    
    // Get ticket priority distribution
    public Map<String, Long> getTicketPriorityDistribution() {
        Map<String, Long> distribution = new HashMap<>();
        distribution.put("URGENT", ticketRepository.countByPriority(Ticket.Priority.URGENT));
        distribution.put("HIGH", ticketRepository.countByPriority(Ticket.Priority.HIGH));
        distribution.put("MEDIUM", ticketRepository.countByPriority(Ticket.Priority.MEDIUM));
        distribution.put("LOW", ticketRepository.countByPriority(Ticket.Priority.LOW));
        return distribution;
    }
    
    // Get order status distribution
    public Map<String, Long> getOrderStatusDistribution() {
        Map<String, Long> distribution = new HashMap<>();
        distribution.put("PENDING", orderRepository.countByStatus(Order.OrderStatus.PENDING));
        distribution.put("CONFIRMED", orderRepository.countByStatus(Order.OrderStatus.CONFIRMED));
        distribution.put("PREPARING", orderRepository.countByStatus(Order.OrderStatus.PREPARING));
        distribution.put("OUT_FOR_DELIVERY", orderRepository.countByStatus(Order.OrderStatus.OUT_FOR_DELIVERY));
        distribution.put("DELIVERED", orderRepository.countByStatus(Order.OrderStatus.DELIVERED));
        distribution.put("CANCELLED", orderRepository.countByStatus(Order.OrderStatus.CANCELLED));
        return distribution;
    }
    
    // Get top 5 customers by ticket count
    public List<Object[]> getTop5CustomersByTickets() {
        List<Object[]> allCustomers = ticketRepository.findTopCustomersByTicketCount();
        return allCustomers.size() > 5 ? allCustomers.subList(0, 5) : allCustomers;
    }
    
    // Get top 5 customers by order count
    public List<Object[]> getTop5CustomersByOrders() {
        List<Object[]> allCustomers = orderRepository.findTopCustomersByOrderCount();
        return allCustomers.size() > 5 ? allCustomers.subList(0, 5) : allCustomers;
    }
}
