package sk.tuke.fei.bdi.emotionalengine.component.emotion.plan;

import sk.tuke.fei.bdi.emotionalengine.component.emotion.Emotion;
import sk.tuke.fei.bdi.emotionalengine.component.emotionalevent.EmotionalEvent;
import sk.tuke.fei.bdi.emotionalengine.res.R;

/**
 * @author Tomáš Herich
 */

public class Shame extends Emotion {

    @Override
    public void calculateIntensity(EmotionalEvent event) {

        double approval = -1;
        double disapproval = -1;

        if (event.getUserParameters().containsKey(R.PARAM_APPROVAL)) {
            approval = event.getUserParameters().get(R.PARAM_APPROVAL);
        }

        if (event.getUserParameters().containsKey(R.PARAM_DISAPPROVAL)) {
            disapproval = event.getUserParameters().get(R.PARAM_DISAPPROVAL);
        }

        double result = 0;

        if (approval >= 0 && disapproval >= 0) {
            result = Math.max((disapproval - approval), 0);
        } else if (approval < 0 && disapproval >= 0) {
            result = disapproval;
        }

        setIntensity(result);
    }
}
