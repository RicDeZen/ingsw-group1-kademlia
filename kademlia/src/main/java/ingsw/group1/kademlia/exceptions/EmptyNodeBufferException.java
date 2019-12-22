package ingsw.group1.kademlia.exceptions;

/**
 * Exception meant to be thrown if a PendingRequest finds its buffer empty. Meaning the calling Peer
 * is not part of a Network, because it does not know anybody.
 */
public class EmptyNodeBufferException extends IllegalStateException {
    public EmptyNodeBufferException(String message){
        super(message);
    }
    public EmptyNodeBufferException(){
        super();
    }
}
