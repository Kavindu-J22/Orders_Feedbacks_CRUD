package com.foodorder.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@Entity
@Table(name = "ticket_replies")
public class TicketReply {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotBlank(message = "Reply message is required")
    @Column(name = "message", nullable = false, length = 2000)
    private String message;
    
    @NotBlank(message = "Author name is required")
    @Column(name = "author_name", nullable = false)
    private String authorName;
    
    @NotBlank(message = "Author email is required")
    @Column(name = "author_email", nullable = false)
    private String authorEmail;
    
    @Column(name = "created_date", nullable = false)
    private LocalDateTime createdDate;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ticket_id", nullable = false)
    private Ticket ticket;
    
    // Constructors
    public TicketReply() {
        this.createdDate = LocalDateTime.now();
    }
    
    public TicketReply(String message, String authorName, String authorEmail, Ticket ticket) {
        this();
        this.message = message;
        this.authorName = authorName;
        this.authorEmail = authorEmail;
        this.ticket = ticket;
    }
    
    // Getters and Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getMessage() {
        return message;
    }
    
    public void setMessage(String message) {
        this.message = message;
    }
    
    public String getAuthorName() {
        return authorName;
    }
    
    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }
    
    public String getAuthorEmail() {
        return authorEmail;
    }
    
    public void setAuthorEmail(String authorEmail) {
        this.authorEmail = authorEmail;
    }
    
    public LocalDateTime getCreatedDate() {
        return createdDate;
    }
    
    public void setCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
    }
    
    public Ticket getTicket() {
        return ticket;
    }
    
    public void setTicket(Ticket ticket) {
        this.ticket = ticket;
    }
}
