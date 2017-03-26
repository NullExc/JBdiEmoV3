package sk.tuke.fei.bdi.emotionalengine.component;

/*

   Created with IntelliJ IDEA.

   Bc. Tomáš Herich
   ---------------------------
   08. 01. 2013
   8:40 PM

*/


import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

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
