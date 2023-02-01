package br.com.food.ordering.system.order.service.domain.ports.input.service;

import br.com.food.ordering.system.order.service.domain.dto.create.CreateOrderCommand;
import br.com.food.ordering.system.order.service.domain.dto.create.CreateOrderResponse;
import br.com.food.ordering.system.order.service.domain.dto.track.TrackOrderQuery;
import br.com.food.ordering.system.order.service.domain.dto.track.TrackOrderResponse;

public interface OrderApplicationService {

   CreateOrderResponse createOrder(CreateOrderCommand createOrderCommand);
   TrackOrderResponse trackOrder(TrackOrderQuery trackOrderQuery);
}
