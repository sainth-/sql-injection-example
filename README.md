This application is intended to demonstrate an SQL injection vulnerability.

It's a simple web service returning a count of all films containing a given string via GET. There are the following endpoints:

1. GET `http://localhost:8080/manualFilmCount?name=<name>` generates the query via
   ```java
   "SELECT * FROM film WHERE title LIKE '%" + name + "%'";
   ```
   after executing the query, the corresponding resultSet is evaluated via a simple `while`-Loop to count all appropriate rows.

1. GET `http://localhost:8080/filmCount?name=<name>` generates the query via
   ```java
   "SELECT COUNT(*) FROM film WHERE title LIKE '%" + name + "%'"
   ```
   
1. GET `http://localhost:8080/preparedFilmCount1?name=<name>` generates uses a bad prepared statement without bind parameters to query the database 

1. GET `http://localhost:8080/preparedFilmCount2?name=<name>` generates uses a prepared statement using bind parameters to query the database 

As database the great [Sakila Sample Database](https://dev.mysql.com/doc/sakila/en/) is used. So you need a running mysql database. 

As connection url `jdbc:mysql://localhost/sakila` is used with `root` as user and of course without any password.
