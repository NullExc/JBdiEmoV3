package sk.tuke.fei.bdi.emotionalengine.component.emotion.goal;


import sk.tuke.fei.bdi.emotionalengine.component.emotion.Emotion;
import sk.tuke.fei.bdi.emotionalengine.component.emotionalevent.EmotionalEvent;
import sk.tuke.fei.bdi.emotionalengine.res.R;

/**
 * @author Tomáš Herich
 */

public class Pity extends Emotion {

    @Override
    public void calculateIntensity(EmotionalEvent event) {

        double otherDesireSuccess = event.getUserParameters().get(R.PARAM_OTHER_DESIRE_GOAL_SUCCESS);

        setIntensity(otherDesireSuccess);

    }
}
