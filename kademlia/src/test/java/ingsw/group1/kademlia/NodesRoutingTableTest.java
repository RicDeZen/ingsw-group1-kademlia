package ingsw.group1.kademlia;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.BitSet;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
@RunWith(MockitoJUnitRunner.class)
public class NodesRoutingTableTest {

    public static final int NUMBER_BITS = 3;
    private RoutingTableNodes rt;
    private Node<BinarySet>  nodeOwner, node;
    private BitSet bitSet;
    private BinarySet distance;

    @Before
    public void createRoutingTable(){
        bitSet = BitSet.valueOf(new byte[]{(new Integer(7)).byteValue()});

        nodeOwner = mock(Node.class);
        when(nodeOwner.getKey()).thenReturn(new BinarySet(bitSet));
        when(nodeOwner.keyLength()).thenReturn(NUMBER_BITS);

        node = mock(Node.class);
        when(node.keyLength()).thenReturn(NUMBER_BITS);

        rt = new RoutingTableNodes(nodeOwner, NUMBER_BITS);
    }

    /**
     * Calculating distance of node respect of nodeOwner using getDistance of PeerNode
     * @param nodeOwner
     * @param node
     * @return BinarySet represents distance between two parameters
     */
    private BinarySet getDistancePeerNode(Node<BinarySet> nodeOwner, Node<BinarySet> node){
        return new PeerNode(nodeOwner.getKey()).getDistance(node);
    }

    /**
     * Calculating distance of node respect of nodeOwner using getDistance of ResourceNode
     * @param nodeOwner
     * @param node
     * @return BinarySet represents distance between two parameters
     */
    private BinarySet getDistanceResourceNode(Node<BinarySet> nodeOwner, Node<BinarySet> node){
        return new ResourceNode(nodeOwner.getKey(), "").getDistance(node);
    }


    @Test
    public void add() {
        bitSet = BitSet.valueOf(new byte[]{(new Integer(1)).byteValue()}); //KEY = 001
        when(node.getKey()).thenReturn(new BinarySet(bitSet));
        distance =  getDistancePeerNode(nodeOwner, node);
        when(nodeOwner.getDistance(node)).thenReturn(distance);

        assertTrue(rt.add(node));
    }

    @Test
    public void contains() {
        bitSet = BitSet.valueOf(new byte[]{(new Integer(1)).byteValue()}); //KEY = 001
        when(node.getKey()).thenReturn(new BinarySet(bitSet));
        distance =  getDistancePeerNode(nodeOwner, node);
        when(nodeOwner.getDistance(node)).thenReturn(distance);

        assertTrue(rt.add(node));
        assertTrue(rt.contains(node));
    }

    @Test
    public void notContains() {
        bitSet = BitSet.valueOf(new byte[]{(new Integer(2)).byteValue()}); //KEY = 010
        when(node.getKey()).thenReturn(new BinarySet(bitSet));
        distance =  getDistancePeerNode(nodeOwner, node);
        when(nodeOwner.getDistance(node)).thenReturn(distance);

        assertFalse(rt.contains(node));
    }

    @Test
    public void removeTrue() {
        bitSet = BitSet.valueOf(new byte[]{(new Integer(4)).byteValue()}); //KEY = 100
        when(node.getKey()).thenReturn(new BinarySet(bitSet));
        distance =  getDistancePeerNode(nodeOwner, node);
        when(nodeOwner.getDistance(node)).thenReturn(distance);

        assertTrue(rt.add(node));
        assertTrue(rt.remove(node));
    }

    @Test
    public void removeFalse() {
        bitSet = BitSet.valueOf(new byte[]{(new Integer(3)).byteValue()}); //KEY = 011
        when(node.getKey()).thenReturn(new BinarySet(bitSet));
        distance =  getDistancePeerNode(nodeOwner, node);
        when(nodeOwner.getDistance(node)).thenReturn(distance);

        assertFalse(rt.remove(node));
    }

    @Test
    public void getBucket() {
        bitSet = BitSet.valueOf(new byte[]{(new Integer(5)).byteValue()}); //KEY = 101
        when(node.getKey()).thenReturn(new BinarySet(bitSet));
        distance =  getDistancePeerNode(nodeOwner, node);
        when(nodeOwner.getDistance(node)).thenReturn(distance);

        assertTrue(rt.add(node));
        KBucket bucket = rt.getBucket(1);
        assertEquals(node, bucket.getOldest());
    }

    @Test
    public void getBucketInvalidPosition() {
        assertEquals(null, rt.getBucket(NUMBER_BITS));
    }

    @Test
    public void getNodeOwner() {
        assertEquals(nodeOwner, rt.getNodeOwner());
    }

    @Test
    public void getLocation() {
        bitSet = BitSet.valueOf(new byte[]{(new Integer((int)Math.pow(2,NUMBER_BITS)-2)).byteValue()}); //110
        when(node.getKey()).thenReturn(new BinarySet(bitSet));
        distance =  getDistancePeerNode(nodeOwner, node);
        when(nodeOwner.getDistance(node)).thenReturn(distance);

        assertEquals(NUMBER_BITS-1, rt.getLocation(node));
    }

    @Test
    public void getClosest() {
        Node<BinarySet> referenceNode = mock(Node.class);
        Node<BinarySet> closestNode = mock(Node.class);

        bitSet = BitSet.valueOf(new byte[]{(new Integer(6)).byteValue()}); //KEY = 110
        when(node.getKey()).thenReturn(new BinarySet(bitSet));
        distance =  getDistancePeerNode(nodeOwner, node);
        when(nodeOwner.getDistance(node)).thenReturn(distance);
        assertTrue(rt.add(node));

        bitSet = BitSet.valueOf(new byte[]{(new Integer(1)).byteValue()}); //KEY = 001
        when(referenceNode.getKey()).thenReturn(new BinarySet(bitSet));
        distance =  getDistancePeerNode(nodeOwner, referenceNode);
        when(nodeOwner.getDistance(referenceNode)).thenReturn(distance);
        assertTrue(rt.add(referenceNode));

        bitSet = BitSet.valueOf(new byte[]{(new Integer(2)).byteValue()}); //KEY = 010
        when(closestNode.getKey()).thenReturn(new BinarySet(bitSet));
        distance =  getDistancePeerNode(nodeOwner, closestNode);
        when(nodeOwner.getDistance(closestNode)).thenReturn(distance);
        assertTrue(rt.add(closestNode));

        distance =  getDistancePeerNode(referenceNode, closestNode);
        when(referenceNode.getDistance(closestNode)).thenReturn(distance);
        distance =  getDistancePeerNode(referenceNode, node);
        when(referenceNode.getDistance(node)).thenReturn(distance);
        distance =  getDistancePeerNode(referenceNode, referenceNode);
        when(referenceNode.getDistance(referenceNode)).thenReturn(distance);

        assertEquals(closestNode, rt.getClosest(referenceNode));
    }

    @Test
    public void getKClosest() {
        bitSet = BitSet.valueOf(new byte[]{(new Integer(3)).byteValue()}); //KEY = 011
        when(node.getKey()).thenReturn(new BinarySet(bitSet));
        distance =  getDistancePeerNode(nodeOwner, node);
        when(nodeOwner.getDistance(node)).thenReturn(distance);

        int numberNodesClosest = 2;
        ArrayList<Node<BinarySet>> nodesClosest = new ArrayList<>();
        int key = 1;
        for(int i=0; i<numberNodesClosest; i++){  //1,2,3
            nodesClosest.add(mock(Node.class));
            Node<BinarySet> nodeAdded = nodesClosest.get(i);
            bitSet = BitSet.valueOf(new byte[]{(new Integer(key)).byteValue()});
            when(nodeAdded.getKey()).thenReturn(new BinarySet(bitSet));

            distance =  getDistancePeerNode(nodeOwner, nodeAdded); //from nodeOwner for the add in rt
            when(nodeOwner.getDistance(nodeAdded)).thenReturn(distance);

            distance =  getDistancePeerNode(node, nodeAdded); //from the node to getKClosest
            when(node.getDistance(nodeAdded)).thenReturn(distance);

            assertTrue(rt.add(nodeAdded));

            key++;
        }

        ArrayList<Node<BinarySet>> othersNodes = new ArrayList<>();
        key += 2;
        for(int i=0; i<2; i++){ //5,6
            othersNodes.add(mock(Node.class));
            Node<BinarySet> nodeAdded = othersNodes.get(i);
            bitSet = BitSet.valueOf(new byte[]{(new Integer(key)).byteValue()});
            when(nodeAdded.getKey()).thenReturn(new BinarySet(bitSet));

            distance =  getDistancePeerNode(nodeOwner, nodeAdded); //from nodeOwner for the add in rt
            when(nodeOwner.getDistance(nodeAdded)).thenReturn(distance);

            distance =  getDistancePeerNode(node, nodeAdded); //from the node to getKClosest
            when(node.getDistance(nodeAdded)).thenReturn(distance);

            key++;

            assertTrue(rt.add(nodeAdded));
        }

        assertEquals(nodesClosest.toArray(), rt.getKClosest(node));


    }

    @Test
    public void getKClosest_NodeOwner() {
        int numberNodesClosest = 3;
        ArrayList<Node<BinarySet>> othersNodes = new ArrayList<>();
        int key = 1;
        for(int i=0; i<numberNodesClosest; i++){
            othersNodes.add(mock(Node.class));
            Node<BinarySet> nodeAdded = othersNodes.get(i);
            bitSet = BitSet.valueOf(new byte[]{(new Integer(key)).byteValue()});
            when(nodeAdded.getKey()).thenReturn(new BinarySet(bitSet));

            distance =  getDistancePeerNode(nodeOwner, nodeAdded); //from nodeOwner
            when(nodeOwner.getDistance(nodeAdded)).thenReturn(distance);

            assertTrue(rt.add(nodeAdded));

            key++;
        }

        ArrayList<Node<BinarySet>> nodesClosest = new ArrayList<>();
        key = 4;
        for(int i=0; i<2; i++){ //4,5
            nodesClosest.add(mock(Node.class));
            Node<BinarySet> nodeAdded = nodesClosest.get(i);
            bitSet = BitSet.valueOf(new byte[]{(new Integer(key)).byteValue()});
            when(nodeAdded.getKey()).thenReturn(new BinarySet(bitSet));

            distance =  getDistancePeerNode(nodeOwner, nodeAdded); //from nodeOwner for the add in rt
            when(nodeOwner.getDistance(nodeAdded)).thenReturn(distance);

            key++;

            assertTrue(rt.add(nodeAdded));
        }

        assertEquals(nodesClosest.toArray(), rt.getKClosest(nodeOwner));


    }

}