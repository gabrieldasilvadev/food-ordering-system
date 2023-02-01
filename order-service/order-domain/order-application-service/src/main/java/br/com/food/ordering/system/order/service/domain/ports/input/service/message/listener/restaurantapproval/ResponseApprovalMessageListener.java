package br.com.food.ordering.system.order.service.domain.ports.input.service.message.listener.restaurantapproval;

import br.com.food.ordering.system.order.service.domain.dto.message.RestaurantApprovalResponse;

public interface ResponseApprovalMessageListener {
    void orderApproved(RestaurantApprovalResponse restaurantApprovalResponse);
    void orderRejected(RestaurantApprovalResponse restaurantApprovalResponse);
}
