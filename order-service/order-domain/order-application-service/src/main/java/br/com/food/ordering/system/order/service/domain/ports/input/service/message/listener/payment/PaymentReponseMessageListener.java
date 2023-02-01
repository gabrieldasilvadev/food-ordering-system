package br.com.food.ordering.system.order.service.domain.ports.input.service.message.listener.payment;

import br.com.food.ordering.system.order.service.domain.dto.message.PaymentResponse;

public interface PaymentReponseMessageListener {
    void paymentCompleted(PaymentResponse paymentResponse);
    void paymentCancelled(PaymentResponse paymentResponse);
}
