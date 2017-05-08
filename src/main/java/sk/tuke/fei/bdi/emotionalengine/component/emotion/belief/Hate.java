package sk.tuke.fei.bdi.emotionalengine.component.emotion.belief;

/*

   Created with IntelliJ IDEA.

   Bc. Tomáš Herich
   --------------------------- 
   04. 02. 2013
   9:15 PM
   
*/

import sk.tuke.fei.bdi.emotionalengine.component.emotion.Emotion;
import sk.tuke.fei.bdi.emotionalengine.component.emotionalevent.EmotionalEvent;

public class Hate extends Emotion {

    @Override
    public void calculateIntensity(EmotionalEvent event) {

        double attractivenessIntensity = event.getBeliefAttractionIntensity();

        setIntensity(attractivenessIntensity);

    }
}
