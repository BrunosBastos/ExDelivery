package tqs.exdelivery.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import tqs.exdelivery.entity.Courier;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class CourierRepositoryTest {

  @Autowired private TestEntityManager entityManager;
  @Autowired private CourierRepository courierRepository;

  @Test
  void givenSetOfCouriers_whenFindAll_thenReturnSet() {
    Courier c1 = new Courier(3.5, 0.0, 0.0, null);
    Courier c2 = new Courier(5, 10, 20, null);
    Arrays.asList(c1, c2)
        .forEach(
            courier -> {
              entityManager.persistAndFlush(courier);
            });
    List<Courier> courierList = courierRepository.findAll();
    assertThat(courierList).hasSize(2).extracting(Courier::getId).contains(c1.getId(), c2.getId());
  }

  @Test
  void whenFindCourierByExistingId_thenReturnCourier() {
    Courier c1 = new Courier(3.5, 0.0, 0.0, null);
    entityManager.persistAndFlush(c1);
    Courier courierdb = courierRepository.findById(c1.getId()).orElse(null);
    assertThat(courierdb).isNotNull();
    assertThat(courierdb.getId()).isEqualTo(c1.getId());
    assertThat(courierdb.getReputation()).isEqualTo(c1.getReputation());
  }

  @Test
  void whenFindCourierByInvalidId_thenReturnNull() {
    Courier courierdb = courierRepository.findById(-99L).orElse(null);
    assertThat(courierdb).isNull();
  }

  @Test
  void whenFindAllCouriersByIdNotInList_thenReturnCouriers() {
    Courier c1 = new Courier(3.5, 0.0, 0.0, null);
    Courier c2 = new Courier(5, 10, 20, null);
    Courier c3 = new Courier(5, 10, 20, null);
    Arrays.asList(c1, c2, c3)
        .forEach(
            courier -> {
              entityManager.persistAndFlush(courier);
            });
    List<Courier> courierList =
        courierRepository.findAllByIdNotIn(Arrays.asList(c1.getId(), c2.getId()));
    assertThat(courierList).hasSize(1).extracting(Courier::getId).contains(c3.getId());
  }
}
