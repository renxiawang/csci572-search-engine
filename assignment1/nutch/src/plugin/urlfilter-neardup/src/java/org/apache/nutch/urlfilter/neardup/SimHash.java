package org.apache.nutch.urlfilter.neardup;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.StringTokenizer;

class SimHashMap {
    private static final Logger LOG = LoggerFactory
            .getLogger(SimHashMap.class);

    private static ArrayList<HashMap<BigInteger, ArrayList<SimHash>>> maps = new ArrayList<HashMap<BigInteger, ArrayList<SimHash>>>();

    private int hashbits = 64;
    private int distance = 3;

    public SimHashMap(int hashbits, int distance) {
        this.distance = distance;
        this.hashbits = hashbits;
        for (int i = 0; i < distance + 1; i++) {
            HashMap<BigInteger, ArrayList<SimHash>> _m = new HashMap<BigInteger, ArrayList<SimHash>>();
            maps.add(_m);
        }
    }

    // dup return true/ else return false false
    public boolean isDuplicate(int group, SimHash newHash) {
        List chs = newHash.subByDistance(newHash, distance);
        if(group >= chs.size()) return false;
        BigInteger key = (BigInteger) chs.get(group);
        if (maps.get(group).containsKey(key)) {
            ArrayList<SimHash> l = maps.get(group).get(key);
            for (int i = 0; i < l.size(); i++) {
                if (newHash.hammingDistance(l.get(i)) <= distance) {
                    return true;
                }
            }
            maps.get(group).get(key).add(newHash);
            return false;
        } else {
            ArrayList<SimHash> _s = new ArrayList<SimHash>();
            maps.get(group).put(key, _s);
            maps.get(group).get(key).add(newHash);
            return false;
        }
    }

    public boolean isDuplicate(SimHash newHash) {
        for (int i = 0; i < distance + 1; i++) {
            if (isDuplicate(i, newHash)) {
                return true;
            }
        }
        return false;
    }

    public boolean isDuplicate(String s) {
        LOG.info("size of simhash map: " + maps.size());
        SimHash newHash = new SimHash(s, 64);
        return isDuplicate(newHash);
    }
}

public class SimHash {

    private String tokens;
    private BigInteger intSimHash;
    private String strSimHash;
    private int hashbits = 64;

    public SimHash(String tokens, int hashbits) {
        this.tokens = tokens;
        this.hashbits = hashbits;
        this.intSimHash = this.simHash();
    }

    public static void main(String[] args) {

        //args simhash is 64 bits and use distance 3
        SimHashMap simHashMap = new SimHashMap(64, 3);

        String s = "This is a test string for testing";
        SimHash hash1 = new SimHash(s, 64);
        System.out.println(simHashMap.isDuplicate(hash1));
        System.out.println(simHashMap.isDuplicate(s));

        s = "This is a test string for testing, This is a test string for testing abcdef";
        SimHash hash2 = new SimHash(s, 64);

        System.out.println(simHashMap.isDuplicate(hash2));
        System.out.println(simHashMap.isDuplicate(s));

        s = "This is a test string for testing als";
        SimHash hash3 = new SimHash(s, 64);
        System.out.println(simHashMap.isDuplicate(hash3));
        System.out.println(simHashMap.isDuplicate(s));

        s = "taaawjljerlmflasjflsfjlkadjlfj  ffajlafjldf";
        SimHash hash4 = new SimHash(s, 64);
        System.out.println(simHashMap.isDuplicate(hash4));
        System.out.println(simHashMap.isDuplicate(s));

    }

    public BigInteger simHash() {
        // Defining feature vector/array
        int[] v = new int[this.hashbits];
        // 1、removing the text format, word segmentation
        StringTokenizer stringTokens = new StringTokenizer(this.tokens);
        while (stringTokens.hasMoreTokens()) {
            String temp = stringTokens.nextToken();
            // 2、Each word segmentation hash as a set of fixed length sequence. For example, a 64 - bit integer.
            BigInteger t = this.hash(temp);
            for (int i = 0; i < this.hashbits; i++) {
                BigInteger bitmask = new BigInteger("1").shiftLeft(i);
                // 3, to establish a length of 64 integer array (assuming to generate a 64 - bit digital fingerprint, may also be other Numbers).
				// for each word after the hash sequence to judge, if it is 1000... 1, the array at the end of the first and a + 1,
				// in the middle of the 62 minus one, that is to say, every 1 plus 1, 0 as the minus 1. Until all the participle hash sequence judgement completely.
                if (t.and(bitmask).signum() != 0) {
                    // here is to calculate all the feature vector and the entire document
					//need + - weights in the actual use, rather than simply + 1 / - 1,
                    v[i] += 1;
                } else {
                    v[i] -= 1;
                }
            }
        }
        BigInteger fingerprint = new BigInteger("0");
        StringBuffer simHashBuffer = new StringBuffer();
        for (int i = 0; i < this.hashbits; i++) {
            // 4、Finally to judge array, greater than 0 to 1, less than or equal to 0 to 0, remember to get a 64 - bit digital fingerprint/signature.
            if (v[i] >= 0) {
                fingerprint = fingerprint.add(new BigInteger("1").shiftLeft(i));
                simHashBuffer.append("1");
            } else {
                simHashBuffer.append("0");
            }
        }
        this.strSimHash = simHashBuffer.toString();
        //System.out.println(this.strSimHash + " length "+ this.strSimHash.length());
        return fingerprint;
    }

    private BigInteger hash(String source) {
        if (source == null || source.length() == 0) {
            return new BigInteger("0");
        } else {
            char[] sourceArray = source.toCharArray();
            BigInteger x = BigInteger.valueOf(((long) sourceArray[0]) << 7);
            BigInteger m = new BigInteger("1000003");
            BigInteger mask = new BigInteger("2").pow(this.hashbits).subtract(
                    new BigInteger("1"));
            for (char item : sourceArray) {
                BigInteger temp = BigInteger.valueOf((long) item);
                x = x.multiply(m).xor(temp).and(mask);
            }
            x = x.xor(new BigInteger(String.valueOf(source.length())));
            if (x.equals(new BigInteger("-1"))) {
                x = new BigInteger("-2");
            }
            return x;
        }
    }

    public int hammingDistance(SimHash other) {

        BigInteger x = this.intSimHash.xor(other.intSimHash);
        int tot = 0;

        // statistics the number of binary digits 1 x
		// we think about it, a binary number minus 1, so, from that last 1 (including the 1) all the Numbers behind the counter, right, then, n & behind (n - 1) is equivalent to the Numbers 0,
		// we see n how many times can do such operations with respect to OK.

        while (x.signum() != 0) {
            tot += 1;
            x = x.and(x.subtract(new BigInteger("1")));
        }
        return tot;
    }

    public List subByDistance(SimHash simHash, int distance) {
        // devide into several groups to check
        int numEach = this.hashbits / (distance + 1);
        List characters = new ArrayList();

        StringBuffer buffer = new StringBuffer();

        int k = 0;
        for (int i = 0; i < this.intSimHash.bitLength(); i++) {
            // If and only if set the specified, returns true
            boolean sr = simHash.intSimHash.testBit(i);

            if (sr) {
                buffer.append("1");
            } else {
                buffer.append("0");
            }

            if ((i + 1) % numEach == 0) {
                // turn binary to BigInteger 
                BigInteger eachValue = new BigInteger(buffer.toString(), 2);
                //System.out.println("----" + eachValue);
                buffer.delete(0, buffer.length());
                characters.add(eachValue);
            }
        }
        return characters;
    }

}