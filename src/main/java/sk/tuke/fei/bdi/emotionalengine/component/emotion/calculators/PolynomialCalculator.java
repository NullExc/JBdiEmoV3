package sk.tuke.fei.bdi.emotionalengine.component.emotion.calculators;

/*

   Created with IntelliJ IDEA.

   Bc. Tomáš Herich
   ---------------------------
   02. 02. 2013
   9:32 PM

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