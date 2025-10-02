package com.foodorder.repository;

import com.foodorder.entity.TicketReply;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface TicketReplyRepository extends JpaRepository<TicketReply, Long> {
    
    // Find replies by ticket ID
    List<TicketReply> findByTicketIdOrderByCreatedDateAsc(Long ticketId);
    
    // Find replies by author email
    List<TicketReply> findByAuthorEmailIgnoreCase(String authorEmail);
    
    // Find replies by author name (case-insensitive)
    List<TicketReply> findByAuthorNameContainingIgnoreCase(String authorName);
    
    // Find replies by date range
    List<TicketReply> findByCreatedDateBetween(LocalDateTime startDate, LocalDateTime endDate);
    
    // Count replies by ticket ID
    long countByTicketId(Long ticketId);
    
    // Find recent replies for a ticket
    @Query("SELECT tr FROM TicketReply tr WHERE tr.ticket.id = :ticketId " +
           "ORDER BY tr.createdDate DESC")
    List<TicketReply> findRecentRepliesByTicketId(@Param("ticketId") Long ticketId);
}
