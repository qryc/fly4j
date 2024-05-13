package jdk5.generics.builder;

public class Order {
    private Long id;
    private String pin;
    private String addr;

    public static void main(String[] args) {
        var Order = OrderBuilder.anOrder().withId(1L).withPin("pin").withAddr("sdf").build();
    }

    public static final class OrderBuilder {
        private Long id;
        private String pin;
        private String addr;

        private OrderBuilder() {
        }

        public static OrderBuilder anOrder() {
            return new OrderBuilder();
        }

        public OrderBuilder withId(Long id) {
            this.id = id;
            return this;
        }

        public OrderBuilder withPin(String pin) {
            this.pin = pin;
            return this;
        }

        public OrderBuilder withAddr(String addr) {
            this.addr = addr;
            return this;
        }

        public Order build() {
            Order order = new Order();
            order.id = this.id;
            order.addr = this.addr;
            order.pin = this.pin;
            return order;
        }
    }
}
