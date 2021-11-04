package security.errorhandling;

/**
 *
 * @author lam@cphbusiness.dk
 */
public class AuthenticationException extends Exception{

    public AuthenticationException(String message) {
        super(message);
    }

    public AuthenticationException() {
        super("{\"msg\": \"Could not be authenticated\"}");
    }  
}
