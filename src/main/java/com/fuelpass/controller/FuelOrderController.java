package com.fuelpass.controller;

import com.fuelpass.dto.response.ApiResponse;
import com.fuelpass.dto.request.CreateFuelOrderRequest;
import com.fuelpass.dto.request.UpdateOrderStatusRequest;
import com.fuelpass.entity.FuelOrder;
import com.fuelpass.entity.OrderStatus;
import com.fuelpass.entity.User;
import com.fuelpass.service.AuthService;
import com.fuelpass.service.FuelOrderService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Controller for fuel order endpoints
 */
@RestController
@RequestMapping("/fuel-orders")
@CrossOrigin(origins = {"http://localhost:3000", "http://localhost:3001"}, allowCredentials = "true")
public class FuelOrderController {

    @Autowired
    private FuelOrderService fuelOrderService;

    @Autowired
    private AuthService authService;

    /**
     * Create a new fuel order
     */
    @PostMapping
    public ResponseEntity<ApiResponse<FuelOrder>> createOrder(@Valid @RequestBody CreateFuelOrderRequest request,
                                                              Authentication authentication) {
        try {
            // Get current user
            System.out.println("FuelOrderController.createOrder" + authentication.getName());
            User currentUser = getCurrentUser(authentication);

            // Create the order
            FuelOrder newOrder = fuelOrderService.createOrder(request, currentUser);

            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(ApiResponse.success("Fuel order created successfully", newOrder));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.error("Failed to create fuel order: " + e.getMessage()));
        }
    }

    @GetMapping("/debug")
    public ResponseEntity<ApiResponse<Map<String, Object>>> debugAuth(Authentication authentication) {
        Map<String, Object> debugInfo = new HashMap<>();
        debugInfo.put("authentication", authentication != null ? authentication.getName() : "null");
        debugInfo.put("authorities", authentication != null ? authentication.getAuthorities() : "null");
        debugInfo.put("authenticated", authentication != null && authentication.isAuthenticated());

        return ResponseEntity.ok(ApiResponse.success("Debug info", debugInfo));
    }

    /**
     * Get all fuel orders with pagination and filters
     */
    @GetMapping
    public ResponseEntity<ApiResponse<Page<FuelOrder>>> getOrders(
            @RequestParam(required = false) String airportIcaoCode,
            @RequestParam(required = false) OrderStatus status,
            @RequestParam(required = false) String tailNumber,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String sortOrder,
            Authentication authentication) {

        try {
            // Get current user
            User currentUser = getCurrentUser(authentication);

            // For aircraft operators, only show their own orders
            // For operations managers, show all orders
            User filterUser = currentUser.getRole().name().equals("AIRCRAFT_OPERATOR") ? currentUser : null;

            Page<FuelOrder> orders = fuelOrderService.getOrders(
                    airportIcaoCode, status, tailNumber, filterUser, page, size, sortBy, sortOrder
            );

            return ResponseEntity.ok(ApiResponse.success("Orders retrieved successfully", orders));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Failed to retrieve orders: " + e.getMessage()));
        }
    }

    /**
     * Get fuel order by ID
     */
    @GetMapping("/{orderId}")
    public ResponseEntity<ApiResponse<FuelOrder>> getOrderById(@PathVariable UUID orderId,
                                                               Authentication authentication) {
        try {
            // Get current user
            User currentUser = getCurrentUser(authentication);

            // Get the order
            FuelOrder order = fuelOrderService.getOrderById(orderId);

            // Check if user has access to this order
            if (currentUser.getRole().name().equals("AIRCRAFT_OPERATOR") &&
                    !order.getCreatedBy().getId().equals(currentUser.getId())) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body(ApiResponse.error("Access denied to this order"));
            }

            return ResponseEntity.ok(ApiResponse.success("Order retrieved successfully", order));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error("Order not found: " + e.getMessage()));
        }
    }

    /**
     * Update fuel order status (Operations Manager only)
     */
    @PatchMapping("/{orderId}/status")
    public ResponseEntity<ApiResponse<FuelOrder>> updateOrderStatus(@PathVariable UUID orderId,
                                                                    @Valid @RequestBody UpdateOrderStatusRequest request,
                                                                    Authentication authentication) {
        try {
            // Get current user
            User currentUser = getCurrentUser(authentication);

            // Check if user is operations manager
            if (!currentUser.getRole().name().equals("OPERATIONS_MANAGER")) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body(ApiResponse.error("Only operations managers can update order status"));
            }

            // Update the order status
            FuelOrder updatedOrder = fuelOrderService.updateOrderStatus(orderId, request, currentUser);

            return ResponseEntity.ok(ApiResponse.success("Order status updated successfully", updatedOrder));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.error("Failed to update order status: " + e.getMessage()));
        }
    }

    /**
     * Get order statistics (Operations Manager only)
     */
    @GetMapping("/statistics")
    public ResponseEntity<ApiResponse<Map<String, Long>>> getOrderStatistics(Authentication authentication) {
        try {
            // Get current user
            System.out.println("FuelOrderController.getOrderStatistics" + authentication.getName() + " --- "+ authentication);
            User currentUser = getCurrentUser(authentication);

            // Check if user is operations manager
            if (!currentUser.getRole().name().equals("OPERATIONS_MANAGER")) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body(ApiResponse.error("Only operations managers can view statistics"));
            }

            // Get statistics
            Map<String, Long> statistics = fuelOrderService.getOrderStatistics();

            return ResponseEntity.ok(ApiResponse.success("Statistics retrieved successfully", statistics));
        } catch (Exception e) {
            System.out.println("FuelOrderController.getOrderStatistics catch bloack" + " --- "+ authentication);

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Failed to retrieve statistics: " + e.getMessage()));
        }
    }

    /**
     * Get orders by current user (Aircraft Operator only)
     */
    @GetMapping("/my-orders")
    public ResponseEntity<ApiResponse<Page<FuelOrder>>> getMyOrders(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String sortOrder,
            Authentication authentication) {

        try {
            // Get current user
            User currentUser = getCurrentUser(authentication);

            // Check if user is aircraft operator
            if (!currentUser.getRole().name().equals("AIRCRAFT_OPERATOR")) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body(ApiResponse.error("Only aircraft operators can view their orders"));
            }

            // Get user's orders
            Page<FuelOrder> orders = fuelOrderService.getOrdersByUser(currentUser, page, size, sortBy, sortOrder);

            return ResponseEntity.ok(ApiResponse.success("Orders retrieved successfully", orders));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Failed to retrieve orders: " + e.getMessage()));
        }
    }

    /**
     * Get orders by status (Operations Manager only)
     */
    @GetMapping("/status/{status}")
    public ResponseEntity<ApiResponse<Page<FuelOrder>>> getOrdersByStatus(@PathVariable OrderStatus status,
                                                                          @RequestParam(defaultValue = "0") int page,
                                                                          @RequestParam(defaultValue = "10") int size,
                                                                          @RequestParam(defaultValue = "createdAt") String sortBy,
                                                                          @RequestParam(defaultValue = "desc") String sortOrder,
                                                                          Authentication authentication) {

        try {
            // Get current user
            User currentUser = getCurrentUser(authentication);

            // Check if user is operations manager
            if (!currentUser.getRole().name().equals("OPERATIONS_MANAGER")) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body(ApiResponse.error("Only operations managers can filter by status"));
            }

            // Get orders by status
            Page<FuelOrder> orders = fuelOrderService.getOrdersByStatus(status, page, size, sortBy, sortOrder);

            return ResponseEntity.ok(ApiResponse.success("Orders retrieved successfully", orders));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Failed to retrieve orders: " + e.getMessage()));
        }
    }

    /**
     * Get orders by airport (Operations Manager only)
     */
    @GetMapping("/airport/{airportIcaoCode}")
    public ResponseEntity<ApiResponse<Page<FuelOrder>>> getOrdersByAirport(@PathVariable String airportIcaoCode,
                                                                           @RequestParam(defaultValue = "0") int page,
                                                                           @RequestParam(defaultValue = "10") int size,
                                                                           @RequestParam(defaultValue = "createdAt") String sortBy,
                                                                           @RequestParam(defaultValue = "desc") String sortOrder,
                                                                           Authentication authentication) {

        try {
            // Get current user
            User currentUser = getCurrentUser(authentication);

            // Check if user is operations manager
            if (!currentUser.getRole().name().equals("OPERATIONS_MANAGER")) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body(ApiResponse.error("Only operations managers can filter by airport"));
            }

            // Get orders by airport
            Page<FuelOrder> orders = fuelOrderService.getOrdersByAirport(airportIcaoCode, page, size, sortBy, sortOrder);

            return ResponseEntity.ok(ApiResponse.success("Orders retrieved successfully", orders));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Failed to retrieve orders: " + e.getMessage()));
        }
    }

    /**
     * Helper method to get current user from authentication
     */
    private User getCurrentUser(Authentication authentication) {
        String email = authentication.getName();
        if(email == null) {
            email = "";
        }
        return authService.getUserByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }
}