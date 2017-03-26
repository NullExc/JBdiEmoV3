package sk.tuke.fei.bdi.emotionalengine.component;


/*

   Created with IntelliJ IDEA.

   Bc. Tomáš Herich
   ---------------------------
   09. 11. 2012
   11:02 AM

*/


import sk.tuke.fei.bdi.emotionalengine.component.emotion.*;
import sk.tuke.fei.bdi.emotionalengine.component.emotionalevent.EmotionalEvent;
import sk.tuke.fei.bdi.emotionalengine.component.emotionalevent.EmotionalEventListener;
import sk.tuke.fei.bdi.emotionalengine.res.R;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Element extends JadexBeliefChangeDetectionSupport {

    private String name;
    private int type;
    private Set<Emotion> emotions = new HashSet<Emotion>();
    private List<EmotionalEventListener> emotionalEventListeners = new ArrayList<EmotionalEventListener>();
    private String parentBeliefSetName;

    /**
     * Create element instance with specified name
     * (same as name of BDI element e.g. "example_goal")
     * and specified type (e.g. R.GOAL)
     *
     * Based on type, constructor instantiate respective
     * emotion factory and creates all applicable emotions
     * for type, which are stored in emotions set of element.
     *
     * @param name name of element
     * @param type type of element
     */
    public Element(String name, int type) {
        super();

        this.name = name;
        this.type = type;

        // Resolve element type and assign possible emotions
        if (type == R.GOAL) {
            EmotionFactory emotionFactory = new GoalEmotionFactory();
            emotions = emotionFactory.getPossibleEmotions();
        }

        // Resolve element type and assign possible emotions
        if (type == R.PLAN) {
            EmotionFactory emotionFactory = new PlanEmotionFactory();
            emotions = emotionFactory.getPossibleEmotions();
        }

        // Resolve element type and assign possible emotions
        if (type == R.BELIEF || type == R.BELIEF_SET_BELIEF) {
            EmotionFactory emotionFactory = new BeliefEmotionFactory();
            emotions = emotionFactory.getPossibleEmotions();
        }

        // If possible emotions are valid assign property change listener to them
        if (emotions != null && !emotions.isEmpty()) {
            for (Emotion emotion : emotions) {
                emotion.addPropertyChangeListener(new PropertyChangeListener() {

                    public void propertyChange(PropertyChangeEvent evt) {
                        fireBeliefChange();
                    }
                });
            }
        }

        this.parentBeliefSetName = null;
    }

    /**
     * Get element name (e.g. "example_goal")
     *
     * @return element name
     */
    public String getName() {
        return name;
    }

    /**
     * Get element type (e.g. R.GOAL)
     *
     * @return element type
     */
    public int getType() {
        return type;
    }

    /**
     * Check if element contains emotion
     *
     * @param emotionId id specified in R, e.g. R.FEAR
     * @return True if element contains specified emotion
     */
    public boolean isEmotion(int emotionId) {

        boolean result = false;

        for (Emotion emotion : emotions) {
            if (emotion.getEmotionId() == emotionId) {
                result = true;
            }
        }

        return result;
    }

    /**
     * Get emotion contained in element
     *
     * @param emotionId id specified in R, e.g. R.FEAR
     * @return Emotion instance specified by emotionId or null when element doesn't contain such emotion
     */
    public Emotion getEmotion(int emotionId) {

        Emotion result = null;

        for (Emotion emotion : emotions) {
            if (emotion.getEmotionId() == emotionId) {
                result = emotion;
            }
        }

        return result;
    }

    /**
     * Get all element emotions
     *
     * @return Set containing all emotions assigned to this element
     */
    public Set<Emotion> getEmotions() {

        return emotions;

    }

    public void addEmotionalEventListener(EmotionalEventListener listener) {
        emotionalEventListeners.add(listener);
    }

    public void removeEmotionalEventListener(EmotionalEventListener listener) {
        emotionalEventListeners.remove(listener);
    }

    public void notifyEmotionalEventListeners(EmotionalEvent event) {
        for (EmotionalEventListener listener : emotionalEventListeners) {
            listener.eventHappened(event);
        }
    }

    /**
     * Method called by ElementEventMonitor when BDI agent event occur
     * (e.g. plan created, plan finished for plan type element) or
     * MessageCenter when valid emotional message was received
     * for this particular element.
     *
     * Emotional event is passed to all element's emotions
     * for eliciting condition's checking
     *
     * @param event emotional event
     */
    public void processEmotionalEvent(EmotionalEvent event) {

        // Check if element has valid emotions
        if (emotions != null && !emotions.isEmpty()) {

            // Iterate emotions
            for (Emotion emotion : emotions) {

                // Send emotional event to every emotion for processing
                emotion.processEmotionalEvent(event);
            }
        }

        // Notify listeners about emotional event
        notifyEmotionalEventListeners(event);

    }

    /**
     * Test if element is belief set belief,
     * only belief set beliefs have non-null
     * parentBeliefSetName
     *
     * @return true if element is belief set belief
     */
    public boolean hasParentBeliefSet() {
        return parentBeliefSetName != null;
    }

    /**
     * Get name of parent belief set
     *
     * @return name of parent belief set for belief set beliefs or null
     *         for other element types
     */
    public String getParentBeliefSetName() {
        return parentBeliefSetName;
    }


    /**
     * Set parent belief set name
     *
     * @param parentBeliefSetName name of parent belief set
     */
    public void setParentBeliefSetName(String parentBeliefSetName) {
        this.parentBeliefSetName = parentBeliefSetName;
    }
}