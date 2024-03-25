package com.tgbot.HotelBot.models;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "bookings")
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "room_id", referencedColumnName = "id")
    private Room room;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    @Column(name = "check_in_date_time")
    private LocalDateTime checkInDateTime;

    @Column(name = "check_out_date_time")
    private LocalDateTime checkOutDateTime;

    @Column(name = "total_price")
    private Integer totalPrice;

}
