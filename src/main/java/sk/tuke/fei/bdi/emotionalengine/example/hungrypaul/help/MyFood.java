package sk.tuke.fei.bdi.emotionalengine.example.hungrypaul.help;

/**
 * Created by PeterZemianek on 5/14/2017.
 */
import java.util.*;

public class MyFood {


    private static MyFood instance = new MyFood();

    private Map<String, Double> nutritionValues;
    private Map<String, Double> attractionValues;
    private Map<String, Boolean> healthyValues;
    private Set<String> positiveAdjectives;
    private Set<String> negativeAdjectives;
    private Set<String> healthyFoodNames;
    private Set<String> unhealthyFoodNames;

    public static final int HEALTHY_FOOD_NAMES = 0;
    public static final int UNHEALTHY_FOOD_NAMES = 1;
    public static final int POSITIVE_ADJECTIVES = 2;
    public static final int NEGATIVE_ADJECTIVES = 2;

    private int foodId = 0;

    private MyFood() {

        initializeHealthyFoodNames();
        initializeUnhealthyFoodNames();
        initializeNutritionValues();
        initializeAttractionValues();
        initializeHealthyValues();
        initializePositiveAdjectives();
        initializeNegativeAdjectives();

    }

    public static MyFood getInstance() {

        if (instance == null) {

            instance = new MyFood();

        }

        return instance;
    }

    private void initializeHealthyFoodNames() {

        healthyFoodNames = new HashSet<String>();
        healthyFoodNames.add("tofu");
        healthyFoodNames.add("tomato");
        healthyFoodNames.add("yogurt");
        healthyFoodNames.add("fruit salad");
        healthyFoodNames.add("apple");

    }

    private void initializeUnhealthyFoodNames() {

        unhealthyFoodNames = new HashSet<String>();
        unhealthyFoodNames.add("cake");
        unhealthyFoodNames.add("bacon");
        unhealthyFoodNames.add("cookie");
        unhealthyFoodNames.add("pizza");
        unhealthyFoodNames.add("hamburger");

    }

    private void initializeNutritionValues() {

        nutritionValues = new HashMap<String, Double>();
        nutritionValues.put("cake", 0.2);
        nutritionValues.put("bacon", 0.3);
        nutritionValues.put("cookie", 0.05);
        nutritionValues.put("pizza", 0.4);
        nutritionValues.put("hamburger", 0.3);
        nutritionValues.put("tofu", 0.25);
        nutritionValues.put("tomato", 0.05);
        nutritionValues.put("yogurt", 0.1);
        nutritionValues.put("fruit salad", 0.15);
        nutritionValues.put("apple", 0.05);

    }

    private void initializeAttractionValues() {

        attractionValues = new HashMap<String, Double>();
        attractionValues.put("cake", 0.9);
        attractionValues.put("bacon", 1.0);
        attractionValues.put("cookie", 0.8);
        attractionValues.put("pizza", 0.8);
        attractionValues.put("hamburger", 0.5);
        attractionValues.put("tofu", 0.3);
        attractionValues.put("tomato", 0.2);
        attractionValues.put("yogurt", 0.2);
        attractionValues.put("fruit salad", 0.3);
        attractionValues.put("apple", 0.3);

    }

    private void initializeHealthyValues() {

        healthyValues = new HashMap<String, Boolean>();
        healthyValues.put("cake", false);
        healthyValues.put("bacon", false);
        healthyValues.put("cookie", false);
        healthyValues.put("pizza", false);
        healthyValues.put("hamburger", false);
        healthyValues.put("tofu", true);
        healthyValues.put("tomato", true);
        healthyValues.put("yogurt", true);
        healthyValues.put("fruit salad", true);
        healthyValues.put("apple", true);

    }

    private void initializePositiveAdjectives() {

        positiveAdjectives = new HashSet<String>();
        positiveAdjectives.add("tasty");
        positiveAdjectives.add("nice");
        positiveAdjectives.add("big");
        positiveAdjectives.add("small");
        positiveAdjectives.add("fresh");
        positiveAdjectives.add("good looking");
        positiveAdjectives.add("rich");
        positiveAdjectives.add("aromatic");
        positiveAdjectives.add("classic");
        positiveAdjectives.add("delicious");
        positiveAdjectives.add("velvety");

    }

    private void initializeNegativeAdjectives() {

        negativeAdjectives = new HashSet<String>();
        negativeAdjectives.add("old");
        negativeAdjectives.add("weird");
        negativeAdjectives.add("smelly");
        negativeAdjectives.add("unattractive");
        negativeAdjectives.add("harsh");

    }

    public String getRandomSetValue(int valueType) {

        Set<String> workingSet;

        if (valueType == HEALTHY_FOOD_NAMES) {
            workingSet = healthyFoodNames;
        } else if (valueType == UNHEALTHY_FOOD_NAMES) {
            workingSet = unhealthyFoodNames;
        } else if (valueType == POSITIVE_ADJECTIVES) {
            workingSet = positiveAdjectives;
        } else if (valueType == NEGATIVE_ADJECTIVES) {
            workingSet = negativeAdjectives;
        } else {
            workingSet = null;
        }

        int size = workingSet.size();
        int item = new Random().nextInt(size);
        int i = 0;
        for(String result : workingSet)
        {
            if (i == item)
                return result;
            i++;
        }

        return null;
    }

    public double getAttractionValue(String foodName) {

        return attractionValues.get(foodName);

    }

    public double getNutritionValue(String foodName) {

        return nutritionValues.get(foodName);

    }

    public boolean getHealthyValue(String foodName) {

        return healthyValues.get(foodName);

    }

    public synchronized String getNextFoodId() {

        foodId++;

        return "[id: " + foodId + "]";

    }

}

