package sk.tuke.fei.bdi.emotionalengine.helper;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * @author Tomáš Herich
 */

public class MyTime {

    public static String currentTimeString() {

        Calendar cal = Calendar.getInstance();
        cal.getTime();
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        return sdf.format(cal.getTime());

    }

    public static String currentTimeString(String timeFormat) {

        Calendar cal = Calendar.getInstance();
        cal.getTime();
        SimpleDateFormat sdf = new SimpleDateFormat(timeFormat);
        return sdf.format(cal.getTime());

    }
}
