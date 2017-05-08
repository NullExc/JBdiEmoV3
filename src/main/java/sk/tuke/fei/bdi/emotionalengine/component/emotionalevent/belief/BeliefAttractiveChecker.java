package sk.tuke.fei.bdi.emotionalengine.component.emotionalevent.belief;

import sk.tuke.fei.bdi.emotionalengine.component.emotionalevent.EmotionalEvent;
import sk.tuke.fei.bdi.emotionalengine.component.emotionalevent.EmotionalEventChecker;

/**
 * @author Tomáš Herich
 */

public class BeliefAttractiveChecker implements EmotionalEventChecker {

    public boolean checkEmotionalEvent(EmotionalEvent event) {
        return event.isBeliefAttractive() != null && event.isBeliefAttractive();
    }
}
