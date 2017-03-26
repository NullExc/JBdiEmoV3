package sk.tuke.fei.bdi.emotionalengine.helper;
/*

   Created with IntelliJ IDEA.

   Bc. Tomáš Herich
   ---------------------------
   03. 02. 2013
   12:58 AM

*/

import java.util.HashSet;
import java.util.Set;

public class MyMath {

    public static double roundDouble(double value, int decimalPlaces) {

        int decimals = (int) Math.pow(10, decimalPlaces);

        return (double) Math.round(value * decimals) / decimals;

    }

    public static double convertToPercent(double value) {
        return  roundDouble(value * 100, 2);
    }

    public static double rootMeanSquareDouble(Set<Double> values) {

        double result =0;

        for (double value : values) {

            result += Math.pow(value, 2);

        }

        result = result / values.size();
        result = Math.sqrt(result);

        return result;

    }

    public static double rootMeanSquareDouble(double a, double b) {

        double result =0;

        Set<Double> values = new HashSet<Double>();

        values.add(a);
        values.add(b);

        for (double value : values) {

            result += Math.pow(value, 2);

        }

        result = result / values.size();
        result = Math.sqrt(result);

        return result;

    }

}
