package tqs.exdelivery.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tqs.exdelivery.entity.Courier;
import tqs.exdelivery.entity.Delivery;
import tqs.exdelivery.pojo.DeliveryPOJO;
import tqs.exdelivery.repository.CourierRepository;

import java.util.ArrayList;
import java.util.List;

@Service
public class CourierService {
  private static final int EARTH_RADIUS = 6371;
  private static final Logger logger = LogManager.getLogger(CourierService.class);
  @Autowired private CourierRepository courierRepository;
  @Autowired private DeliveryService deliveryService;

  public List<Courier> getAllCouriers() {
    return courierRepository.findAll();
  }

  private List<Courier> getAvailableCouriers() {
    List<Delivery> assignedDeliveries = deliveryService.getAssignedDeliveries();
    List<Long> assignedCouriers = new ArrayList<>();

    for (Delivery delivery : assignedDeliveries) {
      assignedCouriers.add(delivery.getCourier().getId());
    }

    return courierRepository.findAllByIdNotIn(assignedCouriers);
  }

  public Courier assignBestCourier(DeliveryPOJO deliveryPOJO) {
    var availableCouriers = getAvailableCouriers();
    logger.info(availableCouriers);
    Courier bestCourier = null;
    double min = Double.MAX_VALUE;

    for (Courier courier : availableCouriers) {
      double courierLon = Math.toRadians(courier.getLon());
      double courierLat = Math.toRadians(courier.getLat());

      double dLon = Math.toRadians(deliveryPOJO.getLon()) - courierLon;
      double dLat = Math.toRadians(deliveryPOJO.getLat()) - courierLat;
      double a =
          Math.pow(Math.sin(dLat / 2), 2)
              + Math.cos(courierLat)
                  * Math.cos(Math.toRadians(deliveryPOJO.getLat()))
                  * Math.pow(Math.sin(dLon / 2), 2);

      double c = 2 * Math.asin(Math.sqrt(a));

      double distance = c * EARTH_RADIUS;

      double finalResult = distance / 30 * 0.6 + 0.4 * (1 - courier.getReputation() / 5);

      var formatstr =
          String.format(
              "Found a new Best Courier %s with a value %s.",
              courier.getId(), finalResult);

      if (finalResult < min) {
        logger.info(formatstr);
        min = finalResult;
        bestCourier = courier;
      }
    }

    return bestCourier;
  }
}
