package com.fuelpass.repository;

import com.fuelpass.entity.FuelOrder;
import com.fuelpass.entity.OrderStatus;
import com.fuelpass.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * Repository interface for FuelOrder entity operations
 */
@Repository
public interface FuelOrderRepository extends JpaRepository<FuelOrder, UUID> {

    /**
     * Find orders by created by user
     */
    Page<FuelOrder> findByCreatedBy(User createdBy, Pageable pageable);

    /**
     * Find orders by status
     */
    Page<FuelOrder> findByStatus(OrderStatus status, Pageable pageable);

    /**
     * Find orders by airport ICAO code
     */
    Page<FuelOrder> findByAirportIcaoCode(String airportIcaoCode, Pageable pageable);

    /**
     * Find orders by tail number
     */
    Page<FuelOrder> findByTailNumber(String tailNumber, Pageable pageable);

    /**
     * Find orders by status and airport
     */
    Page<FuelOrder> findByStatusAndAirportIcaoCode(OrderStatus status, String airportIcaoCode, Pageable pageable);

    /**
     * Find orders by created by user and status
     */
    Page<FuelOrder> findByCreatedByAndStatus(User createdBy, OrderStatus status, Pageable pageable);

    /**
     * Find orders by delivery time window
     */
    @Query("SELECT fo FROM FuelOrder fo WHERE fo.deliveryTimeWindowStart >= :startTime AND fo.deliveryTimeWindowEnd <= :endTime")
    Page<FuelOrder> findByDeliveryTimeWindow(@Param("startTime") LocalDateTime startTime,
                                             @Param("endTime") LocalDateTime endTime,
                                             Pageable pageable);

    /**
     * Count orders by status
     */
    long countByStatus(OrderStatus status);

    /**
     * Count orders by created by user
     */
    long countByCreatedBy(User createdBy);

    /**
     * Count orders by airport ICAO code
     */
    long countByAirportIcaoCode(String airportIcaoCode);

    /**
     * Find orders with custom filters
     */
    @Query("SELECT fo FROM FuelOrder fo WHERE " +
            "(:airportIcaoCode IS NULL OR fo.airportIcaoCode = :airportIcaoCode) AND " +
            "(:status IS NULL OR fo.status = :status) AND " +
            "(:tailNumber IS NULL OR fo.tailNumber LIKE %:tailNumber%) AND " +
            "(:createdBy IS NULL OR fo.createdBy = :createdBy)")
    Page<FuelOrder> findWithFilters(@Param("airportIcaoCode") String airportIcaoCode,
                                    @Param("status") OrderStatus status,
                                    @Param("tailNumber") String tailNumber,
                                    @Param("createdBy") User createdBy,
                                    Pageable pageable);
}