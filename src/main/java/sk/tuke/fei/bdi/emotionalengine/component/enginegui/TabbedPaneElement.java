package sk.tuke.fei.bdi.emotionalengine.component.enginegui;

/*

   Created with IntelliJ IDEA.

   Bc. Tomáš Herich
   --------------------------- 
   22. 02. 2013
   5:23 PM
   
*/

import sk.tuke.fei.bdi.emotionalengine.component.Element;
import sk.tuke.fei.bdi.emotionalengine.component.Engine;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class TabbedPaneElement extends JTabbedPane {

    private Engine engine;
    private TabsContainer parentPane;

    public TabbedPaneElement(int elementType, TabsContainer parentPane, Engine engine) {

        this.parentPane = parentPane;
        this.engine = engine;

        setTabPlacement(JTabbedPane.LEFT);
        setBorder(MyBorders.getEmptyBorder());

        addElementTabs(elementType);

    }


    private void addElementTabs(int elementType) {

        // Get emotional element names of set type (goals, plans, beliefs)
        String[] elementNames = engine.getElementsNames(elementType);

        if (elementNames == null) {
            return;
        }

        // Iterate emotional element names
        for (String elementName : elementNames) {

            // Get particular element by name
            Element element = engine.getElement(elementName, elementType);

            // Create element panel for element
            PanelElement myPanelElement = new PanelElement(engine, element);

            // Add element panel to parent component
            addTab(elementName, null, myPanelElement, null);
        }

        // Add change listener for active element selection
        addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {

                JPanel selectedElementPanel = (JPanel) getSelectedComponent();
                parentPane.setActiveScrollablePanel((ScrollablePanelEvent) selectedElementPanel.getComponent(1));
            }
        });
    }
}
