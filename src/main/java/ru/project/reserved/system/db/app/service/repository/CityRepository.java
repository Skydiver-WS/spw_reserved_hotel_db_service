package ru.project.reserved.system.db.app.service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.project.reserved.system.db.app.service.entity.City;

@Repository
public interface CityRepository extends JpaRepository<City, Integer> {
}
