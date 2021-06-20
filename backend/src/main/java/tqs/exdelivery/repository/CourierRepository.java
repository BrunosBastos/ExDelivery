package tqs.exdelivery.repository;

import io.cucumber.docstring.DocString;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tqs.exdelivery.entity.Courier;

import java.util.List;

@Repository
public interface CourierRepository extends JpaRepository<Courier, Long> {
  List<Courier> findAllByIdNotInAndActiveIsTrue(List<Long> courierIds);
  Page<Courier> findAllByActiveIsTrue(Pageable pageable);
}
