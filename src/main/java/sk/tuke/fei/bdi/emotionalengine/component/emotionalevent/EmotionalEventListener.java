package sk.tuke.fei.bdi.emotionalengine.component.emotionalevent;

/**
 * This interface acts as listener, which is waiting for processing of any emotional event.
 *
 * GUI classes use this interface to listen for new emotional events to show them on the screen.
 *
 * @author Tomáš Herich
 */
public interface EmotionalEventListener {
    void eventHappened(EmotionalEvent event);
}
