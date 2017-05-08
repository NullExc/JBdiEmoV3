package sk.tuke.fei.bdi.emotionalengine.component.emotion;

import java.util.Set;

/**
 * @author Tomáš Herich
 */

public interface EmotionFactory {

    public Emotion getEmotion(int emotionId);
    public Set<Emotion> getPossibleEmotions();

}
