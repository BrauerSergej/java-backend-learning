package app.service;

import app.domain.House;

import java.util.List;

public interface HouseService {

    House save(House house);
    List<House> getAll();
    House getById(Long id);
    void deleteById(Long id);
}
