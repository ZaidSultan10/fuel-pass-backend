package com.fuelpass.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.Statement;

/**
 * Database migration utility for creating tables and indexes
 */
@Component
@Order(1) // Run before other initializers
public class DatabaseMigration implements CommandLineRunner {

    @Autowired
    private DataSource dataSource;

    @Override
    public void run(String... args) throws Exception {
        createTablesIfNotExist();
        createIndexesIfNotExist();
    }

    /**
     * Create tables if they don't exist
     */
    private void createTablesIfNotExist() {
        String createUsersTable = """
            CREATE TABLE IF NOT EXISTS users (
                id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
                email VARCHAR(255) NOT NULL UNIQUE,
                password VARCHAR(255) NOT NULL,
                role VARCHAR(20) NOT NULL,
                is_active BOOLEAN NOT NULL DEFAULT true,
                created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
            )
            """;

        String createFuelOrdersTable = """
            CREATE TABLE IF NOT EXISTS fuel_orders (
                id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
                tail_number VARCHAR(10) NOT NULL,
                airport_icao_code VARCHAR(4) NOT NULL,
                requested_fuel_volume DECIMAL(10,2) NOT NULL,
                delivery_time_window_start TIMESTAMP NOT NULL,
                delivery_time_window_end TIMESTAMP NOT NULL,
                status VARCHAR(20) NOT NULL DEFAULT 'PENDING',
                created_by UUID NOT NULL REFERENCES users(id),
                updated_by UUID NOT NULL REFERENCES users(id),
                notes TEXT,
                created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
            )
            """;

        executeSql(createUsersTable);
        executeSql(createFuelOrdersTable);

        System.out.println("✓ Database tables created/verified successfully!");
    }

    /**
     * Create indexes if they don't exist
     */
    private void createIndexesIfNotExist() {
        String[] indexes = {
                "CREATE INDEX IF NOT EXISTS idx_user_email ON users(email)",
                "CREATE INDEX IF NOT EXISTS idx_user_role ON users(role)",
                "CREATE INDEX IF NOT EXISTS idx_fuel_order_tail_number ON fuel_orders(tail_number)",
                "CREATE INDEX IF NOT EXISTS idx_fuel_order_airport ON fuel_orders(airport_icao_code)",
                "CREATE INDEX IF NOT EXISTS idx_fuel_order_status ON fuel_orders(status)",
                "CREATE INDEX IF NOT EXISTS idx_fuel_order_created_by ON fuel_orders(created_by)",
                "CREATE INDEX IF NOT EXISTS idx_fuel_order_created_at ON fuel_orders(created_at)"
        };

        for (String index : indexes) {
            executeSql(index);
        }

        System.out.println("✓ Database indexes created/verified successfully!");
    }

    /**
     * Execute SQL statement
     */
    private void executeSql(String sql) {
        try (Connection connection = dataSource.getConnection();
             Statement statement = connection.createStatement()) {
            statement.execute(sql);
        } catch (Exception e) {
            System.err.println("Error executing SQL: " + sql);
            System.err.println("Error: " + e.getMessage());
        }
    }
}