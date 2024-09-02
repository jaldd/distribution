package event;

import lombok.AllArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class EventPublisher {

    private final ApplicationEventPublisher applicationEventPublisher;

    public void publishProjectCreatedEvent(String msg) {
        applicationEventPublisher.publishEvent(new EventModel(1L, msg));
    }
}
