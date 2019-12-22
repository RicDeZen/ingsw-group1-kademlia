package ingsw.group1.kademlia;

import org.junit.Before;
import org.junit.Test;

import ingsw.group1.msglibrary.SMSPeer;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class KBucketTest {
    int dim = 20;
    KBucket bucket;
    PeerNode node;
    SMSPeer peer = new SMSPeer("+390425678102");
    BinarySet set;

    @Before
    public void Initialize() {
        bucket = new KBucket(dim);
        set = new BinarySet(BitSetUtils.hash(peer.getAddress(), 128));
        node = new PeerNode(set);
        bucket.add(node);
    }

    @Test
    public void KBucket_ContainsPositiveTest() {
        assertTrue(bucket.contains(node));
        set = new BinarySet(BitSetUtils.hash("+39348456789", 128));
        Node anotherNode = new PeerNode(set);

        assertFalse(bucket.contains(anotherNode));
    }

    @Test
    public void KBucket_AddsPositiveTest() {
        PeerNode a = new PeerNode(new BinarySet(BitSetUtils.hash("+393444464789", 128)));

        assertTrue(bucket.add(a));
        assertTrue(bucket.contains(a));
    }

    @Test
    public void KBucket_RemovesPositiveTest() {
        PeerNode a = new PeerNode(new BinarySet(BitSetUtils.hash("+39348456789", 128)));

        assertTrue(bucket.add(a));
        assertTrue(bucket.contains(a));
        assertTrue(bucket.remove(a));
        assertFalse(bucket.contains(a));
    }

    @Test
    public void KBucket_GetsElementsPositiveTest() {
        PeerNode a = new PeerNode(new BinarySet(BitSetUtils.hash("+39348456789", 128)));
        PeerNode b = new PeerNode(new BinarySet(BitSetUtils.hash("+39348676789", 128)));
        PeerNode c = new PeerNode(new BinarySet(BitSetUtils.hash("+39568456789", 128)));

        KBucket newBucket = new KBucket(3);

        newBucket.add(a);
        newBucket.add(b);
        newBucket.add(c);

        Node[] result = newBucket.getElements();

        assertEquals(result[0], a);
        assertEquals(result[1], b);
        assertEquals(result[2], c);
    }

    @Test
    public void KBucket_AddAlreadyPresentTest(){
        assertFalse(bucket.add(node));
    }

    @Test
    public void KBucket_RemovesKeepsSortedPositiveTest() {
        PeerNode a = new PeerNode(new BinarySet(BitSetUtils.hash("+39348456789", 128)));
        PeerNode b = new PeerNode(new BinarySet(BitSetUtils.hash("+39348676789", 128)));
        PeerNode c = new PeerNode(new BinarySet(BitSetUtils.hash("+39568456789", 128)));


        KBucket newBucket = new KBucket(3);

        newBucket.add(a);
        newBucket.add(b);
        newBucket.add(c);

        newBucket.remove(b);

        Node[] elements = newBucket.getElements();

        assertEquals(elements[0], a);
        assertEquals(elements[1], c);
    }

    @Test
    public void KBucket_getOldestPositiveTest() {

        PeerNode a = new PeerNode(new BinarySet(BitSetUtils.hash("+39348456789", 128)));
        PeerNode b = new PeerNode(new BinarySet(BitSetUtils.hash("+39348676789", 128)));
        PeerNode c = new PeerNode(new BinarySet(BitSetUtils.hash("+39568456789", 128)));


        KBucket newBucket = new KBucket(3);

        newBucket.add(a);
        newBucket.add(b);
        newBucket.add(c);

        assertEquals(a, newBucket.getOldest());
    }
}