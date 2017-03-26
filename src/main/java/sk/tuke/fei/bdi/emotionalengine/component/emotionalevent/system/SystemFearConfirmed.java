package sk.tuke.fei.bdi.emotionalengine.component.emotionalevent.system;

/*

   Created with IntelliJ IDEA.

   Bc. Tomáš Herich
   --------------------------- 
   03. 02. 2013
   2:00 AM
   
*/

import sk.tuke.fei.bdi.emotionalengine.component.emotionalevent.EmotionalEvent;
import sk.tuke.fei.bdi.emotionalengine.component.emotionalevent.EmotionalEventChecker;
import sk.tuke.fei.bdi.emotionalengine.res.R;

public class SystemFearConfirmed implements EmotionalEventChecker {

    public boolean checkEmotionalEvent(EmotionalEvent event) {

        return event.getSystemParameters().containsKey(R.FEAR_CONFIRMED);

    }

}
