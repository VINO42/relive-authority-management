package com.relive.utils;

import java.io.*;

/**
 * 对称加密
 *
 * @Author ReLive
 * @Date 2021/3/26-21:51
 */
public class RC4Encrypt {
    private static final int BOX_LEN = 256;

    private static final String RC4_FIRST_KEY = "~Dy@&sbg#3$1?:2%";

    private static final String CHARSET = "UTF-8";

    private static final String DEFAULT_PWD = "dbApp1q2w3e!Q@W#E";

    public static String encrypt(String src) {
        return encrypt(src, "dbApp1q2w3e!Q@W#E");
    }

    public static String decrypt(String src) {
        return decrypt(src, "dbApp1q2w3e!Q@W#E");
    }

    public static String encrypt(String source, String passWord) {
        byte[] srcByte, pwdByte;
        if (source == null || passWord == null)
            return null;
        String pwStr = "~Dy@&sbg#3$1?:2%" + passWord;
        try {
            srcByte = source.getBytes("UTF-8");
            pwdByte = pwStr.getBytes("UTF-8");
        } catch (UnsupportedEncodingException e) {
            srcByte = source.getBytes();
            pwdByte = pwStr.getBytes();
        }
        int sourceLength = srcByte.length;
        byte[] ret = new byte[sourceLength];
        int retLen = doRC4(srcByte, sourceLength, pwdByte, pwdByte.length, ret);
        if (retLen == 0)
            return null;
        return byteToHex(ret, retLen);
    }

    public static String decrypt(String source, String passWord) {
        byte[] pwdByte;
        if (source == null || source.length() % 2 != 0 || passWord == null)
            return null;
        String pwStr = "~Dy@&sbg#3$1?:2%" + passWord;
        byte[] src = hexToByte(source);
        byte[] ret = new byte[source.length() / 2];
        try {
            pwdByte = pwStr.getBytes("UTF-8");
        } catch (UnsupportedEncodingException e) {
            pwdByte = pwStr.getBytes();
        }
        int retLen = doRC4(src, source.length() / 2, pwdByte, pwdByte.length, ret);
        if (retLen == 0)
            return "";
        try {
            return new String(ret, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            return new String(ret);
        }
    }

    private static String byteToHex(byte[] vByte, int vLen) {
        if (vLen == 0)
            return null;
        byte[] tmp = new byte[vLen * 2];
        for (int i = 0; i < vLen; i++) {
            int tmp2 = unsignedByte(vByte[i]) / 16;
            tmp[i * 2] = (byte) (tmp2 + ((tmp2 > 9) ? 55 : 48));
            tmp2 = unsignedByte(vByte[i]) % 16;
            tmp[i * 2 + 1] = (byte) (tmp2 + ((tmp2 > 9) ? 55 : 48));
        }
        try {
            return new String(tmp, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            return new String(tmp);
        }
    }

    private static byte[] hexToByte(String hex) {
        byte[] tmp;
        if (hex == null)
            return null;
        int iLen = hex.length();
        if (iLen <= 0 || iLen % 2 != 0)
            return null;
        byte[] pdBuf = new byte[iLen / 2];
        try {
            tmp = hex.getBytes("UTF-8");
        } catch (UnsupportedEncodingException e) {
            tmp = hex.getBytes();
        }
        for (int i = 0; i < iLen / 2; i++) {
            int tmp1 = tmp[i * 2] - ((tmp[i * 2] >= 65) ? 55 : 48);
            if (tmp1 >= 16)
                return null;
            int tmp2 = tmp[i * 2 + 1] - ((tmp[i * 2 + 1] >= 65) ? 55 : 48);
            if (tmp2 >= 16)
                return null;
            pdBuf[i] = (byte) (tmp1 * 16 + tmp2);
        }
        return pdBuf;
    }

    private static int doRC4(byte[] data, int dataLen, byte[] key, int keyLen, byte[] out) {
        if (data == null || key == null || out == null)
            return 0;
        byte[] mBox = new byte[256];
        if (getKey(key, keyLen, mBox) == 0)
            return 0;
        int x = 0;
        int y = 0;
        for (int k = 0; k < dataLen; k++) {
            x = (x + 1) % 256;
            y = (unsignedByte(mBox[x]) + y) % 256;
            swapByte(mBox, x, y);
            out[k] = (byte) (data[k] ^ mBox[(unsignedByte(mBox[x]) + unsignedByte(mBox[y])) % 256]);
        }
        return dataLen;
    }

    private static int getKey(byte[] pass, int passLen, byte[] out) {
        if (pass == null || out == null)
            return 0;
        int i;
        for (i = 0; i < 256; i++)
            out[i] = (byte) i;
        int j = 0;
        for (i = 0; i < 256; i++) {
            int tmp = out[i];
            if (tmp < 0)
                tmp += 256;
            j = (pass[i % passLen] + tmp + j) % 256;
            swapByte(out, i, j);
        }
        return -1;
    }

    private static void swapByte(byte[] bytes, int i, int j) {
        byte swapByte = bytes[i];
        bytes[i] = bytes[j];
        bytes[j] = swapByte;
    }

    private static int unsignedByte(byte b) {
        if (b < 0)
            return b + 256;
        return b;
    }

    private static String inputStream2String(InputStream is) {
        BufferedReader in;
        try {
            in = new BufferedReader(new InputStreamReader(is, "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            in = new BufferedReader(new InputStreamReader(is));
        }
        StringBuilder buffer = new StringBuilder();
        try {
            String line;
            while ((line = in.readLine()) != null)
                buffer.append(line);
        } catch (IOException iOException) {

        } finally {
            if (null != in)
                try {
                    in.close();
                } catch (IOException iOException) {
                }
        }
        return buffer.toString().trim();
    }

    private static InputStream string2InputStream(String str) {
        InputStream stream;
        if (str == null)
            return null;
        try {
            stream = new ByteArrayInputStream(str.getBytes("UTF-8"));
        } catch (UnsupportedEncodingException e) {
            stream = new ByteArrayInputStream(str.getBytes());
        }
        return stream;
    }

    public static InputStream iSDecrypt(InputStream inputStream, String passWord) {
        if (inputStream == null)
            return null;
        String deStr = decrypt(inputStream2String(inputStream), passWord);
        return string2InputStream(deStr);
    }

    public static byte[] encryptOrDecryptStream(byte[] srcByte, String passWord) {
        byte[] pwdByte;
        if (srcByte == null || passWord == null)
            return null;
        String pwStr = "~Dy@&sbg#3$1?:2%" + passWord;
        try {
            pwdByte = pwStr.getBytes("UTF-8");
        } catch (UnsupportedEncodingException e) {
            pwdByte = pwStr.getBytes();
        }
        int sourceLength = srcByte.length;
        byte[] ret = new byte[sourceLength];
        int retLen = doRC4(srcByte, sourceLength, pwdByte, pwdByte.length, ret);
        if (retLen == 0)
            return null;
        return ret;
    }

    public static String getDefaultPwd() {
        return "dbApp1q2w3e!Q@W#E";
    }
}
