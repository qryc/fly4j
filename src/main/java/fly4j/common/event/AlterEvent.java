package fly4j.common.event;

/**
 * @author qryc
 */
public class AlterEvent<T> {
    private T eventObj;

    public AlterEvent(T eventObj) {
        this.eventObj = eventObj;
    }

    public T getEventObj() {
        return eventObj;
    }

    public void setEventObj(T eventObj) {
        this.eventObj = eventObj;
    }
}
