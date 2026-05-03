package app.repository;

import app.domain.House;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import org.hibernate.cfg.Configuration;

import java.util.List;

public class HouseRepositoryHibernate implements HouseRepository {

    private final EntityManager entityManager;

    public HouseRepositoryHibernate() {
        entityManager = new Configuration()
                .configure("postgres.cfg.xml")
                .buildSessionFactory()
                .createEntityManager();
    }

    @Override
    public House save(House house) {
        if (house == null) {
            throw new IllegalArgumentException("Дом не может быть null");
        }
        EntityTransaction transaction = entityManager.getTransaction();
        try {
            transaction.begin();
            entityManager.persist(house);
            transaction.commit();
            return house;
        } catch (Exception e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            throw new RuntimeException("Ошибка при сохранении дома ", e);
        }
    }

    @Override
    public List<House> findAll() {
        try {
            return entityManager.createQuery("from House", House.class).getResultList();
        } catch (Exception e) {
            throw new RuntimeException("Не удалось загрузить список данных из базы данных", e);
        }

    }

    @Override
    public House findById(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("ID не может быть null");
        }
        if(id <= 0) {
            throw new IllegalArgumentException("ID должен быть положительным числом, получено: " + id);
        }
        return entityManager.find(House.class, id);
    }

    @Override
    public void deleteById(Long id) {
        EntityTransaction transaction = entityManager.getTransaction();
        try {
            transaction.begin();
            House foundHouse = findById(id);
            if (foundHouse == null) {
                throw new RuntimeException("House with id " + id + " not found");
            }
            entityManager.remove(foundHouse);
            transaction.commit();
        } catch (Exception e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            throw new RuntimeException(e);
        }
    }
}
