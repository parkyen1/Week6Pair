package com.techelevator.dao;

import com.techelevator.model.Reservation;
import com.techelevator.model.Site;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import javax.sql.DataSource;
import java.time.LocalDate;
import java.util.Calendar;

public class JdbcReservationDao implements ReservationDao {

    private JdbcTemplate jdbcTemplate;

    public JdbcReservationDao(DataSource dataSource) {
        jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public int createReservation(int siteId, String name, LocalDate fromDate, LocalDate toDate) {
        String sql = "SELECT campground_id " +
                "FROM site " +
                "WHERE site_id = ?;";
        Long campId = jdbcTemplate.queryForObject(sql, Long.class, siteId);

        sql = "SELECT open_from_mm, open_to_mm " +
                "FROM campground " +
                "WHERE campground_id = ?;";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, campId);
        if (results.next()) {
            int startMM = results.getInt("open_from_mm");
            int endMM = results.getInt("open_to_mm");
//            fromDate.getMonthValue();
//            toDate.getMonthValue();
            if (fromDate.getMonthValue() >= startMM && fromDate.getMonthValue() <= endMM
                    && toDate.getMonthValue() >= startMM && toDate.getMonthValue() <= endMM) {
                sql = "INSERT INTO reservation (site_id, name, from_date, to_date, create_date) " +
                        "VALUES (?, ?, ?, ?, ?) RETURNING reservation_id";
                Integer reservationId = jdbcTemplate.queryForObject(sql, Integer.class, siteId, name, fromDate, toDate, Calendar.getInstance().getTime());
                return reservationId;
            }

        }
        return -1;
    }

    private Reservation mapRowToReservation(SqlRowSet results) {
        Reservation r = new Reservation();
        r.setReservationId(results.getInt("reservation_id"));
        r.setSiteId(results.getInt("site_id"));
        r.setName(results.getString("name"));
        r.setFromDate(results.getDate("from_date").toLocalDate());
        r.setToDate(results.getDate("to_date").toLocalDate());
        r.setCreateDate(results.getDate("create_date").toLocalDate());
        return r;
    }


}
