package jdk5.generics.combin;

public class IdPin {
    private Long id;
    private String pin;

    public IdPin setId(Long id) {
        this.id = id;
        return this;
    }

    public IdPin setPin(String pin) {
        this.pin = pin;
        return this;
    }
}
