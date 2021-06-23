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
import tqs.exdelivery.pojo.ReviewRequestPOJO;
import tqs.exdelivery.repository.DeliveryRepository;
import tqs.exdelivery.repository.ReviewRepository;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReviewServiceTest {

  private static final String DELIVERY_HOST = "http://localhost:8081/api/v1/";

  ReviewRequestPOJO reviewRequestPOJO;
  Delivery delivery;
  Delivery invalidDelivery;
  Review existentReview;

  @Mock(lenient = true)
  private DeliveryRepository deliveryRepository;

  @Mock(lenient = true)
  private CourierService courierService;

  @Mock(lenient = true)
  private ReviewRepository reviewRepository;

  @InjectMocks private ReviewService reviewService;

  @BeforeEach
  void setUp() {

    var courier = new Courier(1L, 3.5, 0.0, 0.0, null, true);

    reviewRequestPOJO = new ReviewRequestPOJO(DELIVERY_HOST, 1L, 5, "test");

    delivery = new Delivery(1L, 1L, 40.23123, 50.63244, "delivered", DELIVERY_HOST, courier);
    invalidDelivery = new Delivery(2L, 2L, 40.23123, 50.63244, "pending", DELIVERY_HOST, courier);

    existentReview = new Review(1L, 3, "Demorou um bocado mais do que esperava", courier, delivery);
  }

  @Test
  void whenCreateReviewWithInvalidDeliveryId_thenReturnNull() {
    when(deliveryRepository.findById(anyLong())).thenReturn(Optional.empty());
    when(reviewRepository.findByDelivery(any())).thenReturn(Optional.empty());
    var review = reviewService.createReview(reviewRequestPOJO);
    assertThat(review).isNull();
  }

  @Test
  void whenCreateReviewThatIsNotDelivered_thenReturnNull() {
    when(deliveryRepository.findById(anyLong())).thenReturn(Optional.of(invalidDelivery));
    when(reviewRepository.findByDelivery(any())).thenReturn(Optional.empty());
    var review = reviewService.createReview(reviewRequestPOJO);
    assertThat(review).isNull();
  }

  @Test
  void whenCreateReviewThatIsAlreadyReviewed_thenReturnNull() {
    when(deliveryRepository.findById(anyLong())).thenReturn(Optional.of(invalidDelivery));
    when(reviewRepository.findByDelivery(any())).thenReturn(Optional.of(existentReview));
    var review = reviewService.createReview(reviewRequestPOJO);
    assertThat(review).isNull();
  }

  @Test
  void whenCreateValidReview_thenReturnReview() {
    when(deliveryRepository.findByPurchaseHostAndPurchaseId(anyString(), anyLong())).thenReturn(Optional.of(delivery));
    when(reviewRepository.findByDelivery(any())).thenReturn(Optional.empty());
    var review = reviewService.createReview(reviewRequestPOJO);
    assertThat(review.getRating()).isEqualTo(reviewRequestPOJO.getRating());
    assertThat(review.getComment()).isEqualTo(reviewRequestPOJO.getComment());
    assertThat(review.getCourier().getId()).isEqualTo(delivery.getCourier().getId());
    assertThat(review.getDelivery().getId()).isEqualTo(delivery.getId());
  }

  @Test
  void whenGetExistentReview_thenReturnReview() {
    when(deliveryRepository.findById(anyLong())).thenReturn(Optional.of(delivery));
    when(reviewRepository.findByDelivery(any())).thenReturn(Optional.of(existentReview));
    var review = reviewService.getReview(1L);
    assertThat(review.getRating()).isEqualTo(existentReview.getRating());
    assertThat(review.getComment()).isEqualTo(existentReview.getComment());
    assertThat(review.getCourier().getId()).isEqualTo(delivery.getCourier().getId());
    assertThat(review.getDelivery().getId()).isEqualTo(delivery.getId());
  }

  @Test
  void whenGetNonExistentDelivery_thenReturnNull() {
    when(deliveryRepository.findById(anyLong())).thenReturn(Optional.of(delivery));
    when(reviewRepository.findByDelivery(any())).thenReturn(Optional.empty());
    var review = reviewService.getReview(1L);
    assertThat(review).isNull();
  }

  @Test
  void whenGetNonExistentReview_thenReturnNull() {
    when(deliveryRepository.findById(anyLong())).thenReturn(Optional.empty());
    when(reviewRepository.findByDelivery(any())).thenReturn(Optional.empty());
    var review = reviewService.getReview(1L);
    assertThat(review).isNull();
  }
}
