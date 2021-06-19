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
import tqs.exdelivery.repository.DeliveryRepository;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DeliveryServiceTest {

  private final String DELIVERY_HOST = "http:localhost:8080/";
  Courier c1;
  Delivery d1;
  Delivery d2;
  DeliveryPOJO delPojo1;

  @Mock(lenient = true)
  private DeliveryRepository deliveryRepository;

  @Mock(lenient = true)
  private CourierService courierService;

  @InjectMocks private DeliveryService deliveryService;

  @BeforeEach
  void setUp() {
    c1 = new Courier(1L, 5, 0.0, 0.0, null);

    d1 = new Delivery(1L, 1L, 40.23123, 50.63244, "delivered", DELIVERY_HOST, c1);
    d2 = new Delivery(2L, 2L, 50.23123, 50.63244, "pending", DELIVERY_HOST, null);

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
}
