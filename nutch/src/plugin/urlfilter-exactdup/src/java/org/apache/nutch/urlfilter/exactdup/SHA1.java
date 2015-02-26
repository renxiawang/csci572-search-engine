package org.apache.nutch.urlfilter.exactdup;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.MessageDigest;
import java.util.TreeSet;

/**
 * author: Zhenni Huang
 */
public class SHA1 {
    private static final Logger LOG = LoggerFactory
            .getLogger(SHA1.class);

    public static TreeSet<String> sha1Set = new TreeSet<String>();

    public final static String getSHA1(String s) {
        char hexDigits[] = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
                'A', 'B', 'C', 'D', 'E', 'F'};
        try {
            byte[] btInput = s.getBytes();
            // 获得MD5摘要算法的 MessageDigest 对象
            MessageDigest mdInst = MessageDigest.getInstance("SHA-1");
            // 使用指定的字节更新摘要
            mdInst.update(btInput);
            // 获得密文
            byte[] md = mdInst.digest();
            // 把密文转换成十六进制的字符串形式
            int j = md.length;
            char str[] = new char[j * 2];
            int k = 0;
            for (int i = 0; i < j; i++) {
                byte byte0 = md[i];
                str[k++] = hexDigits[byte0 >>> 4 & 0xf];
                str[k++] = hexDigits[byte0 & 0xf];
            }
            return new String(str);
        } catch (Exception e) {
            LOG.error("Failed to compute SHA1 for string: " + s, e);
            return null;
        }
    }

    public static boolean isDuplicate(String s) {
        LOG.info("size of sha1 set: " + sha1Set.size());
        String _s = SHA1.getSHA1(s);
        if (sha1Set.contains(_s)) {
            return true;
        }
        sha1Set.add(_s);
        return false;
    }

    public static void main(String[] args) {
        System.out.println(isDuplicate("test"));
        System.out.println(isDuplicate("test"));
        System.out.println(isDuplicate("test123"));
        System.out.println(isDuplicate("test213"));
        System.out.println(isDuplicate("test123"));
    }
}