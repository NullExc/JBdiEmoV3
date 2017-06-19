package sk.tuke.fei.bdi.emotionalengine.belief;

import sk.tuke.fei.bdi.emotionalengine.component.JadexBeliefChangeDetectionSupport;

/**
 * @author Tomáš Herich
 * @author Peter Zemianek
 */
public class EmotionalBelief extends JadexBeliefChangeDetectionSupport {

    private String name;
    private String parent;
    private boolean isFamiliar;
    private boolean isAttractive;
    private double attractionIntensity;

    public EmotionalBelief(String name) {
        this.name = name;
    }

    public EmotionalBelief(String name, String parent, boolean familiar, boolean attractive, double attractionIntensity) {
        this.name = name;
        this.parent = parent;
        isFamiliar = familiar;
        isAttractive = attractive;
        this.attractionIntensity = attractionIntensity;
    }

    /**
     * Get name of Emotional Belief
     *
     * @return Name of emotional belief
     */
    public String getName() {
        return name;
    }

    /**
     * Get name of BeliefSet which contains this Belief
     *
     * @return Name of parent Beliefset
     */
    public String getParent() {
        return parent;
    }

    /**
     * Get familiarity property value
     *
     * @return Familiarity boolean value (familiar, not familiar)
     */
    public Boolean isFamiliar() {
        return isFamiliar;
    }

    /**
     * Get attraction property value
     *
     * @return Attraction boolean value (attractive, not attractive)
     */
    public Boolean isAttractive() {
        return isAttractive;
    }

    /**
     * Get attraction intensity property value
     *
     * @return Intensity double value always belongs to interval &lt;0,1>
     */
    public Double getAttractionIntensity() {
        return attractionIntensity;
    }

    /**
     * Update EmotionalBelief property values and fire belief change event.
     * You can set values you want to change or NULL in case you want to preserve original values.
     * Value of attraction intensity belongs to interval &lt;0,1> so even if you set different value
     * it will be set to interval min or max.
     *
     * @param familiar set familiarity (boolean), or NULL when you don't want to change original property value
     * @param attractive set attractiveness (boolean), or NULL when you don't want to change original property value
     * @param attractionIntensity  set belief attraction intensity (double), or NULL when you don't want to change original property value
     */
    public void updateBelief(Boolean familiar, Boolean attractive, Double attractionIntensity) {

        if (familiar != null) {
            setFamiliar(familiar);
        }

        if (attractive != null) {
            setAttractive(attractive);
        }

        if (attractionIntensity != null) {
            setAttractionIntensity(attractionIntensity);
        }

        fireBeliefChange();
    }

    private void setAttractionIntensity(double attractionIntensity) {

        // Value must belong to interval <0,1>
        if (attractionIntensity < 0) {
            attractionIntensity = 0;
        }

        // Value must belong to interval <0,1>
        if (attractionIntensity > 1) {
            attractionIntensity = 1;
        }
        this.attractionIntensity = attractionIntensity;
    }

    private void setAttractive(boolean attractive) {
        isAttractive = attractive;
    }

    private void setFamiliar(boolean familiar) {
        isFamiliar = familiar;
    }

}
