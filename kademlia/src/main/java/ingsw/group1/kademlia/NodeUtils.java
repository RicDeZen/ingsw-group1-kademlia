package ingsw.group1.kademlia;

import java.util.BitSet;

import ingsw.group1.msglibrary.Peer;
import ingsw.group1.msglibrary.SMSPeer;
import ingsw.group1.repnetwork.Resource;

/**
 * @author niccoloturcato
 * Class used to quickly create BitSet binary key and PeerNodes from Peer and resources
 * Uses SHA-1 Hashing from class BitSetUtils
 */
public class NodeUtils {

    public static final int DEFAULT_KEY_LENGTH = KademliaConstants.KADEMLIA_DEFAULT_ID_LENGTH;

    private final static String KEY_LENGTH_INVALID_MSG = "invalid key length, must be > 0 && <=160";
    private final static String PEER_INVALID_MSG = "invalid or null Peer";
    private final static String RESOURCE_INVALID_MSG = "invalid or null Resource";

    /**
     * @param peer      the peer for which generate the binary key
     * @param keyLength number of bits required for the generated Key (> 0 && <=160)
     * @return the binary key generated with SHA-1 hashing for the given peer
     * @throws IllegalArgumentException if key length or given peer are invalid or null
     */
    public static BitSet getIdForPeer(Peer<String> peer, int keyLength) {
        if (keyLength > 0 && keyLength <= 160) {
            if (peer != null && peer.isValid()) {
                return BitSetUtils.hash(peer.getAddress().getBytes(), keyLength);
            }
            throw new IllegalArgumentException(PEER_INVALID_MSG);
        }
        throw new IllegalArgumentException(KEY_LENGTH_INVALID_MSG);
    }

    /**
     * @param resource  the resource for which generate the binary key
     * @param keyLength number of bits required for the generated Key (> 0 && <=160)
     * @return the binary key generated with SHA-1 hashing from the given resource's name
     * @throws IllegalArgumentException if key length or given resource are invalid or null
     */
    public static BitSet getIdForResource(Resource<String, String> resource, int keyLength) {
        if (keyLength > 0 && keyLength <= 160) {
            if (resource != null && resource.isValid()) {
                return BitSetUtils.hash(resource.getName().getBytes(), keyLength);
            }
            throw new IllegalArgumentException(RESOURCE_INVALID_MSG);
        }
        throw new IllegalArgumentException(KEY_LENGTH_INVALID_MSG);
    }

    /**
     * @param peer      the peer for which generate the binary key
     * @param keyLength number of bits required for the generated Key (> 0 && <=160)
     * @return an instance of PeerNode with binary key generated with SHA-1 hashing from the given peer
     * @throws IllegalArgumentException if key length or given peer are invalid or null
     */
    public static PeerNode getNodeForPeer(Peer<String> peer, int keyLength) {
        if (keyLength > 0 && keyLength <= 160) {
            if (peer != null && peer.isValid()) {
                BinarySet set = new BinarySet(BitSetUtils.hash(peer.getAddress().getBytes(), keyLength));
                PeerNode node = new PeerNode(set);
                node.setPhysicalPeer((SMSPeer) peer);
                return node;
            }
            throw new IllegalArgumentException(PEER_INVALID_MSG);
        }
        throw new IllegalArgumentException(KEY_LENGTH_INVALID_MSG);
    }
}
