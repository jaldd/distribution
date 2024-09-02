package event;

import org.springframework.context.ApplicationEvent;

public class EventModel extends ApplicationEvent {

    private Long id;
    private String msg;

    public EventModel(Long id, String msg) {
        super(id);
        this.msg = msg;
    }
}
