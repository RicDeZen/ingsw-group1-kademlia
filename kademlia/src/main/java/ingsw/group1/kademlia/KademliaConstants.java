package ingsw.group1.kademlia;

/**
 * Class defining constants to be used in a distributed network using the Kademlia protocol.
 */
public class KademliaConstants {
    /**
     * Default ID lenght for a {@code Node} in a Kademlia network.
     */
    public static final int KADEMLIA_DEFAULT_ID_LENGTH = 128;

    /**
     * A shorter ID option.
     */
    public static final int KADEMLIA_SHORT_ID_LENGTH = 64;

    /**
     * Default constant used during {@code Node} lookup operations.
     */
    public static final int KADEMLIA_DEFAULT_K = 20;

    /**
     * A smaller constant.
     */
    public static final int KADEMLIA_SMALL_K = 5;
}
