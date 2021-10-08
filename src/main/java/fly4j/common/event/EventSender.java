package fly4j.common.event;

/**
 * Created by qryc on 2020/2/8.
 */
public class EventSender {

    public static void sendEvent(AlterEvent alterEvent) {
        EvntDB.offer(alterEvent);
    }

}
