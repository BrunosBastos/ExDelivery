package tqs.exdelivery.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.internal.verification.VerificationModeFactory;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import tqs.exdelivery.entity.Courier;
import tqs.exdelivery.entity.Delivery;
import tqs.exdelivery.pojo.DeliveryPOJO;
import tqs.exdelivery.repository.DeliveryRepository;
import java.util.Arrays;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
public class DeliveryServiceTest {

    private final String DELIVERY_HOST = "http:localhost:8080/";


    @Mock(lenient = true)
    private DeliveryRepository deliveryRepository;

    @Mock(lenient = true) private CourierService courierService;

    @InjectMocks
    private DeliveryService deliveryService;

    Courier c1;
    Delivery d1;
    Delivery d2;
    DeliveryPOJO delPojo1;
    @BeforeEach
    void setUp() {
        c1 = new Courier(1L, 5, 0.0, 0.0, null);


        d1 = new Delivery(1L, 1L,40.23123,50.63244,"delivered",DELIVERY_HOST,c1);
        d2 = new Delivery(2L, 2L,50.23123,50.63244,"pending",DELIVERY_HOST,null);

        delPojo1 = new DeliveryPOJO(DELIVERY_HOST, 1L, 0, 0);

        when(deliveryService.getAssignedDeliveries()).thenReturn(Arrays.asList(d1,d2));
        when(deliveryRepository.findAll()).thenReturn(Arrays.asList());
        when(deliveryRepository.save(any())).thenReturn(d1);
    }

    @Test
    void whenGetAssignedDeliveries_thenReturnDeliveries() {
        var deliveries = deliveryService.getAssignedDeliveries();
        assertThat(deliveries).hasSize(2);
        verify(deliveryRepository, VerificationModeFactory.times(1)).findAllByState(any());
    }

    @Test
    void whenAssignDeliveryWithNoCourier_thenReturnNull() {
        when(courierService.assignBestCourier(any())).thenReturn(null);
        var courier = deliveryService.assignDelivery(delPojo1);
        assertThat(courier).isNull();
    }

    @Test
    void whenAssignDeliveryWithValidCourier_thenReturnNull() {
        when(courierService.assignBestCourier(any())).thenReturn(c1);
        var delivery = deliveryService.assignDelivery(delPojo1);
        assertThat(delivery.getCourier().getId()).isEqualTo(c1.getId());
    }


}
