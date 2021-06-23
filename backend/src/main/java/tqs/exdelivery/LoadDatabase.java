package tqs.exdelivery;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import tqs.exdelivery.entity.Courier;
import tqs.exdelivery.entity.Delivery;
import tqs.exdelivery.entity.Review;
import tqs.exdelivery.entity.User;
import tqs.exdelivery.repository.CourierRepository;
import tqs.exdelivery.repository.DeliveryRepository;
import tqs.exdelivery.repository.ReviewRepository;
import tqs.exdelivery.repository.UserRepository;

import java.util.Arrays;

@Configuration
class LoadDatabase {

  private static final String DELIVERED_STATE = "delivered";
  private static final String EXAMPLE_PASS = "string";
  @Value("${app.MY_HOST:localhost}")
  private String DELIVERY_HOST;

  @Bean
  CommandLineRunner initDatabase(
      UserRepository users,
      CourierRepository couriers,
      DeliveryRepository deliveries,
      ReviewRepository reviews) {

    return args -> {
      String deliveryHost = "http://" + DELIVERY_HOST + ":8080/api/v1/purchases";

      BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
      User adminUser =
          new User(
              1L,
              "leandro@gmail.com",
              encoder.encode(LoadDatabase.EXAMPLE_PASS),
              "Leandro",
              true,
              null);

      User courierUser1 =
          new User(
              2L,
              "tiago@gmail.com",
              encoder.encode(LoadDatabase.EXAMPLE_PASS),
              "Tiago",
              false,
              null);
      User courierUser2 =
          new User(
              3L,
              "joaquim@gmail.com",
              encoder.encode(LoadDatabase.EXAMPLE_PASS),
              "Joaquim",
              false,
              null);
      User courierUser3 =
          new User(
              4L,
              "Lionel@gmail.com",
              encoder.encode(LoadDatabase.EXAMPLE_PASS),
              "Lionel",
              false,
              null);
      User courierUser4 =
          new User(
              5L,
              "leonardo@gmail.com",
              encoder.encode(LoadDatabase.EXAMPLE_PASS),
              "Leonardo",
              false,
              null);
      Arrays.asList(adminUser, courierUser1, courierUser2, courierUser3, courierUser4)
          .forEach(users::save);

      Courier courier1 = new Courier(1L, 3.5, 0.0, 0.0, courierUser1, true);
      Courier courier2 = new Courier(2L, 5, 0.0, 0.0, courierUser2, true);
      Courier courier3 = new Courier(3L, 3, 0.0, 0.0, courierUser3, true);
      Courier courier4 = new Courier(4L, 2.0, 0.0, 0.0, courierUser4, true);
      Arrays.asList(courier1, courier2, courier3, courier4).forEach(couriers::save);

      Delivery delivery1 =
          new Delivery(
              1L, 1L, 40.23123, 50.63244, LoadDatabase.DELIVERED_STATE, deliveryHost, courier1);
      Delivery delivery2 =
          new Delivery(
              2L, 2L, 44.32132, 55.32132, LoadDatabase.DELIVERED_STATE, deliveryHost, courier1);
      Delivery delivery3 =
          new Delivery(3L, 3L, 44.32132, 51.32132, "assigned", deliveryHost, courier1);
      Delivery delivery4 =
          new Delivery(
              4L, 4L, 43.32132, 56.32132, LoadDatabase.DELIVERED_STATE, deliveryHost, courier2);
      Delivery delivery5 = new Delivery(5L, 5L, 42.32132, 54.32132, "pending", deliveryHost, null);
      Arrays.asList(delivery1, delivery2, delivery3, delivery4, delivery5)
          .forEach(deliveries::save);

      Review review1 =
          new Review(1L, 3, "Demorou um bocado mais do que esperava", courier1, delivery1);
      Review review2 = new Review(2L, 4, "Muito simpátido", courier1, delivery2);
      Arrays.asList(review1, review2).forEach(reviews::save);
    };
  }
}
