package tqs.exdelivery.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import tqs.exdelivery.entity.Courier;
import tqs.exdelivery.entity.Delivery;
import tqs.exdelivery.pojo.DeliveryPOJO;
import tqs.exdelivery.repository.DeliveryRepository;

import java.util.List;

@Service
public class DeliveryService {
  private static final int PAGE_SIZE = 10;

  @Autowired private DeliveryRepository deliveryRepository;

  @Autowired private CourierService courierService;

  public Delivery assignDelivery(DeliveryPOJO deliveryPOJO) {
    if (deliveryRepository.existsByPurchaseHostAndPurchaseId(
        deliveryPOJO.getPurchaseHost(), deliveryPOJO.getPurchaseId())) {
      return null;
    }

    var delivery =
        new Delivery(
            deliveryPOJO.getPurchaseHost(),
            deliveryPOJO.getPurchaseId(),
            deliveryPOJO.getLat(),
            deliveryPOJO.getLon());

    var courier = courierService.assignBestCourier(deliveryPOJO);

    if (courier != null) {
      delivery.setCourier(courier);
      delivery.setState("assigned");
    }
    deliveryRepository.save(delivery);

    return delivery;
  }

  public List<Delivery> getAssignedDeliveries() {
    return deliveryRepository.findAllByState("assigned");
  }

  public List<Delivery> getCourierDeliveries(Courier courier, int page, boolean recent) {
    Pageable pageable =
        PageRequest.of(
            page, PAGE_SIZE, recent ? Sort.by("id").descending() : Sort.by("id").ascending());

    return deliveryRepository.findAllByCourier(courier, pageable).getContent();
  }

  public List<Delivery> getDeliveries(String email, int page, boolean recent) {
    Pageable pageable =
        PageRequest.of(
            page, PAGE_SIZE, recent ? Sort.by("id").descending() : Sort.by("id").ascending());
    if (email == null) {
      return deliveryRepository.findAll(pageable).getContent();
    } else {
      return deliveryRepository.findAllByCourierUserEmail(email, pageable).getContent();
    }
  }
}
