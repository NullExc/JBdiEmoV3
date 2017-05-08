package sk.tuke.fei.bdi.emotionalengine.component.emotionalevent.operator;



import sk.tuke.fei.bdi.emotionalengine.component.emotionalevent.EmotionalEvent;
import sk.tuke.fei.bdi.emotionalengine.component.emotionalevent.EmotionalEventChecker;

/**
 * @author Tomáš Herich
 */

public class Or implements EmotionalEventChecker {

    EmotionalEventChecker firstChecker;
    EmotionalEventChecker secondChecker;

    public Or(EmotionalEventChecker firstChecker, EmotionalEventChecker secondChecker) {
        this.firstChecker = firstChecker;
        this.secondChecker = secondChecker;
    }

    @Override
    public boolean checkEmotionalEvent(EmotionalEvent event) {

        return firstChecker.checkEmotionalEvent(event) || secondChecker.checkEmotionalEvent(event);

    }

}
