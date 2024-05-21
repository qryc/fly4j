package fnote.user.domain.entity;

/**
 * Created by qryc on 2020/2/4.
 */
public class AlterUserParam {
    private String pin;
    private String email;

    public String getPin() {
        return pin;
    }

    public void setPin(String pin) {
        this.pin = pin;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
