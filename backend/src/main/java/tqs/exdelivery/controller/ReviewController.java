package tqs.exdelivery.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import tqs.exdelivery.entity.Review;
import tqs.exdelivery.pojo.ReviewPOJO;
import tqs.exdelivery.service.ReviewService;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/v1")
public class ReviewController {

  @Autowired ReviewService reviewService;

  @PostMapping("/deliveries/{id}/reviews")
  public ResponseEntity<Review> reviewDelivery(
      @PathVariable Long id, @Valid @RequestBody ReviewPOJO reviewPOJO) {

    var review = reviewService.createReview(id, reviewPOJO);
    if (review == null) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Could not create review");
    }

    return ResponseEntity.ok().body(review);
  }
}
