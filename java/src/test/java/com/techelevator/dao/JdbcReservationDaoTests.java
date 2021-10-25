package com.techelevator.dao;

import com.techelevator.model.Reservation;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.cglib.core.Local;

import java.util.*;
import java.time.LocalDate;

import static org.junit.Assert.assertEquals;

public class JdbcReservationDaoTests extends BaseDaoTests {

    private ReservationDao dao;
   // private JdbcReservationDao sut;

    @Before
    public void setup() {
        dao = new JdbcReservationDao(dataSource);
    }

    @Test
    public void createReservation_Should_ReturnNewReservationId() {
        int reservationCreated = dao.createReservation(1,
                "TEST NAME",
                LocalDate.now().plusDays(1),
                LocalDate.now().plusDays(3));

        assertEquals(5, reservationCreated);
    }



    @Test
    public void getAvailableSitesDateRange_Should_ReturnSites() {
        List<Reservation> reservationList = dao.getAvailableSitesDateRange(1);
        Assert.assertEquals(2, reservationList.size());

    }

}
