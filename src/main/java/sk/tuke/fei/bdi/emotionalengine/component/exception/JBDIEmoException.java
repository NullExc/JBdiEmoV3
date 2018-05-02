package sk.tuke.fei.bdi.emotionalengine.component.exception;

/**
 *
 * This exception throws when user doesn't define Emotional Agent in the correct way.
 *
 * The cases when the exception throws:
 *
 *  1. The 'JBDIEmoAgent' annotation is missing.
 *  2. The 'engine' belief is missing.
 *  3. THe 'ServiceNotFoundException' service was thrown in case of missing 'ICommunicationService'
 *  4. The 'InitializeEmotionalEnginePlan' plan is not defined in BDI configuration.
 *
 * @author Peter Zemianek
 */

public class JBDIEmoException extends Exception {

    public JBDIEmoException() { }

    public JBDIEmoException(String message) {
        super(message);
    }

    public JBDIEmoException(String message, Throwable cause) {
        super(message, cause);
    }

    public JBDIEmoException(Throwable cause) {
        super(cause);
    }

}
