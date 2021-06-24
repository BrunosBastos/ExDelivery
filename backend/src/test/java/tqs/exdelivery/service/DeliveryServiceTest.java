package tqs.exdelivery.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.internal.verification.VerificationModeFactory;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import tqs.exdelivery.entity.Courier;
import tqs.exdelivery.entity.Delivery;
import tqs.exdelivery.entity.User;
import tqs.exdelivery.pojo.DeliveryPOJO;
import tqs.exdelivery.repository.DeliveryRepository;

import java.util.Arrays;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DeliveryServiceTest {

  private final String DELIVERY_HOST = "http:localhost:8080/";
  Courier c1;
  Courier c2;
  Delivery d1;
  Delivery d2;
  Delivery d3;
  Delivery d4;
  DeliveryPOJO delPojo1;

  @Mock(lenient = true)
  private DeliveryRepository deliveryRepository;

  @Mock(lenient = true)
  private CourierService courierService;

  @InjectMocks private DeliveryService deliveryService;

  @BeforeEach
  void setUp() {
    var user = new User();
    user.setEmail("tiago@gmail.com");
    c1 = new Courier(1L, 5, 0.0, 0.0, user, true);

    var user2 = new User();
    user2.setEmail("tiago@gmail.com");
    c2 = new Courier(2L, 5, 0, 0, user2, true);

    d1 = new Delivery(1L, 1L, 40.23123, 50.63244, "delivered", DELIVERY_HOST, c1);
    d2 = new Delivery(2L, 2L, 50.23123, 50.63244, "pending", DELIVERY_HOST, null);
    d3 = new Delivery(3L, 3L, 10, 20, "assigned", DELIVERY_HOST, c2);
    d4 = new Delivery(4L, 4L, 10, 20, "assigned", DELIVERY_HOST, c1);

    delPojo1 = new DeliveryPOJO(DELIVERY_HOST, 1L, 0, 0);

    when(deliveryService.getAssignedDeliveries()).thenReturn(Arrays.asList(d1, d2));
    when(deliveryRepository.findAll()).thenReturn(Arrays.asList());
    when(deliveryRepository.save(any())).thenReturn(d1);
  }

  @Test
  void whenGetAssignedDeliveries_thenReturnDeliveries() {
    when(deliveryRepository.existsByPurchaseHostAndPurchaseId(any(), any())).thenReturn(false);

    var deliveries = deliveryService.getAssignedDeliveries();
    assertThat(deliveries).hasSize(2);
    verify(deliveryRepository, VerificationModeFactory.times(1)).findAllByState(any());
  }

  @Test
  void whenAssignDeliveryWithNoCourier_thenReturnPendingDelivery() {
    when(deliveryRepository.existsByPurchaseHostAndPurchaseId(any(), any())).thenReturn(false);

    when(courierService.assignBestCourier(any())).thenReturn(null);
    var delivery = deliveryService.assignDelivery(delPojo1);
    assertThat(delivery.getCourier()).isNull();
    assertThat(delivery.getState()).isEqualTo("pending");
    verify(courierService, VerificationModeFactory.times(1)).assignBestCourier(any());
  }

  @Test
  void whenAssignDeliveryWithValidCourier_thenReturnAssignedDelivery() {
    when(deliveryRepository.existsByPurchaseHostAndPurchaseId(any(), any())).thenReturn(false);

    when(courierService.assignBestCourier(any())).thenReturn(c1);
    var delivery = deliveryService.assignDelivery(delPojo1);
    assertThat(delivery.getCourier().getId()).isEqualTo(c1.getId());
    assertThat(delivery.getState()).isEqualTo("assigned");
    verify(courierService, VerificationModeFactory.times(1)).assignBestCourier(any());
  }

  @Test
  void whenAssignExistingDelivery_thenReturnNull() {
    when(deliveryRepository.existsByPurchaseHostAndPurchaseId(any(), any())).thenReturn(true);
    var delivery = deliveryService.assignDelivery(delPojo1);
    assertThat(delivery).isNull();
    verify(deliveryRepository, VerificationModeFactory.times(1))
        .existsByPurchaseHostAndPurchaseId(any(), any());
  }

  @Test
  void whenGetCourierDeliveries_thenReturnDeliveries() {
    Page<Delivery> page = new PageImpl<>(Arrays.asList(d1));
    when(deliveryRepository.findAllByCourier(any(), any())).thenReturn(page);

    var deliveryPage = deliveryService.getCourierDeliveries(c1, 0, true);
    assertThat(deliveryPage).hasSize(1);
    assertThat(deliveryPage.get(0).getCourier().getId()).isEqualTo(c1.getId());
    assertThat(deliveryPage.get(0).getId()).isEqualTo(d1.getId());
  }

  @Test
  void whenGetDeliveries_thenReturnDeliveries() {
    Page<Delivery> page = new PageImpl<>(Arrays.asList(d1, d2));
    when(deliveryRepository.findAll(any(Pageable.class))).thenReturn(page);

    var deliveryPage = deliveryService.getDeliveries(null, 0, true);
    assertThat(deliveryPage).hasSize(2);
    assertThat(deliveryPage.get(0).getId()).isEqualTo(d1.getId());
    assertThat(deliveryPage.get(1).getId()).isEqualTo(d2.getId());
  }

  @Test
  void whenGetDeliveriesByCourierEmail_thenReturnCourierDeliveries() {
    Page<Delivery> page = new PageImpl<>(Arrays.asList(d1));
    when(deliveryRepository.findAllByCourierUserEmail(anyString(), any(Pageable.class)))
        .thenReturn(page);

    var deliveryPage = deliveryService.getDeliveries(c1.getUser().getEmail(), 0, true);
    assertThat(deliveryPage).hasSize(1);
    assertThat(deliveryPage.get(0).getCourier().getId()).isEqualTo(c1.getId());
    assertThat(deliveryPage.get(0).getId()).isEqualTo(d1.getId());
  }

  @Test
  void whenConfirmDeliveryWithInvalidDeliveryId_thenReturnNull() {
    when(deliveryRepository.findById(1000L)).thenReturn(Optional.empty());
    var delivery = deliveryService.confirmDelivery(1000L, c1);
    assertThat(delivery).isNull();
  }

  @Test
  void whenConfirmDeliveryNotAssigned_thenReturnNull() {
    when(deliveryRepository.findById(2L)).thenReturn(Optional.of(d2));
    var delivery = deliveryService.confirmDelivery(2L, c1);
    assertThat(delivery).isNull();
  }

  @Test
  void whenConfirmDeliveryNotAssignedToCourier_thenReturnNull() {
    when(deliveryRepository.findById(3L)).thenReturn(Optional.of(d3));
    var delivery = deliveryService.confirmDelivery(3L, c1);
    assertThat(delivery).isNull();
  }

  @Test
  void whenConfirmDeliveryWithAssignedCourier_thenReturnUpdatedDelivery() {
    when(deliveryRepository.findById(4L)).thenReturn(Optional.of(d4));
    var delivery = deliveryService.confirmDelivery(4L, c1);
    assertThat(delivery.getState()).isEqualTo("delivered");
    verify(deliveryRepository, times(1)).save(any());
  }

  @Test
  void whenCheckDeliveriesToAssign_thenVerifyFunctionCall() {

    d3.setCourier(null);
    d4.setCourier(null);
    d3.setState("pending");
    d4.setState("pending");

    when(deliveryRepository.findAllByState("pending")).thenReturn(Arrays.asList(d2, d3, d4));
    deliveryService.checkDeliveriesToAssign();
    verify(deliveryRepository, times(3)).save(any());
  }

  @Test
  void whenReassignCourier_thenVerifyMethodCall() {
    d3.setCourier(null);
    d4.setCourier(null);
    d3.setState("pending");
    d4.setState("pending");
    when(deliveryRepository.findAllByStateAndCourier(any(), any(Courier.class)))
        .thenReturn(Arrays.asList(d3, d4));
    deliveryService.reAssignCourierAssignedDeliveries(c1);
    verify(deliveryRepository, times(2)).save(any());
    verify(courierService, times(2)).assignBestCourier(any());
  }

  @Test
  void whenGetDeliveryWithInvalidId_thenReturnNull() {
    when(deliveryRepository.findById(any())).thenReturn(Optional.empty());
    var delivery = deliveryService.getDelivery(1000L, c1);
    assertThat(delivery).isNull();

    verify(deliveryRepository, times(1)).findById(1000L);
  }

  @Test
  void whenGetDeliveryWithCourier_thenReturnDelivery() {
    when(deliveryRepository.findById(any())).thenReturn(Optional.of(d1));
    var delivery = deliveryService.getDelivery(c1.getId(), c1);
    assertThat(delivery).isNotNull();
    assertThat(delivery.getId()).isEqualTo(d1.getId());
    assertThat(delivery.getCourier().getId()).isEqualTo(d1.getCourier().getId());
    verify(deliveryRepository, times(1)).findById(1L);
  }

  @Test
  void whenGetDeliveryWithAdmin_thenReturnDelivery() {
    when(deliveryRepository.findById(any())).thenReturn(Optional.of(d1));
    var delivery = deliveryService.getDelivery(1000L, null);
    assertThat(delivery.getId()).isEqualTo(d1.getId());
    verify(deliveryRepository, times(1)).findById(any());
  }

  @Test
  void whenGetDeliveryWithWrongCourier_thenReturnDelivery() {
    when(deliveryRepository.findById(any())).thenReturn(Optional.of(d1));
    var delivery = deliveryService.getDelivery(1L, c2);
    assertThat(delivery).isNull();
    verify(deliveryRepository, times(1)).findById(any());
  }
}
