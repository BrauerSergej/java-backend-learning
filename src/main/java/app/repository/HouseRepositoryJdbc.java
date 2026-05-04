package app.repository;

import app.domain.House;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import static app.constants.Constants.*;

public class HouseRepositoryJdbc implements HouseRepository {

    private Connection getConnection() {
        try {
            Class.forName(DB_DRIVER_PATH);
            // jdbc:postgresql://10.20.30.40:5432/h_w_03_Houses
            // 1-й %s → protocol       → jdbc:postgresql
            // 2-й %s → host           → 10.20.30.40
            // %d     → port           → 5432
            // 3-й %s → databaseName   → h_w_03_Houses
            String dbUrl = String.format("%s://%s:%d/%s",
                    DB_PROTOCOL, DB_HOST, DB_PORT, DB_NAME);
            return DriverManager.getConnection(dbUrl, DB_USERNAME, DB_PASSWORD);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }


    }

    @Override
    public House save(House house) {
        String sql = "INSERT INTO house(color, area, rooms, year, price) VALUES (?, ?, ?, ?, ?)";
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setString(1, house.getColor());
            preparedStatement.setDouble(2, house.getArea());
            preparedStatement.setInt(3, house.getRooms());
            preparedStatement.setInt(4, house.getYear());
            preparedStatement.setBigDecimal(5, house.getPrice());
            preparedStatement.executeUpdate();

            try (ResultSet resultSet = preparedStatement.getGeneratedKeys()) {
                if (resultSet.next()) {
                    long newId = resultSet.getLong(1);
                    house.setId(newId);
                }

            }
            return house;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<House> findAll() {
        try (Connection connection = getConnection()) {
            String query = "SELECT * FROM house;";
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);
            List<House> houseList = new ArrayList<>();
            while (resultSet.next()) {
                Long id = resultSet.getLong("id");
                String color = resultSet.getString("color");
                double area = resultSet.getDouble("area");
                int rooms = resultSet.getInt("rooms");
                int year = resultSet.getInt("year");
                BigDecimal price = resultSet.getBigDecimal("price");
                House house = new House(id, color, area, rooms, year, price);
                houseList.add(house);

            }
            return houseList;

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public House findById(Long id) {
        try (Connection connection = getConnection()) {
            String query = "SELECT * FROM house WHERE id = ?;";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setLong(1, id);
            ResultSet result = statement.executeQuery();
            House house = null;
            if (result.next()) {
                String color = result.getString("color");
                double area = result.getDouble("area");
                int rooms = result.getInt("rooms");
                int year = result.getInt("year");
                BigDecimal price = result.getBigDecimal("price");
                house = new House(id, color, area, rooms, year, price);
            }
            return house;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public void deleteById(Long id) {
        try (Connection connection = getConnection()) {
            String query = "DELETE FROM house WHERE id = ?;";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setLong(1, id);
            statement.executeUpdate();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }
}
