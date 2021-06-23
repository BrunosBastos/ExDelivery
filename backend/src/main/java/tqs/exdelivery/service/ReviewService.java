package tqs.exdelivery.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tqs.exdelivery.entity.Review;
import tqs.exdelivery.pojo.ReviewPOJO;
import tqs.exdelivery.pojo.ReviewRequestPOJO;
import tqs.exdelivery.repository.DeliveryRepository;
import tqs.exdelivery.repository.ReviewRepository;

@Service
public class ReviewService {

  @Autowired DeliveryRepository deliveryRepository;

  @Autowired ReviewRepository reviewRepository;

  @Autowired CourierService courierService;


  public Review getReview(Long deliveryId) {
    var delivery = deliveryRepository.findById(deliveryId);

    if (delivery.isPresent()) {
      var review = reviewRepository.findByDelivery(delivery.get());

      if (review.isPresent()) {
        return review.get();
      }
    }
    return null;
  }

  public Review createReview(ReviewRequestPOJO reviewRequestPOJO) {

    var delivery = deliveryRepository.findByPurchaseHostAndPurchaseId(
            reviewRequestPOJO.getHost(), reviewRequestPOJO.getPurchaseId());
    if (delivery.isEmpty()) {
      return null;
    }
    // can only review a delivery when it has been delivered
    if (!delivery.get().getState().equals("delivered")) {
      return null;
    }
    // cannot review the same delivery twice
    if (reviewRepository.findByDelivery(delivery.get()).isPresent()) {
      return null;
    }

    var review =
        new Review(
                reviewRequestPOJO.getRating(),
                reviewRequestPOJO.getComment(),
            delivery.get().getCourier(),
            delivery.get());

    reviewRepository.save(review);

    courierService.updateReputation(delivery.get().getCourier());
    return review;
  }
}
