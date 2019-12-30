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
     * @return true if the node has been removed, false otherwise
     */
    @Override
    public boolean remove(Node<BinarySet> node) {
        if (node == null)
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
     * @param i index of the bucket in buckets container
     * @return the bucket at index i, null otherwise
     */
    @Override
    public KBucket getBucket(int i) {
        try {
            return bucketsTable.get(i);
        } catch (IndexOutOfBoundsException e) {
            return null;
        }
    }

    /**
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
     * @param node {@link Node<BinarySet>} to find its Closest node present in the routing table
     * @return the closest {@link Node} at the node in the routing table if it is present, null otherwise
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
     * @param node {@link Node<BinarySet>} to find its KClosest nodes present in the routing table
     * @return the closest K {@link Node} at the node in the routing table if it is present, null otherwise
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
