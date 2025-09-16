package com.fuelpass.dto.request;

import com.fuelpass.entity.OrderStatus;
import jakarta.validation.constraints.NotNull;

/**
 * DTO for updating fuel order status
 */
public class UpdateOrderStatusRequest {

    @NotNull(message = "New status is required")
    private OrderStatus newStatus;

    // Constructors
    public UpdateOrderStatusRequest() {}

    public UpdateOrderStatusRequest(OrderStatus newStatus) {
        this.newStatus = newStatus;
    }

    // Getters and Setters
    public OrderStatus getNewStatus() { return newStatus; }
    public void setNewStatus(OrderStatus newStatus) { this.newStatus = newStatus; }
}