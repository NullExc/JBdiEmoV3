package sk.tuke.fei.bdi.emotionalengine.component.emotion.mood;

/*

   Created with IntelliJ IDEA.

   Bc. Tomáš Herich
   ---------------------------
   24. 02. 2013
   1:18 PM

*/

import sk.tuke.fei.bdi.emotionalengine.component.Element;
import sk.tuke.fei.bdi.emotionalengine.component.Engine;
import sk.tuke.fei.bdi.emotionalengine.helper.MyMath;
import sk.tuke.fei.bdi.emotionalengine.res.R;

import java.util.HashSet;
import java.util.Set;

public class Positive extends Mood {

    @Override
    public void calculateIntensity(Engine engine) {

        Set<Double> values = new HashSet<Double>();

        addValidEmotionIntensityValues(engine, R.GOAL, R.PLEASED, values);
        addValidEmotionIntensityValues(engine, R.PLAN, R.APPROVING, values);
        addValidEmotionIntensityValues(engine, R.BELIEF, R.LIKING, values);

        if (values.isEmpty()) {
            values.add(0d);
        }

        setIntensity(MyMath.rootMeanSquareDouble(values) / 2);

    }

    private void addValidEmotionIntensityValues(Engine engine, int elementType, int emotionId, Set valueSet) {

        // Get all elements of specified element type from engine
        Element[] elements = engine.getElements(elementType);

        // Check if elements are valid
        if (elements != null && elements.length > 0) {

            // Iterate elements
            for (Element element : elements) {

                // Get emotion intensity of specified emotion
                double intensity = element.getEmotion(emotionId).getIntensity();

                // Check if intensity is valid
                if (intensity > 0) {
                    valueSet.add(intensity);
                }
            }
        }
    }


}