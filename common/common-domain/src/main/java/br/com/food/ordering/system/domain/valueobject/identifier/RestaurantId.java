package br.com.food.ordering.system.domain.valueobject.identifier;

import java.util.UUID;

public class RestaurantId extends BaseId<UUID>{
    protected RestaurantId(UUID value) {
        super(value);
    }
}
