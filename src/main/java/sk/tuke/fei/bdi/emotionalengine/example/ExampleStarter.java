package sk.tuke.fei.bdi.emotionalengine.example;

import sk.tuke.fei.bdi.emotionalengine.starter.JBDIEmo;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by PeterZemianek on 5/14/2017.
 */
public class ExampleStarter {

    public static void main(String[] args) {

        Map<String, String> agents = new HashMap<>();

        agents.put("TelemarketerAnna", "sk.tuke.fei.bdi.emotionalengine.example.telemarketeranna.TelemarketerAnnaBDI.class");

        agents.put("HungryPaul", "sk.tuke.fei.bdi.emotionalengine.example.hungrypaul.HungryPaulBDI.class");

        JBDIEmo.start(agents);
    }
}
