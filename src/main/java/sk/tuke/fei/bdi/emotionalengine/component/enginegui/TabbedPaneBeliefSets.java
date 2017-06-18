package sk.tuke.fei.bdi.emotionalengine.component.enginegui;

/*

   Created with IntelliJ IDEA.

   Bc. Tomáš Herich
   --------------------------- 
   22. 02. 2013
   4:29 PM
   
*/

import sk.tuke.fei.bdi.emotionalengine.component.Element;
import sk.tuke.fei.bdi.emotionalengine.component.Engine;
import sk.tuke.fei.bdi.emotionalengine.res.R;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.util.HashSet;
import java.util.Set;

public class TabbedPaneBeliefSets extends JTabbedPane {

    private TabsContainer parentPane;
    private Engine engine;
    private Set<JTabbedPane> beliefSetsTabbedPanes;

    public TabbedPaneBeliefSets(final TabsContainer parentPane, Engine engine) {

        this.parentPane = parentPane;
        this.engine = engine;

        beliefSetsTabbedPanes = new HashSet<JTabbedPane>();

        setTabPlacement(JTabbedPane.LEFT);
        setBorder(MyBorders.getEmptyBorder());

        addBeliefSetsTabs();

        // Listener to update active component (and sub components)
        // Active component is set for Gui action clear history (to clear it only in active component)
        addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {

                JTabbedPane selectedBeliefSet = (JTabbedPane) getSelectedComponent();

                if (selectedBeliefSet != null) {
                    JPanel selectedBelief = (JPanel) selectedBeliefSet.getSelectedComponent();

                    if (selectedBelief != null) {
                        ScrollablePanel selectedBeliefEventPanel = (ScrollablePanel) selectedBelief.getComponent(1);
                        parentPane.setActiveScrollablePanel(selectedBeliefEventPanel);
                    }
                }
            }
        });

    }

    private void addBeliefSetsTabs() {

        // Get belief set names from engine
        String[] beliefSetsNames = engine.getElementsNames(R.BELIEF_SET);

        if (beliefSetsNames == null) {
            return;
        }

        // Iterate belief sets names
        for (String beliefSetName : beliefSetsNames) {

            // Create tabbed pane for belief set
            final JTabbedPane beliefSetTabbedPane = new JTabbedPane();
            beliefSetTabbedPane.setTabPlacement(JTabbedPane.LEFT);
            beliefSetTabbedPane.setBorder(MyBorders.getEmptyBorder());
            beliefSetTabbedPane.setName(beliefSetName);

            // Add belief set tabbed pane to parent
            addTab(beliefSetName, beliefSetTabbedPane);

            // Add belief set tabbed pane to belief set tabbed panes set
            beliefSetsTabbedPanes.add(beliefSetTabbedPane);

            // Add beliefs to recently created beliefs set tabbed pane
            addBeliefsToBeliefSetTab(beliefSetTabbedPane);


            // Listener to update active component (and sub components) for recently created belief set tabbed pane
            // Active component is set for Gui action clear history (to clear it only in active component)
            beliefSetTabbedPane.addChangeListener(new ChangeListener() {
                @Override
                public void stateChanged(ChangeEvent e) {

                    JPanel selectedBelief = (JPanel) beliefSetTabbedPane.getSelectedComponent();

                    if (selectedBelief != null) {
                        ScrollablePanel selectedBeliefEventPanel = (ScrollablePanel) selectedBelief.getComponent(1);
                        parentPane.setActiveScrollablePanel(selectedBeliefEventPanel);
                    }

                }
            });

        }
    }

    private void addBeliefsToBeliefSetTab(JTabbedPane beliefSetTabbedPane) {

        // Get belief set beliefs from engine
        Element[] elements = engine.getElements(R.BELIEF_SET_BELIEF);

        // Iterate belief set beliefs
        for (Element element : elements) {

            // Check if belief set beliefs has valid parent belief set property
            if (element.hasParentBeliefSet()) {

                // Check if parent belief set property value equals to currently
                // created belief set which is being filled with belonging beliefs
                if (element.getParentBeliefSetName().equals(beliefSetTabbedPane.getName())) {

                    // Create element panel for element
                    PanelElement myPanelElement = new PanelElement(engine, element);

                    // Add element panel to parent component
                    beliefSetTabbedPane.addTab(element.getName(), null, myPanelElement, null);

                }
            }
        }
    }

    public void updateBeliefSetElements(Element[] dataElements) {

        // Add new facts (beliefs) to belief sets
        addNewBeliefsGui(dataElements);

        // Remove discarded facts (beliefs) from belief sets
        removeDiscardedBeliefsFromGui(dataElements);


    }

    private void addNewBeliefsGui(Element[] dataElements) {

        // Iterate engine belief set beliefs
        for (Element dataElement : dataElements) {

            boolean isBeliefAlreadyInGui = false;
            JTabbedPane selectedGuiBeliefSet = null;

            // Get belief set belief parent belief set name
            String dataElementParentName = dataElement.getParentBeliefSetName();

            // Iterate set of stored belief set panes
            for (JTabbedPane guiBeliefSet : beliefSetsTabbedPanes) {

                // Get belief set name
                String guiBeliefSetName = guiBeliefSet.getName();

                // Check if belief set name equals belief set belief parent belief set name
                if (guiBeliefSetName.equals(dataElementParentName)) {

                    // If belief set belief belongs to belief set iterate belief set components
                    for (int i = 0; i < guiBeliefSet.getComponentCount(); i++) {

                        // Get component
                        PanelElement guiElement = (PanelElement) guiBeliefSet.getComponent(i);

                        // Check if belief set belief name equals component name
                        if (guiElement.getName().equals(dataElement.getName())) {

                            // If yes, belief is already in gui and shouldn't be added again
                            // Break loop for this belief set
                            isBeliefAlreadyInGui = true;
                            break;

                        }
                    }

                    // Set currently iterated belief set as selected
                    selectedGuiBeliefSet = guiBeliefSet;

                    // If belief was found, break loop for iterating belief sets
                    if (isBeliefAlreadyInGui) {
                        break;
                    }
                }
            }

            // Check if belief is not in gui and belongs to some belief set
            if (!isBeliefAlreadyInGui && selectedGuiBeliefSet != null) {

                // Add new belief to gui (which wasn't previously included)
                addBeliefToBeliefSetTab(dataElement, selectedGuiBeliefSet);

            }
        }
    }

    private void addBeliefToBeliefSetTab(Element element, JTabbedPane beliefSetTabbedPane) {

        // Create element panel for element
        PanelElement myPanelElement = new PanelElement(engine, element);

        // Add element panel to parent component
        beliefSetTabbedPane.addTab(element.getName(), null, myPanelElement, null);

    }

    private void removeDiscardedBeliefsFromGui(Element[] dataElements) {

        // Iterate stored belief sets set
        for (JTabbedPane guiBeliefSet : beliefSetsTabbedPanes) {

            // Iterate belief set pane components
            for (int i = 0; i < guiBeliefSet.getComponentCount(); i++) {

                boolean wasElementDiscarded = true;

                // Cast component to PanelElement
                PanelElement guiElement = (PanelElement) guiBeliefSet.getComponent(i);

                // Get component name
                String guiElementName = guiElement.getName();

                // Iterate engine belief set beliefs
                for (Element dataElement : dataElements) {

                    // Check component name equals engine belief set belief name
                    if (dataElement.getName().equals(guiElementName)) {

                        // If yes, component has its counterpart in engine and was not discarded
                        wasElementDiscarded = false;
                        break;
                    }
                }

                // If component doesn't have its counterpart in engine it was discarded
                if (wasElementDiscarded) {

                    // Remove component from gui
                    guiBeliefSet.remove(i);

                }

            }
        }
    }


}

