package ingsw.group1.kademlia;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * @author Turcato
 * <p>
 * This class acts like a wrapper for NodesRoutingTable through the interface NodeDataProvider
 */
class KadDataProvider implements NodeDataProvider<BinarySet, Node<BinarySet>> {
    private Node<BinarySet> networkTarget;
    private NodesRoutingTable routingTable;
    private final static int TABLE_DIM = 128; //FIXME

    /**
     * Builds the dataProvider starting from the routing table
     *
     * @param targetTable The routing table of the node representing myself
     */
    public KadDataProvider(@NonNull NodesRoutingTable targetTable) {
        routingTable = targetTable;
        networkTarget = targetTable.getNodeOwner();
    }

    /**
     * @param target The target Key.
     * @return The closest node in the routing table to the Node with the target key
     */
    @Override
    public Node<BinarySet> getClosest(@NonNull BinarySet target) {
        return routingTable.getClosest(new PeerNode(target));
    }

    /**
     * @param k      The max amount of Nodes returned.
     * @param target The target Node key.
     * @return The closest k nodes in the routing table to the Node with the target key
     */
    @Override
    public ArrayList<Node<BinarySet>> getKClosest(int k, @NonNull BinarySet target) {
        return new ArrayList<>(Arrays.asList(routingTable.getKClosest(new PeerNode(target))));
        //TODO: check that returns k nodes
    }

    /**
     * @return The default root, which is the owner of the routing table
     */
    @Override
    public Node<BinarySet> getRootNode() {
        return networkTarget;
    }

    /**
     * @param k      number of nodes to extract
     * @param target The target Node key.
     * @param nodes  The nodes to filter
     * @return The k closest nodes to a Target address
     */
    @Override
    public ArrayList<Node<BinarySet>> filterKClosest(int k, @NonNull BinarySet target, @NonNull List<Node<BinarySet>> nodes) {
        NodesRoutingTable table = new NodesRoutingTable(new PeerNode(target), TABLE_DIM);
        for (Node<BinarySet> n : nodes) {
            table.add(n);
        }
        ArrayList<Node<BinarySet>> closestNodes = new ArrayList<>(Arrays.asList(table.getKClosest(new PeerNode(target))));

        Collections.sort(closestNodes, new NodesComparator(new PeerNode(target)));

        return new ArrayList<>(closestNodes.subList(0, k));
    }

    /**
     * Not implemented because NodesRoutingTable does not provide this functionality
     *
     * @param visitedNode The visited Node.
     */
    @Override
    public void visitNode(@NonNull Node<BinarySet> visitedNode) {
        //NOPE
    }
}
