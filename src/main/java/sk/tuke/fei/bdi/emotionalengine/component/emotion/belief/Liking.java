package sk.tuke.fei.bdi.emotionalengine.component.emotion.belief;

import sk.tuke.fei.bdi.emotionalengine.component.emotion.RootEmotion;
import sk.tuke.fei.bdi.emotionalengine.res.R;

/**
 * @author Tomáš Herich
 */

public class Liking extends RootEmotion {

    public Liking() {
        super();

        // Add parameters on which intensity calculation will be performed
        applicableSystemParameters.add(R.LOVE);
        applicableSystemParameters.add(R.INTEREST);

    }

}
