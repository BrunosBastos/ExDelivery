package tqs.exdelivery.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tqs.exdelivery.entity.Courier;
import tqs.exdelivery.entity.Delivery;

import java.util.List;

@Repository
public interface DeliveryRepository extends JpaRepository<Delivery, Long> {
  List<Delivery> findAllByState(String state);
  List<Delivery> findAllByCourier(Courier courier);
  Page<Delivery> findAllByCourier(Courier courier, Pageable pageable);

  boolean existsByPurchaseHostAndPurchaseId(String purchaseHost, Long purchaseId);
}
