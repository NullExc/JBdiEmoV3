package sk.tuke.fei.bdi.emotionalengine.component.enginegui;

/*

   Created with IntelliJ IDEA.

   Bc. Tomáš Herich
   --------------------------- 
   18. 02. 2013
   11:24 PM
   
*/

import sk.tuke.fei.bdi.emotionalengine.component.emotionalevent.EmotionalEvent;
import sk.tuke.fei.bdi.emotionalengine.helper.MyMath;
import sk.tuke.fei.bdi.emotionalengine.helper.MyTime;
import sk.tuke.fei.bdi.emotionalengine.res.R;

import javax.swing.*;
import java.awt.*;

public class ContentPanelEvent extends JPanel {

    private EmotionalEvent event;
    private int eventNumber;

    public ContentPanelEvent(EmotionalEvent event, int eventNumber) {

        this.event = event;
        this.eventNumber = eventNumber;

        // Borders and layout
        setBorder(MyBorders.getEmptyLineBorder());
        GridBagLayout grid = new GridBagLayout();
        setLayout(grid);

        createEventPanel();
    }

    private void createEventPanel() {

        createBasicInfoPanel(event);
        createUserParametersPanel(event);
        createSystemParametersPanel(event);
        createBeliefClassParametersPanel(event);

    }

    private void createBasicInfoPanel(EmotionalEvent event) {

        JPanel basicInfo = new JPanel(new GridLayout(0, 2));
        basicInfo.setOpaque(false);

        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 0;
        c.weightx = 1;
        c.weighty = 1;
        c.fill = GridBagConstraints.HORIZONTAL;
        add(basicInfo, c);

        basicInfo.add(new JLabel("Event " + eventNumber + ": "));
        basicInfo.add(createLabel(MyTime.currentTimeString(), SwingConstants.CENTER, SwingConstants.CENTER));
        basicInfo.add(new JLabel("Type: "));
        basicInfo.add(createLabel(R.EMOTIONAL_EVENT_TYPE_NAMES.get(event.getEventType()), SwingConstants.CENTER, SwingConstants.CENTER));
        basicInfo.add(new JLabel("Result: "));
        basicInfo.add(createLabel(R.EMOTIONAL_RESULT_TYPE_NAMES.get(event.getResultType()), SwingConstants.CENTER, SwingConstants.CENTER));

    }

    private void createUserParametersPanel(EmotionalEvent event) {

        JPanel userParams = new JPanel(new GridLayout(0, 2));
        userParams.setOpaque(false);

        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 1;
        c.weightx = 1;
        c.weighty = 1;
        c.fill = GridBagConstraints.HORIZONTAL;
        add(userParams, c);

        if (event.getUserParameters() != null) {
            for (String parameter : event.getUserParameters().keySet()) {
                userParams.add(new JLabel("User: " + R.EMOTIONAL_PARAMETER_LONG_NAMES.get(parameter) + ": "));
                userParams.add(createLabel("" + MyMath.roundDouble(event.getUserParameters().get(parameter), 4), SwingConstants.CENTER, SwingConstants.CENTER));
            }
        }

    }

    private void createSystemParametersPanel(EmotionalEvent event) {

        JPanel systemParams = new JPanel(new GridLayout(0, 2));
        systemParams.setOpaque(false);

        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 2;
        c.weightx = 1;
        c.weighty = 1;
        c.fill = GridBagConstraints.HORIZONTAL;
        add(systemParams, c);

        if (event.getSystemParameters() != null) {
            for (Integer parameter : event.getSystemParameters().keySet()) {
                systemParams.add(new JLabel("System: " + R.EMOTIONAL_IDS_NAMES.get(parameter) + ": "));
                systemParams.add(createLabel("" + MyMath.roundDouble(event.getSystemParameters().get(parameter), 4), SwingConstants.CENTER, SwingConstants.CENTER));
            }
        }

    }


    private void createBeliefClassParametersPanel(EmotionalEvent event) {

        JPanel beliefClassParams = new JPanel(new GridLayout(0, 2));
        beliefClassParams.setOpaque(false);

        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 3;
        c.weightx = 1;
        c.weighty = 1;
        c.fill = GridBagConstraints.HORIZONTAL;
        add(beliefClassParams, c);

        if (event.isBeliefFamiliar() != null) {
            beliefClassParams.add(new JLabel("Belief: " + R.EMOTIONAL_BELIEF_PARAMETER_LONG_NAMES.get(R.BELIEF_FAMILIAR) + ": "));
            beliefClassParams.add(createLabel("" + event.isBeliefFamiliar(), SwingConstants.CENTER, SwingConstants.CENTER));
        }

        if (event.isBeliefAttractive() != null) {
            beliefClassParams.add(new JLabel("Belief: " + R.EMOTIONAL_BELIEF_PARAMETER_LONG_NAMES.get(R.BELIEF_ATTRACTIVE) + ": "));
            beliefClassParams.add(createLabel("" + event.isBeliefAttractive(), SwingConstants.CENTER, SwingConstants.CENTER));
        }

        if (event.getBeliefAttractionIntensity() != null) {
            beliefClassParams.add(new JLabel("Belief: " + R.EMOTIONAL_BELIEF_PARAMETER_LONG_NAMES.get(R.BELIEF_INTENSITY) + ": "));
            beliefClassParams.add(createLabel("" + MyMath.roundDouble(event.getBeliefAttractionIntensity(), 4), SwingConstants.CENTER, SwingConstants.CENTER));
        }

    }

    private JLabel createLabel(String text, int horizontalAlignment, int verticalAlignment) {
        JLabel label = new JLabel(text);
        label.setHorizontalAlignment(horizontalAlignment);
        label.setVerticalAlignment(verticalAlignment);
        label.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createEmptyBorder(1,0,1,0), MyBorders.getLineBorder()));
        return label;
    }


}
