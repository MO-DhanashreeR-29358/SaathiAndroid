package com.mosl.user.prefs;

import android.content.SharedPreferences;
import android.util.Base64;

import java.nio.charset.Charset;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * Created by lalkishore on 8/21/2017.
 */

public class SafeStringPreference {
    public static final Charset UTF_8 = Charset.forName("UTF-8");
    private final SharedPreferences preferences;
    private final String key;
    private final String defaultValue;

    private static String CIPHER_ALGORITHM = "QUVTL0NCQy9QS0NTNVBhZGRpbmc=";

    private static final IvParameterSpec ivspec = new IvParameterSpec(getKeyValue());
    private static final SecretKeySpec keyspec = new SecretKeySpec(getIVValue(), "AES");


    public SafeStringPreference(SharedPreferences preferences,  String key) {
        this(preferences,  key, null);
    }

    public SafeStringPreference(SharedPreferences preferences, String key,
        String defaultValue) {
        this.preferences = preferences;
        this.key = key;
        this.defaultValue = defaultValue;
    }

    public void set(String value) {
        preferences.edit().putString(key, encrypt(value)).apply();
    }

    public String get() {
        String value = preferences.getString(key, null);
        if (value != null) {
            return decrypt(value);
        }
        return defaultValue;
    }

    public void delete() {
        preferences.edit().remove(key).apply();
    }

    public boolean isSet() {
        return preferences.contains(key);
    }

    private String encrypt(String plainText) {
        try {
            Cipher c = Cipher.getInstance(new String(Base64.decode(CIPHER_ALGORITHM, Base64.DEFAULT)));
            c.init(Cipher.ENCRYPT_MODE, keyspec, ivspec);
            byte[] encVal = c.doFinal(plainText.getBytes());
            String encryptedValue = Base64.encodeToString(encVal, Base64.DEFAULT);
            encryptedValue = encryptedValue.replace("\n", "").replace("\r", "");
            return encryptedValue;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    private String decrypt(String cipherText) {
        try {
            byte[] decryptedValue = Base64.decode(cipherText, Base64.DEFAULT);
            Cipher c =
                    Cipher.getInstance(new String(Base64.decode(CIPHER_ALGORITHM, Base64.DEFAULT)));
            c.init(Cipher.DECRYPT_MODE, keyspec, ivspec);
            return new String(c.doFinal(decryptedValue));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    private static byte[] getKeyValue(){
        byte[] keyValue = "MTAw".getBytes();
        byte [] b1 = Base64.decode(keyValue, Base64.DEFAULT);
        String s1 = new String (b1);
        int i1 = Integer.parseInt(s1);
        int i2 = i1-20;

        String s2 = "";
        for(int x=0; x<8; x++){
            s2+=i2;
        }

        return s2.getBytes();
    }
    private static byte[] getIVValue(){
        byte[] keyValue = "MTAw".getBytes();
        byte [] b1 = Base64.decode(keyValue, Base64.DEFAULT);
        String s1 = new String (b1);
        int i1 = Integer.parseInt(s1);
        int i2 = i1-20;

        String s2 = "";
        for(int x=0; x<8; x++){
            s2+=i2;
        }

        return s2.getBytes();
    }
}
