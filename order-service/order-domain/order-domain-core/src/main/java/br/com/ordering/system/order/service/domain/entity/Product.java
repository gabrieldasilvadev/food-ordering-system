package br.com.ordering.system.order.service.domain.entity;

import br.com.food.ordering.system.domain.entity.BaseEntity;
import br.com.food.ordering.system.domain.valueobject.Money;
import br.com.food.ordering.system.domain.valueobject.identifier.ProductId;

public class Product extends BaseEntity<ProductId> {
    private String name;
    private Money price;

    public Product(ProductId productId,String name, Money price) {
        super.setId(productId);
        this.name = name;
        this.price = price;
    }

    public void updateWithCOnfirmedNamedAndPrice(String name, Money price) {
        this.name = name;
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public Money getPrice() {
        return price;
    }
}
