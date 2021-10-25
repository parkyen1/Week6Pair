package com.techelevator.dao;

import com.techelevator.model.Reservation;
import com.techelevator.model.Site;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import javax.sql.DataSource;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class JdbcSiteDao implements SiteDao {

    private JdbcTemplate jdbcTemplate;

    public JdbcSiteDao(DataSource dataSource) {
        jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public List<Site> getSitesThatAllowRVs(int parkId) {
        List<Site> rvSites = new ArrayList<>();
        String sql = "SELECT site_id, site.campground_id, site_number, max_occupancy, accessible, max_rv_length, utilities " +
                "FROM site " +
                "JOIN campground " +
                "ON site.campground_id = campground.campground_id " +
                "JOIN park " +
                "ON campground.park_id = park.park_id " +
                "WHERE (max_rv_length > 0 AND max_rv_length IS NOT null) AND park.park_id = ?;";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, parkId);
        while (results.next()) {
            rvSites.add(mapRowToSite(results));
        }

        return rvSites;
    }

    @Override
    public List<Site> getAvailableSites(int parkId) {
        List<Site> availableSites = new ArrayList<>();
        String sql = "SELECT s.site_id, s.campground_id, s.site_number, s.max_occupancy, s.accessible, s.max_rv_length, s.utilities " +
                "FROM site s " +
                "LEFT JOIN reservation r " +
                "ON s.site_id = r.site_id " +
                "JOIN campground c " +
                "ON s.campground_id = c.campground_id " +
                "WHERE c.park_id = ? AND s.site_id NOT IN (SELECT s.site_id " +
                "FROM site s " +
                "LEFT JOIN reservation r " +
                "ON s.site_id = r.site_id " +
                "JOIN campground c " +
                "ON s.campground_id = c.campground_id " +
                "WHERE CURRENT_DATE BETWEEN r.from_date AND r.to_date);"; //to_date > CURRENT_DATE AND
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, parkId);
        while (results.next()) {
            availableSites.add(mapRowToSite(results));
        }
        return availableSites;
    }





    private Site mapRowToSite(SqlRowSet results) {
        Site site = new Site();
        site.setSiteId(results.getInt("site_id"));
        site.setCampgroundId(results.getInt("campground_id"));
        site.setSiteNumber(results.getInt("site_number"));
        site.setMaxOccupancy(results.getInt("max_occupancy"));
        site.setAccessible(results.getBoolean("accessible"));
        site.setMaxRvLength(results.getInt("max_rv_length"));
        site.setUtilities(results.getBoolean("utilities"));
        return site;
    }
}
