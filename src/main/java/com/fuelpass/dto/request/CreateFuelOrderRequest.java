package com.fuelpass.dto.request;

import jakarta.validation.constraints.*;
import java.time.LocalDateTime;

/**
 * DTO for creating a new fuel order
 */
public class CreateFuelOrderRequest {

    @NotBlank(message = "Tail number is required")
    @Size(max = 10, message = "Tail number must not exceed 10 characters")
    @Pattern(regexp = "^[A-Z0-9-]+$", message = "Tail number must contain only uppercase letters, numbers, and hyphens")
    private String tailNumber;

    @NotBlank(message = "Airport ICAO code is required")
    @Size(min = 4, max = 4, message = "ICAO code must be exactly 4 characters")
    @Pattern(regexp = "^[A-Z]{4}$", message = "ICAO code must be 4 uppercase letters")
    private String airportIcaoCode;

    @NotNull(message = "Requested fuel volume is required")
    @Positive(message = "Fuel volume must be greater than 0")
    @Max(value = 100000, message = "Fuel volume cannot exceed 100,000 gallons")
    private Double requestedFuelVolume;

    @NotNull(message = "Delivery time window start is required")
    @Future(message = "Delivery time window start must be in the future")
    private LocalDateTime deliveryTimeWindowStart;

    @NotNull(message = "Delivery time window end is required")
    @Future(message = "Delivery time window end must be in the future")
    private LocalDateTime deliveryTimeWindowEnd;

    @Size(max = 500, message = "Notes must not exceed 500 characters")
    private String notes;

    // Constructors
    public CreateFuelOrderRequest() {}

    public CreateFuelOrderRequest(String tailNumber, String airportIcaoCode, Double requestedFuelVolume,
                                  LocalDateTime deliveryTimeWindowStart, LocalDateTime deliveryTimeWindowEnd,
                                  String notes) {
        this.tailNumber = tailNumber;
        this.airportIcaoCode = airportIcaoCode;
        this.requestedFuelVolume = requestedFuelVolume;
        this.deliveryTimeWindowStart = deliveryTimeWindowStart;
        this.deliveryTimeWindowEnd = deliveryTimeWindowEnd;
        this.notes = notes;
    }

    // Getters and Setters
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

    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }
}