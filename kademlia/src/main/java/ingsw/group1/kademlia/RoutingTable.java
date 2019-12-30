package ingsw.group1.kademlia;

/**
 * Structure for a container that realizes the routing table described by Kademlia P2P algorithm
 * It is built using as base Node, the Peer that builds the object. So each peer of the network has its own
 *
 * Given an integer N: number of bit that compose the address space for the network's Nodes
 *
 * The RoutingTable contains N Bucket(of a defined dimension)
 * i: is the index of a Bucket (between 0 and N-1)
 *
 * The bucket of index i contains the nodes that have
 * 2^(N-i-1) <= (numerical distance XOR metric from node owner)  <= 2^(N-i)-1
 * and i = N - 1 - (position of most significant bit at 1 of BitSet distance XOR)
 *
 * i.e.
 *
 * if(i == 0)
 *      bitNode[N-1] = NOT(myself[N-1]) --> bitDistanceXOR[N-1] = 1 --> i = N-1-(N-1) = 0
 *      bitNode[0, N-2] = any
 *
 * if(i > 0 && i < N-1)
 *      bitNode[N-1, N-i] = myself[N-1, N-i] --> bitDistanceXOR[N-1, N-i] = 0
 *      bitNode[N-(i+1)] = NOT(myself[N-(i+1)] --> bitDistanceXOR[N-(i+1)] = 1
 *      bitNode[N-(i+2), 0] = any
 *
 * if(i = N-1)
 *      bitNode[0] = NOT(myself[0]) --> bitDistanceXOR[0] = 1 --> i = N-1-0 = N-1
 *      bitNode[1, N-1] = myself[1, N-1] --> bitDistanceXOR[1, N-1] = 0
 *      the only Node that has distance = 1
 *
 * @param <B> type of {@link Bucket} used for this structure
 * @param <N> type of {@code N} contained in the routing table and in bucket
 *
 * @author Niccolò Turcato
 * @author Giorgia Bortoletti
 */
public abstract class RoutingTable<N extends Node<BinarySet>, B extends Bucket<N>> {

    /**
     * @param node {@code N} to add
     * @return true if the node has been added, false otherwise
     */
    public abstract boolean add(N node);

    /**
     * @param node {@code N} to remove
     * @return true if the node has been removed, false otherwise
     */
    public abstract boolean remove(N node);

    /**
     * @param node {@code N} of which check presence
     * @return true if present, false otherwise
     */
    public abstract boolean contains(N node);

    /**
     * @param i index of the bucket in buckets container
     * @return the bucket at index i
     */
    public abstract B getBucket(int i);

    /**
     * @param node {@code N} of the distributed Network
     * @return the index (between 0 and N -1) of the bucket that maybe containing the given {@code N}, -1 otherwise
     */
    public abstract int getLocation(N node);

    /**
     * @param node {@code N}
     * @return the closest Node {@code N} at the node argument if it is present in the routing table, null otherwise
     */
    public abstract N getClosest(N node);

    /**
     * @param node {@code N}
     * @return the closest K Nodes {@code N} at the node in the routing table if it is present, null otherwise
     */
    public abstract N[] getKClosest(N node);

    /**
     * @return number of nodes present in the routing table
     */
    public abstract int size();

}