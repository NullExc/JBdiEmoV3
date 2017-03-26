package sk.tuke.fei.bdi.emotionalengine.component.emotionalevent.element;

import sk.tuke.fei.bdi.emotionalengine.component.emotionalevent.EmotionalEvent;
import sk.tuke.fei.bdi.emotionalengine.component.emotionalevent.EmotionalEventChecker;
import sk.tuke.fei.bdi.emotionalengine.res.R;

/**
 * Created by Peter on 26.3.2017.
 */
public class GoalCreatedChecker implements EmotionalEventChecker {
    public boolean checkEmotionalEvent(EmotionalEvent event) {
        return event.getEventType() == R.EVT_GOAL_CREATED;
    }
}
