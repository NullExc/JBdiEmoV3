package sk.tuke.fei.bdi.emotionalengine.component.emotionalevent.belief;

import sk.tuke.fei.bdi.emotionalengine.component.emotionalevent.EmotionalEvent;
import sk.tuke.fei.bdi.emotionalengine.component.emotionalevent.EmotionalEventChecker;

/**
 * Created by Peter on 26.3.2017.
 */
public class BeliefFamiliarChecker implements EmotionalEventChecker {

    public boolean checkEmotionalEvent(EmotionalEvent event) {
        return event.isBeliefFamiliar() != null && event.isBeliefFamiliar();
    }
}
