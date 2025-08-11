package com.lky.mytoolbox;

import android.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;

public class DesUtils {
    private static final String KEY = "12345678"; // 8字节密钥

    public static String encrypt(String value) throws Exception {
        DESKeySpec desKey = new DESKeySpec(KEY.getBytes("UTF-8"));
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
        SecretKey secureKey = keyFactory.generateSecret(desKey);

        Cipher cipher = Cipher.getInstance("DES/ECB/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, secureKey);
        byte[] encrypted = cipher.doFinal(value.getBytes());
        return Base64.encodeToString(encrypted, Base64.DEFAULT);
    }

    public static String decrypt(String encrypted) throws Exception {
        DESKeySpec desKey = new DESKeySpec(KEY.getBytes("UTF-8"));
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
        SecretKey secureKey = keyFactory.generateSecret(desKey);

        Cipher cipher = Cipher.getInstance("DES/ECB/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE, secureKey);
        byte[] original = cipher.doFinal(Base64.decode(encrypted, Base64.DEFAULT));
        return new String(original);
    }
}
