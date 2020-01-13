package ingsw.group1.kademlia;

import androidx.annotation.NonNull;

import java.util.List;

/**
 * Interface defining standard behaviour for a class able to provide data and perform operations
 * regarding the {@code Node} instances in a Network.
 * An implementation may add "closer than the root Node" as a more strict definition of
 * "closest Node/Nodes", if this is the case and the root Node is also the closest, the
 * implementing class may choose to return {@code null}.
 *
 * @param <K> Type of key for the {@code Node} the implementing Class manages.
 * @param <N> Type of {@code Node} the implementing class works on
 * @author Riccardo De Zen
 * CODE REVIEW
 */
public interface NodeDataProvider<K, N extends Node<K>> {
    /**
     * @return the Node this Provider considers as default root for "distance".
     */
    N getRootNode();

    /**
     * Method to warn the Provider that a Node has been visited.
     *
     * @param visitedNode the visited Node.
     */
    void visitNode(@NonNull N visitedNode);

    /**
     * Method to find the closest known {@code N} type {@code Node} to the target Key.
     *
     * @param target the target Key.
     * @return the closest known Node to the target Key.
     */
    N getClosest(@NonNull K target);

    /**
     * Method to return the (up to) k known Nodes deemed as "closest" to a target one.
     *
     * @param k      the max amount of Nodes returned.
     * @param target the target Node key.
     * @return a List containing up to k Nodes, the closest to the target Key amongst the known
     * Nodes.
     */
    List<N> getKClosest(int k, @NonNull K target);

    /**
     * Method to return the (up to) k Nodes in the given list that are closest to the target.
     *
     * @param target the target Node key.
     * @return a List containing up to k Nodes, the closest to the target Key amongst the known
     * Nodes in parameter {@code nodes}.
     */
    List<N> filterKClosest(int k, @NonNull K target, @NonNull List<N> nodes);
}
