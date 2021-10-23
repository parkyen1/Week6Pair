package com.techelevator.dao;

import com.techelevator.model.Campground;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;

public class JdbcCampgroundDao implements CampgroundDao {

    private JdbcTemplate jdbcTemplate;

    public JdbcCampgroundDao(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public List<Campground> getCampgroundsByParkId(int parkId) {

    List<Campground> campgrounds = new ArrayList<>();
    //A campground includes an ID, name, open month, closing month, and a daily fee.
    String sql = "SELECT campground_id, park.park_id, campground.name, open_from_mm, open_to_mm, daily_fee " +
        "FROM campground " +
        "JOIN park " +
        "ON park.park_id = campground.park_id ";

        SqlRowSet results = jdbcTemplate.queryForRowSet(sql);
    while (results.next()) {
        campgrounds.add(mapRowToCampground(results));
    }
        return campgrounds;
    }


    public Campground getCampgroundFromId() {
        String sql = "SELECT * " +
                "FROM campground " +
                "WHERE campground_id = ? ;";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql);
        if (results.next()) {
            Campground newCamp = mapRowToCampground(results);
            return newCamp;
        }
        return null;
    }



    private Campground mapRowToCampground(SqlRowSet results) {
        Campground camp = new Campground();
        camp.setCampgroundId(results.getInt("campground_id"));
        camp.setParkId(results.getInt("park_id"));
        camp.setName(results.getString("name"));
        camp.setOpenFromMonth(results.getInt("open_from_mm"));
        camp.setOpenToMonth(results.getInt("open_to_mm"));
        camp.setDailyFee(results.getDouble("daily_fee"));
        return camp;
    }
}
