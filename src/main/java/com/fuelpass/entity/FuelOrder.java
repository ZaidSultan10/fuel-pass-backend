package com.fuelpass.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Fuel Order entity representing fuel delivery requests
 */
@Entity
@Table(name = "fuel_orders", indexes = {
        @Index(name = "idx_fuel_order_tail_number", columnList = "tailNumber"),
        @Index(name = "idx_fuel_order_airport", columnList = "airportIcaoCode"),
        @Index(name = "idx_fuel_order_status", columnList = "status"),
        @Index(name = "idx_fuel_order_created_by", columnList = "createdBy"),
        @Index(name = "idx_fuel_order_created_at", columnList = "createdAt")
})
@EntityListeners(AuditingEntityListener.class)
public class FuelOrder {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @NotBlank(message = "Tail number is required")
    @Size(max = 10, message = "Tail number must not exceed 10 characters")
    @Pattern(regexp = "^[A-Z0-9-]+$", message = "Tail number must contain only uppercase letters, numbers, and hyphens")
    @Column(name = "tail_number", nullable = false, length = 10)
    private String tailNumber;

    @NotBlank(message = "Airport ICAO code is required")
    @Size(min = 4, max = 4, message = "ICAO code must be exactly 4 characters")
    @Pattern(regexp = "^[A-Z]{4}$", message = "ICAO code must be 4 uppercase letters")
    @Column(name = "airport_icao_code", nullable = false, length = 4)
    private String airportIcaoCode;

    @NotNull(message = "Requested fuel volume is required")
    @Positive(message = "Fuel volume must be greater than 0")
    @Max(value = 100000, message = "Fuel volume cannot exceed 100,000 gallons")
    @Column(name = "requested_fuel_volume", nullable = false)
    private Double requestedFuelVolume;

    @NotNull(message = "Delivery time window start is required")
    @Column(name = "delivery_time_window_start", nullable = false)
    private LocalDateTime deliveryTimeWindowStart;

    @NotNull(message = "Delivery time window end is required")
    @Column(name = "delivery_time_window_end", nullable = false)
    private LocalDateTime deliveryTimeWindowEnd;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private OrderStatus status = OrderStatus.PENDING;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by", nullable = false)
    @JsonIgnore
    private User createdBy;

    @Size(max = 500, message = "Notes must not exceed 500 characters")
    @Column(length = 500)
    private String notes;

    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    // Constructors
    public FuelOrder() {}

    public FuelOrder(String tailNumber, String airportIcaoCode, Double requestedFuelVolume,
                     LocalDateTime deliveryTimeWindowStart, LocalDateTime deliveryTimeWindowEnd,
                     User createdBy, String notes) {
        this.tailNumber = tailNumber;
        this.airportIcaoCode = airportIcaoCode;
        this.requestedFuelVolume = requestedFuelVolume;
        this.deliveryTimeWindowStart = deliveryTimeWindowStart;
        this.deliveryTimeWindowEnd = deliveryTimeWindowEnd;
        this.createdBy = createdBy;
        this.notes = notes;
    }

    // Getters and Setters
    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }

    public String getTailNumber() { return tailNumber; }
    public void setTailNumber(String tailNumber) { this.tailNumber = tailNumber; }

    public String getAirportIcaoCode() { return airportIcaoCode; }
    public void setAirportIcaoCode(String airportIcaoCode) { this.airportIcaoCode = airportIcaoCode; }

    public Double getRequestedFuelVolume() { return requestedFuelVolume; }
    public void setRequestedFuelVolume(Double requestedFuelVolume) { this.requestedFuelVolume = requestedFuelVolume; }

    public LocalDateTime getDeliveryTimeWindowStart() { return deliveryTimeWindowStart; }
    public void setDeliveryTimeWindowStart(LocalDateTime deliveryTimeWindowStart) { this.deliveryTimeWindowStart = deliveryTimeWindowStart; }

    public LocalDateTime getDeliveryTimeWindowEnd() { return deliveryTimeWindowEnd; }
    public void setDeliveryTimeWindowEnd(LocalDateTime deliveryTimeWindowEnd) { this.deliveryTimeWindowEnd = deliveryTimeWindowEnd; }

    public OrderStatus getStatus() { return status; }
    public void setStatus(OrderStatus status) { this.status = status; }

    public User getCreatedBy() { return createdBy; }
    public void setCreatedBy(User createdBy) { this.createdBy = createdBy; }

    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}