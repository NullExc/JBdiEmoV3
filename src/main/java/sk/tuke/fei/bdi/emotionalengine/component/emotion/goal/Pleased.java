package sk.tuke.fei.bdi.emotionalengine.component.emotion.goal;


import sk.tuke.fei.bdi.emotionalengine.component.emotion.RootEmotion;
import sk.tuke.fei.bdi.emotionalengine.res.R;

/**
 * @author Tomáš Herich
 */

public class Pleased extends RootEmotion {

    public Pleased() {
        super();

        // Add parameters on which intensity calculation will be performed
        applicableSystemParameters.add(R.SATISFACTION);
        applicableSystemParameters.add(R.RELIEF);
        applicableSystemParameters.add(R.JOY);
        applicableSystemParameters.add(R.HOPE);
        applicableSystemParameters.add(R.HAPPY_FOR);
        applicableSystemParameters.add(R.GLOATING);
    }

}