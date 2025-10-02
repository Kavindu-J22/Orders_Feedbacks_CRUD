package com.foodorder.repository;

import com.foodorder.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    
    // Find orders by customer name (case-insensitive)
    List<Order> findByCustomerNameContainingIgnoreCase(String customerName);
    
    // Find orders by customer email
    List<Order> findByCustomerEmailIgnoreCase(String customerEmail);
    
    // Find orders by status
    List<Order> findByStatus(Order.OrderStatus status);
    
    // Find orders by date range
    List<Order> findByOrderDateBetween(LocalDateTime startDate, LocalDateTime endDate);
    
    // Find orders by customer phone
    List<Order> findByCustomerPhoneContaining(String customerPhone);
    
    // Search orders by multiple criteria
    @Query("SELECT o FROM Order o WHERE " +
           "(:customerName IS NULL OR LOWER(o.customerName) LIKE LOWER(CONCAT('%', :customerName, '%'))) AND " +
           "(:customerEmail IS NULL OR LOWER(o.customerEmail) LIKE LOWER(CONCAT('%', :customerEmail, '%'))) AND " +
           "(:status IS NULL OR o.status = :status) AND " +
           "(:startDate IS NULL OR o.orderDate >= :startDate) AND " +
           "(:endDate IS NULL OR o.orderDate <= :endDate)")
    List<Order> searchOrders(@Param("customerName") String customerName,
                            @Param("customerEmail") String customerEmail,
                            @Param("status") Order.OrderStatus status,
                            @Param("startDate") LocalDateTime startDate,
                            @Param("endDate") LocalDateTime endDate);
    
    // Count orders by status
    long countByStatus(Order.OrderStatus status);
    
    // Find recent orders (last 30 days)
    @Query("SELECT o FROM Order o WHERE o.orderDate >= :thirtyDaysAgo ORDER BY o.orderDate DESC")
    List<Order> findRecentOrders(@Param("thirtyDaysAgo") LocalDateTime thirtyDaysAgo);
    
    // Find top customers by order count
    @Query("SELECT o.customerName, o.customerEmail, COUNT(o) as orderCount " +
           "FROM Order o GROUP BY o.customerName, o.customerEmail " +
           "ORDER BY COUNT(o) DESC")
    List<Object[]> findTopCustomersByOrderCount();
}
