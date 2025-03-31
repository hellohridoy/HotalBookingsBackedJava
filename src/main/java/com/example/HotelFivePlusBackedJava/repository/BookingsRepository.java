package com.example.HotelFivePlusBackedJava.repository;

import com.example.HotelFivePlusBackedJava.entity.Bookings;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BookingsRepository extends JpaRepository<Bookings, Long> {

    List<Bookings>findByRoomId(Long roomId);

    Optional<Bookings> findByBookingConfirmationCode(String confirmationCode);

    List<Bookings>findByUserId(Long userId);
}
