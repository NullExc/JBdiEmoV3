package sk.tuke.fei.bdi.emotionalengine.component.emotion;

import java.util.Set;

/**
 * @author Tomáš Herich
 */

public interface EmotionFactory {

    Emotion getEmotion(int emotionId);
    Set<Emotion> getPossibleEmotions();

}
