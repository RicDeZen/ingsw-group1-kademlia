package ingsw.group1.kademlia;

import org.junit.Before;
import org.junit.Test;

import java.util.BitSet;

import ingsw.group1.msglibrary.SMSPeer;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertTrue;

public class PeerNodeTest {

    PeerNode testNode;
    SMSPeer testPeer;

    @Before
    public void initialize(){
        testPeer = new SMSPeer("+390425678123"); //Valid number
        testNode = new PeerNode(new BinarySet(BitSetUtils.hash(testPeer.getAddress(), 64)));
    }

    @Test
    public void PeerNode_Constructor_NoExceptionTest(){
        SMSPeer peer = new SMSPeer("+390425666001");
        PeerNode node = new PeerNode(new BinarySet(BitSetUtils.hash(peer.getAddress(), 64)));
    }

    @Test(expected = IllegalArgumentException.class)
    public void PeerNode_Constructor_PeerIllegalArgumentExceptionTest(){
        SMSPeer peer = new SMSPeer("This is invalid");
        PeerNode node = new PeerNode(new BinarySet(BitSetUtils.hash(peer.getAddress(), 64)));
    }

    @Test
    public void PeerNode_ConstructorBitSetTest(){
        BitSet a = new BitSet(128);
        a.set(0, 4);
        BinarySet set = new BinarySet(a);
        PeerNode node = new PeerNode(set);
        assertEquals(a, node.getAddress().getKey());
        assertEquals(set, node.getAddress());
    }


    @Test
    public void PeerNode_distanceFromTest(){
        BitSet A = BitSetUtils.hash(testPeer.getAddress(), testNode.keyLength());

        SMSPeer newPeer = new SMSPeer("+390425667007");
        PeerNode newPeerNode = new PeerNode(new BinarySet(BitSetUtils.hash(newPeer.getAddress(), testNode.keyLength())));
        BitSet B = BitSetUtils.hash(newPeer.getAddress(), testNode.keyLength());

        A.xor(B);
        assertEquals(A, testNode.getDistance(newPeerNode).getKey());
        assertEquals(A, newPeerNode.getDistance(testNode).getKey());
    }


    @Test
    public void PeerNode_compareDistancePositiveTest(){
        BitSet A = BitSetUtils.hash(testPeer.getAddress(), testNode.keyLength());

        SMSPeer newPeer = new SMSPeer("+390425667007");
        PeerNode newPeerNode = new PeerNode(new BinarySet(BitSetUtils.hash(newPeer.getAddress(), testNode.keyLength())));
        BitSet B = BitSetUtils.hash(newPeer.getAddress(), testNode.keyLength());

        //A = testNode.getAddress(), B = newPeerNode.getAddress()

        BitSet C = new BitSet(testNode.keyLength()); //000...000
        C.set(0,4); //...00011111

        PeerNode confrontNode = new PeerNode(new BinarySet(C));

        A.xor(C);
        B.xor(C);

        assertEquals(BitSetUtils.compare(A,B), BitSetUtils.compare(confrontNode.getDistance(testNode).getKey(), confrontNode.getDistance(newPeerNode).getKey()));
        assertEquals(BitSetUtils.compare(B,A), BitSetUtils.compare(confrontNode.getDistance(newPeerNode).getKey(), confrontNode.getDistance(testNode).getKey()));
        assertEquals(BitSetUtils.compare(A,A), BitSetUtils.compare(confrontNode.getDistance(testNode).getKey(), confrontNode.getDistance(testNode).getKey()));
    }

    @Test
    public void PeerNode_isValidPositiveTest() {
        assertTrue(testNode.isValid());
        BitSet A = BitSetUtils.hash(testPeer.getAddress(), testNode.keyLength());
        PeerNode newPeerNode = new PeerNode(new BinarySet(A));
        assertTrue(newPeerNode.isValid());
    }

    @Test
    public void PeerNode_ClonePositiveTest() {
        assertNotSame(testNode, testNode.clone());
    }


    @Test
    public void PeerNode_SetpeerPositiveTest(){
        testNode.setPhysicalPeer(testPeer);
        assertEquals(testNode.getPhysicalPeer(), testPeer);
    }

    @Test
    public void PeerNode_CompareNullNegativeTest(){
        assertNotEquals(testPeer, null);
    }

    @Test
    public void PeerNode_CompareDiffTypeNegativeTest(){
        assertNotEquals(testPeer, 4);
    }
}