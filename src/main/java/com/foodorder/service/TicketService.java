package com.foodorder.service;

import com.foodorder.entity.Ticket;
import com.foodorder.entity.TicketReply;
import com.foodorder.repository.TicketRepository;
import com.foodorder.repository.TicketReplyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class TicketService {
    
    @Autowired
    private TicketRepository ticketRepository;
    
    @Autowired
    private TicketReplyRepository ticketReplyRepository;
    
    // Create a new ticket
    public Ticket createTicket(Ticket ticket) {
        ticket.setCreatedDate(LocalDateTime.now());
        ticket.setUpdatedDate(LocalDateTime.now());
        return ticketRepository.save(ticket);
    }
    
    // Get all tickets
    public List<Ticket> getAllTickets() {
        return ticketRepository.findAll();
    }
    
    // Get ticket by ID
    public Optional<Ticket> getTicketById(Long id) {
        return ticketRepository.findById(id);
    }
    
    // Update ticket
    public Ticket updateTicket(Ticket ticket) {
        ticket.setUpdatedDate(LocalDateTime.now());
        return ticketRepository.save(ticket);
    }
    
    // Delete ticket
    public void deleteTicket(Long id) {
        ticketRepository.deleteById(id);
    }
    
    // Get tickets by status
    public List<Ticket> getTicketsByStatus(Ticket.TicketStatus status) {
        return ticketRepository.findByStatus(status);
    }
    
    // Get tickets by priority
    public List<Ticket> getTicketsByPriority(Ticket.Priority priority) {
        return ticketRepository.findByPriority(priority);
    }
    
    // Get tickets by category
    public List<Ticket> getTicketsByCategory(String category) {
        return ticketRepository.findByCategory(category);
    }
    
    // Search tickets by customer name
    public List<Ticket> searchTicketsByCustomerName(String customerName) {
        return ticketRepository.findByCustomerNameContainingIgnoreCase(customerName);
    }
    
    // Get tickets by order ID
    public List<Ticket> getTicketsByOrderId(Long orderId) {
        return ticketRepository.findByOrderId(orderId);
    }
    
    // Search tickets with multiple criteria
    public List<Ticket> searchTickets(String title, String description, String customerName,
                                     String category, Ticket.TicketStatus status,
                                     Ticket.Priority priority, LocalDateTime startDate,
                                     LocalDateTime endDate) {
        return ticketRepository.searchTickets(title, description, customerName, category,
                                            status, priority, startDate, endDate);
    }
    
    // Update ticket status
    public Ticket updateTicketStatus(Long ticketId, Ticket.TicketStatus status) {
        Optional<Ticket> ticketOpt = ticketRepository.findById(ticketId);
        if (ticketOpt.isPresent()) {
            Ticket ticket = ticketOpt.get();
            ticket.setStatus(status);
            return ticketRepository.save(ticket);
        }
        throw new RuntimeException("Ticket not found with id: " + ticketId);
    }
    
    // Add reply to ticket
    public TicketReply addReplyToTicket(Long ticketId, TicketReply reply) {
        Optional<Ticket> ticketOpt = ticketRepository.findById(ticketId);
        if (ticketOpt.isPresent()) {
            Ticket ticket = ticketOpt.get();
            reply.setTicket(ticket);
            reply.setCreatedDate(LocalDateTime.now());
            
            // Update ticket's updated date
            ticket.setUpdatedDate(LocalDateTime.now());
            ticketRepository.save(ticket);
            
            return ticketReplyRepository.save(reply);
        }
        throw new RuntimeException("Ticket not found with id: " + ticketId);
    }
    
    // Get replies for a ticket
    public List<TicketReply> getRepliesForTicket(Long ticketId) {
        return ticketReplyRepository.findByTicketIdOrderByCreatedDateAsc(ticketId);
    }
    
    // Count tickets by status
    public long countTicketsByStatus(Ticket.TicketStatus status) {
        return ticketRepository.countByStatus(status);
    }
    
    // Count tickets by priority
    public long countTicketsByPriority(Ticket.Priority priority) {
        return ticketRepository.countByPriority(priority);
    }
    
    // Get tickets by category count
    public List<Object[]> getTicketsByCategory() {
        return ticketRepository.countTicketsByCategory();
    }
    
    // Get top customers by ticket count
    public List<Object[]> getTopCustomersByTicketCount() {
        return ticketRepository.findTopCustomersByTicketCount();
    }
    
    // Get recent tickets
    public List<Ticket> getRecentTickets() {
        LocalDateTime thirtyDaysAgo = LocalDateTime.now().minusDays(30);
        return ticketRepository.findRecentTickets(thirtyDaysAgo);
    }
    
    // Get all categories
    public List<String> getAllCategories() {
        return ticketRepository.findAllCategories();
    }
    
    // Check if ticket can be edited
    public boolean canEditTicket(Long ticketId) {
        Optional<Ticket> ticketOpt = ticketRepository.findById(ticketId);
        return ticketOpt.map(Ticket::canEdit).orElse(false);
    }
}
