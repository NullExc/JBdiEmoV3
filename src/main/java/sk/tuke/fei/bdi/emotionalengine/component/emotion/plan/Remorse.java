package sk.tuke.fei.bdi.emotionalengine.component.emotion.plan;



import sk.tuke.fei.bdi.emotionalengine.component.emotion.Emotion;
import sk.tuke.fei.bdi.emotionalengine.component.emotionalevent.EmotionalEvent;
import sk.tuke.fei.bdi.emotionalengine.helper.MyMath;
import sk.tuke.fei.bdi.emotionalengine.res.R;

/**
 * @author Tomáš Herich
 */

public class Remorse extends Emotion {

    @Override
    public void calculateIntensity(EmotionalEvent event) {

        double shame = event.getSystemParameters().get(R.SHAME);
        double distress = event.getSystemParameters().get(R.DISTRESS);

        setIntensity(MyMath.rootMeanSquareDouble(shame, distress));

    }
}
