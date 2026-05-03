package app.repository;

import app.domain.House;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class HouseRepositoryList implements HouseRepository{

    // Имитация базы данных
    private final List<House> database = new ArrayList<>();
    // Это поле учитывает максимальный идентификатор в базе данных
    // Соответственно, допустим, если в базе хранится дом с максимальным id 5,
    // то тут будет сидеть значение 5. И тогда следующему дому мы присвоим 6.
    // То есть прибавим к этому полю 1.
    private long maxId;

    public HouseRepositoryList(){
        save(new House("White", 120.5, 4, 2010, new BigDecimal("250000.00")));
        save(new House("Brown", 85.0, 3, 2005, new BigDecimal("180000.00")));
        save(new House("Gray", 95.3, 3, 2012, new BigDecimal("180000.00")));
        save(new House("Red", 165.5, 5, 2009, new BigDecimal("400000.00")));
    }

    @Override
    public House save(House house) {
        house.setId(++maxId);
        database.add(house);
        return house;
    }

    @Override
    public List<House> findAll() {
        return new ArrayList<>(database);
    }

    @Override
    public House findById(Long id) {
        for(House house: database){
            if(house.getId().equals(id)){
                return house;
            }
        }
        return null;
    }

    @Override
    public void deleteById(Long id) {
      database.removeIf(house -> house.getId().equals(id));
    }
}
