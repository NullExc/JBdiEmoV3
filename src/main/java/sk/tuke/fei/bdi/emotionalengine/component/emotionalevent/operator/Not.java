package sk.tuke.fei.bdi.emotionalengine.component.emotionalevent.operator;


import sk.tuke.fei.bdi.emotionalengine.component.emotionalevent.EmotionalEvent;
import sk.tuke.fei.bdi.emotionalengine.component.emotionalevent.EmotionalEventChecker;

/**
 * @author Tomáš Herich
 */

public class Not implements EmotionalEventChecker {

    EmotionalEventChecker checker;

    public Not(EmotionalEventChecker checker) {
        this.checker = checker;
    }

    @Override
    public boolean checkEmotionalEvent(EmotionalEvent event) {

        return !checker.checkEmotionalEvent(event);

    }

}