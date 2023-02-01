package br.com.ordering.system.order.service.domain;

import br.com.ordering.system.order.service.domain.entity.Order;
import br.com.ordering.system.order.service.domain.entity.Product;
import br.com.ordering.system.order.service.domain.exception.OrderDomainException;
import br.com.food.ordering.system.domain.valueobject.identifier.ProductId;
import br.com.ordering.system.order.service.domain.entity.Restaurant;
import br.com.ordering.system.order.service.domain.event.OrderCancelledEvent;
import br.com.ordering.system.order.service.domain.event.OrderCreatedEvent;
import br.com.ordering.system.order.service.domain.event.OrderPaidEvent;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class OrderDomainServiceImpl implements OrderDomainService{
    private static final String UTC = "UTC";
    @Override
    public OrderCreatedEvent validateAndInitiateOrder(Order order, Restaurant restaurant) {
        validateRestaurant(restaurant);
        setOrderProductInformation(order, restaurant);
        order.validateOrder();
        order.initializeOrder();
        log.info("Order with id: {} is initialized", order.getId().getValue());
        return new OrderCreatedEvent(order, ZonedDateTime.now(ZoneId.of(UTC)));
    }

    @Override
    public OrderPaidEvent payOrder(Order order) {
        order.pay();
        log.info("Order with id {}: is paid", order.getId().getValue());
        return new OrderPaidEvent(order, ZonedDateTime.now(ZoneId.of(UTC)));
    }

    @Override
    public void approveOrder(Order order) {
        order.approve();
        log.info("Order with id: {} is approved", order.getId().getValue());
    }

    @Override
    public OrderCancelledEvent cacelOrderPayment(Order order, List<String> failureMessages) {
        order.initCancel(failureMessages);
        log.info("Order paymwent is cancelling for order id: {}", order.getId().getValue());
        return new OrderCancelledEvent(order, ZonedDateTime.now(ZoneId.of(UTC)));
    }

    @Override
    public void cancelOrder(Order order, List<String> failureMessages) {
        order.cancel(failureMessages);
        log.info("Order with id: {} is cancelled", order.getId().getValue());
    }

    private void validateRestaurant(Restaurant restaurant) {
        if(!restaurant.getActive()) {
            throw new OrderDomainException("Restaurant with id " + restaurant.getId().getValue() +
                " is currently not active!");
        }
    }

    private void setOrderProductInformation2(Order order, Restaurant restaurant) {
        order.getItems().forEach(orderItem -> restaurant.getProducts().forEach(restaurantProduct -> {
            Product currentProduct = orderItem.getProduct();
            if(currentProduct.equals(restaurantProduct)) {
                currentProduct.updateWithCOnfirmedNamedAndPrice(restaurantProduct.getName(),
                        restaurantProduct.getPrice());
            }
        }));
    }

    private void setOrderProductInformation(Order order, Restaurant restaurant) {
        Map<ProductId, Product> restaurantProductMap = new HashMap<>();
        restaurant.getProducts().forEach(restaurantProduct -> {
            restaurantProductMap.put(restaurantProduct.getId(), restaurantProduct);
        });

        order.getItems().forEach(orderItem -> {
            Product currentProduct = orderItem.getProduct();
            Product restaurantProduct = restaurantProductMap.get(currentProduct.getId());
            currentProduct.updateWithCOnfirmedNamedAndPrice(restaurantProduct.getName(), restaurantProduct.getPrice());
        });
    }
}
