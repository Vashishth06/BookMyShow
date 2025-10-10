package MyFirstProject.demo.Exceptions;

public class InvalidUserException extends Exception {
    public InvalidUserException(String message) {
        super(message);
    }
}
