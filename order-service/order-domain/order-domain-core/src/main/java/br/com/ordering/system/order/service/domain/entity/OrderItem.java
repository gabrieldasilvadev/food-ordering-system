package br.com.ordering.system.order.service.domain.entity;

import br.com.ordering.system.order.service.domain.valueobject.OrderItemId;
import br.com.food.ordering.system.domain.entity.BaseEntity;
import br.com.food.ordering.system.domain.valueobject.Money;
import br.com.food.ordering.system.domain.valueobject.identifier.OrderId;

public class OrderItem extends BaseEntity<OrderItemId> {
    private OrderId orderId;
    private final Product product;
    private final Integer quantity;
    private final Money price;
    private final Money subtotal;

    public void initializedOrderItem(OrderId orderId, OrderItemId orderItemId) {
        this.orderId = orderId;
        super.setId(orderItemId);
    }

    Boolean isPriceValid() {
        return this.price.isGreaterThanZero() &&
                this.price.equals(this.product.getPrice()) &&
                this.price.multiply(this.quantity).equals(this.subtotal);
    }

    private OrderItem(Builder builder) {
        super.setId(builder.orderItemId);
        product = builder.product;
        quantity = builder.quantity;
        price = builder.price;
        subtotal = builder.subtotal;
    }

    public OrderId getOrderId() {
        return orderId;
    }

    public Product getProduct() {
        return product;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public Money getPrice() {
        return price;
    }

    public Money getSubtotal() {
        return subtotal;
    }

    public static final class Builder {
        private OrderItemId orderItemId;
        private Product product;
        private Integer quantity;
        private Money price;
        private Money subtotal;

        private Builder() {
        }

        public static Builder builder() {
            return new Builder();
        }

        public Builder orderItemId(OrderItemId val) {
            orderItemId = val;
            return this;
        }

        public Builder product(Product val) {
            product = val;
            return this;
        }

        public Builder quantity(Integer val) {
            quantity = val;
            return this;
        }

        public Builder price(Money val) {
            price = val;
            return this;
        }

        public Builder subtotal(Money val) {
            subtotal = val;
            return this;
        }

        public OrderItem build() {
            return new OrderItem(this);
        }
    }
}
