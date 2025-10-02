package com.foodorder.service;

import com.foodorder.entity.Order;
import com.foodorder.entity.Ticket;
import com.foodorder.entity.TicketReply;
import com.foodorder.repository.OrderRepository;
import com.foodorder.repository.TicketRepository;
import com.foodorder.repository.TicketReplyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

@Service
public class DataInitializationService implements CommandLineRunner {
    
    @Autowired
    private OrderRepository orderRepository;
    
    @Autowired
    private TicketRepository ticketRepository;
    
    @Autowired
    private TicketReplyRepository ticketReplyRepository;
    
    private final Random random = new Random();
    
    // Sri Lankan names
    private final List<String> sriLankanNames = Arrays.asList(
        "Kasun Perera", "Nimali Silva", "Chaminda Fernando", "Sanduni Jayawardena",
        "Ruwan Wickramasinghe", "Dilini Rajapaksa", "Tharaka Gunasekara", "Priyanka Mendis",
        "Asanka Wijeratne", "Chathurika Bandara", "Mahesh Dissanayake", "Samanthi Kumari",
        "Janith Rathnayake", "Thilini Seneviratne", "Dhanushka Amarasinghe", "Kavitha Liyanage"
    );
    
    // Sri Lankan food items
    private final List<String> foodItems = Arrays.asList(
        "Chicken Kottu Roti", "Fish Curry with Rice", "Hoppers with Egg",
        "Chicken Fried Rice", "Vegetable Curry", "Pol Sambol with Rice",
        "String Hoppers", "Chicken Curry", "Dhal Curry", "Parippu Curry",
        "Fish Ambul Thiyal", "Beef Curry", "Chicken Devilled", "Egg Hoppers",
        "Coconut Roti", "Kiribath", "Watalappan", "Curd with Treacle"
    );
    
    private final List<String> ticketCategories = Arrays.asList(
        "Food Quality Issue", "Delivery Problem", "Order Incorrect", 
        "Payment Issue", "Customer Service", "Technical Problem", 
        "Refund Request", "General Inquiry"
    );
    
    @Override
    public void run(String... args) throws Exception {
        // Only initialize data if database is empty
        if (orderRepository.count() == 0) {
            initializeOrders();
            initializeTickets();
            System.out.println("✅ Database initialized with dummy data!");
        } else {
            System.out.println("ℹ️ Database already contains data. Skipping initialization.");
        }
    }
    
    private void initializeOrders() {
        System.out.println("🍕 Creating dummy orders...");
        
        for (int i = 0; i < 10; i++) {
            Order order = new Order();
            
            // Customer information
            String customerName = sriLankanNames.get(random.nextInt(sriLankanNames.size()));
            order.setCustomerName(customerName);
            order.setCustomerEmail(generateEmail(customerName));
            order.setCustomerPhone(generatePhoneNumber());
            order.setDeliveryAddress(generateAddress());
            
            // Order information
            order.setFoodItems(generateFoodItemsList());
            order.setTotalAmount(generateTotalAmount());
            order.setCurrency("LKR");
            order.setStatus(generateOrderStatus());
            order.setOrderDate(generateOrderDate());
            order.setSpecialInstructions(generateSpecialInstructions());
            
            orderRepository.save(order);
        }
        
        System.out.println("✅ Created " + orderRepository.count() + " dummy orders");
    }
    
    private void initializeTickets() {
        System.out.println("🎫 Creating dummy tickets...");
        
        List<Order> orders = orderRepository.findAll();
        
        // Create tickets for some orders
        for (int i = 0; i < Math.min(8, orders.size()); i++) {
            Order order = orders.get(i);
            
            Ticket ticket = new Ticket();
            ticket.setOrder(order);
            ticket.setCustomerName(order.getCustomerName());
            ticket.setCustomerEmail(order.getCustomerEmail());
            ticket.setCustomerPhone(order.getCustomerPhone());
            
            ticket.setTitle(generateTicketTitle());
            ticket.setDescription(generateTicketDescription());
            ticket.setPriority(generateTicketPriority());
            ticket.setCategory(ticketCategories.get(random.nextInt(ticketCategories.size())));
            ticket.setStatus(generateTicketStatus());
            ticket.setCreatedDate(generateTicketDate());
            ticket.setUpdatedDate(ticket.getCreatedDate().plusHours(random.nextInt(48)));
            
            if (ticket.getStatus() == Ticket.TicketStatus.RESOLVED) {
                ticket.setResolvedDate(ticket.getUpdatedDate().plusHours(random.nextInt(24)));
            }
            
            Ticket savedTicket = ticketRepository.save(ticket);
            
            // Add some replies to tickets
            if (random.nextBoolean()) {
                createTicketReplies(savedTicket);
            }
        }
        
        System.out.println("✅ Created " + ticketRepository.count() + " dummy tickets");
    }
    
    private void createTicketReplies(Ticket ticket) {
        int replyCount = random.nextInt(3) + 1; // 1-3 replies
        
        for (int i = 0; i < replyCount; i++) {
            TicketReply reply = new TicketReply();
            reply.setTicket(ticket);
            
            if (i == 0 || random.nextBoolean()) {
                // Customer reply
                reply.setAuthorName(ticket.getCustomerName());
                reply.setAuthorEmail(ticket.getCustomerEmail());
                reply.setMessage(generateCustomerReply());
            } else {
                // Support reply
                reply.setAuthorName("Support Team");
                reply.setAuthorEmail("support@foodordering.lk");
                reply.setMessage(generateSupportReply());
            }
            
            reply.setCreatedDate(ticket.getCreatedDate().plusHours(i * 6 + random.nextInt(6)));
            ticketReplyRepository.save(reply);
        }
    }
    
    private String generateEmail(String name) {
        String[] parts = name.toLowerCase().split(" ");
        return parts[0] + "." + parts[1] + "@gmail.com";
    }
    
    private String generatePhoneNumber() {
        return "077" + String.format("%07d", random.nextInt(10000000));
    }
    
    private String generateAddress() {
        String[] areas = {"Colombo", "Kandy", "Galle", "Negombo", "Matara", "Kurunegala", "Anuradhapura", "Ratnapura"};
        String[] streets = {"Main Street", "Galle Road", "Kandy Road", "Temple Road", "Lake Road", "Station Road"};
        
        return random.nextInt(999) + 1 + ", " + 
               streets[random.nextInt(streets.length)] + ", " + 
               areas[random.nextInt(areas.length)];
    }
    
    private String generateFoodItemsList() {
        int itemCount = random.nextInt(4) + 2; // 2-5 items
        StringBuilder items = new StringBuilder();
        
        for (int i = 0; i < itemCount; i++) {
            if (i > 0) items.append("\n");
            int quantity = random.nextInt(3) + 1;
            String item = foodItems.get(random.nextInt(foodItems.size()));
            items.append(quantity).append("x ").append(item);
        }
        
        return items.toString();
    }
    
    private BigDecimal generateTotalAmount() {
        return BigDecimal.valueOf(500 + random.nextInt(4500)); // LKR 500-5000
    }
    
    private Order.OrderStatus generateOrderStatus() {
        Order.OrderStatus[] statuses = Order.OrderStatus.values();
        return statuses[random.nextInt(statuses.length)];
    }
    
    private LocalDateTime generateOrderDate() {
        return LocalDateTime.now().minusDays(random.nextInt(30)).minusHours(random.nextInt(24));
    }
    
    private String generateSpecialInstructions() {
        String[] instructions = {
            "Please call before delivery",
            "Leave at the gate if no one answers",
            "Extra spicy please",
            "No onions in the curry",
            "Deliver to the back entrance",
            "Ring the bell twice",
            null, null // Some orders have no special instructions
        };
        return instructions[random.nextInt(instructions.length)];
    }
    
    private String generateTicketTitle() {
        String[] titles = {
            "Food was cold when delivered",
            "Wrong order received",
            "Missing items from my order",
            "Delivery was very late",
            "Food quality was poor",
            "Payment was charged twice",
            "Delivery person was rude",
            "Order was cancelled without notice"
        };
        return titles[random.nextInt(titles.length)];
    }
    
    private String generateTicketDescription() {
        String[] descriptions = {
            "I ordered food but it arrived cold and the taste was not good. Please look into this matter.",
            "I received a completely different order than what I requested. This is very disappointing.",
            "Some items were missing from my order. I paid for them but didn't receive them.",
            "The delivery took more than 2 hours which is unacceptable. The food was cold by then.",
            "The food quality was very poor. The rice was undercooked and curry was too salty.",
            "I was charged twice for the same order. Please refund the extra amount immediately.",
            "The delivery person was very rude and unprofessional. This needs to be addressed.",
            "My order was cancelled without any prior notice. This caused a lot of inconvenience."
        };
        return descriptions[random.nextInt(descriptions.length)];
    }
    
    private Ticket.Priority generateTicketPriority() {
        Ticket.Priority[] priorities = Ticket.Priority.values();
        // Weight towards medium and low priorities
        int rand = random.nextInt(10);
        if (rand < 1) return Ticket.Priority.URGENT;
        if (rand < 3) return Ticket.Priority.HIGH;
        if (rand < 7) return Ticket.Priority.MEDIUM;
        return Ticket.Priority.LOW;
    }
    
    private Ticket.TicketStatus generateTicketStatus() {
        Ticket.TicketStatus[] statuses = Ticket.TicketStatus.values();
        return statuses[random.nextInt(statuses.length)];
    }
    
    private LocalDateTime generateTicketDate() {
        return LocalDateTime.now().minusDays(random.nextInt(15)).minusHours(random.nextInt(24));
    }
    
    private String generateCustomerReply() {
        String[] replies = {
            "Thank you for looking into this. I hope this gets resolved soon.",
            "I'm still waiting for a proper response to my complaint.",
            "This is taking too long. I need a quick resolution.",
            "I appreciate your help with this matter.",
            "Please update me on the progress of this issue."
        };
        return replies[random.nextInt(replies.length)];
    }
    
    private String generateSupportReply() {
        String[] replies = {
            "Thank you for contacting us. We are looking into your issue and will get back to you soon.",
            "We apologize for the inconvenience. We have forwarded your complaint to the relevant department.",
            "Your issue has been resolved. Please let us know if you need any further assistance.",
            "We have processed your refund. It should reflect in your account within 3-5 business days.",
            "Thank you for your patience. We have taken necessary actions to prevent this in the future."
        };
        return replies[random.nextInt(replies.length)];
    }
}
