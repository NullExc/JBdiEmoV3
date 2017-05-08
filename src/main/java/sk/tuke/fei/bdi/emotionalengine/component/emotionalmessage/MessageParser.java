package sk.tuke.fei.bdi.emotionalengine.component.emotionalmessage;

import sk.tuke.fei.bdi.emotionalengine.res.R;

import java.util.Map;

/**
 * @author Tomáš Herich
 * @author Peter Zemianek
 */

public class MessageParser {

    private String tag;
    private String event;
    private String result;
    private String sender;
    private String plan;
    private String messageContent;

    public MessageParser(Map<String, String> message) {

        tag = message.get(R.KEY_MESSAGE_EMOTIONAL);
        event = message.get(R.KEY_MESSAGE_PLAN);
        result = message.get(R.KEY_MESSAGE_RESULT);
        sender = message.get(R.KEY_SENDER_ID);
        plan = message.get(R.KEY_PLAN_NAME);

        messageContent = tag + R.MESSAGE_DELIMITER + event + R.MESSAGE_DELIMITER + result
                + R.MESSAGE_DELIMITER + sender + R.MESSAGE_DELIMITER + plan;

    }

    public String getTag() {
        return tag;
    }

    public String getEvent() {
        return event;
    }

    public String getResult() {
        return result;
    }

    public String getSender() {
        return sender;
    }

    public String getPlan() {
        return plan;
    }

    public String getMessageContent() {
        return messageContent;
    }

}
