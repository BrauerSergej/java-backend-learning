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
        return houses;
    }

    @Override
    public House getById(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("Id не может быть пустым");
        }

        House house = repository.findById(id);
        if(house == null){
            throw new IllegalArgumentException("Дом с id " + id + " не найден");
        }

        return house;
    }

    @Override
    public void deleteById(Long id) {
        if(id == null) {
            throw new IllegalArgumentException("Id не может быть пустым");
        }

        House house = repository.findById(id);
        if (house == null){
            throw new IllegalArgumentException("Дом с id " + id + " не найден");
        }
        repository.deleteById(id);
    }

}
