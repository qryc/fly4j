package fnote.common.event;

import fly4j.common.event.AlterEvent;
import fly4j.common.event.EventHandler;

import java.util.List;

public class EventCenter {
    private List<EventHandler> eventHandlers;

    public void fire(AlterEvent alterEvent) {
        eventHandlers.forEach(handler -> handler.doHandle(alterEvent));
    }

    public void setEventHandlers(List<EventHandler> eventHandlers) {
        this.eventHandlers = eventHandlers;
    }

}
