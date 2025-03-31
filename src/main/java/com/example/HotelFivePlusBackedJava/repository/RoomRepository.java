package com.example.HotelFivePlusBackedJava.repository;

import com.example.HotelFivePlusBackedJava.entity.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface RoomRepository extends JpaRepository<Room, Long> {

    /**
     * Find rooms by their type
     */
    List<Room> findByRoomType(String roomType);

    /**
     * Get all distinct room types
     */
    @Query("SELECT DISTINCT r.roomType FROM Room r")
    List<String> findAllRoomTypes();

    /**
     * Find available rooms
     */
    List<Room> findByAvailableTrue();

    /**
     * Find available rooms for a specific date range and room type
     */
    @Query("SELECT r FROM Room r WHERE r.roomType = :roomType AND r.available = true AND r.id NOT IN " +
        "(SELECT b.room.id FROM Bookings b WHERE " +
        "(:checkOutDate > b.checkInDate AND :checkInDate < b.checkOutDate))")
    List<Room> findAvailableRoomsByDateAndType(
        @Param("checkInDate") LocalDate checkInDate,
        @Param("checkOutDate") LocalDate checkOutDate,
        @Param("roomType") String roomType);

    /**
     * Find all available rooms that don't have current bookings
     */
    @Query("SELECT r FROM Room r WHERE r.available = true AND r.id NOT IN " +
        "(SELECT b.room.id FROM Bookings b WHERE " +
        "CURRENT_DATE BETWEEN b.checkInDate AND b.checkOutDate)")
    List<Room> findAllAvailableRooms();

    /**
     * Get rooms by multiple room types
     */
    List<Room> findByRoomTypeIn(List<String> roomTypes);

    /**
     * Find rooms by price range
     */
    @Query("SELECT r FROM Room r WHERE r.roomPrice BETWEEN :minPrice AND :maxPrice")
    List<Room> findByPriceRange(
        @Param("minPrice") java.math.BigDecimal minPrice,
        @Param("maxPrice") java.math.BigDecimal maxPrice);
}
