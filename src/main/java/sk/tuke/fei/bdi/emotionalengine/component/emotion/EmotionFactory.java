package sk.tuke.fei.bdi.emotionalengine.component.emotion;

import java.util.Set;

/**
 * Interface which is implemented by all class which creates set of the emotions. It uses Factory design pattern to create
 * instances of emotions.
 *
 * @author Tomáš Herich
 */

public interface EmotionFactory {

    /**
     * Gets specific emotion instance.
     * @param emotionId ID of emotion (e.g. R.HOPE)
     * @return
     */
    Emotion getEmotion(int emotionId);

    /**
     * Called during initialization of emotional Element.
     * @return
     */
    Set<Emotion> getPossibleEmotions();

}
