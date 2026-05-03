package app.service;

import app.domain.House;
import app.repository.HouseRepository;
import app.repository.HouseRepositoryHibernate;
import app.repository.HouseRepositoryJdbc;
import app.repository.HouseRepositoryList;

import java.math.BigDecimal;
import java.util.List;

public class HouseServiceImpl implements HouseService {

    // private final HouseRepository repository = new HouseRepositoryList();
    // И вот это называется принцип слабой связности. Вот если бы у нас здесь было поле не типа HouseRepository,
    // а HouseRepositoryMap, то есть мы жестко бы привязали класс к другому классу. Это называлось бы сильная связность,
    // и такого рекомендуют избегать. Потому что в таком случае нельзя было бы легко заменить один репозиторий на другой.
    // А у нас здесь соблюдается принцип слабой связности. Наш класс HouseServiceImpl зависит не от другого класса,
    // он зависит от интерфейса. И это слабая связность называется. И благодаря этому в переменную интерфейсного типа
    // мы можем положить любой объект любого класса, главное, чтобы он реализовывал этот интерфейс.
    // private final HouseRepository repository = new HouseRepositoryJdbc();
    private final HouseRepository repository = new HouseRepositoryHibernate();


    @Override
    public House save(House house) {
        if(house == null){
            throw new IllegalArgumentException("Дом не может быть пустым");
        }
        if(house.getColor() == null || house.getColor().trim().isEmpty()){
            throw new IllegalArgumentException("Цвет дома не может быть пустым");
        }
        if(house.getArea() <= 0){
            throw new IllegalArgumentException("Площадь должна быть больше 0");
        }
        if(house.getRooms() <= 0){
            throw new IllegalArgumentException("Количество комнат должно быть больше 0");
        }
        if(house.getYear() <= 0){
            throw new IllegalArgumentException("Год постройки указан не верно");
        }
        if(house.getPrice() == null || house.getPrice().compareTo(BigDecimal.ZERO) <= 0){
            throw new IllegalArgumentException("Цена должна быть больше 0");
        }
        return repository.save(house);
    }

    @Override
    public List<House> getAll() {
        List<House> houses = repository.findAll();
        // Здесь может быть какая-либо бизнес-логика, например, фильтрация домов
        return houses;
    }

    @Override
    public House getById(Long id) {
        // Может прийти null - поэтому нужна проверка
        // Сначала проверяем входные данные
        if (id == null) {
            throw new IllegalArgumentException("Id не может быть пустым");
        }

        House house = repository.findById(id);
        // Потом проверяем, есть ли объект
        if(house == null){
            throw new IllegalArgumentException("Дом с id " + id + " не найден");
        }

        return house;
    }

    @Override
    public void deleteById(Long id) {
        // Сначала проверяем входные данные
        if(id == null) {
            throw new IllegalArgumentException("Id не может быть пустым");
        }

        House house = repository.findById(id);
        // Потом проверяем, есть ли объект
        if (house == null){
            throw new IllegalArgumentException("Дом с id " + id + " не найден");
        }
        // Только потом работаем с репозиторием
        repository.deleteById(id);
    }

}
