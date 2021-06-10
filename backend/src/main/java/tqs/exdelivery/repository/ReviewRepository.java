package tqs.exdelivery.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tqs.exdelivery.entity.Review;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {}
