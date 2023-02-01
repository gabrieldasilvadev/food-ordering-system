package br.com.ordering.system.order.service.domain.entity;

import br.com.ordering.system.order.service.domain.exception.OrderDomainException;
import br.com.ordering.system.order.service.domain.valueobject.OrderItemId;
import br.com.food.ordering.system.domain.entity.AggregateRoot;
import br.com.food.ordering.system.domain.exception.DomainException;
import br.com.food.ordering.system.domain.valueobject.Money;
import br.com.food.ordering.system.domain.valueobject.OrderStatus;
import br.com.food.ordering.system.domain.valueobject.identifier.CustomerId;
import br.com.food.ordering.system.domain.valueobject.identifier.OrderId;
import br.com.food.ordering.system.domain.valueobject.identifier.RestaurantId;
import br.com.ordering.system.order.service.domain.valueobject.StreetAddress;
import br.com.ordering.system.order.service.domain.valueobject.TrackingId;
import java.util.List;
import java.util.UUID;

public class Order extends AggregateRoot<OrderId> {
    private final CustomerId customerId;
    private final RestaurantId restaurantId;
    private final StreetAddress streetAddress;
    private final Money price;
    private final List<OrderItem> items;

    private TrackingId trackingId;
    private OrderStatus orderStatus;
    private List<String> failureMessages;

    public void validateOrder() {
        validateInitialOrder();
        validateTotalPrice();
        validateItemsPrices();
    }

    private void validateTotalPrice() {
        if (this.price == null || !this.getPrice().isGreaterThanZero()) {
            throw new OrderDomainException("Total price must be greater than zero!");
        }
    }

    private void validateItemsPrices() {
        Money orderItemsTotal = this.items.stream().map(orderItem -> {
            validateItemPrices(orderItem);
            return orderItem.getSubtotal();
        }).reduce(Money.ZERO, Money::add);

        if (!this.price.equals(orderItemsTotal)) {
            throw new OrderDomainException("Total price: " + this.price.getAmount()
                    + " is not equal to Order ites total: " + orderItemsTotal.getAmount() + "!"
            );
        }
    }

    private void validateItemPrices(OrderItem orderItem) {
        if (!orderItem.isPriceValid()) {
            throw new OrderDomainException("Order item price: " + orderItem.getPrice().getAmount() +
                    " is not valid for product " + orderItem.getProduct().getId().getValue()
            );
        }
    }

    private void validateInitialOrder() {
        if (this.orderStatus != null || getId() != null) {
            throw new OrderDomainException("Order is not in correct state for initialization!");
        }
    }

    public void pay() {
        if (this.orderStatus != OrderStatus.PENDING) {
            throw new OrderDomainException("Order is not in correct state for pay operation!");
        }
        this.orderStatus = OrderStatus.PAID;
    }

    public void approve() {
        if (this.orderStatus != OrderStatus.PAID) {
            throw new DomainException("Order is not in correct state for approve operation!");
        }
        this.orderStatus = OrderStatus.APPROVED;
    }

    public void initCancel(List<String> failureMessages) {
        if (this.orderStatus != OrderStatus.PAID) {
            throw new DomainException("Order is not in correct state for initCancel operation!");
        }
        this.failureMessages = failureMessages;
        this.orderStatus = OrderStatus.CANCELLING;
    }

    public void cancel(List<String> failureMessages) {
        if (!(this.orderStatus == OrderStatus.CANCELLING || this.orderStatus == OrderStatus.PENDING)) {
            throw new DomainException("Order is not in correct state for cancel operation!");
        }
        this.failureMessages = failureMessages;
        this.orderStatus = OrderStatus.CANCELLED;
    }

    public void updateFailureMessages(List<String> failureMessages) {
        if (this.failureMessages != null && failureMessages != null) {
            this.failureMessages.addAll(failureMessages
                    .stream()
                    .filter(message -> !message.isEmpty()).toList());
        }
        if (this.failureMessages == null) {
            this.failureMessages = failureMessages;
        }
    }

    public void initializeOrder() {
        super.setId(new OrderId(UUID.randomUUID()));
        this.trackingId = new TrackingId(UUID.randomUUID());
        this.orderStatus = OrderStatus.PENDING;
        this.initializeOrderItems();
    }

    public void initializeOrderItems() {
        Long itemId = 1L;
        for (OrderItem orderItem : items) {
            orderItem.initializedOrderItem(super.getId(), new OrderItemId(itemId++));
        }
    }

    private Order(Builder builder) {
        super.setId(builder.orderId);
        customerId = builder.customerId;
        restaurantId = builder.restaurantId;
        streetAddress = builder.streetAddress;
        price = builder.money;
        items = builder.items;
        trackingId = builder.trackingId;
        orderStatus = builder.orderStatus;
        failureMessages = builder.failureMessages;
    }



    public CustomerId getCustomerId() {
        return customerId;
    }

    public RestaurantId getRestaurantId() {
        return restaurantId;
    }

    public StreetAddress getStreetAddress() {
        return streetAddress;
    }

    public Money getPrice() {
        return price;
    }

    public List<OrderItem> getItems() {
        return items;
    }

    public TrackingId getTrackingId() {
        return trackingId;
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }

    public List<String> getFailureMessages() {
        return failureMessages;
    }

    public static final class Builder {
        private OrderId orderId;
        private CustomerId customerId;
        private RestaurantId restaurantId;
        private StreetAddress streetAddress;
        private Money money;
        private List<OrderItem> items;
        private TrackingId trackingId;
        private OrderStatus orderStatus;
        private List<String> failureMessages;

        private Builder() {
        }

        public static Builder builder() {
            return new Builder();
        }

        public Builder orderId(OrderId val) {
            orderId = val;
            return this;
        }

        public Builder customerId(CustomerId val) {
            customerId = val;
            return this;
        }

        public Builder restaurantId(RestaurantId val) {
            restaurantId = val;
            return this;
        }

        public Builder streetAddress(StreetAddress val) {
            streetAddress = val;
            return this;
        }

        public Builder money(Money val) {
            money = val;
            return this;
        }

        public Builder items(List<OrderItem> val) {
            items = val;
            return this;
        }

        public Builder trackingId(TrackingId val) {
            trackingId = val;
            return this;
        }

        public Builder orderStatus(OrderStatus val) {
            orderStatus = val;
            return this;
        }

        public Builder failureMessages(List<String> val) {
            failureMessages = val;
            return this;
        }

        public Order build() {
            return new Order(this);
        }
    }
}
