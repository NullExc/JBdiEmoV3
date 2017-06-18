package sk.tuke.fei.bdi.emotionalengine.component.enginegui;

/*

   Created with IntelliJ IDEA.

   Bc. Tomáš Herich
   --------------------------- 
   19. 02. 2013
   1:51 PM
   
*/

import jadex.bridge.IComponentIdentifier;
import sk.tuke.fei.bdi.emotionalengine.component.emotionalmessage.MessageParser;
import sk.tuke.fei.bdi.emotionalengine.res.R;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.util.HashSet;
import java.util.Set;

public class TabbedPaneOthers extends JTabbedPane {

    private Set<IComponentIdentifier> emotionalOthers;
    private Set<JScrollPane> agentPanes;
    private int counter = 1;
    private boolean isChangeNewTab = false;


    public TabbedPaneOthers(final TabsContainer parentPane) {

        emotionalOthers = new HashSet<IComponentIdentifier>();
        agentPanes = new HashSet<JScrollPane>();

        setTabPlacement(JTabbedPane.LEFT);
        setBorder(MyBorders.getEmptyBorder());

        // Add change listener for active element selection
        addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {

                // Check if change event was caused by addition of new tab
                if (!isChangeNewTab) {

                    // If change wasn't caused by new tab addition, set selected component as active
                    parentPane.setActiveScrollablePanel((ScrollablePanel) getSelectedComponent());

                }
            }
        });

    }

    public synchronized void updateEmotionalOthersSet(Set<IComponentIdentifier> othersUpdate) {

        // Set change caused by new tab addition
        isChangeNewTab = true;

        // Iterate emotional other identifiers
        for (IComponentIdentifier id : othersUpdate) {

            // Check if local set already contains emotional other id
            if (!emotionalOthers.contains(id)) {

                // Add new id to local set to avoid duplications
                emotionalOthers.add(id);

                // Create component for new id
                JScrollPane agentPane = new ScrollablePanelMessage(id.getName());

                // Add new component to local set
                agentPanes.add(agentPane);

                // Add new component to parent component to display it
                addTab(id.getName(), null, agentPane, null);
            }
        }

        // Tab added, further changes won't be caused by addition of new tab
        isChangeNewTab = false;
    }

    public synchronized void addMessage(String receivedMessage) {

        JScrollPane selectedAgentPane = null;

        // Prepare message parser to get needed message parts
        MessageParser message = new MessageParser(receivedMessage);

      //  System.err.println("||||| PROPERTY CHANGE MESSAGE  ||||| " + receivedMessage);

        // Iterate local component set
        for (JScrollPane agentPane : agentPanes) {

            String simpleName = agentPane.getName().split("@")[0];

         //   System.err.println("||||| agentPane " + simpleName + " Sender agent : " + message.getSender());

            // If local component name match message sender
            if (simpleName.equals(message.getSender())) {

                // Set selected component and break loop
                selectedAgentPane = agentPane;
                break;

            }
        }

        // If component was selected
        if (selectedAgentPane != null) {

            // Create message panel (to display message)
            ContentPanelMessage contentPanelMessage = new ContentPanelMessage(message, counter);

            // Add message panel and spacing to selected local component
            ((JPanel) selectedAgentPane.getViewport().getView()).add(contentPanelMessage);
            ((JPanel) selectedAgentPane.getViewport().getView()).add(Box.createRigidArea(new Dimension(R.GUI_SPACING, R.GUI_SPACING)));

            // Increment counter for message numbering
            counter++;

        }
    }

}
