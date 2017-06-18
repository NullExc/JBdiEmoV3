package sk.tuke.fei.bdi.emotionalengine.component.enginegui;

/*

   Created with IntelliJ IDEA.

   Bc. Tomáš Herich
   --------------------------- 
   18. 02. 2013
   10:59 PM
   
*/

import sk.tuke.fei.bdi.emotionalengine.component.Engine;
import sk.tuke.fei.bdi.emotionalengine.res.R;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;

public class TabsContainer extends JTabbedPane implements ChangeListener {

    private JTabbedPane goals;
    private JTabbedPane plans;
    private JTabbedPane beliefs;
    private TabbedPaneBeliefSets beliefSets;
    private TabbedPaneOthers others;
    private ScrollablePanel activeScrollablePanel;

    public TabsContainer(Engine engine) {

        setTabPlacement(JTabbedPane.LEFT);
        setBorder(MyBorders.getEmptyLineBorder());

        // Create goals tab to display goal elements info
        goals = new TabbedPaneElement(R.GOAL, this, engine);
        addTab(R.EMOTIONAL_ELEMENT_TYPE_LONG_NAMES.get(R.GOAL), null, goals, null);

        // Create plans tab to display plan elements info
        plans = new TabbedPaneElement(R.PLAN, this, engine);
        addTab(R.EMOTIONAL_ELEMENT_TYPE_LONG_NAMES.get(R.PLAN), null, plans, null);

        // Create beliefs tab to display belief elements info
        beliefs = new TabbedPaneElement(R.BELIEF, this, engine);
        addTab(R.EMOTIONAL_ELEMENT_TYPE_LONG_NAMES.get(R.BELIEF), null, beliefs, null);

        // Create belief sets tab to display belief sets and their beliefs info
        beliefSets = new TabbedPaneBeliefSets(this, engine);
        addTab(R.EMOTIONAL_ELEMENT_TYPE_LONG_NAMES.get(R.BELIEF_SET), null, beliefSets, null);

        // Create others tab to display emotional others info
        others = new TabbedPaneOthers(this);
        addTab(R.EMOTIONAL_PARAMETER_LONG_NAMES.get(R.PARAM_EMOTIONAL_OTHER), null, others, null);

        // Main listener to handle tab selection
        addChangeListener(this);

        // Set first displayed tab as active component
        try {
            setActiveScrollablePanel((ScrollablePanel) ((JPanel) ((JTabbedPane) getComponent(0)).getComponent(0)).getComponent(1));
        } catch (Exception e) {
            // There is no valid content of first displayed tab
        }
    }

    public void addContentTabsToParent(JFrame parent) {

        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 0;
        c.weightx = 1;
        c.weighty = 0.9;
        c.fill = GridBagConstraints.BOTH;

        parent.add(this, c);
    }



    public ScrollablePanel getActiveScrollablePanel() {
        return activeScrollablePanel;
    }

    public void setActiveScrollablePanel(ScrollablePanel activeScrollablePanel) {
        this.activeScrollablePanel = activeScrollablePanel;
    }

    public JTabbedPane getGoals() {
        return goals;
    }

    public JTabbedPane getPlans() {
        return plans;
    }

    public JTabbedPane getBeliefs() {
        return beliefs;
    }

    public TabbedPaneBeliefSets getBeliefSets() {
        return beliefSets;
    }

    public TabbedPaneOthers getOthers() {
        return others;
    }

    // Listeners to update active component (and sub components)
    // Active component is set for Gui action clear history (to clear it only in active component)

    @Override
    public void stateChanged(ChangeEvent e) {

        // For belief sets tab (different internal structure)
        if (getSelectedComponent() instanceof TabbedPaneBeliefSets) {

            JTabbedPane selectedBeliefSet = (JTabbedPane) beliefSets.getSelectedComponent();

            if (selectedBeliefSet != null) {
                JPanel selectedBelief = (JPanel) selectedBeliefSet.getSelectedComponent();

                if (selectedBelief != null) {
                    ScrollablePanel selectedBeliefEventPanel = (ScrollablePanel) selectedBelief.getComponent(1);
                    setActiveScrollablePanel(selectedBeliefEventPanel);
                }
            }

            return;
        }

        // For others tab (different internal structure)
        if (getSelectedComponent() instanceof TabbedPaneOthers) {

            if (others.getComponentCount() > 0) {

                ScrollablePanel selectedAgent = (ScrollablePanel) others.getSelectedComponent();
                setActiveScrollablePanel(selectedAgent);

            }

            return;
        }

        // For goals, plans, beliefs tab
        if (getSelectedComponent() instanceof TabbedPaneElement) {

            TabbedPaneElement selectedElementPane = (TabbedPaneElement) getSelectedComponent();

            if (selectedElementPane != null) {

                JPanel elementPanel = (JPanel) selectedElementPane.getSelectedComponent();

                if (elementPanel != null) {

                    setActiveScrollablePanel((ScrollablePanel) elementPanel.getComponent(1));

                }
            }
        }
    }

}
