package sk.tuke.fei.bdi.emotionalengine.component.emotionalevent.user;

/*

   Created with IntelliJ IDEA.

   Bc. Tomáš Herich
   --------------------------- 
   04. 02. 2013
   6:21 PM
   
*/

import sk.tuke.fei.bdi.emotionalengine.component.emotionalevent.EmotionalEvent;
import sk.tuke.fei.bdi.emotionalengine.component.emotionalevent.EmotionalEventChecker;
import sk.tuke.fei.bdi.emotionalengine.res.R;

public class UserDisapprovalChecker implements EmotionalEventChecker {

    public boolean checkEmotionalEvent(EmotionalEvent event) {

        return event.getUserParameters().containsKey(R.PARAM_DISAPPROVAL);

    }

}
