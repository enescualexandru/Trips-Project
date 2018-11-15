package ro.scoalainformala.trips.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ro.scoalainformala.trips.entity.Trip;

@Repository
public interface TripRepository extends JpaRepository<Trip, Long> {
}
