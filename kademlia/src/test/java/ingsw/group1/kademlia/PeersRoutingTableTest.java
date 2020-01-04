package ingsw.group1.kademlia;

import org.junit.Before;
import org.junit.Test;

import java.util.BitSet;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Test for {@link NodesRoutingTable} using {@link PeerNode}
 *
 * @author Giorgia Bortoletti
 */
public class PeersRoutingTableTest {

    public static final int NUMBER_BITS_KEY = 3;

    private NodesRoutingTable routingTable;
    private PeerNode nodeOwner;

    @Before
    public void createRoutingTable() {
        BitSet bitSet = BitSet.valueOf(new byte[]{(new Integer(7)).byteValue()});
        nodeOwner = new PeerNode(new BinarySet(bitSet)); //KEY = 111
        routingTable = new NodesRoutingTable(nodeOwner, NUMBER_BITS_KEY);
    }

    @Test
    public void add() {
        BitSet bitSet = BitSet.valueOf(new byte[]{(new Integer(1)).byteValue()});
        Node newNode = new PeerNode(new BinarySet(bitSet)); //KEY = 001
        assertTrue(routingTable.add(newNode));
        assertFalse(routingTable.add(null));
    }

    @Test
    public void contains() {
        BitSet bitSet = BitSet.valueOf(new byte[]{(new Integer(1)).byteValue()});
        Node newNode = new PeerNode(new BinarySet(bitSet)); //KEY = 001
        assertTrue(routingTable.add(newNode));
        assertTrue(routingTable.contains(newNode));
    }

    @Test
    public void notContains() {
        BitSet bitSet = BitSet.valueOf(new byte[]{(new Integer(2)).byteValue()});
        Node newNode = new PeerNode(new BinarySet(bitSet)); //KEY = 010
        assertFalse(routingTable.contains(newNode));
    }


    @Test
    public void removeTrue() {
        BitSet bitSet = BitSet.valueOf(new byte[]{(new Integer(4)).byteValue()});
        Node newNode = new PeerNode(new BinarySet(bitSet)); //KEY = 100
        assertTrue(routingTable.add(newNode));
        assertTrue(routingTable.remove(newNode));
        assertFalse(routingTable.contains(newNode));
    }

    @Test
    public void removeFalse() {
        BitSet bitSet = BitSet.valueOf(new byte[]{(new Integer(3)).byteValue()});
        Node newNode = new PeerNode(new BinarySet(bitSet)); //KEY = 011
        assertFalse(routingTable.remove(newNode));
    }

    @Test
    public void getBucket() {
        BitSet bitSet = BitSet.valueOf(new byte[]{(new Integer(5)).byteValue()});
        PeerNode newNode = new PeerNode(new BinarySet(bitSet)); //KEY = 101
        assertTrue(routingTable.add(newNode));
        KBucket bucket = routingTable.getBucket(1);
        assertEquals(newNode, bucket.getOldest());
    }

    @Test
    public void getNodeOwner() {
        assertEquals(nodeOwner, routingTable.getNodeOwner());
    }

    @Test
    public void getLocation() {
        BitSet bitSet = BitSet.valueOf(new byte[]{(new Integer((int) Math.pow(2, NUMBER_BITS_KEY) - 2)).byteValue()});
        Node newNode = new PeerNode(new BinarySet(bitSet)); //KEY = 110
        assertEquals(NUMBER_BITS_KEY - 1, routingTable.getLocation(newNode));
    }

    @Test
    public void getClosest() {
        BitSet bitSet = BitSet.valueOf(new byte[]{(new Integer(6)).byteValue()});
        Node closestNode = new PeerNode(new BinarySet(bitSet)); //KEY = 110
        assertTrue(routingTable.add(closestNode));
        bitSet = BitSet.valueOf(new byte[]{(new Integer(2)).byteValue()});
        Node newNode = new PeerNode(new BinarySet(bitSet)); //KEY = 010
        assertTrue(routingTable.add(newNode));
        bitSet = BitSet.valueOf(new byte[]{(new Integer(1)).byteValue()});
        Node testNode = new PeerNode(new BinarySet(bitSet)); //KEY = 001
        assertTrue(routingTable.add(testNode));
        assertEquals(newNode, routingTable.getClosest(testNode));
    }

    @Test
    public void getKClosest() {
        BitSet bitSet = BitSet.valueOf(new byte[]{(new Integer(6)).byteValue()});
        Node closestNode = new PeerNode(new BinarySet(bitSet)); //KEY = 110
        routingTable.remove(closestNode);

        Node[] nodes = new PeerNode[2];
        bitSet = BitSet.valueOf(new byte[]{(new Integer(1)).byteValue()});
        nodes[0] = new PeerNode(new BinarySet(bitSet)); //KEY = 001
        assertTrue(routingTable.add(nodes[0]));

        bitSet = BitSet.valueOf(new byte[]{(new Integer(2)).byteValue()});
        nodes[1] = new PeerNode(new BinarySet(bitSet)); //KEY = 010
        assertTrue(routingTable.add(nodes[1]));

        bitSet = BitSet.valueOf(new byte[]{(new Integer(4)).byteValue()});
        Node newNode = new PeerNode(new BinarySet(bitSet)); //KEY = 100
        assertTrue(routingTable.add(newNode));

        bitSet = BitSet.valueOf(new byte[]{(new Integer(3)).byteValue()});
        Node nodeTest = new PeerNode(new BinarySet(bitSet)); //KEY = 011

        assertArrayEquals(nodes, routingTable.getKClosest(nodeTest));
    }

}