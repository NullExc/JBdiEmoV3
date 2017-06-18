package sk.tuke.fei.bdi.emotionalengine.component.enginegui;

/*

   Created with IntelliJ IDEA.

   Bc. Tomáš Herich
   --------------------------- 
   20. 02. 2013
   8:13 PM
   
*/

import sk.tuke.fei.bdi.emotionalengine.component.emotionalmessage.MessageParser;
import sk.tuke.fei.bdi.emotionalengine.helper.MyTime;

import javax.swing.*;
import java.awt.*;

public class ContentPanelMessage extends JPanel {

    private MessageParser message;
    private int messageNumber;

    public ContentPanelMessage(MessageParser message, int eventNumber) {
        this.message = message;
        this.messageNumber = eventNumber;

        // Borders and layout
        setBorder(MyBorders.getEmptyLineBorder());
        setLayout(new GridLayout(0, 2));
        setOpaque(false);

        createMessagePanel();
    }

    private void createMessagePanel() {

        // Add message content into layout
        add(new JLabel("Message " + messageNumber + ": "));
        add(createLabel(MyTime.currentTimeString(), SwingConstants.CENTER, SwingConstants.CENTER));

        add(new JLabel("Sender: "));
        add(createLabel(message.getSender(), SwingConstants.CENTER, SwingConstants.CENTER));

        add(new JLabel("Plan: "));
        add(createLabel(message.getPlan(), SwingConstants.CENTER, SwingConstants.CENTER));

        add(new JLabel("Event: "));
        add(createLabel(message.getEvent(), SwingConstants.CENTER, SwingConstants.CENTER));

        add(new JLabel("Result: "));
        add(createLabel(message.getResult(), SwingConstants.CENTER, SwingConstants.CENTER));

    }

    private JLabel createLabel(String text, int horizontalAlignment, int verticalAlignment) {
        JLabel label = new JLabel(text);
        label.setHorizontalAlignment(horizontalAlignment);
        label.setVerticalAlignment(verticalAlignment);
        label.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createEmptyBorder(1, 0, 1, 0), MyBorders.getLineBorder()));
        return label;
    }


}
