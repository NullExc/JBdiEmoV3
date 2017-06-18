package sk.tuke.fei.bdi.emotionalengine.example.hungrypaul.belief;

import sk.tuke.fei.bdi.emotionalengine.example.hungrypaul.help.MyFood;

/**
 * Created by PeterZemianek on 6/17/2017.
 */
public class Fridge {

    public Fridge() {

    }

    public MyFoodEmotionalBelief getFood(String parent, boolean isHealthy) {


        String foodName;

        if (isHealthy) {
            foodName = MyFood.getInstance().getRandomSetValue(MyFood.HEALTHY_FOOD_NAMES);
        } else {
            foodName = MyFood.getInstance().getRandomSetValue(MyFood.UNHEALTHY_FOOD_NAMES);
        }


        if (MyFood.getInstance().getHealthyValue(foodName)) {


            boolean isFoodAttractive = Math.random() > 0.1;
            String foodAdjective;
            if (isFoodAttractive) {
                foodAdjective = MyFood.getInstance().getRandomSetValue(MyFood.POSITIVE_ADJECTIVES);
            } else {
                foodAdjective = MyFood.getInstance().getRandomSetValue(MyFood.NEGATIVE_ADJECTIVES);
            }

            String foodId = MyFood.getInstance().getNextFoodId();

            String foodWholeName = foodId + " " + foodAdjective + " " + foodName;

            MyFoodEmotionalBelief foodBelief = new MyFoodEmotionalBelief(foodWholeName, parent, false, isFoodAttractive, MyFood.getInstance().getAttractionValue(foodName));

            foodBelief.setHealthy(MyFood.getInstance().getHealthyValue(foodName));
            foodBelief.setNutritionValue(MyFood.getInstance().getNutritionValue(foodName));
            foodBelief.setEaten(false);


            return foodBelief;

        } else {

            return null;

        }

    }
}
