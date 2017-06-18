package sk.tuke.fei.bdi.emotionalengine.component.enginegui;

/*

   Created with IntelliJ IDEA.

   Bc. Tomáš Herich
   --------------------------- 
   20. 02. 2013
   8:46 PM
   
*/

import javax.swing.*;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;

public class ScrollablePanel extends JScrollPane {

    protected static boolean isAutoScroll = true;
    protected boolean isClearHistory = false;
    protected int counter = 1;
    protected JPanel scrollPaneView;

    public ScrollablePanel() {

        setBorder(MyBorders.getEmptyBorder());

        // Create and set scroll pane view component
        scrollPaneView = new JPanel();
        scrollPaneView.setLayout(new BoxLayout(scrollPaneView, BoxLayout.Y_AXIS));
        scrollPaneView.setBorder(MyBorders.getEmptyBorder());
        setViewportView(scrollPaneView);

        // Set scrolling policies
        setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        // Auto scroll functionality
        // It is static so that behaviour can be changed on all components at once
        getVerticalScrollBar().addAdjustmentListener(new AdjustmentListener() {
            public void adjustmentValueChanged(AdjustmentEvent e) {

                // Auto scroll only if enabled
                if (isAutoScroll) {
                    e.getAdjustable().setValue(e.getAdjustable().getMaximum());
                }
            }
        });

    }

    public static boolean isAutoScroll() {
        return isAutoScroll;
    }

    public static void setAutoScroll(boolean autoScroll) {
        isAutoScroll = autoScroll;
    }

    public void clearHistory() {

        // Clear history switch
        // Don't add new components while all components are being removed
        isClearHistory = true;

        scrollPaneView.removeAll();
        scrollPaneView.revalidate();
        scrollPaneView.repaint();

        // Now it's safe to add new components
        isClearHistory = false;
    }

}

