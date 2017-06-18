package sk.tuke.fei.bdi.emotionalengine.example.hungrypaul.belief;

import sk.tuke.fei.bdi.emotionalengine.belief.EmotionalBelief;

/**
 * Created by PeterZemianek on 6/17/2017.
 */
public class MyFoodEmotionalBelief extends EmotionalBelief {

    private Double nutritionValue = 0d;
    private boolean eaten = false;
    private boolean healthy = false;

    public MyFoodEmotionalBelief(String name) {
        super(name);
    }

    public MyFoodEmotionalBelief(String name, String parent, boolean familiar, boolean attractive, double attractionIntensity) {
        super(name, parent, familiar, attractive, attractionIntensity);
    }


    public Double getNutritionValue() {
        return nutritionValue;
    }

    public void setNutritionValue(Double nutritionValue) {
        this.nutritionValue = nutritionValue;
    }

    public boolean isHealthy() {
        return healthy;
    }

    public void setHealthy(boolean healthy) {
        this.healthy = healthy;
    }

    public boolean isEaten() {
        return eaten;
    }

    public void setEaten(boolean eaten) {
        this.eaten = eaten;
    }


}
