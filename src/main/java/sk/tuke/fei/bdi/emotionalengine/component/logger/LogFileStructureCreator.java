package sk.tuke.fei.bdi.emotionalengine.component.logger;

import javanet.staxutils.IndentingXMLStreamWriter;
import sk.tuke.fei.bdi.emotionalengine.component.Element;
import sk.tuke.fei.bdi.emotionalengine.component.Engine;
import sk.tuke.fei.bdi.emotionalengine.component.emotion.Emotion;
import sk.tuke.fei.bdi.emotionalengine.res.R;

import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.Set;

/**
 * @author Tomáš Herich
 */

public class LogFileStructureCreator {

    private File file;
    private Engine engine;
    private IndentingXMLStreamWriter xsw;

    public LogFileStructureCreator(File file, Engine engine) {

        this.file = file;
        this.engine = engine;

    }

    public void createLogFileStructure() {

        try {

            createAgentStructure();

        } catch (Exception e) {

            System.out.println(getClass().getSimpleName() + " - " + e.getMessage());

        }

    }

    private void createAgentStructure() throws FileNotFoundException, XMLStreamException {

        FileOutputStream fileOutputStream = new FileOutputStream(file, false);
        XMLOutputFactory factory = XMLOutputFactory.newInstance();
        xsw = new IndentingXMLStreamWriter(factory.createXMLStreamWriter(fileOutputStream, "UTF-8"));
        xsw.setIndent("    ");

        xsw.writeStartDocument("UTF-8", "1.0");

        xsw.writeCharacters("\n");
        xsw.writeStartElement("agent");
        xsw.writeAttribute("name", engine.getAgentName());
        xsw.writeCharacters("\n");

        xsw.writeStartElement("elementtypes");

        xsw.writeStartElement("goals");
        createElementTypeStructure(R.GOAL);
        xsw.writeEndElement();

        xsw.writeStartElement("plans");
        createElementTypeStructure(R.PLAN);
        xsw.writeEndElement();

        xsw.writeStartElement("beliefs");
        createElementTypeStructure(R.BELIEF);
        xsw.writeEndElement();

        xsw.writeStartElement("beliefsets");
        createElementTypeStructure(R.BELIEF_SET);
        xsw.writeEndElement();

        xsw.writeEndElement();

        xsw.writeCharacters("\n\n");
        xsw.writeEndDocument();
        xsw.close();

    }

    private void createElementTypeStructure(int elementType) throws XMLStreamException {

        Element[] elements = engine.getElements(elementType);

        for (Element element : elements) {

            String engineElementName = element.getName().replace(" ", "_");

            if (elementType != R.BELIEF_SET) {

                xsw.writeStartElement(R.EMOTIONAL_ELEMENT_TYPE_NAMES.get(elementType).replace(" ", ""));
                xsw.writeAttribute("name", engineElementName);
                createElementEmotionStructure(element);
                xsw.writeEndElement();

            } else {

                xsw.writeStartElement(R.EMOTIONAL_ELEMENT_TYPE_NAMES.get(elementType).replace(" ", ""));
                xsw.writeAttribute("name", engineElementName);
                createElementTypeStructure(R.BELIEF_SET_BELIEF);
                xsw.writeEndElement();

            }

        }

    }

    private void createElementEmotionStructure(Element element) throws XMLStreamException {

        Set<Emotion> emotions = element.getEmotions();

        for (Emotion emotion : emotions) {

            String emotionName = emotion.getEmotionName().replace(" ", "_");

            xsw.writeStartElement("emotion");
            xsw.writeAttribute("name", emotionName);
            xsw.writeEndElement();

        }

    }

}
