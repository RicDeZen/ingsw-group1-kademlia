package ingsw.group1.kademlia;

import androidx.annotation.NonNull;

import ingsw.group1.msglibrary.Peer;
import ingsw.group1.msglibrary.SMSPeer;

/**
 * @author Niccolo' Turcato
 * Class that represents the Peer DistributedNetworkNode of DistributedNetwork
 * Address is a Bitset of fixed length
 */
public class PeerNode extends Peer<BinarySet> implements Node<BinarySet> {
    private BinarySet binaryKey;
    private SMSPeer physicalPeer;

    /**
     * Constructor that initializes the key with the given value
     *
     * @param buildingKey given value for the node's Key
     */
    public PeerNode(@NonNull BinarySet buildingKey) {
        binaryKey = (BinarySet) buildingKey.clone();
    }

    public void setPhysicalPeer(SMSPeer peer) {
        if (peer != null && peer.isValid())
            physicalPeer = peer;
    }

    public SMSPeer getPhysicalPeer() {
        return physicalPeer;
    }


    /**
     * @return binary address of the node
     */
    public BinarySet getAddress() {
        return (BinarySet) binaryKey.clone();
    }

    public BinarySet getKey() {
        return (BinarySet) binaryKey.clone();
    }


    /**
     * @return number of bits that compose the BitSet key
     */
    public int keyLength() {
        return binaryKey.keyLength();
    }

    /**
     * @param node node of which calculate distance
     * @return the distance of the two nodes in XOR metric
     */
    @Override
    public BinarySet getDistance(Node<BinarySet> node) {
        return binaryKey.getDistance(node.getKey());
    }

    /**
     * @return a new PeerNode equal to this
     */
    @Override
    public Object clone() {
        return new PeerNode(binaryKey);
    }

    /**
     * @param other peer to confront
     * @return true if this peer and the other are equals, false otherwise
     */
    @Override
    public boolean equals(Object other) {
        if (other == null)
            return false;
        if (this == other)
            return true;
        if (other instanceof PeerNode)
            return this.getAddress().equals(((PeerNode) other).getAddress());
        else return false;
    }

    /**
     * @return true if this Peer node is to consider Valid, false otherwise
     */
    public boolean isValid() {
        return binaryKey != null && binaryKey.keyLength() > 0;
    }

}