

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
        agents.put("Reciever", "sk.tuke.fei.bdi.emotionalengine.parser.parser_example.RecieverBDI.class");
        agents.put("Sender", "sk.tuke.fei.bdi.emotionalengine.parser.parser_example.SenderBDI.class");


        JBDIEmo.start(agents);


    }
}
