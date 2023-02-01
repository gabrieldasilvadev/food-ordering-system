package br.com.food.ordering.system.order.service.domain.ports.output.message.publisher.payment;

import br.com.food.ordering.system.domain.event.publisher.DomainEventPublisher;
import br.com.ordering.system.order.service.domain.event.OrderCreatedEvent;

public interface OrderCreatedPaymentRequestMessagePublisher extends DomainEventPublisher<OrderCreatedEvent> {

}
