package ingsw.group1.kademlia;

import java.util.ArrayList;
import java.util.List;

/**
 * Extends Routing Table with {@link KBucket} that contains {@link Node<BinarySet>}
 * Size of RoutingTable is fixed and equals to nodeOwner's key length
 *
 * @author Giorgia Bortoletti
 */
public class NodesRoutingTable extends RoutingTable<Node<BinarySet>, KBucket> {

    private List<KBucket> bucketsTable;
    private int sizeTable;
    private PeerNode nodeOwner;

    /**
     * Constructor where the routing table length is equal to nodeOwner length
     *
     * @param nodeOwner {@link PeerNode} node owner of routing table
     */
    public NodesRoutingTable(PeerNode nodeOwner) {
        new NodesRoutingTable(nodeOwner, nodeOwner.keyLength());
    }

    /**
     * Constructor where the routing table length is sizeTable
     *
     * @param nodeOwner {@link PeerNode} node owner of routing table
     * @param sizeTable dimension of routing table
     */
    public NodesRoutingTable(PeerNode nodeOwner, int sizeTable) {
        this.nodeOwner = nodeOwner;
        this.sizeTable = sizeTable;
        bucketsTable = new ArrayList<>(sizeTable);
        for (int i = 0; i < sizeTable; i++)
            bucketsTable.add(new KBucket(sizeTable));
    }

    /**
     * @return {@link PeerNode} that is node owner of this routing table
     */
    public PeerNode getNodeOwner() {
        return nodeOwner;
    }

    /**
     * @param node {@link Node<BinarySet>} to add
     * @return true if the node has been added, false otherwise
     */
    @Override
    public boolean add(Node<BinarySet> node) {
        if (node == null)
            return false;
        return bucketsTable.get(getLocation(node)).add(node);
    }

    /**
     * @param node {@link Node<BinarySet>} to remove
     * @return true if the node is present in the table and it has been removed and, false otherwise
     */
    @Override
    public boolean remove(Node<BinarySet> node) {
        if (!(contains(node)))
            return false;
        int positionNode = getLocation(node);
        KBucket bucketFound = bucketsTable.get(positionNode);
        if (!bucketFound.contains(node))
            return false;
        return bucketFound.remove(node);
    }

    /**
     * @param node {@link Node<BinarySet>} of which check presence
     * @return true if present, false otherwise
     */
    @Override
    public boolean contains(Node<BinarySet> node) {
        if (node == null || bucketsTable.isEmpty())
            return false;
        int positionNode = getLocation(node);
        if (positionNode == -1)
            return false;
        return bucketsTable.get(positionNode).contains(node);
    }

    /**
     * @param position index of the bucket in buckets container
     * @return the bucket at index i if it exists, null otherwise
     */
    @Override
    public KBucket getBucket(int position) {
        if(position>=0 && position<bucketsTable.size())
            return bucketsTable.get(position);
        return null;
    }

    /**
     * This method returns the index of the bucket that perhaps contains the given node argument
     *
     * @param node {@link Node<BinarySet>} to return its position
     * @return the index (between 0 and N -1) of the bucket that maybe containing the given node argument, -1 otherwise
     */
    @Override
    public int getLocation(Node<BinarySet> node) {
        if (node == null)
            return -1;
        BinarySet distance = nodeOwner.getDistance(node);
        return (sizeTable - 1 - distance.getFirstPositionOfOne());
    }

    /**
     * This method returns the closest node to the argument node
     * if closest node is present in the routing table, null otherwise
     *
     * @param node {@link Node<BinarySet>} to find its Closest node present in the routing table
     * @return the closest {@link Node} to the argument node if it is present in the routing table, null otherwise
     */
    @Override
    public Node getClosest(Node<BinarySet> node) {
        Node[] nodesClosest = getKClosest(node);
        if (nodesClosest != null) {
            int minDistance = sizeTable + 1;
            Node nodeClosest = null;
            for (Node nodeMaybeClosest : nodesClosest) {
                if (!nodeMaybeClosest.equals(node)) {
                    BinarySet distanceBinarySet = node.getDistance(nodeMaybeClosest);
                    int distance = sizeTable - 1 - distanceBinarySet.getFirstPositionOfOne();
                    if (distance < minDistance) {
                        minDistance = distance;
                        nodeClosest = nodeMaybeClosest;
                    }
                }
            }
            return nodeClosest;
        }
        return null;
    }

    /**
     * This method returns the closest nodes to the argument node
     * if these nodes are presents in the routing table, null otherwise.
     * They are a number equals to the size of the nearest existing bucket.
     *
     * @param node {@link Node<BinarySet>} to find its KClosest nodes present in the routing table
     * @return the closest K {@link Node} to the argument node if they are presents in the routing table, null otherwise
     */
    @Override
    public Node[] getKClosest(Node<BinarySet> node) {
        int position;
        if (!node.equals(nodeOwner))
            position = getLocation(node); //the higher the position, the closer it is
        else
            position = sizeTable - 1;
        for (int i = position; i >= 0; i--) { //moves away but it looks for a bucket with some nodes
            Node[] nodesClosest = bucketsTable.get(i).getElements();
            if (nodesClosest.length >= 1)
                return nodesClosest;
        }
        return null;
    }

    /**
     * @return number of nodes present in the routing table
     */
    @Override
    public int size() {
        int size = 0;
        for (KBucket bucket : bucketsTable) {
            size += bucket.size();
        }
        return size;
    }

}
