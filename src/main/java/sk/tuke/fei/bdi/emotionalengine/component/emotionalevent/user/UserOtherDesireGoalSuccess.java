package sk.tuke.fei.bdi.emotionalengine.component.emotionalevent.user;

/*

   Created with IntelliJ IDEA.

   Bc. Tomáš Herich
   --------------------------- 
   07. 02. 2013
   6:52 PM
   
*/

import sk.tuke.fei.bdi.emotionalengine.component.emotionalevent.EmotionalEvent;
import sk.tuke.fei.bdi.emotionalengine.component.emotionalevent.EmotionalEventChecker;
import sk.tuke.fei.bdi.emotionalengine.res.R;

public class UserOtherDesireGoalSuccess implements EmotionalEventChecker {

    public boolean checkEmotionalEvent(EmotionalEvent event) {

        return event.getUserParameters().containsKey(R.PARAM_OTHER_DESIRE_GOAL_SUCCESS);

    }

}
