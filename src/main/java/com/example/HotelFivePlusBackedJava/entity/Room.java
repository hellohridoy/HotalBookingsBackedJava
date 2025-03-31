package com.example.HotelFivePlusBackedJava.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@Table(name = "rooms")
public class Room {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String roomType;
    private BigDecimal roomPrice;
    private String roomPhotoUrl;
    private String roomDescription;

    // Using available without "is" prefix for better JPA compatibility
    private boolean available = true;

    @OneToMany(mappedBy = "room", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Bookings> bookings = new ArrayList<>();

    @Override
    public String toString() {
        return "Room{" +
            "id=" + id +
            ", roomType='" + roomType + '\'' +
            ", roomPrice=" + roomPrice +
            ", roomPhotoUrl='" + roomPhotoUrl + '\'' +
            ", roomDescription='" + roomDescription + '\'' +
            ", available=" + available +
            '}';
    }
}
