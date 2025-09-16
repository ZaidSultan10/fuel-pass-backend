package com.fuelpass.repository;

import com.fuelpass.entity.User;
import com.fuelpass.entity.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Repository interface for User entity operations
 */
@Repository
public interface UserRepository extends JpaRepository<User, UUID> {

    /**
     * Find user by email
     */
    Optional<User> findByEmail(String email);

    /**
     * Find user by email and active status
     */
    Optional<User> findByEmailAndIsActiveTrue(String email);

    /**
     * Find users by role
     */
    List<User> findByRole(UserRole role);

    /**
     * Find active users by role
     */
    List<User> findByRoleAndIsActiveTrue(UserRole role);

    /**
     * Check if email exists
     */
    boolean existsByEmail(String email);

    /**
     * Find users by name containing (case insensitive)
     */
    @Query("SELECT u FROM User u WHERE LOWER(u.email) LIKE LOWER(CONCAT('%', :email, '%')) AND u.isActive = true")
    List<User> findByNameContainingIgnoreCase(@Param("email") String email);

    /**
     * Count users by role
     */
    long countByRole(UserRole role);

    /**
     * Count active users by role
     */
    long countByRoleAndIsActiveTrue(UserRole role);

    /**
     * Count active users
     */
    long countByIsActiveTrue();

    /**
     * Count inactive users
     */
    long countByIsActiveFalse();
}