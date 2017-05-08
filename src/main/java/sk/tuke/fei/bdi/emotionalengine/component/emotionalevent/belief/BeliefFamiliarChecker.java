package sk.tuke.fei.bdi.emotionalengine.component.emotionalevent.belief;

import sk.tuke.fei.bdi.emotionalengine.component.emotionalevent.EmotionalEvent;
import sk.tuke.fei.bdi.emotionalengine.component.emotionalevent.EmotionalEventChecker;

/**
 * @author Tomáš Herich
 */

public class BeliefFamiliarChecker implements EmotionalEventChecker {

    public boolean checkEmotionalEvent(EmotionalEvent event) {
        return event.isBeliefFamiliar() != null && event.isBeliefFamiliar();
    }
}
