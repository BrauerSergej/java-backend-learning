package app.repository;

import app.domain.House;

import java.util.List;

public interface HouseRepository {

    // CRUD - Create Read Update Delete
    House save (House house);
    List<House> findAll();
    House findById(Long id);
    void deleteById(Long id);
}
