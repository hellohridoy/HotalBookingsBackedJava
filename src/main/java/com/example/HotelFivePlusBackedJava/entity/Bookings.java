package com.example.HotelFivePlusBackedJava.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.time.LocalDate;

@Entity
@Data
public class Bookings {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Check-in date is required")
    private LocalDate checkInDate;

    @NotNull(message = "Check-out date is required")
    private LocalDate checkOutDate;

    @Min(value = 0, message = "Total number of adults must be at least 0")
    private int totalNumberOfAdult;

    @Min(value = 0, message = "Total number of children must be at least 0")
    private int totalNumberOfChildren;

    @Transient  // This field is auto-calculated, so no need to persist it
    private int totalNumberOfGuest;

    @NotBlank(message = "Booking confirmation code is required")
    @Size(min = 6, max = 20, message = "Booking confirmation code must be between 6 and 20 characters")
    private String bookingConfirmationCode;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_id")
    private Room room;

    public void calculateTotalNumberOfGuest() {
        this.totalNumberOfGuest = this.totalNumberOfAdult + this.totalNumberOfChildren;
    }

    public void setTotalNumberOfAdult(int totalNumberOfAdult) {
        this.totalNumberOfAdult = totalNumberOfAdult;
        calculateTotalNumberOfGuest();
    }

    public void setTotalNumberOfChildren(int totalNumberOfChildren) {
        this.totalNumberOfChildren = totalNumberOfChildren;
        calculateTotalNumberOfGuest();
    }

    @Override
    public String toString() {
        return "Bookings{" +
            "id=" + id +
            ", checkInDate=" + checkInDate +
            ", checkOutDate=" + checkOutDate +
            ", totalNumberOfAdult=" + totalNumberOfAdult +
            ", totalNumberOfChildren=" + totalNumberOfChildren +
            ", totalNumberOfGuest=" + totalNumberOfGuest +
            ", bookingConfirmationCode='" + bookingConfirmationCode + '\'' +
            '}';
    }
}
