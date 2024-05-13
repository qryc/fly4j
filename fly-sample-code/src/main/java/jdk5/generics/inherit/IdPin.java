package jdk5.generics.inherit;

public class IdPin<T> {
    private Long id;
    private String pin;

    public T setId(Long id) {
        this.id = id;
        return (T) this;
    }

    public T setPin(String pin) {
        this.pin = pin;
        return (T) this;
    }
}
