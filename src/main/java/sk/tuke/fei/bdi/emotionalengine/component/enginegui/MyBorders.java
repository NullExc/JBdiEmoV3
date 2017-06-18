package sk.tuke.fei.bdi.emotionalengine.component.enginegui;

/*

   Created with IntelliJ IDEA.

   Bc. Tomáš Herich
   --------------------------- 
   18. 02. 2013
   10:40 PM
   
*/

import sk.tuke.fei.bdi.emotionalengine.res.R;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;

public class MyBorders {

    public static Border getLineBorder(Color color) {
        return BorderFactory.createLineBorder(color);
    }

    public static Border getLineBorder() {
        return BorderFactory.createLineBorder(Color.GRAY);
    }

    public static Border getEmptyBorder() {

        int s = R.GUI_SPACING;

        return BorderFactory.createEmptyBorder(s, s, s, s);
    }

    public static Border getEmptyLineBorder() {
        return  BorderFactory.createCompoundBorder(getLineBorder(), getEmptyBorder());
    }

}
