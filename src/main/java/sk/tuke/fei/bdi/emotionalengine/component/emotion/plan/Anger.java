package sk.tuke.fei.bdi.emotionalengine.component.emotion.plan;


import sk.tuke.fei.bdi.emotionalengine.component.emotion.Emotion;
import sk.tuke.fei.bdi.emotionalengine.component.emotionalevent.EmotionalEvent;
import sk.tuke.fei.bdi.emotionalengine.res.R;

/**
 * @author Tomáš Herich
 */

public class Anger extends Emotion {

    @Override
    public void calculateIntensity(EmotionalEvent event) {

        double desirability = event.getUserParameters().get(R.PARAM_DESIRABILITY);
        double reproach = event.getSystemParameters().get(R.REPROACH);

        setIntensity(desirability * reproach);

    }
}
