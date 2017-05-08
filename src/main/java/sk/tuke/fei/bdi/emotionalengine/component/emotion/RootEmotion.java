package sk.tuke.fei.bdi.emotionalengine.component.emotion;

import sk.tuke.fei.bdi.emotionalengine.component.emotionalevent.EmotionalEvent;
import sk.tuke.fei.bdi.emotionalengine.helper.MyMath;

import java.util.HashSet;
import java.util.Set;

/**
 * @author Tomáš Herich
 */

public class RootEmotion extends Emotion {

    protected Set<Double> availableSystemParameters;
    protected Set<Integer> applicableSystemParameters;

    public RootEmotion() {

        availableSystemParameters = new HashSet<Double>();
        applicableSystemParameters = new HashSet<Integer>();

    }

    @Override
    public void setIntensity(double newIntensity) {

        /*
        Subtract current intensity to counteract default
        behaviour where intensity = intensity + newIntensity
        which is not suitable for root type of emotions
        which are triggered for every emotional event belonging to emotions
        in their respective branch (e.g. Pleased is triggered for all positive emotions
        in goal branch: satisfaction, relief, joy, hope, happy_for and gloating )
        */
        newIntensity = newIntensity - getIntensity();

        super.setIntensity(newIntensity);

    }

    @Override
    public void calculateIntensity(EmotionalEvent event) {

        availableSystemParameters.clear();

        for (int parameter : applicableSystemParameters) {
            checkAndAddAvailableSystemParameter(parameter, event);
        }

        Set<Double> values = new HashSet<Double>();

        if (!availableSystemParameters.isEmpty()) {
            for (Double value : availableSystemParameters){
                values.add(value);
            }
        } else {
            values.add(0d);
        }

        setIntensity(MyMath.rootMeanSquareDouble(values));

    }

    private void checkAndAddAvailableSystemParameter(int parameter, EmotionalEvent event) {

        if (event.getSystemParameters().containsKey(parameter)) {
            availableSystemParameters.add(event.getSystemParameters().get(parameter));
        }

    }

}
