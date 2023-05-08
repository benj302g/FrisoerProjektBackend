package com.example.frisoerprojektbackend.repository;

import com.example.frisoerprojektbackend.model.Booking;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BookingRepository extends JpaRepository<Booking,Integer> {

  public List<Booking> findBookingsByBookingUserProfile_Id(int id);


}
