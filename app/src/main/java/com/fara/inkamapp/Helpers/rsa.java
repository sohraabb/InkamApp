package com.fara.inkamapp.Helpers;

import android.util.Base64;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.RSAPrivateKeySpec;
import java.security.spec.RSAPublicKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

/**
 ** http://lugeek.com
 */

public class rsa {
    public static final String KEY_ALGORITHM = "RSA";
    public static final String SIGNATURE_ALGORITHM = "MD5withRSA";

    private static final String PUBLIC_KEY = "RSAPublicKey";
    private static final String PRIVATE_KEY = "RSAPrivateKey";

    public static byte[] decryptBASE64(String key) throws Exception {
        return Base64.decode(key, Base64.DEFAULT);
    }

    public static String encryptBASE64(byte[] key) throws Exception {
        return Base64.encodeToString(key, Base64.DEFAULT);
    }

    /**
     * 用私钥对信息生成数字签名
     *
     * @param data
     *            加密数据
     * @param privateKey
     *            私钥
     *
     * @return
     * @throws Exception
     */
    public static String sign(byte[] data, String privateKey) throws Exception {
        // 解密由base64编码的私钥
        byte[] keyBytes = decryptBASE64(privateKey);

        // 构造PKCS8EncodedKeySpec对象
        PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(keyBytes);

        // KEY_ALGORITHM 指定的加密算法
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);

        // 取私钥匙对象
        PrivateKey priKey = keyFactory.generatePrivate(pkcs8KeySpec);

        // 用私钥对信息生成数字签名
        Signature signature = Signature.getInstance(SIGNATURE_ALGORITHM);
        signature.initSign(priKey);
        signature.update(data);

        return encryptBASE64(signature.sign());
    }

    /**
     * 校验数字签名
     *
     * @param data
     *            加密数据
     * @param publicKey
     *            公钥
     * @param sign
     *            数字签名
     *
     * @return 校验成功返回true 失败返回false
     * @throws Exception
     *
     */
    public static boolean verify(byte[] data, String publicKey, String sign)
            throws Exception {

        // 解密由base64编码的公钥
        byte[] keyBytes = decryptBASE64(publicKey);

        // 构造X509EncodedKeySpec对象
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);

        // KEY_ALGORITHM 指定的加密算法
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);

        // 取公钥匙对象
        PublicKey pubKey = keyFactory.generatePublic(keySpec);

        Signature signature = Signature.getInstance(SIGNATURE_ALGORITHM);
        signature.initVerify(pubKey);
        signature.update(data);

        // 验证签名是否正常
        return signature.verify(decryptBASE64(sign));
    }

    /**
     * 解密<br>
     * 用私钥解密
     *
     * @param data
     * @param key
     * @return
     * @throws Exception
     */
    public static String decryptByPrivateKey(String data, String key)
            throws Exception {
        // 对密钥解密
        byte[] keyBytes = decryptBASE64(key);

        // 取得私钥
        PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        Key privateKey = keyFactory.generatePrivate(pkcs8KeySpec);

        // 对数据解密
        Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
        cipher.init(Cipher.DECRYPT_MODE, privateKey);

        byte[] decrypted = cipher.doFinal(data.getBytes());

        String strDecrypted = Base64.encodeToString(decrypted, Base64.DEFAULT);
        return strDecrypted;
    }

    /**
     * 解密<br>
     * 用公钥解密
     *
     * @param data
     * @param key
     * @return
     * @throws Exception
     */
    public static byte[] decryptByPublicKey(byte[] data, String key)
            throws Exception {
        // 对密钥解密
        byte[] keyBytes = decryptBASE64(key);

        // 取得公钥
        X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        Key publicKey = keyFactory.generatePublic(x509KeySpec);

        // 对数据解密
        Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
        cipher.init(Cipher.DECRYPT_MODE, publicKey);

        return cipher.doFinal(data);
    }

    /**
     * 加密<br>
     * 用公钥加密
     *
     * @param data
     * @param key
     * @return
     * @throws Exception
     */
    public static byte[] encryptByPublicKey(byte[] data, String key)
            throws Exception {
        // 对公钥解密
        byte[] keyBytes = decryptBASE64(key);

        // 取得公钥
        X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        Key publicKey = keyFactory.generatePublic(x509KeySpec);

        // 对数据加密
        Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);

        return cipher.doFinal(data);
    }

    /**
     * 加密<br>
     * 用私钥加密
     *
     * @param data
     * @param key
     * @return
     * @throws Exception
     */
    public static byte[] encryptByPrivateKey(byte[] data, String key)
            throws Exception {
        // 对密钥解密
        byte[] keyBytes = decryptBASE64(key);

        // 取得私钥
        PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        Key privateKey = keyFactory.generatePrivate(pkcs8KeySpec);

        // 对数据加密
        Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
        cipher.init(Cipher.ENCRYPT_MODE, privateKey);

        return cipher.doFinal(data);
    }

    /**
     * 取得私钥
     *
     * @param keyMap
     * @return
     * @throws Exception
     */
    public static String getPrivateKey(Map<String, Object> keyMap)
            throws Exception {
        Key key = (Key) keyMap.get(PRIVATE_KEY);

        return encryptBASE64(key.getEncoded());
    }

    /**
     * 取得公钥
     *
     * @param keyMap
     * @return
     * @throws Exception
     */
    public static String getPublicKey(Map<String, Object> keyMap)
            throws Exception {
        Key key = (Key) keyMap.get(PUBLIC_KEY);

        return encryptBASE64(key.getEncoded());
    }

    /**
     * 初始化密钥
     *
     * @return
     * @throws Exception
     */
    public static Map<String, Object> initKey() throws Exception {
        KeyPairGenerator keyPairGen = KeyPairGenerator
                .getInstance(KEY_ALGORITHM);
        keyPairGen.initialize(3074);

        KeyPair keyPair = keyPairGen.generateKeyPair();

        // 公钥
        RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();

        // 私钥
        RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();

        Map<String, Object> keyMap = new HashMap<String, Object>(2);

        keyMap.put(PUBLIC_KEY, publicKey);
        keyMap.put(PRIVATE_KEY, privateKey);

        BigInteger mod_Int = publicKey.getModulus();
        BigInteger exp_Int = publicKey.getPublicExponent();
        byte[] mod_Bytes_Extra = mod_Int.toByteArray();
        byte[] mod_Bytes = new byte[128];
        System.arraycopy(mod_Bytes_Extra, 1, mod_Bytes, 0, 128);
        byte[] exp_Bytes = exp_Int.toByteArray();
        String modulus = Base64.encodeToString(mod_Bytes, Base64.DEFAULT);
        String exponent = Base64.encodeToString(exp_Bytes, Base64.DEFAULT);
        System.out.println(modulus);
        System.out.println(exponent);
        String public_Xml = "<RSAKeyValue><Modulus>"+modulus+"</Modulus><Exponent>"+exponent+"</Exponent></RSAKeyValue>";


        return keyMap;
    }


//        public static String PUBLIC_KEY = "2dX//cYHg8ZuSU9oYjcPCpQ9bvoq0SpaY0KK/9f8GWXzv3Sv9vbew9cmikY3DeSGy6W1I4VvG5LmYbsgT9ntZ7BJd6KAsuN6TPV0s114kEw74/RBhmG5qarJtU7w/eWRVypmg8CRHrggCxFQ5w7ArcsIcHsP3cOVv47gdnMQIOq/jzbs+XMypaIC6jQ0wIxxYZph16UrLzUJz2BLuLEfjrPsmD2HerKzLror9sHOdBWI0ZtjTk46ZCPU1Mr55fpYZ3sEsF7l6aCx+BgxxAMN3OwiqvmI1GrqCbIo65I+Jt/5GcWH8RlpsEHYZRV+nznIFmCb/PBUN+RTH84kw5hXnKhFi4rmDMUSS0v3gqGKyjL2YQ7ZEeV2b7wr0p/1KiuDsMdkMKqQDfR6A6wcIEMu/JqvNi1SarQXh6OM4U4+CMhUnVSOR2/VFpikqeeVVxTr2dbACVI5BMo6UuhinHsxOkSlbOq4QNAsYgRCBHAmnFPJ+UDYSEUsKrIjv0Wap2KB";
//        public static String PRIVATE_KEY = "ApxjhJWmimhK8xr+C8tQJVbQg6+A6t/glHb8NoPOtDk6Gi3aXBHqapt+DUM5LrPx/6fdS28yfTieBUbaZMqeY1NEFih+9WXvldONUgcJRmV7hEhtrqAmOwfeggtPrSNewhYyg1rAAoe/bpKHDzXojky0LbDz6DsLZO/f9dt8MoDNyfglD/pW/ZA7C7aTeUlVpI9Wo+CJa3ftxawITE7nK20xpUN7h7dzmVhG5Z6+DDsj2CtOHeanIYMibmh+ZUjCV9X+/sl9UTGIbt/s/kwuJXuP/m/5Zj/2H3Y+yVp8sWi0O/+AgTFsE/1/5F2yFUn89VlNBdOuxlv3hzYNVSnRpXbtEiELdLbQwX+EtfTbdeLPZryH3C8hueR/GZ4svIqVeQ4zHRU0+RaDmMi5yCIo+Vg0LxzlXxFQypxmApJXHopDd71dlY4OvKnN6d++nuL8hrSFkTkp/pJvzqSZjEkBa/rv7yebZ848AZamj7bV6/i77B6I8mCqLEYA+3tK8DMn";
////    public static byte [] decrypted = new byte[3072];
//
        public static String encrypt(String input) throws UnsupportedEncodingException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeySpecException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
            byte[] expBytes = Base64.decode("AQAB".getBytes("UTF-8"), Base64.DEFAULT);
            byte[] modBytes = Base64.decode(PUBLIC_KEY.getBytes("UTF-8"), Base64.DEFAULT);

            BigInteger modules = new BigInteger(1, modBytes);
            BigInteger exponent = new BigInteger(1, expBytes);


            KeyFactory factory = KeyFactory.getInstance("RSA");
            Cipher cipher1 = Cipher.getInstance("RSA/ECB/PKCS1PADDING");


            RSAPublicKeySpec pubSpec = new RSAPublicKeySpec(modules, exponent);
            PublicKey pubKey = factory.generatePublic(pubSpec);
            cipher1.init(Cipher.ENCRYPT_MODE, pubKey);
            byte[] encrypted = cipher1.doFinal(input.getBytes());

            String strEncrypted = Base64.encodeToString(encrypted, Base64.DEFAULT);
            return strEncrypted;
        }
//        //
//        public static String decrypt(String input) throws UnsupportedEncodingException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeySpecException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException
//        {
//
//            byte[] expBytes = Base64.decode("AQAB".getBytes("UTF-8"), Base64.DEFAULT);
//            byte[] modBytes = Base64.decode(PRIVATE_KEY.getBytes("UTF-8"), Base64.DEFAULT);
//
//            BigInteger modules = new BigInteger(1, modBytes);
//            BigInteger exponent = new BigInteger(1, expBytes);
//
//            KeyFactory factory = KeyFactory.getInstance("RSA");
//            Cipher cipher1 = Cipher.getInstance("RSA/ECB/PCKS#8");
//
//
//            RSAPrivateKeySpec privateSpec = new RSAPrivateKeySpec(modules, exponent);
//            PrivateKey priKey = factory.generatePrivate(privateSpec);
//            cipher1.init(Cipher.DECRYPT_MODE, priKey);
//            byte[] decrypted = cipher1.doFinal(input.getBytes());
//
//            String strDecrypted = Base64.encodeToString(decrypted, Base64.DEFAULT);
//            return strDecrypted;
//
//
//        }
//
//
//    }

}
