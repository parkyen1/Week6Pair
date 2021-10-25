package com.techelevator.dao;

import com.techelevator.model.Reservation;
import com.techelevator.model.Site;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import java.time.LocalDate;
import java.util.List;

public interface ReservationDao {

    int createReservation(int siteId, String name, LocalDate fromDate, LocalDate toDate);

    List<Reservation> getAvailableSitesDateRange(int parkId);

}
