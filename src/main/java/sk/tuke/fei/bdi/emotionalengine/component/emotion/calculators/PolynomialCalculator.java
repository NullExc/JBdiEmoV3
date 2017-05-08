package sk.tuke.fei.bdi.emotionalengine.component.emotion.calculators;

/**
 * @author Tomáš Herich
 */

public class PolynomialCalculator implements Calculator {

    private int power;

    public PolynomialCalculator(int power) {

        this.power = power;

    }

    public String getName() {
        return "Polynomial calculator";
    }

    public double calculateValue(double value) {
        return  Math.pow(value, power);
    }

}