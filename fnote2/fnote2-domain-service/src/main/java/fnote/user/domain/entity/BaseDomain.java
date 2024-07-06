package fnote.user.domain.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Date;

/**
 * Created by qryc on 2017/2/23.
 */
public abstract class BaseDomain<T> {

    protected String noteFileStr;
    private String pin;
    private Date createTime;
    private Date modifyTime;

    public BaseDomain() {
    }


    public BaseDomain(String pin) {
        this.pin = pin;
    }

    public void copyTo(BaseDomain toDo) {
        toDo.setPin(this.getPin());
        toDo.setCreateTime(this.getCreateTime());
        toDo.setModifyTime(this.getModifyTime());
    }

    public T setIdPin(IdPin idPin) {
        this.noteFileStr = idPin.getNoteFileStr();
        this.pin = idPin.getPin();
        return (T) this;
    }


    public T setPin(String pin) {
        this.pin = pin;
        return (T) this;
    }

    public T setCreateTime(Date createTime) {
        this.createTime = createTime;
        return (T) this;
    }

    public T setModifyTime(Date modifyTime) {
        this.modifyTime = modifyTime;
        return (T) this;
    }

    @JsonIgnore
    public IdPin getIdPin() {
        return IdPin.of(getNoteFileStr(), getPin());
    }


    public String getPin() {
        return pin;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public Date getModifyTime() {
        return modifyTime;
    }


    public void setNoteFileStr(String noteFileStr) {
        this.noteFileStr = noteFileStr;
    }

    public String getNoteFileStr() {
        return noteFileStr;
    }
    @JsonIgnore
    public String getEncodeNoteFileStr() {
        return URLEncoder.encode(noteFileStr, StandardCharsets.UTF_8);
    }


}
