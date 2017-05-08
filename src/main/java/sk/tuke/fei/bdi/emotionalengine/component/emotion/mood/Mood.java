package sk.tuke.fei.bdi.emotionalengine.component.emotion.mood;

import sk.tuke.fei.bdi.emotionalengine.component.Engine;

/**
 * @author Tomáš Herich
 */

public abstract class Mood {

    protected double intensity;
    private int emotionId;
    private String emotionName;
    private boolean isEmotionPositive;

    protected Mood() {
        super();
    }

    public int getEmotionId() {
        return emotionId;
    }

    public void setEmotionId(int emotionId) {
        this.emotionId = emotionId;
    }

    public String getEmotionName() {
        return emotionName;
    }

    public void setEmotionName(String emotionName) {
        this.emotionName = emotionName;
    }

    public boolean isEmotionPositive() {
        return isEmotionPositive;
    }

    public void setEmotionPositive(boolean emotionPositive) {
        isEmotionPositive = emotionPositive;
    }

    public double getIntensity() {
        return intensity;
    }

    public void setIntensity(double newIntensity) {

        intensity = newIntensity;

        // Intensity must belong to interval <0,1>
        if (intensity > 1) {
            intensity = 1;
        }

    }

    public boolean isActive() {
        return intensity > 0;
    }

    // Intensity calculation is implemented for every emotion (e.g. Hope, Fear, Joy, e.t.c ... in their classes)
    public abstract void calculateIntensity(Engine engine);

}
