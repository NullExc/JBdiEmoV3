package sk.tuke.fei.bdi.emotionalengine.component.enginegui;

/*

   Created with IntelliJ IDEA.

   Bc. Tomáš Herich
   --------------------------- 
   18. 02. 2013
   11:05 PM
   
*/

import jadex.commons.beans.PropertyChangeEvent;
import jadex.commons.beans.PropertyChangeListener;
import sk.tuke.fei.bdi.emotionalengine.component.Element;
import sk.tuke.fei.bdi.emotionalengine.component.Engine;
import sk.tuke.fei.bdi.emotionalengine.component.emotion.Emotion;
import sk.tuke.fei.bdi.emotionalengine.helper.MyMath;
import sk.tuke.fei.bdi.emotionalengine.res.R;

import javax.swing.*;
import java.awt.*;
import java.util.*;

//import java.beans.PropertyChangeEvent;
//import java.beans.PropertyChangeListener;

public class ContentPanelEmotion extends JPanel {

    private Engine engine;
    private Element element;


    public ContentPanelEmotion(Engine engine, Element element) {
        this.engine = engine;
        this.element = element;

        // Borders and layout
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBorder(MyBorders.getEmptyBorder());

        // Small preferred size used for GridBagLayout weight to work as needed
        // weights distribute extra free space in ratio specified
        // when component has small preferred size, there is more extra free space
        // so weights have bigger impact on layout
        setPreferredSize(new Dimension(100, 100));

        createEmotionPanelHolder();
    }

    private void createEmotionPanelHolder() {

        // Get possible element emotions
        Set<Emotion> emotions = element.getEmotions();

        // Check if element possible emotions are valid
        if (emotions != null && !emotions.isEmpty()) {

            // Create emotion map which will be used for sorting
            Map<Integer, Emotion> emotionIdMap = new HashMap<Integer, Emotion>();

            // Emotion sorting based on their ID ( can be changed in centralized resources R)
            for (Emotion emotion : emotions) {
                emotionIdMap.put(emotion.getEmotionId(), emotion);
            }

            // Sort emotions by their ID
            SortedSet<Integer> sortedEmotionIds = new TreeSet<Integer>(emotionIdMap.keySet());

            // Iterate sorted emotions
            for (Integer emotionId : sortedEmotionIds) {

                // Get emotion
                final Emotion emotion = emotionIdMap.get(emotionId);

                // Create components (name, intensity, intensity meter)
                JPanel emotionPanel = createEmotionPanel();
                JLabel emotionName = createEmotionLabel(emotion);
                final JLabel emotionIntensity = createIntensityLabel();
                final JProgressBar emotionIntensityMeter = createIntensityMeter(emotion);

                // Create progress bar panel
                JPanel progressBarPanel = new JPanel();
                progressBarPanel.setLayout(new BoxLayout(progressBarPanel, BoxLayout.Y_AXIS));
                progressBarPanel.setBorder(MyBorders.getLineBorder());
                progressBarPanel.add(emotionIntensityMeter);

                // Add components to main panel
                emotionPanel.add(emotionName);
                emotionPanel.add(emotionIntensity);
                emotionPanel.add(progressBarPanel);

                // Bind components to data source
                // Update values when emotion values change
                engine.addPropertyChangeListener(new PropertyChangeListener() {
                    @Override
                    public void propertyChange(PropertyChangeEvent evt) {

                        emotionIntensity.setText("" + MyMath.roundDouble(emotion.getIntensity(), 4));
                        emotionIntensityMeter.setValue((int) (MyMath.roundDouble(emotion.getIntensity() * 100, 4)));

                    }
                });

                // Add particular emotion to emotion panel
                add(emotionPanel);
            }
        }
    }

    private JPanel createEmotionPanel() {

        JPanel panel = new JPanel();
        panel.setBorder(MyBorders.getEmptyBorder());

        GridLayout grid = new GridLayout(1, 3);
        grid.setHgap(R.GUI_SPACING);
        panel.setLayout(grid);

        return  panel;
    }

    private JLabel createEmotionLabel(Emotion emotion) {

        JLabel nameLabel = new JLabel(emotion.getEmotionName().toUpperCase());
        nameLabel.setBorder(MyBorders.getLineBorder());
        nameLabel.setHorizontalAlignment(SwingConstants.CENTER);
        nameLabel.setFont(new Font("SansSerif", Font.BOLD, 12));
        nameLabel.setForeground((Color) emotion.getColor());

        return nameLabel;
    }

    private JLabel createIntensityLabel() {

        JLabel intensity = new JLabel();
        intensity.setBorder(MyBorders.getLineBorder());
        intensity.setHorizontalAlignment(SwingConstants.CENTER);
        intensity.setText("0.0");

        return intensity;
    }

    private JProgressBar createIntensityMeter(Emotion emotion) {

        JProgressBar intensityMeter = new JProgressBar();
        intensityMeter.setStringPainted(false);
        intensityMeter.setBorderPainted(false);
        intensityMeter.setForeground((Color) emotion.getColor());
        intensityMeter.setPreferredSize(new Dimension(1000,1000));

        return intensityMeter;
    }


}
