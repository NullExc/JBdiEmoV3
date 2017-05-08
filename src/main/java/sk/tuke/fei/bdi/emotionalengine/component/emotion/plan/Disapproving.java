package sk.tuke.fei.bdi.emotionalengine.component.emotion.plan;

import sk.tuke.fei.bdi.emotionalengine.component.emotion.RootEmotion;
import sk.tuke.fei.bdi.emotionalengine.res.R;

/**
 * @author Tomáš Herich
 */

public class Disapproving extends RootEmotion {

    public Disapproving() {
        super();

        // Add parameters on which intensity calculation will be performed
        applicableSystemParameters.add(R.SHAME);
        applicableSystemParameters.add(R.REMORSE);
        applicableSystemParameters.add(R.REPROACH);
        applicableSystemParameters.add(R.ANGER);
    }

}