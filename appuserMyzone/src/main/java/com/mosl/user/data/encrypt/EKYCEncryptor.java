package com.mosl.user.data.encrypt;


import static java.nio.charset.StandardCharsets.UTF_8;

import android.util.Base64;

import java.nio.charset.Charset;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import okhttp3.FormBody;
import okhttp3.RequestBody;


/**
 * Created by pramod on 26-08-2016.
 */
public class EKYCEncryptor {
    public static final Charset UTF_8 = Charset.forName("UTF-8");
    private static final String ZERO_PADDING_KEY = "8181818181818181";
    private static final String ZERO_PADDING_KEY_NEW = "8181818181818181";
    private static final String IV = "8181818181818181";
    private static final String IV_NEW = "8181818181818181";
    private static final byte[] keyValue = ZERO_PADDING_KEY.getBytes(UTF_8);
    private static final byte[] keyValueNew = ZERO_PADDING_KEY_NEW.getBytes(UTF_8);
    private static final byte[] ivValue = IV.getBytes(UTF_8);
    private static final byte[] ivValueNew = IV_NEW.getBytes(UTF_8);
    private static final IvParameterSpec ivspec = new IvParameterSpec(ivValue);
    private static final IvParameterSpec ivspecNew = new IvParameterSpec(ivValueNew);
    private static final SecretKeySpec keyspec = new SecretKeySpec(keyValue, "AES");
    private static final SecretKeySpec keyspecNew = new SecretKeySpec(keyValueNew, "AES");
    private static String CIPHER_ALGORITHM = "AES/CBC/PKCS5Padding";

    public static String encrypt(String data) {
        try {
            Cipher c = Cipher.getInstance(CIPHER_ALGORITHM);
            c.init(Cipher.ENCRYPT_MODE, keyspec, ivspec);
            byte[] encVal = c.doFinal(data.getBytes(UTF_8));
            return Base64.encodeToString(encVal, Base64.DEFAULT);
        } catch (Exception ex) {
            return "";
        }
    }

    public static String encryptWithNewKey(String data) {
        try {
            Cipher c = Cipher.getInstance(CIPHER_ALGORITHM);
            c.init(Cipher.ENCRYPT_MODE, keyspecNew, ivspecNew);
            byte[] encVal = c.doFinal(data.getBytes(UTF_8));
            String encryptedValue = Base64.encodeToString(encVal, Base64.DEFAULT);
            encryptedValue = encryptedValue.replace("\n", "").replace("\r", "");
            return encryptedValue;
        } catch (Exception ex) {
            return "";
        }
    }

    public static String decrypt(String data) {
        try {
            byte[] byteData = Base64.decode(data, Base64.DEFAULT);
            Cipher c = Cipher.getInstance(CIPHER_ALGORITHM);
            c.init(Cipher.DECRYPT_MODE, keyspec, ivspec);
            byte[] encVal = c.doFinal(byteData);
            return new String(encVal, UTF_8);
        } catch (Exception ex) {
            return "";
        }
    }

    public static RequestBody createRequest(String flag, String category, String body) {
        //Timber.d("FLAG : %s, CATEGORY : %s, BODY : %s", flag, category, body);
        return new FormBody.Builder().add("FLAG", encryptWithNewKey(flag))
            .add("CATEGORY", encryptWithNewKey(category))
            .add("INPUT", encryptWithNewKey(body))
            .add("REQUESTTYPE", "MOBILE")
            .build();
    }
}
