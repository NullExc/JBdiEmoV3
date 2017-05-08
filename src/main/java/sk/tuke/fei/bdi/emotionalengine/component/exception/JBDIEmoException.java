package sk.tuke.fei.bdi.emotionalengine.component.exception;

/**
 * @author Peter Zemianek
 */

public class JBDIEmoException extends Exception {

    public JBDIEmoException() {

    }

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
