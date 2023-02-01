package br.com.ordering.system.order.service.domain.valueobject;

import br.com.food.ordering.system.domain.valueobject.identifier.BaseId;
import java.util.UUID;

public class TrackingId extends BaseId<UUID> {
    public TrackingId(UUID value) {
        super(value);
    }
}
