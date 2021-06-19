package tqs.exdelivery.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import tqs.exdelivery.entity.Courier;
import tqs.exdelivery.entity.Delivery;
import tqs.exdelivery.entity.User;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class DeliveryRepositoryTest {
  private final String DELIVERY_HOST = "http:localhost:8080/";

  @Autowired private TestEntityManager entityManager;
  @Autowired private DeliveryRepository deliveryRepository;

  @Test
  void givenSetOfDeliveries_whenFindAll_thenReturnSet() {
    Delivery d1 = new Delivery();
    Delivery d2 = new Delivery();
    Arrays.asList(d1, d2)
        .forEach(
            delivery -> {
              entityManager.persistAndFlush(delivery);
            });
    List<Delivery> deliveryList = deliveryRepository.findAll();
    assertThat(deliveryList)
        .hasSize(2)
        .extracting(Delivery::getId)
        .contains(d1.getId(), d2.getId());
  }

  @Test
  void whenFindDeliveryByExistingId_thenReturnDelivery() {
    Delivery d1 = new Delivery();
    entityManager.persistAndFlush(d1);
    Delivery deliverydb = deliveryRepository.findById(d1.getId()).orElse(null);
    assertThat(deliverydb).isNotNull();
    assertThat(deliverydb.getId()).isEqualTo(d1.getId());
  }

  @Test
  void whenFindDeliveryByInvalidId_thenReturnNull() {
    Delivery deliverydb = deliveryRepository.findById(-99L).orElse(null);
    assertThat(deliverydb).isNull();
  }

  @Test
  void whenFindAllDeliveriesByState_thenReturnDeliveries() {
    Delivery d1 = new Delivery();
    Delivery d2 = new Delivery();
    Delivery d3 = new Delivery();
    d2.setState("assigned");
    d3.setState("delivered");
    var deliveries = Arrays.asList(d1, d2, d3);
    deliveries.forEach(
        delivery -> {
          entityManager.persistAndFlush(delivery);
        });
    for (Delivery delivery : deliveries) {
      List<Delivery> deliveryList = deliveryRepository.findAllByState(delivery.getState());
      assertThat(deliveryList)
          .hasSize(1)
          .extracting(Delivery::getState)
          .contains(delivery.getState());
    }
  }

  @Test
  void whenFindAllDeliveries_thenReturnAllDeliveries() {
    Delivery d1 = new Delivery();
    Delivery d2 = new Delivery();
    Arrays.asList(d1, d2)
            .forEach(
                    delivery -> {
                      entityManager.persistAndFlush(delivery);
                    });

    Pageable pageable = PageRequest.of(0, 10, Sort.by("id").descending());
    var deliveryList = deliveryRepository.findAll(pageable);
    assertThat(deliveryList)
            .hasSize(2)
            .extracting(Delivery::getId)
            .contains(d1.getId(), d2.getId());
  }

  @Test
  void whenExistsDeliveryByPurchaseHostAndByPurchaseId_thenReturnExists() {
    Delivery d1 = new Delivery();
    d1.setPurchaseHost(DELIVERY_HOST);
    d1.setPurchaseId(1L);
    entityManager.persistAndFlush(d1);

    boolean existsDelivery =
        deliveryRepository.existsByPurchaseHostAndPurchaseId(
            d1.getPurchaseHost(), d1.getPurchaseId());

    assertThat(existsDelivery).isTrue();
  }

  @Test
  void whenDoesntExistDeliveryByPurchaseHostAndByPurchaseId_thenReturnsFalse() {
    Delivery d1 = new Delivery();
    d1.setPurchaseHost(DELIVERY_HOST);
    d1.setPurchaseId(1L);
    entityManager.persistAndFlush(d1);

    boolean existsDelivery =
        deliveryRepository.existsByPurchaseHostAndPurchaseId(d1.getPurchaseHost(), 2L);

    assertThat(existsDelivery).isFalse();
  }

  @Test
  void whenFindAllDeliveriesByValidCourier_thenReturnCourierDeliveries() {
    var user = new User("tiago@gmail.com","string","Tiago", false, null);
    entityManager.persistAndFlush(user);

    var courier = new Courier(3.5, 0.0, 0.0, user);
    entityManager.persistAndFlush(courier);

    Delivery d1 = new Delivery();
    d1.setCourier(courier);
    entityManager.persistAndFlush(d1);

    Pageable pageable = PageRequest.of(0, 10, Sort.by("id").descending());
    var deliveryList = deliveryRepository.findAllByCourier(courier, pageable);
    assertThat(deliveryList)
            .hasSize(1)
            .extracting(Delivery::getCourier)
            .contains(courier);
  }

  @Test
  void whenFindAllDeliveriesByInvalidCourier_thenReturnEmpty() {
    var user = new User("tiago@gmail.com","string","Tiago", false, null);
    entityManager.persistAndFlush(user);

    var courier = new Courier(3.5, 0.0, 0.0, user);
    entityManager.persistAndFlush(courier);

    Pageable pageable = PageRequest.of(0, 10, Sort.by("id").descending());
    var deliverydb = deliveryRepository.findAllByCourier(courier, pageable);
    assertThat(deliverydb.getContent()).isEmpty();
  }

  @Test
  void whenFindAllDeliveriesByCourierUserEmail_thenReturnCourierDeliveries() {
    var user = new User("tiago@gmail.com","string","Tiago", false, null);
    entityManager.persistAndFlush(user);

    var courier = new Courier(3.5, 0.0, 0.0, user);
    entityManager.persistAndFlush(courier);

    Delivery d1 = new Delivery();
    d1.setCourier(courier);
    entityManager.persistAndFlush(d1);

    Pageable pageable = PageRequest.of(0, 10, Sort.by("id").descending());
    var deliveryList = deliveryRepository.findAllByCourierUserEmail(courier.getUser().getEmail(), pageable);
    assertThat(deliveryList)
              .hasSize(1)
              .extracting(Delivery::getCourier)
              .contains(courier);
  }

  @Test
  void whenFindAllDeliveriesByCourierInvalidUserEmail_thenReturnEmpty() {
    Pageable pageable = PageRequest.of(0, 10, Sort.by("id").descending());
    var deliverydb = deliveryRepository.findAllByCourierUserEmail("tiago@gmail.com", pageable);
    assertThat(deliverydb.getContent()).isEmpty();
  }
}
