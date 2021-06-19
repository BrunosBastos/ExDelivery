package tqs.exdelivery.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tqs.exdelivery.entity.Delivery;

import java.util.List;

@Repository
public interface DeliveryRepository extends JpaRepository<Delivery, Long> {
  List<Delivery> findAllByState(String state);

  boolean existsByPurchaseHostAndPurchaseId(String purchaseHost, Long purchaseId);
}
