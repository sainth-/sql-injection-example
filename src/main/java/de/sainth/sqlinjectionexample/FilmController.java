package de.sainth.sqlinjectionexample;

import java.sql.*;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class FilmController {

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
      ResultSet resultSet = stmt.executeQuery(sql);
      if (resultSet.next()) {
        count = resultSet.getInt(1);
      }
    }
    return count;
  }

  @GetMapping("/preparedFilmCount")
  public int getPreparedFilmCountByTitle(@RequestParam(value = "name") String name) throws SQLException {
    int count = 0;
    try (Connection con = dataSource.getConnection(); PreparedStatement stmt = con.prepareStatement("SELECT COUNT(*) FROM film WHERE title LIKE ?")) {
      stmt.setString(1, "%" + name + "%");
      ResultSet resultSet = stmt.executeQuery();
      if (resultSet.next()) {
        count = resultSet.getInt(1);
      }
    }
    return count;
  }
}
