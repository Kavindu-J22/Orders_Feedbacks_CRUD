package com.foodorder.controller;

import com.foodorder.service.StatisticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Map;

@Controller
public class HomeController {
    
    @Autowired
    private StatisticsService statisticsService;
    
    @GetMapping("/")
    public String home() {
        return "redirect:/dashboard";
    }
    
    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        Map<String, Object> stats = statisticsService.getDashboardStatistics();
        model.addAttribute("stats", stats);
        
        // Add individual statistics for easier access in template
        model.addAttribute("totalTickets", stats.get("totalTickets"));
        model.addAttribute("openTickets", stats.get("openTickets"));
        model.addAttribute("inProgressTickets", stats.get("inProgressTickets"));
        model.addAttribute("resolvedTickets", stats.get("resolvedTickets"));
        model.addAttribute("closedTickets", stats.get("closedTickets"));
        
        model.addAttribute("urgentTickets", stats.get("urgentTickets"));
        model.addAttribute("highPriorityTickets", stats.get("highPriorityTickets"));
        model.addAttribute("mediumPriorityTickets", stats.get("mediumPriorityTickets"));
        model.addAttribute("lowPriorityTickets", stats.get("lowPriorityTickets"));
        
        model.addAttribute("totalOrders", stats.get("totalOrders"));
        model.addAttribute("pendingOrders", stats.get("pendingOrders"));
        model.addAttribute("confirmedOrders", stats.get("confirmedOrders"));
        model.addAttribute("preparingOrders", stats.get("preparingOrders"));
        model.addAttribute("outForDeliveryOrders", stats.get("outForDeliveryOrders"));
        model.addAttribute("deliveredOrders", stats.get("deliveredOrders"));
        model.addAttribute("cancelledOrders", stats.get("cancelledOrders"));
        
        model.addAttribute("ticketsByCategory", stats.get("ticketsByCategory"));
        model.addAttribute("topCustomersByTickets", stats.get("topCustomersByTickets"));
        model.addAttribute("topCustomersByOrders", stats.get("topCustomersByOrders"));
        
        return "dashboard";
    }
}
