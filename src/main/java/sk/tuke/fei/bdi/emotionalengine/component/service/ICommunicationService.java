package sk.tuke.fei.bdi.emotionalengine.component.service;

import jadex.commons.future.IFuture;
import sk.tuke.fei.bdi.emotionalengine.component.Element;

import java.util.Map;

/**
 *
 * This interface represents communication service of JBDIEmo. Agent, which should interact with other agents needs to
 * provide this service in his definition. Sender uses provided service of receiver and calls its methods to retrieve some
 * informations about him.
 *
 * @author Peter Zemianek
 */
public interface ICommunicationService {

    /**
     * Other agents can send message by calling this method on receiver's Communication Service
     * @param message received message
     * @return Future result of successful or unsuccessful receive
     */
    IFuture<Void> messageReceived(Map<String, String> message);

    /**
     * Provides emotional representation of BDI element in agent definition.
     * @param elementName Name of BDI element
     * @param type Type of element : R.GOAL, R.PLAN, R.BELIEF
     * @return Instance of emotional BDI element
     */
    Element getElement(String elementName, int type);

    /**
     * Provides array emotional BDI elements, based on specific BDI type.
     * @param type Type of elements: R.GOAL, R.PLAN, R.BELIEF
     * @return Array of emotional BDI elements
     */
    Element[] getElements(int type);

    /**
     * Provides intensity of positive mood.
     * @return Intensity of positive mood.
     */
    Double getPositiveMood();

    /**
     * Provides intensity of negative mood.
     * @return Intensity of positive mood.
     */
    Double getNegativeMood();

    /**
     * Check if remote agent was already initialized.
     * @return Information about initialization of remote agent.
     */
    Boolean isInitialized();

}
