package ingsw.group1.kademlia;

import android.util.Log;

import androidx.annotation.NonNull;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.BitSet;

public class BitSetUtils {

    private static int minLength = 64;
    private static final String NOT_VALID_NUMBIT_EXCEPTION_MSG = "numBits isn't > 0 or a multiple of 64";
    private static final String NOT_VALID_NUMBIT_SHA_EXCEPTION_MSG = "numBits isn't > 0 or <= 160";
    private static final String INVALID_HEX_CHAR_MSG = "Invalid Hexadecimal Character: ";
    private static final String INVALID_HEX_STRING_MSG="Invalid hexadecimal String supplied.";
    private static final String UTILS_TAG = BitSetUtils.class.toString();
    private static final String SHA_1 = "SHA-1";

    /**
     * @param
     * @return The distance of the Keys, calculated in XOR logic
     */
    public static BitSet distanceFrom(@NonNull BitSet first, @NonNull BitSet second) {
        BitSet distance = (BitSet) first.clone();
        distance.xor(second);
        return distance;
    }

    /**
     * @param toHash  the String (a key) of which generate hashcode
     * @param numBits number of bits that the hash code will be constituted of, must be multiple of 64 and >0
     * @return the bitSet containing the hash of the peer's address, bitSet's length is a multiple of 64
     * @throws IllegalArgumentException if peer isn't valid or the numBit isn't multiple of 64
     */
    public static BitSet hash(@NonNull String toHash, int numBits) {
        if (numBits > 0 && numBits % minLength == 0) {
            int numLong = numBits / minLength;
            long[] numbers = new long[numLong];
            for (int i = 0; i < numLong; i++) {
                numbers[numLong - (i + 1)] = Double.doubleToLongBits(hash(toHash) / (i + 1));
            }
            return BitSet.valueOf(numbers);
        } else throw new IllegalArgumentException(NOT_VALID_NUMBIT_EXCEPTION_MSG);
    }

    /**
     * @param toHash  the String (a key) of which generate hashcode
     * @param numBits number of bits that the hash code will be constituted of, must be > 0 &&  <= 160
     * @return the bitSet containing the hash (SHA-1) of the bytes in input, truncated to numBits, bitSet's size is <= numBits
     * @throws IllegalArgumentException if peer isn't valid or the numBit isn't in its bounds
     */
    public static BitSet hash(@NonNull byte[] toHash, int numBits) {
        if(numBits > 0 && numBits <= 160) {
            byte[] digest = {0};
            try {
                MessageDigest md = MessageDigest.getInstance(SHA_1);
                md.update(toHash);
                digest = md.digest();
            } catch (NoSuchAlgorithmException e) {
                Log.e(UTILS_TAG, e.getMessage());
                //Shouldn't happen, if it happens: report to log
            }
            if (digest.length * 8 > numBits) {
                byte[] trunk = new byte[(numBits % 8 > 0) ? numBits / 8 + 1 : numBits / 8];
                System.arraycopy(digest, 0, trunk, 0, trunk.length);
                digest = trunk;
            }
            return BitSet.valueOf(digest);
        }
        throw new  IllegalArgumentException(NOT_VALID_NUMBIT_SHA_EXCEPTION_MSG);
    }

    /**
     * Based on String.hashCode()
     *
     * @param key string of which generate hash code on 64 bits
     * @return a 64 bits hash of the given String
     */
    private static long hash(@NonNull String key) {
        long num = 0;
        int n = key.length();
        for (int i = 0; i < n; i++) {
            num += key.charAt(i) * (63 ^ (n - (i + 1))); //original has 31 instead of 63
        }
        return num;
    }

    /**
     * Method to compare two bitSets, useful to compare Distances
     * compare(b1.xor(b2), b1.xor(b3)) ==> compare(D(b1, b2), D(b1, b3))
     * ==> D(b1, b2) ? D(b1, b3)
     * That is: is b1 closer to b2 or b3?
     *
     * @param lhs first bitSet
     * @param rhs second bitSet
     * @return a negative integer, zero, or a positive integer as rhs < lhs, rhs = lhs, rhs > lhs.
     */
    public static int compare(@NonNull BitSet lhs, @NonNull BitSet rhs) {
        if (lhs.equals(rhs)) return 0;
        BitSet distance = (BitSet) lhs.clone();
        distance.xor(rhs);
        int firstDifferent = distance.length() - 1;
        if (firstDifferent == -1)
            return 0; //redundant, won't be executed
        return rhs.get(firstDifferent) ? 1 : -1;
    }

    /**
     * @param hashInBytes byte array to convert to HEX on a String
     * @return the byte array converted to Hex written on a String (lowercase)
     */
    private static String bytesToHex(@NonNull byte[] hashInBytes) {
        StringBuilder sb = new StringBuilder();
        for (byte b : hashInBytes) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }

    /**
     *
     * @param hash the given BitSet to convert to HEX on a String
     * @return the given BitSet converted to Hex written on a String
     */
    public static String BitSetsToHex(BitSet hash){
        return bytesToHex(hash.toByteArray());
    }

    /**
     *
     * @param hexString
     * @return
     * @throws IllegalArgumentException, if the String length isn't multiple of 2, of contains invalid HEX string
     */
    public static BitSet decodeHexString(String hexString) {
        hexString = hexString.toLowerCase();
        if (hexString.length() % 2 == 1) {
            throw new IllegalArgumentException(INVALID_HEX_STRING_MSG);
        }

        byte[] bytes = new byte[hexString.length() / 2];
        for (int i = 0; i < hexString.length(); i += 2) {
            bytes[i / 2] = hexToByte(hexString.substring(i, i + 2));
        }
        return BitSet.valueOf(bytes);
    }

    /**
     *
     * @param hexString a string of length 2 containing an HEX number of two digits
     * @return the converted HEX to byte
     * @throws IllegalArgumentException if the string does not contain valid HEX digits
     */
    private static byte hexToByte(@NonNull String hexString) {
        int firstDigit = toDigit(hexString.charAt(0));
        int secondDigit = toDigit(hexString.charAt(1));
        return (byte) ((firstDigit << 4) + secondDigit);
    }

    /**
     *
     * @param hexChar the given char to convert
     * @return the char converted to int
     * @throws IllegalArgumentException if char is invalid
     */
    private static int toDigit(char hexChar) {
        int digit = Character.digit(hexChar, 16);
        if(digit == -1) {
            throw new IllegalArgumentException(INVALID_HEX_CHAR_MSG + hexChar);
        }
        return digit;
    }



}
