package sk.tuke.fei.bdi.emotionalengine.component.enginegui;

/*

   Created with IntelliJ IDEA.

   Bc. Tomáš Herich
   --------------------------- 
   18. 02. 2013
   10:39 PM
   
*/

import javax.swing.*;
import java.awt.*;

public class Toolbar extends JPanel {

    private JButton autoScrollToggle;
    private JButton clearEventHistory;

    public Toolbar() {

        setLayout(new FlowLayout(FlowLayout.RIGHT));
        setBorder(MyBorders.getLineBorder());

        createClearEventHistory();
        createAutoScrollToggle();

    }

    private void createAutoScrollToggle() {

        autoScrollToggle = new JButton("Disable auto scroll");
        autoScrollToggle.setActionCommand("eventAutoScrollToggle");
        add(autoScrollToggle);

    }

    private void createClearEventHistory() {

        clearEventHistory = new JButton("Clear history");
        clearEventHistory.setActionCommand("clearHistory");
        add(clearEventHistory);

    }

    public void addToolbarToParent(JFrame parent) {

        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 1;
        c.weightx = 0;
        c.weighty = 0.1;
        c.fill = GridBagConstraints.HORIZONTAL;

        // Add toolbar to EngineGui (parent frame)
        parent.add(this, c);
    }

    public JButton getAutoScrollToggle() {
        return autoScrollToggle;
    }

    public JButton getClearEventHistory() {
        return clearEventHistory;
    }

}
