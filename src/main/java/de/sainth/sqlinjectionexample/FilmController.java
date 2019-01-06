package de.sainth.sqlinjectionexample;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

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

  @GetMapping("/films")
  public int getFilmByTitle(@RequestParam(value = "name") String name) throws SQLException {
    int count = 0;
    try (Connection con = dataSource.getConnection(); Statement stmt = con.createStatement()) {
      String sql = "SELECT * FROM film WHERE title like '%" + name + "%'";
      ResultSet resultSet = stmt.executeQuery(sql);
      while (resultSet.next()) {
        ++count;
      }
    }
    return count;
  }
}
