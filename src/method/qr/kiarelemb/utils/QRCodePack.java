package method.qr.kiarelemb.utils;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.Key;

/**
 * 加密和解密的算法类
 */
public class QRCodePack {
    public static String encrypt(String str, String key) {
        EncryptDES des = new EncryptDES(key);
        return des.encrypt(str);
    }

    public static String decrypt(String str, String key) {
        EncryptDES des = new EncryptDES(key);
        return des.decrypt(str);
    }
}

class EncryptDES {

    private static final String DES = "DES";
    //加密工具
    private Cipher encryptCipher;
    // 解密工具
    private Cipher decryptCipher;

    public EncryptDES(String strKey) {
        try {
            Key key = getKey(strKey.getBytes(StandardCharsets.UTF_8));
            encryptCipher = Cipher.getInstance(DES);
            encryptCipher.init(Cipher.ENCRYPT_MODE, key);
            decryptCipher = Cipher.getInstance(DES);
            decryptCipher.init(Cipher.DECRYPT_MODE, key);
        } catch (Exception ignore) {
//            e.printStackTrace();
        }
    }

    /**
     * 将byte数组转换为表示16进制值的字符串， 如：byte[]{8,18}转换为：0813，和public static byte[]
     * <p>
     * hexStr2ByteArr(String strIn) 互为可逆的转换过程
     *
     * @param arrB 需要转换的byte数组
     * @return 转换后的字符串
     */
    private String byteArr2HexStr(byte[] arrB) {
        int iLen = arrB.length;
        // 每个byte用2个字符才能表示，所以字符串的长度是数组长度的2倍
        StringBuilder sb = new StringBuilder(iLen * 2);
        for (int b : arrB) {
            int intTmp = b;
            // 把负数转换为正数
            while (intTmp < 0) {
                intTmp = intTmp + 256;
            }
            // 小于0F的数需要在前面补0
            if (intTmp < 16) {
                sb.append("0");
            }
            sb.append(Integer.toString(intTmp, 16));
        }
        return sb.toString();
    }

    /**
     * 将表示16进制值的字符串转换为byte数组，和public static String byteArr2HexStr(byte[] arrB)
     * 互为可逆的转换过程
     *
     * @param strIn 需要转换的字符串
     * @return 转换后的byte数组
     */
    private byte[] hexStr2ByteArr(String strIn) {
        byte[] arrB = strIn.getBytes(StandardCharsets.UTF_8);
        int iLen = arrB.length;
        // 两个字符表示一个字节，所以字节数组长度是字符串长度除以2
        byte[] arrOut = new byte[iLen / 2];
        for (int i = 0; i < iLen; i = i + 2) {
            String strTmp = new String(arrB, i,2);
            arrOut[i / 2] = (byte) Integer.parseInt(strTmp, 16);
        }
        return arrOut;
    }

    /**
     * 加密字节数组
     *
     * @param arrB 需加密的字节数组
     * @return 加密后的字节数组
     */
    private byte[] encrypt(byte[] arrB) throws IllegalBlockSizeException, BadPaddingException {
        return encryptCipher.doFinal(arrB);
    }

    /**
     * 加密字符串
     *
     * @param strIn 需加密的字符串
     * @return 加密后的字符串
     */
    public String encrypt(String strIn) {
        byte[] bytes;
        try {
            bytes = encrypt(strIn.getBytes(StandardCharsets.UTF_8));
        } catch (Exception e) {
//            e.printStackTrace();
            return null;
        }
        return byteArr2HexStr(bytes);
    }

    /**
     * 解密字节数组
     *
     * @param arrB 需解密的字节数组
     * @return 解密后的字节数组
     */
    private byte[] decrypt(byte[] arrB) throws IllegalBlockSizeException, BadPaddingException {
        return decryptCipher.doFinal(arrB);
    }

    /**
     * 解密字符串
     *
     * @param strIn 需解密的字符串
     * @return 解密后的字符串
     */
    public String decrypt(String strIn) {
        byte[] decrypt;
        try {
            decrypt = decrypt(hexStr2ByteArr(strIn));
        } catch (Exception e) {
//            e.printStackTrace();
            return null;
        }
        return new String(decrypt, StandardCharsets.UTF_8);
    }

    private Key getKey(byte[] arrTmp) {
        // 创建一个空的8位字节数组（默认值为0）
        byte[] arrB = new byte[8];
        // 将原始字节数组转换为8位
        for (int i = 0; i < arrTmp.length && i < arrB.length; i++) {
            arrB[i] = arrTmp[i];
        }
        // 生成密钥
        return new SecretKeySpec(arrB, DES);
    }
}