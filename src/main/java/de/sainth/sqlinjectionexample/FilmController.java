package de.sainth.sqlinjectionexample;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.sql.DataSource;
import java.sql.*;

@RestController
public class FilmController {

  private static final Logger logger = LoggerFactory.getLogger(FilmController.class);
  private final DataSource dataSource;

  @Autowired
  public FilmController(DataSource dataSource) {
    this.dataSource = dataSource;
  }

  @GetMapping("/manualFilmCount")
  public int getManuallyFilmCountByTitle(@RequestParam(value = "name") String name) throws SQLException {
    int count = 0;
    try (Connection con = dataSource.getConnection(); Statement stmt = con.createStatement()) {
      String sql = "SELECT * FROM film WHERE title LIKE '%" + name + "%'";
      logger.debug("getManuallyFilmCountByTitle : " + sql);
      ResultSet resultSet = stmt.executeQuery(sql);
      while (resultSet.next()) {
        count++;
      }
    }
    return count;
  }

  @GetMapping("/filmCount")
  public int getFilmCountByTitle(@RequestParam(value = "name") String name) throws SQLException {
    int count = 0;
    try (Connection con = dataSource.getConnection(); Statement stmt = con.createStatement()) {
      String sql = "SELECT COUNT(*) FROM film WHERE title LIKE '%" + name + "%'";
      logger.debug("getFilmCountByTitle: " + sql);
      ResultSet resultSet = stmt.executeQuery(sql);
      if (resultSet.next()) {
        count = resultSet.getInt(1);
      }
    }
    return count;
  }

  @GetMapping("/preparedFilmCount1")
  public int getMaybePreparedFilmCountByTitle(@RequestParam(value = "name") String name) throws SQLException {
    int count = 0;
    try (Connection con = dataSource.getConnection(); PreparedStatement stmt = con.prepareStatement("SELECT COUNT(*) FROM film WHERE title LIKE '%" + name + "%'")) {
      logger.debug("getMaybePreparedFilmCountByTitle: " + stmt);
      ResultSet resultSet = stmt.executeQuery();
      if (resultSet.next()) {
        count = resultSet.getInt(1);
      }
    }
    return count;
  }

  @GetMapping("/preparedFilmCount2")
  public int getPreparedFilmCountByTitle(@RequestParam(value = "name") String name) throws SQLException {
    int count = 0;
    try (Connection con = dataSource.getConnection(); PreparedStatement stmt = con.prepareStatement("SELECT COUNT(*) FROM film WHERE title LIKE ?")) {
      stmt.setString(1, "%" + name + "%");
      logger.debug("getPreparedFilmCountByTitle: " + stmt);
      ResultSet resultSet = stmt.executeQuery();
      if (resultSet.next()) {
        count = resultSet.getInt(1);
      }
    }
    return count;
  }
}
