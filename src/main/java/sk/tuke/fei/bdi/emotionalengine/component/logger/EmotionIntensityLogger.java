package sk.tuke.fei.bdi.emotionalengine.component.logger;

import org.w3c.dom.*;
import sk.tuke.fei.bdi.emotionalengine.component.Engine;
import sk.tuke.fei.bdi.emotionalengine.component.emotion.Emotion;
import sk.tuke.fei.bdi.emotionalengine.helper.MyMath;
import sk.tuke.fei.bdi.emotionalengine.res.R;

import javax.swing.*;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileOutputStream;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Set;

/**
 * Created by Peter on 22.3.2017.
 */
public class EmotionIntensityLogger {

    private File file;
    private Engine engine;
    private Document doc;
    private Timer timer;
    private int loggingDelayMillis = 1000;

    public EmotionIntensityLogger(Integer loggingDelayMillis, File file, Engine engine) {

        this.file = file;
        this.engine = engine;

        if (loggingDelayMillis != null) {
            this.loggingDelayMillis = loggingDelayMillis;
        }

    }

    public void createEmotionIntensityLogger() {

        createDom();

        addEmotionIntensityLoggers(R.GOAL);
        addEmotionIntensityLoggers(R.PLAN);
        addEmotionIntensityLoggers(R.BELIEF);
        addEmotionIntensityLoggers(R.BELIEF_SET_BELIEF);

        createSaveTimer();

    }

    private void createDom() {

        try {

            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            doc = dBuilder.parse(file);

            // Optional, but recommended
            // Read this - http://stackoverflow.com/questions/13786607/normalization-in-dom-parsing-with-java-how-does-it-work
            doc.getDocumentElement().normalize();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void addEmotionIntensityLoggers(int engineElemType) {

        // Get engine elements of specified type i.e. all goals, plan, beliefs ...
        sk.tuke.fei.bdi.emotionalengine.component.Element[] engineElems = engine.getElements(engineElemType);

        // Iterate engine elements of a specified type
        for (sk.tuke.fei.bdi.emotionalengine.component.Element engineElem : engineElems) {


            boolean newEngineElement = true;

            // Check if engine element is valid
            if (engineElem != null) {

                // Get all xml nodes with tag of engine element type i.e. all nodes with node tag name goal, plan, belief ...
                // e.g. <goal>, <plan>, <belief> ...
                NodeList elemTypeNodes = doc.getElementsByTagName(R.EMOTIONAL_ELEMENT_TYPE_NAMES.get(engineElemType).replace(" ",""));

                // Iterate xml nodes with tag of engine element type
                for (int i = 0; i < elemTypeNodes.getLength(); i++) {

                    // Cast xml node to element
                    Element engineElemTypeElement = (Element) elemTypeNodes.item(i);

                    // If xml node attribute "name" has value of engine element name
                    // e.g. xml node <plan name="test_plan"> and engine element with name "test_plan"
                    if (engineElemTypeElement.getAttribute("name").equals(engineElem.getName())) {

                        // Belief set belief are specific case because they are added and remove dynamicaly
                        // The code that handle belief set belief is below: "if (newEngineElement)"
                        if (engineElemType != R.BELIEF_SET_BELIEF) {
                            // Add emotion intensity listeners to engine element emotions
                            // When triggered they will append xml element containing new emotion intensity and date
                            addEmotionIntensityListeners(engineElem, engineElemTypeElement);
                        }

                        // Xml node was found element is already tracked
                        newEngineElement = false;
                        break;

                    }
                }
            }

            // If engine contains engine element which is not tracked by logger add it to xml
            // Only elements that can be added at runtime are belief sets
            if (newEngineElement) {

                addNewElementToDom(engineElem);

            }
        }
    }

    private void addNewElementToDom(sk.tuke.fei.bdi.emotionalengine.component.Element engineElem) {

        // Get all xml nodes with tag of engine belief set element
        NodeList beliefSetNodes = doc.getElementsByTagName("beliefset");

        // Iterate xml nodes
        for (int i = 0; i < beliefSetNodes.getLength(); i++) {

            // Check if node is xml element type
            if (beliefSetNodes.item(i) instanceof Element) {

                // Get node and cast it to xml element type
                Element beliefSetElement = (Element) beliefSetNodes.item(i);


                // Check if engine element parent belief set is equal to xml element node name
                // (xml element node of belief set type)
                if (engineElem.getParentBeliefSetName().equals(beliefSetElement.getAttribute("name"))) {


                    // Create belief set belief xml node
                    Element beliefSetBelief = doc.createElement("beliefsetbelief");
                    beliefSetBelief.setAttribute("name", engineElem.getName());
                    beliefSetElement.appendChild(beliefSetBelief);


                    // Get engine emotions which belongs to engine element (belief set belief)
                    Set<Emotion> emotions = engineElem.getEmotions();

                    // Iterate emotions
                    for (Emotion emotion : emotions) {

                        String emotionName = emotion.getEmotionName().replace(" ", "_");

                        // Add xml emotion nodes to xml belief base belief node
                        Element beliefSetBeliefEmotion = doc.createElement("emotion");
                        beliefSetBeliefEmotion.setAttribute("name", emotionName);
                        beliefSetBelief.appendChild(beliefSetBeliefEmotion);

                    }


                    // Add emotion intensity listeners to engine element emotions
                    // When triggered they will append xml element containing new emotion intensity and date
                    addEmotionIntensityListeners(engineElem, beliefSetBelief);

                }
            }
        }
    }


    private void addEmotionIntensityListeners(sk.tuke.fei.bdi.emotionalengine.component.Element engineElem, Element engineElemTypeElement) {

        // Get all emotion xml nodes of given elements
        NodeList elementEmotions = engineElemTypeElement.getChildNodes();

        // Iterate emotion xml nodes
        for (int j = 0; j < elementEmotions.getLength(); j++) {

            // Get xml node idem
            Node emotionNode = elementEmotions.item(j);

            // Check if emotion xml node is valid
            if (emotionNode instanceof Element) {

                // Cast emotion xml node to xml element
                final Element emotionElement = (Element) emotionNode;

                // Get engine emotion id
                Integer emotionId = getEmotionIdByName(emotionElement.getAttribute("name"));

                // If emotion id is valid
                if (emotionId != null) {

                    // Get engine emotion
                    final Emotion engineEmotion = engineElem.getEmotion(emotionId);


                    // Update xml file on specified delay
                    Timer emotionTimer = new Timer(loggingDelayMillis, new ActionListener() {

                        public void actionPerformed(ActionEvent e) {

                            // Don't add xml elements when intensity equals 0 (less storage needed and less clutter)
                            if (engineEmotion.getIntensity() >= 0.0009) {

                                // Initialize simple date format (year month day _ hour minute second e.g. 20130309_161135)
                                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");

                                // Create xml element with name "intensity" which will store intensity and date
                                Element emotionIntensity = doc.createElement("intensity");

                                // Create attribute "date" with value "current date formatted with simple date formatter"
                                // in xml element with name "intensity"
                                emotionIntensity.setAttribute("date", simpleDateFormat.format(new Date()));


                                // Create xml text node with name "intensity_value"
                                Text intensityValue = doc.createTextNode("intensity_value");

                                // Round intensity value and cast it to String
                                NumberFormat numberFormat = NumberFormat.getInstance();
                                numberFormat.setMaximumFractionDigits(4);
                                numberFormat.setMinimumFractionDigits(4);
                                numberFormat.setGroupingUsed(false);
                                String intensityString = numberFormat.format(MyMath.roundDouble(engineEmotion.getIntensity(), 4));

                                // Add intensity value string to xml text node with name "intensity_value"
                                intensityValue.setNodeValue(intensityString);

                                // Add xml text node with name "intensity_value" to xml element with name "intensity"
                                emotionIntensity.appendChild(intensityValue);

                                // Add xml element with name "intensity" to emotion xml element
                                emotionElement.appendChild(emotionIntensity);

                            }

                        }
                    });

                    emotionTimer.start();

                }
            }
        }
    }

    private Integer getEmotionIdByName(String emotionName) {

        Integer emotionId = null;

        // Get all emotion ids
        Set<Integer> emotionIds = R.EMOTIONAL_IDS_NAMES.keySet();

        // Iterate emotion ids
        for (Integer emotionIdHelper : emotionIds) {

            // If name got by emotion id equals searched name
            if (R.EMOTIONAL_IDS_NAMES.get(emotionIdHelper).equals(emotionName)) {

                // Return this emotion id
                emotionId = emotionIdHelper;

                // Break cycle because result was found
                break;
            }

        }

        return emotionId;
    }

    private void saveFile() {

        try {

            // Use a Transformer for output
            TransformerFactory tFactory = TransformerFactory.newInstance();
            Transformer transformer = tFactory.newTransformer();

            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(new FileOutputStream(file, false));
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
            transformer.transform(source, result);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    private void createSaveTimer() {

        // Update xml file on specified delay
        timer = new Timer(loggingDelayMillis, new ActionListener() {

            public void actionPerformed(ActionEvent e) {

                addEmotionIntensityLoggers(R.BELIEF_SET_BELIEF);
                saveFile();

            }
        });

        timer.start();
    }

}

