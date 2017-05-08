package sk.tuke.fei.bdi.emotionalengine.component.emotion.plan;

import sk.tuke.fei.bdi.emotionalengine.component.emotion.RootEmotion;
import sk.tuke.fei.bdi.emotionalengine.res.R;

/**
 * @author Tomáš Herich
 */

public class Approving extends RootEmotion {

    public Approving() {
        super();

        // Add parameters on which intensity calculation will be performed
        applicableSystemParameters.add(R.PRIDE);
        applicableSystemParameters.add(R.GRATIFICATION);
        applicableSystemParameters.add(R.ADMIRATION);
        applicableSystemParameters.add(R.GRATITUDE);
    }

}