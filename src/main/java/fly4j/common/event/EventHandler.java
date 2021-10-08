package fly4j.common.event;

/**
 * Created by qryc on 2019/6/7.
 */
public interface EventHandler<T> {
    void doHandle(AlterEvent<T> alterEvent);
}
