package com.mosl.tabapp.common;

import android.util.Base64;
import com.mosl.tabapp.ws.CRMWebServiceHandler;
import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * Created by pramod on 31/10/17.
 */

public class CRMSecurityUtil {
    private static String TAG = CRMWebServiceHandler.class.getName();


    private static String CIPHER_ALGORITHM = "QUVTL0NCQy9QS0NTNVBhZGRpbmc=";

    private static final IvParameterSpec ivspec = new IvParameterSpec(getKeyValue());
    private static final SecretKeySpec keyspec = new SecretKeySpec(getIVValue(), "AES");

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

    public static String encrypt(String Data) {
        try {
            Cipher c = Cipher.getInstance(new String(Base64.decode(CIPHER_ALGORITHM, Base64.DEFAULT)));
            c.init(Cipher.ENCRYPT_MODE, keyspec, ivspec);
            byte[] encVal = c.doFinal(Data.getBytes());
            String encryptedValue = Base64.encodeToString(encVal, Base64.DEFAULT);
            encryptedValue = encryptedValue.replace("\n", "").replace("\r", "");
            return encryptedValue;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public static byte[] decrypt(String Data) {
        //Log.d(TAG, ">>decrypt("+Data+")");
        try {
            Data = Data.replace("\n", "").replace("\r", "").replace("Output", "").replace(":", "");
            byte[] decryptedValue = Base64.decode(Data, Base64.DEFAULT);
            Cipher c = Cipher.getInstance(new String(Base64.decode(CIPHER_ALGORITHM, Base64.DEFAULT)));
            c.init(Cipher.DECRYPT_MODE, keyspec, ivspec);
            return (c.doFinal(decryptedValue));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }
}
