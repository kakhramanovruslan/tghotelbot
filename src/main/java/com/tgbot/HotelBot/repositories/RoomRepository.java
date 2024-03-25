package com.tgbot.HotelBot.repositories;

import com.tgbot.HotelBot.models.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Repository
public interface RoomRepository extends JpaRepository<Room, Long> {

    @Query("SELECT r.roomNumber FROM Room r " +
            "WHERE r.category = :category " +
            "AND r.capacity = :capacity " +
            "AND r.hasBalcony = :hasBalcony " +
            "AND r.hasBabyBed = :hasBabyBed " +
            "AND r.smokingAllowed = :smokingAllowed " +
            "AND NOT EXISTS (" +
            "SELECT 1 FROM Booking b " +
            "WHERE b.room.id = r.id " +
            "AND ((b.checkInDateTime <= :checkOutDateTime AND b.checkOutDateTime >= :checkInDateTime) " +
            "OR (b.checkInDateTime <= :checkInDateTime AND b.checkOutDateTime >= :checkOutDateTime)))")
    List<Integer> findAvailableRoomNumbers(
            @Param("category") String category,
            @Param("capacity") int capacity,
            @Param("hasBalcony") boolean hasBalcony,
            @Param("hasBabyBed") boolean hasBabyBed,
            @Param("smokingAllowed") boolean smokingAllowed,
            @Param("checkInDateTime") LocalDateTime checkInDateTime,
            @Param("checkOutDateTime") LocalDateTime checkOutDateTime
    );

    @Query("SELECT r.pricePerDay FROM Room r WHERE r.roomNumber = :roomNumber")
    Integer findPricePerDayByRoomNumber(@Param("roomNumber") int roomNumber);

    Room findByRoomNumber(String roomNumber);
}

