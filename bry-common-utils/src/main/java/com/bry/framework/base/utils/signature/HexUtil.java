package com.bry.framework.base.utils.signature;

/**
 * 16进制转换工具类
 *
 * @author luochengsong
 * @date 2021/4/15 0015
 */
public class HexUtil {

    private static final char[] HEX_DIGITS = new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};

    private HexUtil() {
    }

    public static byte[] hexToBytes(String str) {
        if (str.length() < 1) {
            return null;
        } else {
            byte[] result = new byte[str.length() / 2];

            for (int i = 0; i < str.length() / 2; ++i) {
                int heigh = Integer.parseInt(str.substring(i * 2, i * 2 + 1), 16);
                int low = Integer.parseInt(str.substring(i * 2 + 1, i * 2 + 2), 16);
                result[i] = (byte) (heigh * 16 + low);
            }

            return result;
        }
    }

    public static String bytesToHex(byte[] bytes) {
        StringBuilder buf = new StringBuilder(bytes.length * 2);

        for (int j = 0; j < bytes.length; ++j) {
            buf.append(HEX_DIGITS[bytes[j] >> 4 & 15]);
            buf.append(HEX_DIGITS[bytes[j] & 15]);
        }

        return buf.toString();
    }
}
