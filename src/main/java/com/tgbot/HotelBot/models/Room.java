package com.tgbot.HotelBot.models;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "rooms")
@Data
public class Room {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "room_number")
    private String roomNumber;

    @Column(name = "category")
    private String category;

    @Column(name = "capacity")
    private Integer capacity;

    @Column(name = "has_balcony")
    private Boolean hasBalcony;

    @Column(name = "has_baby_bed")
    private Boolean hasBabyBed;

    @Column(name = "smoking_allowed")
    private Boolean smokingAllowed;

    @Column(name = "price_per_day")
    private Integer pricePerDay;
}