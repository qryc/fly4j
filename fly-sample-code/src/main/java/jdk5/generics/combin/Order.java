package jdk5.generics.combin;

public class Order {
    private IdPin idPin;
    private Long sku;

    public Order setSku(Long sku) {
        this.sku = sku;
        return this;
    }

    public Order setIdPin(IdPin idPin) {
        this.idPin = idPin;
        return this;
    }

    public Order setIdPin(Long id, String pin) {
        this.idPin = new IdPin().setId(id).setPin(pin);
        return this;
    }

    public static void main(String[] args) {
        var order = new Order().setIdPin(1L, "pin").setSku(1L);
    }
}
