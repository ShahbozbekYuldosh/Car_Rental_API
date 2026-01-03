package com.carrental.repository;

import com.carrental.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Map;

@Repository
public interface DashboardRepository extends JpaRepository<User, Long> {

    @Query(value = "SELECT " +
            "(SELECT COUNT(*) FROM cars) as total_cars, " +
            "(SELECT COUNT(*) FROM bookings WHERE status = 'ACTIVE') as active_bookings, " +
            "(SELECT SUM(amount) FROM payments WHERE status = 'SUCCESS') as total_revenue",
            nativeQuery = true)
    Map<String, Object> getAdminStatistics();
}