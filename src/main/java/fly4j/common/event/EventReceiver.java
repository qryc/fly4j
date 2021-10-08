package fly4j.common.event;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by qryc on 2020/2/8.
 */
public class EventReceiver {
    private List<EventHandler> eventHandlers;
    private long sleepMillis = 1;

    public void start() {
        new Thread(() -> {
            while (true) {
                try {
                    TimeUnit.MILLISECONDS.sleep(sleepMillis);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                try {
                    var alterEvent = EvntDB.poll();
                    eventHandlers.forEach(handler -> handler.doHandle(alterEvent));
                } catch (Exception e) {

                }
            }
        }).start();

    }

    public void setEventHandlers(List<EventHandler> eventHandlers) {
        this.eventHandlers = eventHandlers;
    }

    public void setSleepMillis(long sleepMillis) {
        this.sleepMillis = sleepMillis;
    }
}
