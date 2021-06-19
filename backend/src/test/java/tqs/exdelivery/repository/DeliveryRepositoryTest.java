package tqs.exdelivery.repository;

import tqs.exdelivery.entity.Delivery;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class DeliveryRepositoryTest {

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
        var deliveries =  Arrays.asList(d1, d2, d3);
        deliveries
                .forEach(
                        delivery -> {
                            entityManager.persistAndFlush(delivery);
                        });
        for (Delivery delivery: deliveries) {
            List<Delivery> deliveryList = deliveryRepository.findAllByState(delivery.getState());
            assertThat(deliveryList)
                    .hasSize(1)
                    .extracting(Delivery::getState)
                    .contains(delivery.getState());
        }
    }
}
