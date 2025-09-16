package com.fuelpass.service;

import com.fuelpass.dto.request.CreateFuelOrderRequest;
import com.fuelpass.dto.request.UpdateOrderStatusRequest;
import com.fuelpass.entity.FuelOrder;
import com.fuelpass.entity.OrderStatus;
import com.fuelpass.entity.User;
import com.fuelpass.repository.FuelOrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Service class for fuel order operations
 */
@Service
@Transactional
public class FuelOrderService {

    @Autowired
    private FuelOrderRepository fuelOrderRepository;

    @Autowired
    private AuthService authService;

    /**
     * Create a new fuel order
     */
    public FuelOrder createOrder(CreateFuelOrderRequest request, User createdBy) {
        // Validate delivery time window
        if (request.getDeliveryTimeWindowEnd().isBefore(request.getDeliveryTimeWindowStart())) {
            throw new IllegalArgumentException("Delivery time window end must be after start time");
        }

        // Check if delivery window is not more than 24 hours
        long hoursBetween = java.time.Duration.between(
                request.getDeliveryTimeWindowStart(),
                request.getDeliveryTimeWindowEnd()
        ).toHours();

        if (hoursBetween > 24) {
            throw new IllegalArgumentException("Delivery window cannot exceed 24 hours");
        }

        // Create fuel order
        FuelOrder fuelOrder = new FuelOrder();
        fuelOrder.setTailNumber(request.getTailNumber());
        fuelOrder.setAirportIcaoCode(request.getAirportIcaoCode());
        fuelOrder.setRequestedFuelVolume(request.getRequestedFuelVolume());
        fuelOrder.setDeliveryTimeWindowStart(request.getDeliveryTimeWindowStart());
        fuelOrder.setDeliveryTimeWindowEnd(request.getDeliveryTimeWindowEnd());
        fuelOrder.setCreatedBy(createdBy);
        fuelOrder.setNotes(request.getNotes());
        fuelOrder.setStatus(OrderStatus.PENDING);

        return fuelOrderRepository.save(fuelOrder);
    }

    /**
     * Get fuel orders with pagination and filters
     */
    public Page<FuelOrder> getOrders(String airportIcaoCode, OrderStatus status,
                                     String tailNumber, User createdBy,
                                     int page, int size, String sortBy, String sortOrder) {

        // Create pageable object
        Sort sort = Sort.by(Sort.Direction.fromString(sortOrder), sortBy);
        Pageable pageable = PageRequest.of(page, size, sort);

        // Use repository method with filters
        return fuelOrderRepository.findWithFilters(
                airportIcaoCode, status, tailNumber, createdBy, pageable
        );
    }

    /**
     * Get fuel order by ID
     */
    public FuelOrder getOrderById(UUID orderId) {
        return fuelOrderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Fuel order not found with id: " + orderId));
    }

    /**
     * Update fuel order status
     */
    public FuelOrder updateOrderStatus(UUID orderId, UpdateOrderStatusRequest request, User updatedBy) {
        FuelOrder fuelOrder = getOrderById(orderId);

        // Validate status transition
        if (!isValidStatusTransition(fuelOrder.getStatus(), request.getNewStatus())) {
            throw new IllegalArgumentException(
                    "Invalid status transition from " + fuelOrder.getStatus() + " to " + request.getNewStatus()
            );
        }

        fuelOrder.setStatus(request.getNewStatus());
        return fuelOrderRepository.save(fuelOrder);
    }

    /**
     * Get order statistics
     */
    public Map<String, Long> getOrderStatistics() {
        Map<String, Long> statistics = new HashMap<>();

        statistics.put("totalOrders", fuelOrderRepository.count());
        statistics.put("pendingOrders", fuelOrderRepository.countByStatus(OrderStatus.PENDING));
        statistics.put("confirmedOrders", fuelOrderRepository.countByStatus(OrderStatus.CONFIRMED));
        statistics.put("completedOrders", fuelOrderRepository.countByStatus(OrderStatus.COMPLETED));

        return statistics;
    }

    /**
     * Get orders by user
     */
    public Page<FuelOrder> getOrdersByUser(User user, int page, int size, String sortBy, String sortOrder) {
        Sort sort = Sort.by(Sort.Direction.fromString(sortOrder), sortBy);
        Pageable pageable = PageRequest.of(page, size, sort);

        return fuelOrderRepository.findByCreatedBy(user, pageable);
    }

    /**
     * Get orders by status
     */
    public Page<FuelOrder> getOrdersByStatus(OrderStatus status, int page, int size, String sortBy, String sortOrder) {
        Sort sort = Sort.by(Sort.Direction.fromString(sortOrder), sortBy);
        Pageable pageable = PageRequest.of(page, size, sort);

        return fuelOrderRepository.findByStatus(status, pageable);
    }

    /**
     * Get orders by airport
     */
    public Page<FuelOrder> getOrdersByAirport(String airportIcaoCode, int page, int size, String sortBy, String sortOrder) {
        Sort sort = Sort.by(Sort.Direction.fromString(sortOrder), sortBy);
        Pageable pageable = PageRequest.of(page, size, sort);

        return fuelOrderRepository.findByAirportIcaoCode(airportIcaoCode, pageable);
    }

    /**
     * Validate status transition
     */
    private boolean isValidStatusTransition(OrderStatus currentStatus, OrderStatus newStatus) {
        // Define valid transitions
        return switch (currentStatus) {
            case PENDING -> newStatus == OrderStatus.CONFIRMED || newStatus == OrderStatus.CANCELLED;
            case CONFIRMED -> newStatus == OrderStatus.COMPLETED || newStatus == OrderStatus.CANCELLED;
            case COMPLETED -> false; // Cannot change from completed
            case CANCELLED -> false; // Cannot change from cancelled
        };
    }
}