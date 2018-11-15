package ro.scoalainformala.trips.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ro.scoalainformala.trips.entity.Spot;

public interface SpotRepository extends JpaRepository<Spot, Long> {
}
