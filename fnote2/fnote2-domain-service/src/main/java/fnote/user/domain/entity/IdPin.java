package fnote.user.domain.entity;


import java.nio.file.Path;
import java.util.Objects;

/**
 * 2021-10-28开始支持文件标识，统一ID和文件参数
 */
public class IdPin {
    private String pin;
    private String noteFileStr;

    public static IdPin of(String pin, String noteFileStr) {
        return new IdPin(pin, noteFileStr);
    }

    public IdPin(String pin, String noteFileStr) {
        this.pin = pin;
        this.noteFileStr = noteFileStr;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        IdPin idPin = (IdPin) o;
        return Objects.equals(pin, idPin.pin) && Objects.equals(noteFileStr, idPin.noteFileStr);
    }


    @Override
    public int hashCode() {
        return Objects.hash(pin, noteFileStr);
    }

    public String getPin() {
        return pin;
    }

    public void setPin(String pin) {
        this.pin = pin;
    }

    public String getNoteFileStr() {
        return noteFileStr;
    }

    public void setNoteFileStr(String noteFileStr) {
        this.noteFileStr = noteFileStr;
    }
}

