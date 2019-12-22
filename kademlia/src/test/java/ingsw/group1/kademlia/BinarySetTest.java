package ingsw.group1.kademlia;

import org.junit.Before;
import org.junit.Test;

import java.util.BitSet;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertTrue;

public class BinarySetTest {
    BinarySet binarySet;
    BitSet bitSet;
    String testString = "This is random";
    String empty = "";

    @Before
    public void Init() {
        bitSet = BitSetUtils.hash(testString.getBytes(), 120);
        binarySet = new BinarySet(bitSet);
    }

    @Test(expected = IllegalArgumentException.class)
    public void BinarySet_ConstructorExceptionTest() {
        BinarySet set = new BinarySet(new BitSet(0));
    }

    @Test
    public void BinarySet_CompareEqualsTest() {
        BinarySet second = new BinarySet(BitSetUtils.hash(testString.getBytes(), 120));
        assertEquals(binarySet, second);
        assertEquals(binarySet, binarySet);
        assertTrue(binarySet.compareTo(new BinarySet(binarySet.getKey())) == 0);
    }

    @Test
    public void BinarySet_ComparePositiveTest() {
        BinarySet second = new BinarySet(BitSetUtils.hash(empty.getBytes(), 120));
        assertTrue(binarySet.compareTo(second) > 0);
    }

    @Test
    public void BinarySet_CompareNullNegativeTest() {
        assertNotEquals(binarySet, null);
    }

    @Test
    public void BinarySet_ClonePositiveTest() {
        assertNotSame(binarySet, binarySet.clone());
    }

    @Test
    public void BinarySet_KeyLengthPositiveTest() {
        assertEquals(binarySet.keyLength(), bitSet.size());
    }

    @Test
    public void BinarySet_DifferentnotEqualTest() {
        BitSet set = BitSetUtils.hash("Calogero".getBytes(), 120);
        BinarySet B = new BinarySet(set);
        assertNotEquals(binarySet, B);
    }

    @Test
    public void BinarySet_DifferentTypesnotEqualTest() {
        assertNotEquals(binarySet, 4);
    }

    @Test
    public void BinarySet_ToHexTest() {
        String hexString = binarySet.toHex();
        BinarySet testSet = new BinarySet(hexString);
        assertEquals(binarySet, testSet);
    }

    @Test(expected = IllegalArgumentException.class)
    public void BinarySet_HexToBinaryNegativeEmptyStrTest() {
        BinarySet a = new BinarySet("");
    }

    @Test(expected = IllegalArgumentException.class)
    public void BinarySet_HexToBinaryNegativeInvalidStrTest() {
        BinarySet a = new BinarySet("QQLL0000");
    }

    @Test(expected = IllegalArgumentException.class)
    public void BinarySet_HexToBinaryNegativeInvalidNumberDigitsTest() {
        BinarySet a = new BinarySet("AADDF");
    }

}