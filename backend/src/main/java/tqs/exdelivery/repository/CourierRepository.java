package tqs.exdelivery.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tqs.exdelivery.entity.Courier;

@Repository
public interface CourierRepository extends JpaRepository<Courier, Long> {}
