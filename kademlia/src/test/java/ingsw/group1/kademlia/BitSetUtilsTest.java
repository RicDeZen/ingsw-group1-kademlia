package ingsw.group1.kademlia;

import org.junit.Before;
import org.junit.Test;

import java.util.BitSet;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

public class BitSetUtilsTest {
    public static String[] params = new String[]{
            "+390425678102",
            "+39348456789",
            "+393444464789",
            "+397744464789",
            "+39348676789",
            "+39568456789"
    };
    private BitSet[] resultingKeysStringHash = new BitSet[params.length];
    private BitSet[] resultingKeysSHA = new BitSet[params.length];
    private final String[] HEX ={
            "00",
            "01",
            "02",
            "03",
            "04",
            "05",
            "06",
            "07",
            "08",
            "09",
            "0a",
            "0b",
            "0c",
            "0d",
            "0e",
            "0f"
    };


    @Before
    public void Init() {
        for (int i = 0; i < params.length; i++) {
            resultingKeysStringHash[i] = BitSetUtils.hash(params[i], 128);
            resultingKeysSHA[i] = BitSetUtils.hash(params[i].getBytes(), 120);
        }
    }


    @Test
    public void BitSetUtils_StringHashAllDIFFTest() {
        //All generated Keys must be different
        for (int i = 0; i < resultingKeysStringHash.length; i++)
            for (int j = 0; j < resultingKeysStringHash.length; j++)
                if (i != j)
                    assertNotEquals(resultingKeysStringHash[i], resultingKeysStringHash[j]);

    }

    @Test(expected = IllegalArgumentException.class)
    public void BitSetUtils_StringHashWrongNumBitTest() {
        BitSetUtils.hash("Qwerty", 0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void BitSetUtils_StringHashNumBitNotMultipleTest() {
        BitSetUtils.hash("Qwerty", 62);
    }

    @Test(expected = IllegalArgumentException.class)
    public void BitSetUtils_SHAZeroNumBitTest() {
        BitSetUtils.hash("Qwerty".getBytes(), 0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void BitSetUtils_SHATooManyNumBitTest() {
        BitSetUtils.hash("Qwerty".getBytes(), 170);
    }

    @Test
    public void BitSetUtils_SHA_AllDIFFTest() {
        //All generated Keys must be different
        for (int i = 0; i < resultingKeysSHA.length; i++)
            for (int j = 0; j < resultingKeysSHA.length; j++)
                if (i != j)
                    assertNotEquals(resultingKeysSHA[i], resultingKeysSHA[j]);

    }

    @Test
    public void BitSetUtils_CompareEqualSetsTest() {
        for (BitSet resultingKey : resultingKeysSHA) {
            assertTrue(BitSetUtils.compare(resultingKey, resultingKey) == 0);
        }
    }

    @Test
    public void BitSetUtils_DistanceZeroForEqualSetsTest() {
        for (BitSet resultingKey : resultingKeysSHA) {
            assertEquals(BitSetUtils.distanceFrom(resultingKey, resultingKey), BitSet.valueOf("".getBytes()));
        }
    }

    @Test
    public void PeerNode_compareDistancePositiveTest() {
        BitSet A;
        BitSet B = new BitSet(128); //000...000
        B.set(0, 2); //...00011
        BitSet C = new BitSet(128); //000...000
        C.set(0); //...0001
        for (BitSet resultingKey : resultingKeysSHA) {
            A = (BitSet) resultingKey.clone();
            assertEquals(BitSetUtils.compare(A, B), BitSetUtils.compare(BitSetUtils.distanceFrom(A, C), BitSetUtils.distanceFrom(B, C)));
            assertEquals(BitSetUtils.compare(B, A), BitSetUtils.compare(BitSetUtils.distanceFrom(B, C), BitSetUtils.distanceFrom(A, C)));
            assertEquals(BitSetUtils.compare(A, A), BitSetUtils.compare(BitSetUtils.distanceFrom(A, C), BitSetUtils.distanceFrom(A, C)));
        }
    }
    @Test
    public void PeerNode_BitSetToHexTest() {
        BitSet A;
        A = BitSet.valueOf(new byte[]{(new Integer(16)).byteValue()});

        assertEquals("10", BitSetUtils.BitSetsToHex(A));

        for (int i = 1; i < HEX.length; i++){
            byte[] binary = new byte[]{(new Integer(i)).byteValue()};
            A = BitSet.valueOf(binary);
            assertEquals(HEX[i], BitSetUtils.BitSetsToHex(A));
        }
    }

    @Test
    public void PeerNode_HexToBitSetTest(){
        BitSet A;

        for (int i = 1; i < HEX.length; i++){
            byte[] binary = new byte[]{(new Integer(i)).byteValue()};
            A = BitSet.valueOf(binary);
            assertEquals(A, BitSetUtils.decodeHexString(HEX[i]));
        }
    }

}