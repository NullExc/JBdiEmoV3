package sk.tuke.fei.bdi.emotionalengine.component;

import jadex.commons.beans.PropertyChangeListener;
import jadex.commons.beans.PropertyChangeSupport;
import sk.tuke.fei.bdi.emotionalengine.belief.EmotionalBelief;

/**
 * This class provides support for triggering Change Events of Object Beliefs.
 *
 * @author Tomáš Herich
 * @author Peter Zemianek
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

    /**
     * Triggers Belief Change event by using PropertyChangeSupport instance.
     */
    public synchronized void fireBeliefChange() {
        pcs.firePropertyChange("propertyChangeHelper", false, true);
    }

    /**
     * Triggers Belief Change event exclusively for EmotionalBelief beliefs. The reason for using this method instead of
     * 'fireBeliefChange' is, that in case of Emotional Belief we need actual instance after a change.
     *
     * @param oldValue old instance of Emotional Belief
     * @param newValue new instance of Emotional Belief
     */
    public synchronized void fireEmotionalBeliefChange(EmotionalBelief oldValue, EmotionalBelief newValue) {
        pcs.firePropertyChange("emotionalBeliefChangeHelper", oldValue, newValue);
    }

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        pcs.addPropertyChangeListener(listener);
    }

    public void removePropertyChangeListener(PropertyChangeListener listener) {
        pcs.removePropertyChangeListener(listener);
    }
}
