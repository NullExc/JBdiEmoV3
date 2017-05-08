package sk.tuke.fei.bdi.emotionalengine.component.emotion.goal;



import sk.tuke.fei.bdi.emotionalengine.component.emotion.RootEmotion;
import sk.tuke.fei.bdi.emotionalengine.res.R;

/**
 * @author Tomáš Herich
 */

public class Displeased extends RootEmotion {

    public Displeased() {
        super();

        // Add parameters on which intensity calculation will be performed
        applicableSystemParameters.add(R.FEAR);
        applicableSystemParameters.add(R.FEAR_CONFIRMED);
        applicableSystemParameters.add(R.DISAPPOINTMENT);
        applicableSystemParameters.add(R.DISTRESS);
        applicableSystemParameters.add(R.RESENTMENT);
        applicableSystemParameters.add(R.PITY);
    }

}