package fly4j.common.lang;


import org.apache.commons.lang3.StringUtils;

public class FlyResult {
    private boolean success;
    private StringBuilder msg = new StringBuilder();

    public FlyResult() {
    }

    public FlyResult(boolean success, String msg) {
        this.success = success;
        this.msg.append(msg);
    }

    public FlyResult fail() {
        this.success = false;
        return this;
    }
    public FlyResult fail(String msg) {
        this.success = false;
        this.msg.append(msg);
        return this;
    }

    public FlyResult success() {
        this.success = true;
        return this;
    }

    public FlyResult append(String msg) {
        this.msg.append(msg);
        return this;
    }
    public FlyResult appendLine(String msg) {
        this.msg.append(msg).append(StringUtils.LF);
        return this;
    }
    public FlyResult merge(FlyResult flyResult) {
        if (flyResult.isFail()) {
            this.fail();
        }
        this.msg.append(flyResult.getMsg());
        return this;
    }

    public boolean isSuccess() {
        return success;
    }

    public boolean isFail() {
        return !success;
    }

    public String getMsg() {
        return msg.toString();
    }
}
