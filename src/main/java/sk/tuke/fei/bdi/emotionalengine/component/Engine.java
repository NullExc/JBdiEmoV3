package sk.tuke.fei.bdi.emotionalengine.component;

import jadex.bridge.IComponentIdentifier;
import jadex.commons.beans.PropertyChangeEvent;
import jadex.commons.beans.PropertyChangeListener;
import sk.tuke.fei.bdi.emotionalengine.component.emotion.Emotion;
import sk.tuke.fei.bdi.emotionalengine.component.emotion.mood.Mood;
import sk.tuke.fei.bdi.emotionalengine.component.emotion.mood.Negative;
import sk.tuke.fei.bdi.emotionalengine.component.emotion.mood.Positive;
import sk.tuke.fei.bdi.emotionalengine.res.R;

import java.util.*;
import java.util.concurrent.ArrayBlockingQueue;

/**
 * @author Tomáš Herich
 */

public class Engine extends JadexBeliefChangeDetectionSupport implements Runnable {

    private String agentName;
    private Object agentObject;

    private Map<String, Element> emotionalElements;
    private Queue<String> receivedMessages;
    private Set<IComponentIdentifier> emotionalOtherIds;

    // Default decay values, will be overwritten by values specified in ADF
    // InitializeEmotionalEnginePlan parameters
    private int decayDelay = 1000;
    private int decaySteps = 5;

    private boolean isInitialized = false;

    // Mood properties
    private Mood positiveMood;
    private Mood negativeMood;

    public Engine() {
        super();

        emotionalElements = new HashMap<String, Element>();
        receivedMessages = new ArrayBlockingQueue<String>(50);
        emotionalOtherIds = new HashSet<IComponentIdentifier>();

        initializeMoods();

        // Thread used for emotional intensity decay calls
        new Thread(this).start();

    }

    /**
     * Infinite loop thread used to periodically
     * call emotion's decay method and to calculate agent's moods
     */
    public void run() {

        while (true) {

            // Sleep for decay delay (default or specified in ADF)
            try {
                Thread.sleep(decayDelay);
            } catch (InterruptedException e) {
                System.out.println("Decay thread sleep exception: " + e.getMessage());
            }

            // Check if engine was initialized
            if (isInitialized) {

                // System.out.println("Fire belief change " + agentName);

                // Calculate mood intensity
                positiveMood.calculateIntensity(this);
                negativeMood.calculateIntensity(this);

                // Decay intensity of all emotions
                decayEmotions(R.GOAL);
                decayEmotions(R.PLAN);
                decayEmotions(R.BELIEF);
                decayEmotions(R.BELIEF_SET_BELIEF);

                // Notify listeners that emotion intensities have changed
                fireBeliefChange();
            }
        }

    }

    private void decayEmotions(int type) {

        // Get elements of specified type
        Element[] elements = getElements(type);

        // Iterate elements
        for (Element element : elements) {

            // Get objectValue emotions
            Set<Emotion> emotions = element.getEmotions();

            // Iterate emotions
            for (Emotion emotion : emotions) {

                // Decay emotion intensity
                // Decay steps is parameter of decay function which changes (length of decay from cca 1 to cca 0)
                emotion.decayIntensity(decaySteps);

            }
        }
    }

    private void initializeMoods() {

        positiveMood = new Positive();
        positiveMood.setEmotionId(R.POSITIVE);
        positiveMood.setEmotionName(R.EMOTIONAL_IDS_NAMES.get(R.POSITIVE));
        positiveMood.setEmotionPositive(true);
        positiveMood.setIntensity(0);

        negativeMood = new Negative();
        negativeMood.setEmotionId(R.NEGATIVE);
        negativeMood.setEmotionName(R.EMOTIONAL_IDS_NAMES.get(R.NEGATIVE));
        negativeMood.setEmotionPositive(true);
        negativeMood.setIntensity(0);

    }

    /**
     * Add objectValue to engine
     *
     * @param name objectValue name (e.g. test_goal) as specified in ADF
     *             for every emotional goal, plan or belief
     * @param type objectValue type specified in R (e.g. R.GOAL)
     */
    public void addElement(String name, int type) {

        Element element = new Element(name, type);

        emotionalElements.put(name, element);

        element.addPropertyChangeListener(new PropertyChangeListener() {

            public void propertyChange(PropertyChangeEvent evt) {
                fireBeliefChange();
            }
        });

    }

    /**
     * Add belief set belief objectValue to engine
     * (overloaded method with additional parameter)
     *
     * @param name                objectValue name (e.g. test_goal) as specified in ADF
     *                            for every emotional goal, plan or belief
     * @param type                objectValue type specified in R (e.g. R.GOAL)
     * @param parentBeliefSetName name of belief set to which belief belongs
     */
    public void addElement(String name, int type, String parentBeliefSetName) {

        Element element = new Element(name, type);
        element.setParentBeliefSetName(parentBeliefSetName);

        emotionalElements.put(name, element);

        element.addPropertyChangeListener(new PropertyChangeListener() {

            public void propertyChange(PropertyChangeEvent evt) {
                fireBeliefChange();
            }
        });

    }

    /**
     * Get objectValue specified by its name and type.
     * Element can be goal, plan or belief mapped from
     * agent ADF.
     *
     * @param name objectValue name (e.g. test_goal) as specified in ADF
     *             for every emotional goal, plan or belief
     * @param type objectValue type specified in R (e.g. R.GOAL)
     * @return Element instance
     */
    public Element getElement(String name, int type) {

        Element result;

        result = emotionalElements.get(name);

        if (result != null) {
            if (result.getType() == type) {
                return result;
            } else {
                return null;
            }
        } else {
            return null;
        }
    }

    /**
     * Remove objectValue from engine
     *
     * @param name objectValue name (e.g. test_goal) as specified in ADF
     *             for every emotional goal, plan or belief
     * @param type objectValue type specified in R (e.g. R.GOAL)
     * @return true if objectValue was removed
     */
    public boolean removeElement(String name, int type) {

        Element result;

        result = emotionalElements.get(name);

        if (result != null) {
            if (result.getType() == type) {

                emotionalElements.remove(name);
                fireBeliefChange();
                return true;

            } else {
                return false;
            }
        } else {
            return false;
        }

    }

    /**
     * Check if engine contains objectValue
     *
     * @param name objectValue name (e.g. test_goal) as specified in ADF
     *             for every emotional goal, plan or belief
     * @param type objectValue type specified in R (e.g. R.GOAL)
     * @return true if engine contains objectValue, otherwise false
     */
    public boolean isElement(String name, int type) {

        boolean result = false;

        Set<String> keys = emotionalElements.keySet();

        for (String key : keys) {
            if (emotionalElements.get(key).getType() == type) {
                if (emotionalElements.get(key).getName().equals(name)) {
                    result = true;
                }
            }
        }

        return result;

    }

    /**
     * Get all objectValue names of specified type stored in engine
     *
     * @param type objectValue type specified in R (e.g. R.GOAL)
     * @return Array of objectValue names
     */
    public String[] getElementsNames(int type) {

        List<String> names = new ArrayList<String>();

        Set<String> keys = emotionalElements.keySet();

        if (keys.isEmpty()) {

            return null;

        } else {

            for (String key : keys) {

                if (emotionalElements.get(key).getType() == type) {
                    names.add(key);
                }
            }

            return names.toArray(new String[names.size()]);

        }
    }

    /**
     * Get all objectValue instances of specified type stored in engine
     *
     * @param type objectValue type specified in R (e.g. R.GOAL)
     * @return Array of objectValue instances
     */
    public Element[] getElements(int type) {

        List<Element> elements = new ArrayList<Element>();

        String[] elementNames = getElementsNames(type);

        if (elementNames != null && elementNames.length > 0) {

            for (String elementName : elementNames) {

                elements.add(getElement(elementName, type));

            }
        }

        return elements.toArray(new Element[elements.size()]);
    }

    /**
     * Check if engine was set as initialized.
     *
     * @return true if engine is initialized
     */
    public boolean isInitialized() {
        return isInitialized;
    }

    /**
     * Set engine as initialized
     *
     * @param initialized boolean objectValue of engine being initialized
     */
    public void setInitialized(boolean initialized) {
        isInitialized = initialized;
    }

    /**
     * Get name of agent, which uses this emotional engine
     *
     * @return name of agent
     */
    public String getAgentName() {
        return agentName;
    }

    /**
     * Set name of agent, which uses this emotional engine
     *
     * @param agentName name of agent (as specified in ADF)
     */
    public void setAgentName(String agentName) {
        this.agentName = agentName;
    }

    /**
     * Get delay between two consecutive calls to emotions'
     * decay methods
     *
     * @return delay in milliseconds
     */
    public int getDecayDelay() {
        return decayDelay;
    }

    /**
     * Set delay between two consecutive calls to emotions'
     * decay methods. (Used when custom delay is specified in ADF
     * as parameter of InitializeEmotionalEnginePlan)
     *
     * @param decayDelay delay in milliseconds
     */
    public void setDecayDelay(int decayDelay) {
        this.decayDelay = decayDelay;
    }

    /**
     * Get number of decay steps (number of calls to emotions' decay method)
     * needed to achieve negligible emotional intensity values
     *
     * @return number of decay steps
     */
    public int getDecaySteps() {
        return decaySteps;
    }

    /**
     * Set number of decay steps (number of calls to emotions' decay method)
     * needed to achieve negligible emotional intensity values
     * (Used when custom decay step number is specified in ADF
     * as parameter of InitializeEmotionalEnginePlan)
     *
     * @param decaySteps number of decay steps
     */
    public void setDecaySteps(int decaySteps) {
        this.decaySteps = decaySteps;
    }

    /**
     * Add received message to engine for purpose
     * of later displaying by EngineGui
     *
     * @param receivedMessage content string of received message
     */
    public synchronized void addReceivedMessage(String receivedMessage) {
        try {
            receivedMessages.add(receivedMessage);
        } catch (IllegalStateException e) {

        }
    }

    /**
     * Get next received message for purpose of
     * its displaying by EngineGui
     *
     * @return content string of received message
     */
    public String getReceivedMessage() {
        return receivedMessages.poll();
    }

    /**
     * Get component identifiers of other platform's
     * emotional agents, which were mapped because their
     * name was equal to names specified in ADF as possible
     * other agents (parameter set of InitializeEmotionalEnginePlan)
     *
     * @return component identifiers of other known emotional agents on current platform
     */
    public Set<IComponentIdentifier> getEmotionalOtherIds() {
        return emotionalOtherIds;
    }

    /**
     * Get positive mood object
     *
     * @return positive mood object
     */
    public Mood getPositiveMood() {
        return positiveMood;
    }

    /**
     * Get negative mood object
     *
     * @return negative mood object
     */
    public Mood getNegativeMood() {
        return negativeMood;
    }

    public Object getAgentObject() {
        return agentObject;
    }

    public void setAgentObject(Object agentObject) {
        this.agentObject = agentObject;
    }
}
