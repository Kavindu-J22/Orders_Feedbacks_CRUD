package com.foodorder.repository;

import com.foodorder.entity.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, Long> {
    
    // Find tickets by status
    List<Ticket> findByStatus(Ticket.TicketStatus status);
    
    // Find tickets by priority
    List<Ticket> findByPriority(Ticket.Priority priority);
    
    // Find tickets by category
    List<Ticket> findByCategory(String category);
    
    // Find tickets by customer name (case-insensitive)
    List<Ticket> findByCustomerNameContainingIgnoreCase(String customerName);
    
    // Find tickets by customer email
    List<Ticket> findByCustomerEmailIgnoreCase(String customerEmail);
    
    // Find tickets by order ID
    List<Ticket> findByOrderId(Long orderId);
    
    // Search tickets by multiple criteria
    @Query("SELECT t FROM Ticket t WHERE " +
           "(:title IS NULL OR LOWER(t.title) LIKE LOWER(CONCAT('%', :title, '%'))) AND " +
           "(:description IS NULL OR LOWER(t.description) LIKE LOWER(CONCAT('%', :description, '%'))) AND " +
           "(:customerName IS NULL OR LOWER(t.customerName) LIKE LOWER(CONCAT('%', :customerName, '%'))) AND " +
           "(:category IS NULL OR LOWER(t.category) LIKE LOWER(CONCAT('%', :category, '%'))) AND " +
           "(:status IS NULL OR t.status = :status) AND " +
           "(:priority IS NULL OR t.priority = :priority) AND " +
           "(:startDate IS NULL OR t.createdDate >= :startDate) AND " +
           "(:endDate IS NULL OR t.createdDate <= :endDate)")
    List<Ticket> searchTickets(@Param("title") String title,
                              @Param("description") String description,
                              @Param("customerName") String customerName,
                              @Param("category") String category,
                              @Param("status") Ticket.TicketStatus status,
                              @Param("priority") Ticket.Priority priority,
                              @Param("startDate") LocalDateTime startDate,
                              @Param("endDate") LocalDateTime endDate);
    
    // Count tickets by status
    long countByStatus(Ticket.TicketStatus status);
    
    // Count tickets by category
    @Query("SELECT t.category, COUNT(t) FROM Ticket t GROUP BY t.category ORDER BY COUNT(t) DESC")
    List<Object[]> countTicketsByCategory();
    
    // Count tickets by priority
    long countByPriority(Ticket.Priority priority);
    
    // Find top customers by ticket count
    @Query("SELECT t.customerName, t.customerEmail, COUNT(t) as ticketCount " +
           "FROM Ticket t GROUP BY t.customerName, t.customerEmail " +
           "ORDER BY COUNT(t) DESC")
    List<Object[]> findTopCustomersByTicketCount();
    
    // Find recent tickets (last 30 days)
    @Query("SELECT t FROM Ticket t WHERE t.createdDate >= :thirtyDaysAgo ORDER BY t.createdDate DESC")
    List<Ticket> findRecentTickets(@Param("thirtyDaysAgo") LocalDateTime thirtyDaysAgo);
    
    // Find tickets by date range
    List<Ticket> findByCreatedDateBetween(LocalDateTime startDate, LocalDateTime endDate);
    
    // Find all distinct categories
    @Query("SELECT DISTINCT t.category FROM Ticket t ORDER BY t.category")
    List<String> findAllCategories();
}
