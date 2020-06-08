package com.fara.inkamapp.Helpers;

import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.InvalidParameterSpecException;
import java.util.Random;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class AESEncyption {

    public static byte[] sohrabGeneratesAESKey(int keyLength){
        byte [] key = new byte[keyLength];
        Random rd = new Random();

        rd.nextBytes(key);
        return key;
    }

    public static String decryptMsg(String cipherText, String key)
            throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidParameterSpecException, InvalidAlgorithmParameterException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException, UnsupportedEncodingException {
        /* Decrypt the message, given derived encContentValues and initialization vector. */
        byte[] b = (Base64.decode(cipherText));
        for (int i = 0; i < b.length; i++) {
//
            b[i] = (byte) (b[i] & 0xFF);
        }
        byte[] AES_KEY = Base64.decode(key);
        byte[] AES_IV = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
        SecretKeySpec keyspec = new SecretKeySpec(AES_KEY, "AES");
        IvParameterSpec ivspec = new IvParameterSpec(AES_IV);

        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS7Padding");
        cipher.init(Cipher.DECRYPT_MODE, keyspec, ivspec);

        byte[] decrypted = cipher.doFinal(b);

        String str = new String(decrypted, "UTF-8");
        String decryptString = str.trim();

        return decryptString;
    }


}
