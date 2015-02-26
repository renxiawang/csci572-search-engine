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

    public BigInteger simHash() {
        // 定义特征向量/数组
        int[] v = new int[this.hashbits];
        // 1、将文本去掉格式后, 分词.
        StringTokenizer stringTokens = new StringTokenizer(this.tokens);
        while (stringTokens.hasMoreTokens()) {
            String temp = stringTokens.nextToken();
            // 2、将每一个分词hash为一组固定长度的数列.比如 64bit 的一个整数.
            BigInteger t = this.hash(temp);
            for (int i = 0; i < this.hashbits; i++) {
                BigInteger bitmask = new BigInteger("1").shiftLeft(i);
                // 3、建立一个长度为64的整数数组(假设要生成64位的数字指纹,也可以是其它数字),
                // 对每一个分词hash后的数列进行判断,如果是1000...1,那么数组的第一位和末尾一位加1,
                // 中间的62位减一,也就是说,逢1加1,逢0减1.一直到把所有的分词hash数列全部判断完毕.
                if (t.and(bitmask).signum() != 0) {
                    // 这里是计算整个文档的所有特征的向量和
                    // 这里实际使用中需要 +- 权重，而不是简单的 +1/-1，
                    v[i] += 1;
                } else {
                    v[i] -= 1;
                }
            }
        }
        BigInteger fingerprint = new BigInteger("0");
        StringBuffer simHashBuffer = new StringBuffer();
        for (int i = 0; i < this.hashbits; i++) {
            // 4、最后对数组进行判断,大于0的记为1,小于等于0的记为0,得到一个 64bit 的数字指纹/签名.
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

        // 统计x中二进制位数为1的个数
        // 我们想想，一个二进制数减去1，那么，从最后那个1（包括那个1）后面的数字全都反了，对吧，然后，n&(n-1)就相当于把后面的数字清0，
        // 我们看n能做多少次这样的操作就OK了。

        while (x.signum() != 0) {
            tot += 1;
            x = x.and(x.subtract(new BigInteger("1")));
        }
        return tot;
    }

    public List subByDistance(SimHash simHash, int distance) {
        // 分成几组来检查
        int numEach = this.hashbits / (distance + 1);
        List characters = new ArrayList();

        StringBuffer buffer = new StringBuffer();

        int k = 0;
        for (int i = 0; i < this.intSimHash.bitLength(); i++) {
            // 当且仅当设置了指定的位时，返回 true
            boolean sr = simHash.intSimHash.testBit(i);

            if (sr) {
                buffer.append("1");
            } else {
                buffer.append("0");
            }

            if ((i + 1) % numEach == 0) {
                // 将二进制转为BigInteger
                BigInteger eachValue = new BigInteger(buffer.toString(), 2);
                //System.out.println("----" + eachValue);
                buffer.delete(0, buffer.length());
                characters.add(eachValue);
            }
        }
        return characters;
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

}