package com.foodorder.controller;

import com.foodorder.entity.Order;
import com.foodorder.entity.Ticket;
import com.foodorder.entity.TicketReply;
import com.foodorder.service.OrderService;
import com.foodorder.service.TicketService;
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
@RequestMapping("/tickets")
public class TicketController {

    @Autowired
    private TicketService ticketService;

    @Autowired
    private OrderService orderService;

    @GetMapping
    public String listTickets(@RequestParam(required = false) String title,
            @RequestParam(required = false) String description,
            @RequestParam(required = false) String customerName,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) Ticket.TicketStatus status,
            @RequestParam(required = false) Ticket.Priority priority,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate,
            Model model) {

        List<Ticket> tickets;

        if (title != null || description != null || customerName != null || category != null ||
                status != null || priority != null || startDate != null || endDate != null) {
            tickets = ticketService.searchTickets(title, description, customerName, category,
                    status, priority, startDate, endDate);
        } else {
            tickets = ticketService.getAllTickets();
        }

        model.addAttribute("tickets", tickets);
        model.addAttribute("title", title);
        model.addAttribute("description", description);
        model.addAttribute("customerName", customerName);
        model.addAttribute("category", category);
        model.addAttribute("status", status);
        model.addAttribute("priority", priority);
        model.addAttribute("startDate", startDate);
        model.addAttribute("endDate", endDate);
        model.addAttribute("ticketStatuses", Ticket.TicketStatus.values());
        model.addAttribute("priorities", Ticket.Priority.values());
        model.addAttribute("categories", ticketService.getAllCategories());

        return "tickets/list";
    }

    @GetMapping("/new")
    public String showCreateForm(@RequestParam(required = false) Long orderId, Model model) {
        Ticket ticket = new Ticket();

        if (orderId != null) {
            Optional<Order> order = orderService.getOrderById(orderId);
            if (order.isPresent()) {
                Order orderEntity = order.get();
                ticket.setOrder(orderEntity);
                ticket.setCustomerName(orderEntity.getCustomerName());
                ticket.setCustomerEmail(orderEntity.getCustomerEmail());
                ticket.setCustomerPhone(orderEntity.getCustomerPhone());
            }
        }

        model.addAttribute("ticket", ticket);
        model.addAttribute("orders", orderService.getAllOrders());
        model.addAttribute("priorities", Ticket.Priority.values());
        model.addAttribute("categories", getDefaultCategories());

        return "tickets/form";
    }

    @PostMapping
    public String createTicket(@Valid @ModelAttribute Ticket ticket,
            BindingResult result,
            Model model,
            RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            model.addAttribute("orders", orderService.getAllOrders());
            model.addAttribute("priorities", Ticket.Priority.values());
            model.addAttribute("categories", getDefaultCategories());
            return "tickets/form";
        }

        try {
            ticketService.createTicket(ticket);
            redirectAttributes.addFlashAttribute("successMessage", "Ticket created successfully!");
            return "redirect:/tickets";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error creating ticket: " + e.getMessage());
            return "redirect:/tickets/new";
        }
    }

    @GetMapping("/{id}")
    public String viewTicket(@PathVariable Long id, Model model) {
        Optional<Ticket> ticket = ticketService.getTicketById(id);
        if (ticket.isPresent()) {
            Ticket ticketEntity = ticket.get();
            List<TicketReply> replies = ticketService.getRepliesForTicket(id);

            model.addAttribute("ticket", ticketEntity);
            model.addAttribute("replies", replies);
            model.addAttribute("newReply", new TicketReply());
            model.addAttribute("canEdit", ticketEntity.canEdit());
            model.addAttribute("ticketStatuses", Ticket.TicketStatus.values());

            return "tickets/view";
        } else {
            return "redirect:/tickets?error=Ticket not found";
        }
    }

    @GetMapping("/{id}/edit")
    public String showEditForm(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        Optional<Ticket> ticket = ticketService.getTicketById(id);
        if (ticket.isPresent()) {
            Ticket ticketEntity = ticket.get();

            if (!ticketEntity.canEdit()) {
                redirectAttributes.addFlashAttribute("errorMessage",
                        "Cannot edit ticket that is in progress or resolved. You can only add replies.");
                return "redirect:/tickets/" + id;
            }

            model.addAttribute("ticket", ticketEntity);
            model.addAttribute("orders", orderService.getAllOrders());
            model.addAttribute("priorities", Ticket.Priority.values());
            model.addAttribute("ticketStatuses", Ticket.TicketStatus.values());
            model.addAttribute("categories", getDefaultCategories());

            return "tickets/form";
        } else {
            return "redirect:/tickets?error=Ticket not found";
        }
    }

    @PostMapping("/{id}")
    public String updateTicket(@PathVariable Long id,
            @Valid @ModelAttribute Ticket ticket,
            BindingResult result,
            Model model,
            RedirectAttributes redirectAttributes) {

        Optional<Ticket> existingTicket = ticketService.getTicketById(id);
        if (!existingTicket.isPresent()) {
            return "redirect:/tickets?error=Ticket not found";
        }

        if (!existingTicket.get().canEdit()) {
            redirectAttributes.addFlashAttribute("errorMessage",
                    "Cannot edit ticket that is in progress or resolved.");
            return "redirect:/tickets/" + id;
        }

        if (result.hasErrors()) {
            model.addAttribute("orders", orderService.getAllOrders());
            model.addAttribute("priorities", Ticket.Priority.values());
            model.addAttribute("ticketStatuses", Ticket.TicketStatus.values());
            model.addAttribute("categories", getDefaultCategories());
            return "tickets/form";
        }

        try {
            ticket.setId(id);
            ticketService.updateTicket(ticket);
            redirectAttributes.addFlashAttribute("successMessage", "Ticket updated successfully!");
            return "redirect:/tickets/" + id;
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error updating ticket: " + e.getMessage());
            return "redirect:/tickets/" + id + "/edit";
        }
    }

    @PostMapping("/{id}/delete")
    public String deleteTicket(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            ticketService.deleteTicket(id);
            redirectAttributes.addFlashAttribute("successMessage", "Ticket deleted successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error deleting ticket: " + e.getMessage());
        }
        return "redirect:/tickets";
    }

    @PostMapping("/{id}/status")
    public String updateTicketStatus(@PathVariable Long id,
            @RequestParam Ticket.TicketStatus status,
            RedirectAttributes redirectAttributes) {
        try {
            ticketService.updateTicketStatus(id, status);
            redirectAttributes.addFlashAttribute("successMessage", "Ticket status updated successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error updating ticket status: " + e.getMessage());
        }
        return "redirect:/tickets/" + id;
    }

    @PostMapping("/{id}/reply")
    public String addReply(@PathVariable Long id,
            @Valid @ModelAttribute("newReply") TicketReply reply,
            BindingResult result,
            RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            redirectAttributes.addFlashAttribute("errorMessage", "Please fill in all required fields for the reply.");
            return "redirect:/tickets/" + id;
        }

        try {
            ticketService.addReplyToTicket(id, reply);
            redirectAttributes.addFlashAttribute("successMessage", "Reply added successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error adding reply: " + e.getMessage());
        }
        return "redirect:/tickets/" + id;
    }

    private List<String> getDefaultCategories() {
        return List.of(
                "Food Quality Issue",
                "Delivery Problem",
                "Order Incorrect",
                "Payment Issue",
                "Customer Service",
                "Technical Problem",
                "Refund Request",
                "General Inquiry");
    }
}
