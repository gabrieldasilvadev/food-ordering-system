package br.com.food.ordering.system.order.service.domain.ports.output.repository;

import br.com.ordering.system.order.service.domain.entity.Restaurant;
import java.util.Optional;

public interface RestaurantRepository {
    Optional<Restaurant> findRestaurantInformation(Restaurant restaurant);

}
