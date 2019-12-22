package ingsw.group1.kademlia;

import android.os.Bundle;

import androidx.annotation.NonNull;

import ingsw.group1.repnetwork.Resource;

/**
 * @author Niccolo' Turcato
 * Class that represents a Resource of DistributedNetwork
 */
public class ResourceNode extends Resource<BinarySet, String> implements Node<BinarySet> {
    private BinarySet binaryKey;
    private String resourceValue;

    /**
     * Constructor that initializes the key with the given value
     *
     * @param buildingKey given value for the node's Key
     */
    public ResourceNode(@NonNull BinarySet buildingKey, String value) {
        binaryKey = (BinarySet) buildingKey.clone();
        resourceValue = value;
    }


    /**
     * @return the resource's Name if available, null if not
     */
    public BinarySet getName() {
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
     * @return a new PeerNode equal to this
     */
    @Override
    public Object clone() {
        return new ResourceNode(binaryKey, resourceValue);
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
     * @param other resource to confront
     * @return true if this peer and the other are equals, false otherwise
     */
    @Override
    public boolean equals(Object other) {
        if (other == null)
            return false;
        if (this == other)
            return true;
        if (other instanceof ResourceNode)
            return this.getName().equals(((ResourceNode) other).getName());
        else return false;
    }

    /**
     * @return true if this Peer node is to consider Valid, false otherwise
     */
    public boolean isValid() {
        return binaryKey != null && binaryKey.keyLength() > 0;
    }


    /**
     * @return the resource's value if available, null if not
     */
    public String getValue() {
        return resourceValue;
    }

    /**
     * Not implemented, because it's not applicable to this class
     *
     * @return an empty Bundle, not yet implemented
     */
    public Bundle getExtras() {
        return new Bundle();
    }

}
