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
 * @param <B> type of Bucket used for this structure
 *
 * @author Niccolò Turcato
 * @author Giorgia Bortoletti (some fixes)
 */
public abstract class RoutingTable<B extends Bucket<Node<BinarySet>>> {

    /**
     * @param node Node to add
     * @return true if the node has been added, false otherwise
     */
    public abstract boolean add(Node<BinarySet> node);

    /**
     * @param node Node to remove
     * @return true if the node has been removed, false otherwise
     */
    public abstract boolean remove(Node<BinarySet> node);

    /**
     * @param node Node of which check presence
     * @return true if present, false otherwise
     */
    public abstract boolean contains(Node<BinarySet> node);

    /**
     * @param i index of the bucket in buckets container
     * @return the bucket at index i
     */
    public abstract B getBucket(int i);

    /**
     * @param node Node of the distributed Network
     * @return the index (between 0 and N -1) of the bucket that maybe containing the given Node (Resource or Peer), -1 otherwise
     */
    public abstract int getLocation(Node<BinarySet> node);

    /**
     * @param node
     * @return the closest Node at the node in the routing table if it is present, otherwise null
     */
    public abstract Node<BinarySet> getClosest(Node<BinarySet> node);

    /**
     * @param node
     * @return the closest K Nodes at the node in the routing table if it is present, otherwise null
     */
    public abstract Node<BinarySet>[] getKClosest(Node<BinarySet> node);

    /**
     * @return number of nodes present in the routing table
     */
    public abstract int size();

}