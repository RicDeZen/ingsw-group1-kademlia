package ingsw.group1.kademlia;

import org.junit.Before;
import org.junit.Test;

import java.util.BitSet;

import ingsw.group1.repnetwork.StringResource;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertTrue;

public class ResourceNodeTest {

    ResourceNode testNode;
    StringResource testResource;

    @Before
    public void Initialize(){
        testResource = new StringResource("This is a valid Resource Name", "This is a valid Resource Value");
        testNode = new ResourceNode(new BinarySet(BitSetUtils.hash(testResource.getName(), 128)), testResource.getValue());
    }

    @Test(expected = IllegalArgumentException.class)
    public void ResourceNode__Constructor_ResourceIllegalArgumentExceptionTest(){
        StringResource resource = new StringResource("", "I'm invalid"); //Empty
        BinarySet set = new BinarySet(BitSetUtils.hash(resource.getName(), 128));
        ResourceNode node = new ResourceNode(set, resource.getValue());
    }

    @Test
    public void ResourceNode_getNameTest() {
        assertEquals(testNode.getName(), new BinarySet(BitSetUtils.hash(testResource.getName(), 128)));
    }

    @Test
    public void ResourceNode_getValueTest() {
        assertEquals(testNode.getValue(), testResource.getValue());
    }


    @Test
    public void ResourceNode__distanceFromTest(){
        BitSet A = BitSetUtils.hash(testResource.getName(), 128);

        StringResource resource = new StringResource("Casual Name", "Some info");
        BitSet B = BitSetUtils.hash(resource.getName(), testNode.keyLength());
        ResourceNode newResourceNode = new ResourceNode(new BinarySet(B), resource.getValue());

        A.xor(B);
        assertEquals(A, testNode.getDistance(newResourceNode).getKey());
        assertEquals(A, newResourceNode.getDistance(testNode).getKey());
    }

    @Test
    public void ResourceNode_isValidPositiveTest(){
        assertTrue(testNode.isValid());
        BitSet A = BitSetUtils.hash(testResource.getName(), testNode.keyLength());
        ResourceNode newResNode = new ResourceNode(new BinarySet(A), testResource.getValue());
        assertTrue(newResNode.isValid());
    }

    @Test
    public void ResourceNode_equalsPositiveTest(){
        ResourceNode newResNode = new ResourceNode(testNode.getKey(), "This is value. No, this is Patrick");
        assertEquals(testNode, newResNode);
    }

    @Test
    public void ResourceNode_ClonePositiveTest() {
        assertNotSame(testNode, testNode.clone());
    }

    @Test
    public void ResourceNode_EqualsItselfPositiveTest(){
        assertEquals(testNode, testNode);
    }

    @Test
    public void ResourceNode_EqualsNullNegativeTest(){
        assertNotEquals(testNode, null);
    }

    @Test
    public void ResourceNode_EqualsDiffTypeNegativeTest(){
        assertNotEquals(testNode, 7);
    }
}