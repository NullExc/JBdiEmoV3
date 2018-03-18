package sk.tuke.fei.bdi.emotionalengine.component;

import jadex.commons.beans.PropertyChangeListener;
import jadex.commons.beans.PropertyChangeSupport;
import sk.tuke.fei.bdi.emotionalengine.belief.EmotionalBelief;

/**
 * @author Tomáš Herich
 * @author Peter Zemianek
 */

public class JadexBeliefChangeDetectionSupport {

    protected PropertyChangeSupport pcs;
    protected boolean propertyChangeHelper;
    protected EmotionalBelief emotionalBeliefChangeHelper;

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

    public synchronized void fireEmotionalBeliefChange(Object oldValue, Object newValue) {
        pcs.firePropertyChange("emotionalBeliefChangeHelper", oldValue, newValue);
    }

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        pcs.addPropertyChangeListener(listener);
    }

    public void removePropertyChangeListener(PropertyChangeListener listener) {
        pcs.removePropertyChangeListener(listener);
    }
}
