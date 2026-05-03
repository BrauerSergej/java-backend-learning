package app.repository;

import app.domain.House;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
// Статический импорт позволяет импортировать, например, константы из какого-то интерфейса или класса так,
// как если бы они находились прямо в этом классе
// Можно ли импортировать сразу все константы из интерфейса указать звездочку, и они импортируются сразу все.
import static app.constants.Constants.*;

public class HouseRepositoryJdbc implements HouseRepository {

    // Пропишем метод который открывает соединение с базой данных
    private Connection getConnection() {
        // Первое, что нам нужно сделать, это подгрузить драйвер для подключения
        // к базе в память работающего приложения.
        // Самое интересное, что у нас нигде в коде нет прямого обращения к классу драйвера для базы данных.
        // Следовательно, раз он в коде нигде не упоминается, загрузчик классов его не загрузит в память приложения.
        // А значит, когда наше приложение попытается соединиться с базой, у него это не получится, потому
        // что драйвера нет в памяти. И вот чтобы избежать такой ситуации, мы принудительно подгружаем
        // класс драйвера в память работающей Java-машины. Для этого используется метод forName класса Class.
        // И здесь мы указываем путь к классу, который мы принудительно хотим загрузить в память Java-машины.
        try {
            Class.forName(DB_DRIVER_PATH);
            // Дальше нам нужно создать строку подключения к базе данных
            // jdbc:postgresql://10.20.30.40:5432/h_w_03_Houses
            // 1-й %s → protocol       → jdbc:postgresql
            // 2-й %s → host           → 10.20.30.40
            // %d     → port           → 5432
            // 3-й %s → databaseName   → h_w_03_Houses
            String dbUrl = String.format("%s://%s:%d/%s",
                    DB_PROTOCOL, DB_HOST, DB_PORT, DB_NAME);
            // Осталось создать физическое соединение с базой и вернуть его как результат работы нашего метода
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
        // try с ресурсами в скобках
        // Так как мы с вами использовали блок try с ресурсами, когда он отработает, это соединение с
        // базой будет автоматически закрыто. Это и позволяет нам как раз-таки делать блок try с ресурсами.
        // Вот для чего мы его здесь используем.
        try (Connection connection = getConnection()) {
            // Пользуясь вот этим уже фактически открытым соединением с базой, нам нужно, во-первых, составить
            // SQL-запрос, отправить этот SQL-запрос в базу и получить от базы ответ. И на базе ответа создать
            // объекты автомобилей, которые нам прилетели из базы, и вернуть их в список. Так вот, нам нужно
            // прописать SQL-запрос прямо в коде.
            String query = "SELECT * FROM house;";
            // Так вот, объект Statement, это такой специальный объект, который умеет отправлять запросы в базу данных.
            // Поэтому, чтобы отправить запрос в базу, нужно создать этот объект. А как мы его создаем?
            // А мы его создаем, обращаясь к объекту Connection, то есть к самому подключению к базе.
            Statement statement = connection.createStatement();
            // Дальше мы, пользуясь этим объектом statement, отправим вот этот вот запрос в базу и получим ответ.
            // А ответ от базы мы получаем в виде третьего интерфейса. Это интерфейс ResultSet.
            // Вот в виде объекта ResultSet нам приходит ответ из базы.
            // Так вот, если вы хотите из базы что-то прочитать, то вам нужен метод executeQuery.
            // Если вы хотите в базу что-то сохранить, то вам нужен метод execute.
            ResultSet resultSet = statement.executeQuery(query);
            // Задача нашего метода вернуть список домов. Поэтому мы его давайте сейчас создадим пустой,
            // то есть создаем пустой список домов.
            List<House> houseList = new ArrayList<>();
            // он переключает нас на следующую строчку выборки и одновременно возвращает либо true, либо false.
            // если строка существует, и false, если строка не существует.
            while (resultSet.next()) {
                // Чтобы нам удобно было создавать объекты домов на базе информации, имеющейся в таблице,
                // нам понадобится конструктор в House по всем полям
                Long id = resultSet.getLong("id");
                String color = resultSet.getString("color");
                double area = resultSet.getDouble("area");
                int rooms = resultSet.getInt("rooms");
                int year = resultSet.getInt("year");
                BigDecimal price = resultSet.getBigDecimal("price");
                // И теперь мы можем создать Java-объект дома из этих значений.
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
