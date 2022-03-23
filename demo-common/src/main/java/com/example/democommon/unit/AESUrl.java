package com.example.democommon.unit;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.security.SecureRandom;

public class AESUrl {
    public static final String AES = "AES";
    public static final String CHARSET = null; // 编码格式；默认null为GBK
    public static final int kEYSIZEAES = 128;
    public final String KEY = "123456789abcdefghijklmnopqrstuvwxyz";
    private static AESUrl INSTANCE;
    private AESUrl() {
    }
    // 单例
    public static AESUrl getInstance() {
        if (INSTANCE == null) {
            synchronized (AESUrl.class) {
                if (INSTANCE == null) {
                    INSTANCE = new AESUrl();
                }
            }
        }
        return INSTANCE;
    }
    /**
     * 使用 AES 进行加密
     */
    public String encode(String res) {
        return keyGeneratorES(res, AES, KEY, kEYSIZEAES, true);
    }
    /**
     * 使用 AES 进行解密
     */
    public String decode(String res) {
        return keyGeneratorES(res, AES, KEY, kEYSIZEAES, false);
    }
    // 使用KeyGenerator双向加密，DES/AES，注意这里转化为字符串的时候是将2进制转为16进制格式的字符串，不是直接转，因为会出错
    private String keyGeneratorES(String res, String algorithm, String key, int keysize, boolean isEncode) {
        try {
            // 使用AES，KeyGenerator双向加密
            KeyGenerator kg = KeyGenerator.getInstance(algorithm);
            if (keysize == 0) {
                byte[] keyBytes = CHARSET == null ? key.getBytes() : key.getBytes(CHARSET);
                kg.init(new SecureRandom(keyBytes));
            } else if (key == null) { // 加密使用的编码
                kg.init(keysize);
            } else {
                // 使用编码进行加密
                byte[] keyBytes = CHARSET == null ? key.getBytes() : key.getBytes(CHARSET);
                kg.init(keysize, new SecureRandom(keyBytes));
            }
            // 加密过程
            SecretKey sk = kg.generateKey();
            SecretKeySpec sks = new SecretKeySpec(sk.getEncoded(), algorithm);
            Cipher cipher = Cipher.getInstance(algorithm);
            // 辨认是加密还是解密
            if (isEncode) {
                cipher.init(Cipher.ENCRYPT_MODE, sks);
                byte[] resBytes = CHARSET == null ? res.getBytes() : res.getBytes(CHARSET);
                return parseByte2HexStr(cipher.doFinal(resBytes));
            } else {
                cipher.init(Cipher.DECRYPT_MODE, sks);
                return new String(cipher.doFinal(parseHexStr2Byte(res)));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    // 将二进制转换成16进制
    private String parseByte2HexStr(byte buf[]) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < buf.length; i++) {
            String hex = Integer.toHexString(buf[i] & 0xFF);
            if (hex.length() == 1) {
                hex = '0' + hex;
            }
            sb.append(hex.toUpperCase());
        }
        return sb.toString();
    }
    // 将16进制转换为二进制
    private byte[] parseHexStr2Byte(String hexStr) {
        if (hexStr.length() < 1){
            return null;
        }
        byte[] result = new byte[hexStr.length() / 2];
        for (int i = 0; i < hexStr.length() / 2; i++) {
            int high = Integer.parseInt(hexStr.substring(i * 2, i * 2 + 1), 16);
            int low = Integer.parseInt(hexStr.substring(i * 2 + 1, i * 2 + 2), 16);
            result[i] = (byte) (high * 16 + low);
        }
        return result;
    }

}
