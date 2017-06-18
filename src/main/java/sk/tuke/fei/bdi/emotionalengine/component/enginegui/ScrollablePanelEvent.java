package sk.tuke.fei.bdi.emotionalengine.component.enginegui;

/*

   Created with IntelliJ IDEA.

   Bc. Tomáš Herich
   --------------------------- 
   18. 02. 2013
   11:54 PM
   
*/

import sk.tuke.fei.bdi.emotionalengine.component.Element;
import sk.tuke.fei.bdi.emotionalengine.component.emotionalevent.EmotionalEvent;
import sk.tuke.fei.bdi.emotionalengine.component.emotionalevent.EmotionalEventListener;
import sk.tuke.fei.bdi.emotionalengine.res.R;

import javax.swing.*;
import java.awt.*;

public class ScrollablePanelEvent extends ScrollablePanel {

    public ScrollablePanelEvent(Element element) {

        super();

        // Small preferred size used for GridBagLayout wight to work as needed
        // weights distribute extra free space in ratio specified
        // when component has small preferred size, there is more extra free space
        // so weights have bigger impact on layout
        setPreferredSize(new Dimension(100, 100));

        // Set name
        setName(element.getName());

        // Add listener to source element
        element.addEmotionalEventListener(new EmotionalEventListener() {
            @Override
            public void eventHappened(EmotionalEvent event) {

                // Don't do anything if history is being cleared
                if (!isClearHistory) {

                    // Check if element emotional event exists
                    if (event != null) {

                        // Create event panel (to display event)
                        ContentPanelEvent contentPanelEvent = new ContentPanelEvent(event, counter);

                        // Add event panel and spacing to parent component
                        scrollPaneView.add(contentPanelEvent);
                        scrollPaneView.add(Box.createRigidArea(new Dimension(R.GUI_SPACING, R.GUI_SPACING)));

                        // Increment counter for event numbering
                        counter++;
                    }
                }
            }
        });
    }

}
