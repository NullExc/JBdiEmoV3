package sk.tuke.fei.bdi.emotionalengine.component.enginegui;

/*

   Created with IntelliJ IDEA.

   Bc. Tomáš Herich
   --------------------------- 
   03. 02. 2013
   11:04 PM
   
*/

import org.omg.Messaging.SYNC_WITH_TRANSPORT;
import sk.tuke.fei.bdi.emotionalengine.component.Element;
import sk.tuke.fei.bdi.emotionalengine.component.Engine;
import sk.tuke.fei.bdi.emotionalengine.helper.MyMath;
import sk.tuke.fei.bdi.emotionalengine.res.R;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
//import java.beans.PropertyChangeEvent;
//import java.beans.PropertyChangeListener;
import jadex.commons.beans.PropertyChangeListener;
import jadex.commons.beans.PropertyChangeEvent;

public class EngineGui extends JFrame implements ActionListener {

    private Engine engine;
    private Toolbar toolbar;
    private TabsContainer tabsContainer;

    private boolean isGuiInitialized = false;

    public EngineGui(Engine engine) {

        this.engine = engine;

        initializeDataSource();
        //initializeLookAndFeel();
        initializeFrame();
        initializeContentTabs();
        initializeToolbar();

        isGuiInitialized = true;

        validate();
    }

    private void initializeDataSource() {

        // Can't start gui before agent elements are mapped and engine initialized
        while (!engine.isInitialized()) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                System.out.println("Gui sleep exception: " + e.getMessage());
            }
        }

        // Update data in others tabbed pane
        engine.addPropertyChangeListener(new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {

                if (isGuiInitialized) {



                    // Add new emotional other if exists
                    tabsContainer.getOthers().updateEmotionalOthersSet(engine.getEmotionalOtherIds());


                    // Get new received message from queue
                    String receivedMessage = engine.getReceivedMessage();


                    // If message exists add it to others tabbed pane
                    if (receivedMessage != null) {
                        tabsContainer.getOthers().addMessage(receivedMessage);
                    }


                    // Get belief set beliefs element from engine
                    Element[] beliefSetsBeliefs = engine.getElements(R.BELIEF_SET_BELIEF);
                    if (beliefSetsBeliefs != null) {
                        // Update belief set gui component with current set of belief set beliefs
                        tabsContainer.getBeliefSets().updateBeliefSetElements(beliefSetsBeliefs);
                    }

                    // Get mood values
                    String mood;
                    double positiveMood = engine.getPositiveMood().getIntensity();
                    double negativeMood = engine.getNegativeMood().getIntensity();

                    // Create mood string
                    if (positiveMood > negativeMood) {
                        mood = ", mood: " + R.EMOTIONAL_IDS_NAMES.get(R.POSITIVE) + ": " + MyMath.roundDouble(positiveMood - negativeMood, 4);
                    } else if (negativeMood > positiveMood) {
                        mood = ", mood: " + R.EMOTIONAL_IDS_NAMES.get(R.NEGATIVE) + ": " + MyMath.roundDouble(negativeMood - positiveMood, 4);
                    } else {
                        mood = ", mood: neutral";
                    }

                    // Update gui title
                    setTitle("Emotional engine value monitor " + engine.getAgentName() + mood);
                }
            }
        });

    }

    private void initializeFrame() {

        // Set Gui frame name
        setTitle("Emotional engine value monitor " + engine.getAgentName());
        //setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1200, 750);
        setVisible(true);

        GridBagLayout grid = new GridBagLayout();
        setLayout(grid);
    }



    private void initializeLookAndFeel() {

        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception e) {
            // If Nimbus is not available, use default look and feel.
            System.out.println("Nimbus is not available look and feel: " + e.getMessage());
        }

    }

    private void initializeToolbar() {
        toolbar = new Toolbar();
        toolbar.addToolbarToParent(this);

        // Add Gui as action listener to toolbar buttons
        toolbar.getAutoScrollToggle().addActionListener(this);
        toolbar.getClearEventHistory().addActionListener(this);
    }

    private void initializeContentTabs() {

        tabsContainer = new TabsContainer(engine);
        tabsContainer.addContentTabsToParent(this);

    }

    @Override
    public void actionPerformed(ActionEvent e) {

        // Toggle scroll pane auto scroll to bottom (newest)
        if ("eventAutoScrollToggle".equals(e.getActionCommand())) {

            boolean isAutoScroll = ScrollablePanel.isAutoScroll();

            if (isAutoScroll) {
                ScrollablePanel.setAutoScroll(false);
                ((JButton) e.getSource()).setText("Enable auto scroll");
            } else {
                ScrollablePanel.setAutoScroll(true);
                ((JButton) e.getSource()).setText("Disable auto scroll");
            }
        }

        // Clear history (remove components) from selected (active) scrollable pane
        if ("clearHistory".equals(e.getActionCommand())) {

            ScrollablePanel scrollableEventPanelsHolder = tabsContainer.getActiveScrollablePanel();

            if (scrollableEventPanelsHolder != null) {

                scrollableEventPanelsHolder.clearHistory();

            }
        }
    }
}
