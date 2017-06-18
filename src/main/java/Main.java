

import sk.tuke.fei.bdi.emotionalengine.starter.JBDIEmo;

import java.util.*;

/**
 * @author Peter Zemianek
 */
public class Main {
    
    public static void main(String[] args) {

        String[] defaultArgs = new String[]
                {
                        "-gui", "false",
                        "-welcome", "false",
                        "-cli", "false",
                        "-printpass", "false"
                };

        Map<String, String> agents = new HashMap<String, String>();
       /* agents.put("Reciever", "sk.tuke.fei.bdi.emotionalengine.parser.parser_example.RecieverBDI.class");
        agents.put("Sender", "sk.tuke.fei.bdi.emotionalengine.parser.parser_example.SenderBDI.class");*/

       agents.put("TelemarketerAnna", "sk.tuke.fei.bdi.emotionalengine.example.telemarketeranna.TelemarketerAnnaBDI.class");
       agents.put("HungryPaul", "sk.tuke.fei.bdi.emotionalengine.example.hungrypaul.HungryPaulBDI.class");

        JBDIEmo.start(agents);

//        System.err.println("\n" + CryGoal.class.getAnnotation(EmotionalGoal.class).value().length + " :( FAKT AKO TA JA NEVJEM UZ");

    }
}
