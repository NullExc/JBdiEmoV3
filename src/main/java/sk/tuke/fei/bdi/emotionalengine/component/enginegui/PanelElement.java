package sk.tuke.fei.bdi.emotionalengine.component.enginegui;

/*

   Created with IntelliJ IDEA.

   Bc. Tomáš Herich
   --------------------------- 
   19. 02. 2013
   1:27 PM
   
*/

import sk.tuke.fei.bdi.emotionalengine.component.Element;
import sk.tuke.fei.bdi.emotionalengine.component.Engine;

import javax.swing.*;
import java.awt.*;

public class PanelElement extends JPanel {

    public PanelElement(Engine engine, Element element) {

        GridBagConstraints c = new GridBagConstraints();

        // Borders and layout
        GridBagLayout grid = new GridBagLayout();
        setLayout(grid);
        setName(element.getName());
        setBorder(MyBorders.getEmptyBorder());

        // Element contains information about element emotions and emotional events

        // Create emotional panel to display info about element emotions
        ContentPanelEmotion contentPanelEmotion = new ContentPanelEmotion(engine, element);
        c.gridx = 0;
        c.gridy = 0;
        c.weightx = 0.3;
        c.weighty = 0.8;
        c.fill = GridBagConstraints.BOTH;
        add(contentPanelEmotion, c);

        // Create event panel to display element emotional events
        ScrollablePanelEvent scrollableEventPanel = new ScrollablePanelEvent(element);
        c.gridx = 1;
        c.gridy = 0;
        c.weightx = 0.7;
        c.weighty = 0.8;
        c.fill = GridBagConstraints.BOTH;
        add(scrollableEventPanel, c);

        // Create event panel to display element emotional events
        ContentPanelChart chartPanel = new ContentPanelChart(element);
        c.gridx = 0;
        c.gridy = 1;
        c.gridwidth = 2;
        c.weightx = 0.7;
        c.weighty = 0.2;
        c.fill = GridBagConstraints.BOTH;
        add(chartPanel, c);

    }

}
