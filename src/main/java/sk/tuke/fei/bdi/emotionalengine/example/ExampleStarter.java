package sk.tuke.fei.bdi.emotionalengine.example;

import jadex.bdiv3.annotation.Plan;
import sk.tuke.fei.bdi.emotionalengine.example.hungrypaul.EmotionalCapability;
import sk.tuke.fei.bdi.emotionalengine.starter.JBDIEmo;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by PeterZemianek on 5/14/2017.
 */
public class ExampleStarter {

    public static void main(String[] args) {

        Map<String, String> agents = new HashMap<>();

       /* try {
            Class.forName("sk.tuke.fei.bdi.emotionalengine.example.hungrypaul.EmotionalCapability$CapaClassPlan");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }*/

       /* Class planClass = null;
        try {
            planClass = Class.forName("sk.tuke.fei.bdi.emotionalengine.example.hungrypaul.EmotionalCapability$CapaClassPlan");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        Plan plan = (Plan) planClass.getDeclaredAnnotation(Plan.class);

        System.err.println("counted before : " + plan.trigger().factremoveds().length); */

        //agents.put("TelemarketerAnna", "sk.tuke.fei.bdi.emotionalengine.example.telemarketeranna.TelemarketerAnnaBDI.class");

        agents.put("HungryPaul", "sk.tuke.fei.bdi.emotionalengine.example.hungrypaul.HungryPaulBDI.class");

       // Plan plan1 = (Plan) planClass.getDeclaredAnnotation(Plan.class);

       // System.err.println("counted after: " + plan1.trigger().factremoveds().length);

        JBDIEmo.start(agents);
    }
}
