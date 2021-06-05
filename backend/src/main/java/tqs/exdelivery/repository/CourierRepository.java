package tqs.exdelivery.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tqs.exdelivery.entity.Courier;

public interface CourierRepository extends JpaRepository<Courier, Long> {
}
