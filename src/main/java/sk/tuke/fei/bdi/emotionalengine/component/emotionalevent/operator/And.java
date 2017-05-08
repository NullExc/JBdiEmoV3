package sk.tuke.fei.bdi.emotionalengine.component.emotionalevent.operator;


import sk.tuke.fei.bdi.emotionalengine.component.emotionalevent.EmotionalEvent;
import sk.tuke.fei.bdi.emotionalengine.component.emotionalevent.EmotionalEventChecker;

/**
 * @author Tomáš Herich
 */

public class And implements EmotionalEventChecker {

    EmotionalEventChecker firstChecker;
    EmotionalEventChecker secondChecker;

    public And(EmotionalEventChecker firstChecker, EmotionalEventChecker secondChecker) {
        this.firstChecker = firstChecker;
        this.secondChecker = secondChecker;
    }

    @Override
    public boolean checkEmotionalEvent(EmotionalEvent event) {

        return firstChecker.checkEmotionalEvent(event) && secondChecker.checkEmotionalEvent(event);

    }

}