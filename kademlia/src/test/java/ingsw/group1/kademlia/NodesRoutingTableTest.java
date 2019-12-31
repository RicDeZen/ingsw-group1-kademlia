package ingsw.group1.kademlia;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.BitSet;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Test for {@link NodesRoutingTable} using generic {@link Node<BinarySet>} as Mock object
 *
 * @author Giorgia Bortoletti
 */
@RunWith(MockitoJUnitRunner.class)
public class NodesRoutingTableTest {

    public static final int NUMBER_BITS_KEY = 3;

    private NodesRoutingTable routingTable;
    private PeerNode nodeOwner;
    private Node<BinarySet> node;
    private BitSet bitSet;
    private BinarySet distance;

    @Before
    public void createRoutingTable() {
        bitSet = BitSet.valueOf(new byte[]{(new Integer(7)).byteValue()});
        nodeOwner = mock(PeerNode.class);
        when(nodeOwner.getKey()).thenReturn(new BinarySet(bitSet));
        node = mock(Node.class);
        routingTable = new NodesRoutingTable(nodeOwner, NUMBER_BITS_KEY);
    }

    /**
     * Calculating distance of node respect of nodeOwner using getDistance of PeerNode
     *
     * @param nodeOwner {@link Node<BinarySet>}
     * @param node {@link Node<BinarySet>}
     * @return BinarySet represents distance between two parameters
     */
    private BinarySet getDistancePeerNode(Node<BinarySet> nodeOwner, Node<BinarySet> node) {
        return new PeerNode(nodeOwner.getKey()).getDistance(node);
    }

    /**
     * Calculating distance of node respect of nodeOwner using getDistance of ResourceNode
     *
     * @param nodeOwner {@link Node<BinarySet>}
     * @param node {@link Node<BinarySet>}
     * @return BinarySet represents distance between two parameters
     */
    private BinarySet getDistanceResourceNode(Node<BinarySet> nodeOwner, Node<BinarySet> node) {
        return new ResourceNode(nodeOwner.getKey(), "").getDistance(node);
    }

    @Test
    public void add() {
        bitSet = BitSet.valueOf(new byte[]{(new Integer(1)).byteValue()}); //KEY = 001
        when(node.getKey()).thenReturn(new BinarySet(bitSet));
        distance = getDistancePeerNode(nodeOwner, node);
        when(nodeOwner.getDistance(node)).thenReturn(distance);

        assertTrue(routingTable.add(node));
        assertFalse(routingTable.add(null));
    }

    @Test
    public void contains() {
        bitSet = BitSet.valueOf(new byte[]{(new Integer(1)).byteValue()}); //KEY = 001
        when(node.getKey()).thenReturn(new BinarySet(bitSet));
        distance = getDistancePeerNode(nodeOwner, node);
        when(nodeOwner.getDistance(node)).thenReturn(distance);

        assertTrue(routingTable.add(node));
        assertTrue(routingTable.contains(node));
    }

    @Test
    public void notContains() {
        bitSet = BitSet.valueOf(new byte[]{(new Integer(2)).byteValue()}); //KEY = 010
        when(node.getKey()).thenReturn(new BinarySet(bitSet));
        distance = getDistancePeerNode(nodeOwner, node);
        when(nodeOwner.getDistance(node)).thenReturn(distance);

        assertFalse(routingTable.contains(node));
    }

    @Test
    public void removeTrue() {
        bitSet = BitSet.valueOf(new byte[]{(new Integer(4)).byteValue()}); //KEY = 100
        when(node.getKey()).thenReturn(new BinarySet(bitSet));
        distance = getDistancePeerNode(nodeOwner, node);
        when(nodeOwner.getDistance(node)).thenReturn(distance);

        assertTrue(routingTable.add(node));
        assertTrue(routingTable.remove(node));
        assertFalse(routingTable.contains(node));
    }

    @Test
    public void removeFalse() {
        bitSet = BitSet.valueOf(new byte[]{(new Integer(3)).byteValue()}); //KEY = 011
        when(node.getKey()).thenReturn(new BinarySet(bitSet));
        distance = getDistancePeerNode(nodeOwner, node);
        when(nodeOwner.getDistance(node)).thenReturn(distance);

        assertFalse(routingTable.remove(node));
    }

    @Test
    public void getBucket() {
        bitSet = BitSet.valueOf(new byte[]{(new Integer(5)).byteValue()}); //KEY = 101
        when(node.getKey()).thenReturn(new BinarySet(bitSet));
        distance = getDistancePeerNode(nodeOwner, node);
        when(nodeOwner.getDistance(node)).thenReturn(distance);

        assertTrue(routingTable.add(node));
        KBucket bucket = routingTable.getBucket(1);
        assertEquals(node, bucket.getOldest());
    }

    @Test
    public void getBucketInvalidPosition() {
        assertEquals(null, routingTable.getBucket(NUMBER_BITS_KEY));
    }

    @Test
    public void getNodeOwner() {
        assertEquals(nodeOwner, routingTable.getNodeOwner());
    }

    @Test
    public void getLocation() {
        bitSet = BitSet.valueOf(new byte[]{(new Integer((int) Math.pow(2, NUMBER_BITS_KEY) - 2)).byteValue()}); //110
        when(node.getKey()).thenReturn(new BinarySet(bitSet));
        distance = getDistancePeerNode(nodeOwner, node);
        when(nodeOwner.getDistance(node)).thenReturn(distance);

        assertEquals(NUMBER_BITS_KEY - 1, routingTable.getLocation(node));
    }

    @Test
    public void getClosest() {
        Node<BinarySet> referenceNode = mock(Node.class);
        Node<BinarySet> closestNode = mock(Node.class);

        bitSet = BitSet.valueOf(new byte[]{(new Integer(6)).byteValue()}); //KEY = 110
        when(node.getKey()).thenReturn(new BinarySet(bitSet));
        distance = getDistancePeerNode(nodeOwner, node);
        when(nodeOwner.getDistance(node)).thenReturn(distance);
        assertTrue(routingTable.add(node));

        bitSet = BitSet.valueOf(new byte[]{(new Integer(1)).byteValue()}); //KEY = 001
        when(referenceNode.getKey()).thenReturn(new BinarySet(bitSet));
        distance = getDistancePeerNode(nodeOwner, referenceNode);
        when(nodeOwner.getDistance(referenceNode)).thenReturn(distance);
        assertTrue(routingTable.add(referenceNode));

        bitSet = BitSet.valueOf(new byte[]{(new Integer(2)).byteValue()}); //KEY = 010
        when(closestNode.getKey()).thenReturn(new BinarySet(bitSet));
        distance = getDistancePeerNode(nodeOwner, closestNode);
        when(nodeOwner.getDistance(closestNode)).thenReturn(distance);
        assertTrue(routingTable.add(closestNode));

        distance = getDistancePeerNode(referenceNode, closestNode);
        when(referenceNode.getDistance(closestNode)).thenReturn(distance);
        distance = getDistancePeerNode(referenceNode, node);
        distance = getDistancePeerNode(referenceNode, referenceNode);

        assertEquals(closestNode, routingTable.getClosest(referenceNode));
    }

    @Test
    public void getKClosest() {
        bitSet = BitSet.valueOf(new byte[]{(new Integer(3)).byteValue()}); //KEY = 011
        when(node.getKey()).thenReturn(new BinarySet(bitSet));
        distance = getDistancePeerNode(nodeOwner, node);
        when(nodeOwner.getDistance(node)).thenReturn(distance);

        int numberNodesClosest = 2;
        ArrayList<Node<BinarySet>> nodesClosest = new ArrayList<>();
        int key = 1;
        for (int i = 0; i < numberNodesClosest; i++) {  //1,2,3
            nodesClosest.add(mock(Node.class));
            Node<BinarySet> nodeAdded = nodesClosest.get(i);
            bitSet = BitSet.valueOf(new byte[]{(new Integer(key)).byteValue()});
            when(nodeAdded.getKey()).thenReturn(new BinarySet(bitSet));

            distance = getDistancePeerNode(nodeOwner, nodeAdded); //from nodeOwner for the add in rt
            when(nodeOwner.getDistance(nodeAdded)).thenReturn(distance);

            distance = getDistancePeerNode(node, nodeAdded); //from the node to getKClosest

            assertTrue(routingTable.add(nodeAdded));

            key++;
        }

        ArrayList<Node<BinarySet>> othersNodes = new ArrayList<>();
        key += 2;
        for (int i = 0; i < 2; i++) { //5,6
            othersNodes.add(mock(Node.class));
            Node<BinarySet> nodeAdded = othersNodes.get(i);
            bitSet = BitSet.valueOf(new byte[]{(new Integer(key)).byteValue()});
            when(nodeAdded.getKey()).thenReturn(new BinarySet(bitSet));

            distance = getDistancePeerNode(nodeOwner, nodeAdded); //from nodeOwner for the add in rt
            when(nodeOwner.getDistance(nodeAdded)).thenReturn(distance);
            distance = getDistancePeerNode(node, nodeAdded); //from the node to getKClosest

            key++;

            assertTrue(routingTable.add(nodeAdded));
        }

        assertArrayEquals(nodesClosest.toArray(), routingTable.getKClosest(node));
    }

    @Test
    public void getKClosestToNodeOwner() {
        int numberNodesClosest = 3;
        ArrayList<Node<BinarySet>> othersNodes = new ArrayList<>();
        int key = 1;
        for (int i = 0; i < numberNodesClosest; i++) {
            othersNodes.add(mock(Node.class));
            Node<BinarySet> nodeAdded = othersNodes.get(i);
            bitSet = BitSet.valueOf(new byte[]{(new Integer(key)).byteValue()});
            when(nodeAdded.getKey()).thenReturn(new BinarySet(bitSet));

            distance = getDistancePeerNode(nodeOwner, nodeAdded); //from nodeOwner
            when(nodeOwner.getDistance(nodeAdded)).thenReturn(distance);

            assertTrue(routingTable.add(nodeAdded));

            key++;
        }

        ArrayList<Node<BinarySet>> nodesClosest = new ArrayList<>();
        key = 4;
        for (int i = 0; i < 2; i++) { //4,5
            nodesClosest.add(mock(Node.class));
            Node<BinarySet> nodeAdded = nodesClosest.get(i);
            bitSet = BitSet.valueOf(new byte[]{(new Integer(key)).byteValue()});
            when(nodeAdded.getKey()).thenReturn(new BinarySet(bitSet));

            distance = getDistancePeerNode(nodeOwner, nodeAdded); //from nodeOwner for the add in rt
            when(nodeOwner.getDistance(nodeAdded)).thenReturn(distance);

            key++;

            assertTrue(routingTable.add(nodeAdded));
        }

        assertArrayEquals(nodesClosest.toArray(), routingTable.getKClosest(nodeOwner));
    }

}