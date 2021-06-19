package tqs.exdelivery.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tqs.exdelivery.entity.Delivery;
import tqs.exdelivery.pojo.DeliveryPOJO;
import tqs.exdelivery.repository.DeliveryRepository;

import java.util.List;

@Service
public class DeliveryService {
  @Autowired private DeliveryRepository deliveryRepository;

  @Autowired private CourierService courierService;

  public Delivery assignDelivery(DeliveryPOJO deliveryPOJO) {
    var delivery =
        new Delivery(
            deliveryPOJO.getHost(),
            deliveryPOJO.getPurchaseId(),
            deliveryPOJO.getLat(),
            deliveryPOJO.getLon());

    var courier = courierService.assignBestCourier(deliveryPOJO);
    if (courier == null) {
      return null;
    }
    delivery.setCourier(courier);
    delivery.setState("assigned");
    deliveryRepository.save(delivery);

    return delivery;
  }

  public List<Delivery> getAssignedDeliveries() {
    return deliveryRepository.findAllByState("assigned");
  }
}
