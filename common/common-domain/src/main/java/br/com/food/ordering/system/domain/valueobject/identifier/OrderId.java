package br.com.food.ordering.system.domain.valueobject.identifier;

import java.util.UUID;

public class OrderId extends BaseId<UUID>{
    public OrderId(UUID value) {
        super(value);
    }
}
