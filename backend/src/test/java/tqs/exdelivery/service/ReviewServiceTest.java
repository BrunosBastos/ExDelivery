package tqs.exdelivery.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import tqs.exdelivery.entity.Courier;
import tqs.exdelivery.entity.Delivery;
import tqs.exdelivery.entity.Review;
import tqs.exdelivery.pojo.ReviewPOJO;
import tqs.exdelivery.repository.DeliveryRepository;
import tqs.exdelivery.repository.ReviewRepository;

import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.assertThat;


@ExtendWith(MockitoExtension.class)
class ReviewServiceTest {

    @Mock(lenient = true)
    private DeliveryRepository deliveryRepository;

    @Mock(lenient = true)
    private CourierService courierService;

    @Mock(lenient = true)
    private ReviewRepository reviewRepository;

    @InjectMocks
    private ReviewService reviewService;

    ReviewPOJO reviewPOJO;
    Delivery delivery;
    Delivery invalidDelivery;
    Review existingReview;

    private static final String DELIVERY_HOST = "http://localhost:8081/api/v1/";

    @BeforeEach
    void setUp() {

        var courier = new Courier(1L, 3.5, 0.0, 0.0, null);

        reviewPOJO = new ReviewPOJO(5, "test");
        delivery = new Delivery(1L, 1L, 40.23123, 50.63244, "delivered",
                        DELIVERY_HOST, courier);
        invalidDelivery = new Delivery(2L, 2L, 40.23123, 50.63244, "pending",
                DELIVERY_HOST, courier);

        existingReview = new Review(1L, 3, "Demorou um bocado mais do que esperava",
                courier, delivery);
    }

    @Test
    void whenCreateReviewWithInvalidDeliveryId_thenReturnNull() {
        when(deliveryRepository.findById(anyLong())).thenReturn(Optional.empty());
        when(reviewRepository.findByDelivery(any())).thenReturn(Optional.empty());
        var review = reviewService.createReview(1L, reviewPOJO);
        assertThat(review).isNull();
    }

    @Test
    void whenCreateReviewThatIsNotDelivered_thenReturnNull() {
        when(deliveryRepository.findById(anyLong())).thenReturn(Optional.of(invalidDelivery));
        when(reviewRepository.findByDelivery(any())).thenReturn(Optional.empty());
        var review = reviewService.createReview(1L, reviewPOJO);
        assertThat(review).isNull();
    }

    @Test
    void whenCreateReviewThatIsAlreadyReviewed_thenReturnNull() {
        when(deliveryRepository.findById(anyLong())).thenReturn(Optional.of(invalidDelivery));
        when(reviewRepository.findByDelivery(any())).thenReturn(Optional.of(existingReview));
        var review = reviewService.createReview(1L, reviewPOJO);
        assertThat(review).isNull();
    }

    @Test
    void whenCreateValidReview_thenReturnReview() {
        when(deliveryRepository.findById(anyLong())).thenReturn(Optional.of(delivery));
        when(reviewRepository.findByDelivery(any())).thenReturn(Optional.empty());
        var review = reviewService.createReview(1L, reviewPOJO);
        assertThat(review.getRating()).isEqualTo(reviewPOJO.getRating());
        assertThat(review.getComment()).isEqualTo(reviewPOJO.getComment());
        assertThat(review.getCourier().getId()).isEqualTo(delivery.getCourier().getId());
        assertThat(review.getDelivery().getId()).isEqualTo(delivery.getId());
    }
}
