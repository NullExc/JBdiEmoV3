package sk.tuke.fei.bdi.emotionalengine.component.emotionalevent.user;

import sk.tuke.fei.bdi.emotionalengine.component.emotionalevent.EmotionalEvent;
import sk.tuke.fei.bdi.emotionalengine.component.emotionalevent.EmotionalEventChecker;
import sk.tuke.fei.bdi.emotionalengine.res.R;

/**
 * @author Tomáš Herich
 */

public class UserEmotionalOtherPlan implements EmotionalEventChecker {

    public boolean checkEmotionalEvent(EmotionalEvent event) {

        return event.getUserParameters().containsKey(R.PARAM_EMOTIONAL_OTHER_PLAN);

    }

}
