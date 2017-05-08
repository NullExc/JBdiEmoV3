package sk.tuke.fei.bdi.emotionalengine.component.emotion.belief;

import sk.tuke.fei.bdi.emotionalengine.component.emotion.RootEmotion;
import sk.tuke.fei.bdi.emotionalengine.res.R;

/**
 * @author Tomáš Herich
 */

public class Disliking extends RootEmotion {

    public Disliking() {
        super();

        // Add parameters on which intensity calculation will be performed
        applicableSystemParameters.add(R.HATE);
        applicableSystemParameters.add(R.DISGUST);

    }
}