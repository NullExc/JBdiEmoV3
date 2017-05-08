package sk.tuke.fei.bdi.emotionalengine.component.emotion.plan;

import sk.tuke.fei.bdi.emotionalengine.component.emotion.Emotion;
import sk.tuke.fei.bdi.emotionalengine.component.emotionalevent.EmotionalEvent;
import sk.tuke.fei.bdi.emotionalengine.helper.MyMath;
import sk.tuke.fei.bdi.emotionalengine.res.R;

/**
 * @author Tomáš Herich
 */

public class Gratification extends Emotion {

    @Override
    public void calculateIntensity(EmotionalEvent event) {

        double pride = event.getSystemParameters().get(R.PRIDE);
        double joy = event.getSystemParameters().get(R.JOY);

        setIntensity(MyMath.rootMeanSquareDouble(pride, joy));

    }
}
