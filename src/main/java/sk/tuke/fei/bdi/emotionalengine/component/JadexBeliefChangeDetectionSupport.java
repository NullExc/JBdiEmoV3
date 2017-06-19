package sk.tuke.fei.bdi.emotionalengine.component;

import jadex.commons.beans.PropertyChangeListener;
import jadex.commons.beans.PropertyChangeSupport;

/**
 * @author Tomáš Herich
 */

public class JadexBeliefChangeDetectionSupport {

    protected PropertyChangeSupport pcs;
    protected boolean propertyChangeHelper;

    /*  Used to notify Jadex of belief changes
        e.g.:
        emotion intensity change
        emotioanl event
        message event
        etc...
    */

    public JadexBeliefChangeDetectionSupport() {
        pcs = new PropertyChangeSupport(this);
    }

    public boolean isPropertyChangeHelper() {
        return propertyChangeHelper;
    }

    public void setPropertyChangeHelper(boolean propertyChangeHelper) {
        this.propertyChangeHelper = propertyChangeHelper;
    }

    public synchronized void fireBeliefChange() {
        pcs.firePropertyChange("propertyChangeHelper", false, true);
    }

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        pcs.addPropertyChangeListener(listener);
    }

    public void removePropertyChangeListener(PropertyChangeListener listener) {
        pcs.removePropertyChangeListener(listener);
    }
}
