package br.com.ordering.system.order.service.domain.valueobject;

import br.com.food.ordering.system.domain.valueobject.identifier.BaseId;

public class OrderItemId extends BaseId<Long> {

    public OrderItemId(Long value) {
        super(value);
    }
}
