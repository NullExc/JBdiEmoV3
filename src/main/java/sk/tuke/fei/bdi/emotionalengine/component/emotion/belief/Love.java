package sk.tuke.fei.bdi.emotionalengine.component.emotion.belief;

import sk.tuke.fei.bdi.emotionalengine.component.emotion.Emotion;
import sk.tuke.fei.bdi.emotionalengine.component.emotionalevent.EmotionalEvent;

/**
 * @author Tomáš Herich
 */

public class Love extends Emotion {

    @Override
    public void calculateIntensity(EmotionalEvent event) {

        double attractivenessIntensity = event.getBeliefAttractionIntensity();

        setIntensity(attractivenessIntensity);

    }
}
