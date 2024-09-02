package event;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@EnableAsync
@AllArgsConstructor
public class MyEventListener {


    @Async
    @EventListener(classes = EventModel.class)
    public void afterProjectCreated(EventModel eventModel) {
        System.out.println("commit:" + eventModel);
    }


}
