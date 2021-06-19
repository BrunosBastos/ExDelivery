package tqs.exdelivery.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tqs.exdelivery.entity.Courier;
import tqs.exdelivery.entity.Delivery;
import tqs.exdelivery.entity.User;
import tqs.exdelivery.pojo.DeliveryPOJO;
import tqs.exdelivery.repository.DeliveryRepository;

import java.util.List;

@Service
public class DeliveryService {
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

  public List<Delivery> getCourierDeliveries(Courier courier) {
    return deliveryRepository.findAllByCourier(courier);
  }

  public List<Delivery> getAllDeliveries() {
    return deliveryRepository.findAll();
  }
}
