package tqs.exdelivery.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import tqs.exdelivery.entity.Courier;
import tqs.exdelivery.entity.Delivery;
import tqs.exdelivery.pojo.DeliveryPOJO;
import tqs.exdelivery.repository.DeliveryRepository;

import java.net.ConnectException;
import java.util.Arrays;
import java.util.List;

@Service
public class DeliveryService {
  private static final int PAGE_SIZE = 10;

  private static final String DELIVERY_ASSIGNED = "assigned";

  private static final String DELIVERY_PENDING = "pending";

  @Autowired private DeliveryRepository deliveryRepository;

  @Autowired private CourierService courierService;

  @Autowired private RestTemplate restTemplate;

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

    var courier = courierService.assignBestCourier(delivery);

    if (courier != null) {
      delivery.setCourier(courier);
      delivery.setState(DELIVERY_ASSIGNED);
    }
    deliveryRepository.save(delivery);

    return delivery;
  }

  public List<Delivery> getAssignedDeliveries() {
    return deliveryRepository.findAllByState(DELIVERY_ASSIGNED);
  }

  public List<Delivery> getPendingDeliveries() {
    return deliveryRepository.findAllByState(DELIVERY_PENDING);
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

  public Delivery confirmDelivery(Long deliveryId, Courier courier) {
    var deliverydb = deliveryRepository.findById(deliveryId);
    if (deliverydb.isEmpty()
        || !deliverydb.get().getState().equals(DELIVERY_ASSIGNED)
        || deliverydb.get().getCourier().getId() != courier.getId()) {
      return null;
    }
    var delivery = deliverydb.get();
    delivery.setState("delivered");
    deliveryRepository.save(delivery);
    return delivery;
  }

  public void checkDeliveriesToAssign() {
    for (Delivery delivery : getPendingDeliveries()) {
      var courier = courierService.assignBestCourier(delivery);
      if (courier != null) {
        delivery.setCourier(courier);
        delivery.setState(DELIVERY_ASSIGNED);
      }
      deliveryRepository.save(delivery);
    }
  }

  public void notifyHost(Delivery delivery) throws ConnectException {
    var headers = new HttpHeaders();
    headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
    HttpEntity<String> entity = new HttpEntity<>(headers);
    // doesnt work if service not up
    // var response = restTemplate.exchange(delivery.getPurchaseHost(), HttpMethod.PUT, entity,
    // String.class).getBody();
  }
}
