package farticle.domain.entity;

import fnote.user.domain.entity.IdPin;

import java.nio.file.Path;

/**
 * Created by qryc on 2021/10/7
 */
public class ArticleEventObj {
    private String domain;
    private final String noteFileStr;
    private final String pin;
    private final int opCode;
    public static final int INSERT = 1;
    public static final int DELETE = 2;
    public static final int EDIT = 3;
    public static final int QUERY = 4;


    public ArticleEventObj(String noteFileStr, String pin, int opCode) {
        this.noteFileStr = noteFileStr;
        this.pin = pin;
        this.opCode = opCode;
    }

    public ArticleEventObj(IdPin idPin, int opCode) {
        this.noteFileStr = idPin.getNoteFileStr();
        this.pin = idPin.getPin();
        this.opCode = opCode;
    }

    public ArticleEventObj domain(String domain) {
        this.domain = domain;
        return this;
    }

    public String getDomain() {
        return domain;
    }

    public String getNoteFileStr() {
        return noteFileStr;
    }

    public IdPin getIdPin() {
        return IdPin.of(pin, noteFileStr);
    }

    public String getPin() {
        return pin;
    }

    public int getOpCode() {
        return opCode;
    }
}
