package jdk5.generics.inherit;

public class Order extends IdPin<Order> {
    private Long sku;

    public Order setSku(Long sku) {
        this.sku = sku;
        return this;
    }

    public static void main(String[] args) {
        var order = new Order().setId(1L).setSku(1L);
    }
}
