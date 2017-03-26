package sk.tuke.fei.bdi.emotionalengine.component.emotion;

/*

   Created with IntelliJ IDEA.

   Bc. Tomáš Herich
   ---------------------------
   01. 02. 2013
   2:51 PM

*/

import java.util.Set;

public interface EmotionFactory {

    public Emotion getEmotion(int emotionId);
    public Set<Emotion> getPossibleEmotions();

}
