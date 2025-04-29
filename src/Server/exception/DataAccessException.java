package Server.exception;

public class DataAccessException extends RuntimeException {
    public DataAccessException(String message) {
       super(message);
    }
}
