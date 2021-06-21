package tqs.exdelivery.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.internal.verification.VerificationModeFactory;
import org.mockito.junit.jupiter.MockitoExtension;
import tqs.exdelivery.entity.Courier;
import tqs.exdelivery.entity.Delivery;
import tqs.exdelivery.pojo.DeliveryPOJO;
import tqs.exdelivery.repository.CourierRepository;

import java.util.Arrays;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CourierServiceTest {

  private final String DELIVERY_HOST = "http:localhost:8080/";
  Courier c1;
  Courier c2;
  Courier c3;
  Delivery d1;
  Delivery d2;
  DeliveryPOJO delPojo1;

  @Mock(lenient = true)
  private CourierRepository courierRepository;

  @Mock(lenient = true)
  private DeliveryService deliveryService;

  @InjectMocks private CourierService courierService;

  @BeforeEach
  void setUp() {
    c1 = new Courier(1L, 5, 0.0, 0.0, null, true);
    c2 = new Courier(2L, 5, 10, 20, null, true);
    c3 = new Courier(3L, 3, 10, 20, null, true);

    d1 = new Delivery(1L, 1L, 0, 0, "delivered", DELIVERY_HOST, c1);
    d2 = new Delivery(2L, 2L, 0, 0, "pending", DELIVERY_HOST, null);

    delPojo1 = new DeliveryPOJO(DELIVERY_HOST, 1L, 0, 0);

    when(courierRepository.findAll()).thenReturn(Arrays.asList(c1, c2));
    when(deliveryService.getAssignedDeliveries()).thenReturn(Arrays.asList(d1));
  }

  @Test
  void whenGetAllCouriers_thenReturnAllCouriers() {
    var couriers = courierService.getAllCouriers();
    assertThat(couriers).hasSize(2).extracting(Courier::getId).contains(1L, 2L);
    verify(courierRepository, VerificationModeFactory.times(1)).findAll();
  }

  @Test
  void whenNoCourierIsFree_thenReturnNull() {
    when(courierRepository.findAllByIdNotInAndActiveIsTrue(anyList())).thenReturn(Arrays.asList());
    var courier = courierService.assignBestCourier(d2);
    assertThat(courier).isNull();
    verify(courierRepository, VerificationModeFactory.times(1))
        .findAllByIdNotInAndActiveIsTrue(anyList());
  }

  @Test
  void whenCourierHasGreaterRating_thenReturnCourier() {
    when(courierRepository.findAllByIdNotInAndActiveIsTrue(anyList()))
        .thenReturn(Arrays.asList(c2, c3));
    var courier = courierService.assignBestCourier(d2);
    assertThat(courier.getId()).isEqualTo(c2.getId());
    verify(courierRepository, VerificationModeFactory.times(1))
        .findAllByIdNotInAndActiveIsTrue(anyList());
  }

  @Test
  void whenCourierIsCloser_thenReturnCourier() {
    when(courierRepository.findAllByIdNotInAndActiveIsTrue(anyList()))
        .thenReturn(Arrays.asList(c1, c2));
    var courier = courierService.assignBestCourier(d2);
    assertThat(courier.getId()).isEqualTo(c1.getId());
    verify(courierRepository, VerificationModeFactory.times(1))
        .findAllByIdNotInAndActiveIsTrue(anyList());
  }

  @Test
  void whenFireCourierInvalidId_thenReturnNull() {
    when(courierRepository.findById(-99L)).thenReturn(Optional.empty());
    var courier = courierService.fireCourier(-99L);
    assertThat(courier).isNull();
    verify(courierRepository, VerificationModeFactory.times(1)).findById(-99L);
  }

  @Test
  void whenFireCourierValidId_thenReturnCourier() {
    when(courierRepository.findById(1L)).thenReturn(Optional.of(c1));
    var courier = courierService.fireCourier(1L);
    assertThat(courier.isActive()).isFalse();
    verify(courierRepository, VerificationModeFactory.times(1)).findById(1L);
    verify(deliveryService, VerificationModeFactory.times(1)).reAssignCourierAssignedDeliveries(c1);
    verify(courierRepository, VerificationModeFactory.times(1)).save(any());
  }
}
