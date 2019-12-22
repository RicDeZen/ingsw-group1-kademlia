package ingsw.group1.kademlia.exceptions;

/**
 * Exception meant to be thrown when a PendingRequest is used after being canceled
 * @author Riccardo De Zen
 */
public class CanceledRequestException extends IllegalStateException {
    /**
     * @param message Message for this Exception
     */
    public CanceledRequestException(String message){
        super(message);
    }
    public CanceledRequestException(){super();}
}
