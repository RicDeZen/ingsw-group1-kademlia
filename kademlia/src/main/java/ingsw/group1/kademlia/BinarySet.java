package ingsw.group1.kademlia;

import androidx.annotation.NonNull;

import java.util.BitSet;


/**
 * @author Niccolo' Turcato
 * This class was made to extend BitSet as comparable
 */
public class BinarySet implements Comparable<BinarySet>, Cloneable {
    private BitSet key;
    private String EMPTY_KEY_EXCEPTION = "The key is empty";

    /**
     * Constructor that initializes the key with the given value
     *
     * @param buildingKey Given value for the Key
     * @throws IllegalArgumentException If the given BitSet is empty
     */
    public BinarySet(@NonNull BitSet buildingKey) {
        if (buildingKey.size() > 0)
            key = (BitSet) buildingKey.clone();
        else throw new IllegalArgumentException(EMPTY_KEY_EXCEPTION);
    }

    /**
     * Constructor that builds a BinarySet starting from a string containing hexadecimal digits
     *
     * @param hexString A string containing hexadecimal digits (length must be multiple of 2, write 0A instead of A)
     * @throws IllegalArgumentException If the String length isn't multiple of 2, or contains invalid HEX string
     */
    public BinarySet(@NonNull String hexString) {
        this(BitSetUtils.decodeHexString(hexString));
    }

    /**
     * @param set Another object of BinarySet to compare
     * @return A negative integer, zero, or a positive integer as this < other set, the two are equal, this > other set.
     */
    public int compareTo(@NonNull BinarySet set) {
        if (this.equals(set)) return 0;
        BitSet distance = set.getDistance(this).getKey();
        int firstDifferent = distance.length() - 1;
        if (firstDifferent == -1)
            return 0; //actually, this is redundant
        return key.get(firstDifferent) ? 1 : -1;
    }

    /**
     * @return A new BinarySet equals to this
     */
    public Object clone() {
        return new BinarySet((BitSet) key.clone());
    }

    /**
     * @return The size of the BitSet key
     */
    public int keyLength() {
        return key.size();
    }

    /**
     * @param set Node of which calculate distance
     * @return The distance in XOR metric
     */
    public BinarySet getDistance(@NonNull BinarySet set) {
        BitSet distance = getKey();
        distance.xor(set.getKey());
        return new BinarySet(distance);
    }

    /**
     * @return The position of most significant bit at 1, -1 if key is zero
     */
    public int getFirstPositionOfOne() {
        BitSet bitSet = getKey();
        return bitSet.length() - 1;
    }

    /**
     * @return The BitSet key
     */
    public BitSet getKey() {
        return (BitSet) key.clone();
    }

    /**
     * @param other Peer to confront
     * @return True if this peer and the other are equals, false otherwise
     */
    @Override
    public boolean equals(Object other) {
        if (other == null)
            return false;
        if (this == other)
            return true;
        if (other instanceof BinarySet)
            return key.equals(((BinarySet) other).getKey());
        return false;
    }

    /**
     * @return The key converted to Hexadecimal number written on a String
     */
    public String toHex() {
        return BitSetUtils.BitSetsToHex(key);
    }
}