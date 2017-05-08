package sk.tuke.fei.bdi.emotionalengine.component.emotion.goal;

import sk.tuke.fei.bdi.emotionalengine.component.emotion.Emotion;
import sk.tuke.fei.bdi.emotionalengine.component.emotionalevent.EmotionalEvent;
import sk.tuke.fei.bdi.emotionalengine.res.R;

/**
 * @author Tomáš Herich
 */

public class Resentment extends Emotion {

    @Override
    public void calculateIntensity(EmotionalEvent event) {

        double otherDesireFailure = event.getUserParameters().get(R.PARAM_OTHER_DESIRE_GOAL_FAILURE);

        setIntensity(otherDesireFailure);

    }
}
