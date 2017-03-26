package sk.tuke.fei.bdi.emotionalengine.component.emotionalevent.user;

/*

   Created with IntelliJ IDEA.

   Bc. Tomáš Herich
   --------------------------- 
   02. 02. 2013
   1:03 PM
   
*/

import sk.tuke.fei.bdi.emotionalengine.component.emotionalevent.EmotionalEvent;
import sk.tuke.fei.bdi.emotionalengine.component.emotionalevent.EmotionalEventChecker;
import sk.tuke.fei.bdi.emotionalengine.res.R;

public class UserProbabilityChecker implements EmotionalEventChecker {

    public boolean checkEmotionalEvent(EmotionalEvent event) {

        return event.getUserParameters().containsKey(R.PARAM_PROBABILITY);

    }

}
