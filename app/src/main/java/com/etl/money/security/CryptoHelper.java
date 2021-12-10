package com.etl.money.security;

import android.org.apache.commons.codec.binary.Base64;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class CryptoHelper {
/*
    public static void main(String args[]) throws Exception {
        String input = "Text to encryp";
        System.out.println("input:" + input);
        String encriptedValue = encrypt(input);
        System.out.println("cipher:" + encriptedValue);
        //encriptedValue = "n0c0KYNl+PM6cekV7YMHI5b0fLodT3jU27cHt7+VGrA=";
        System.out.println("cipher:" + encriptedValue);
        String decriptedValue = decrypt(encriptedValue);
        System.out.println("output:" + decriptedValue);

    }
*/

    private IvParameterSpec ivspec;
    private SecretKeySpec keyspec;
    private Cipher cipher;
    private final static String SecretKey  = "1234567890abcder";//16 char secret key

    public CryptoHelper() {
        ivspec = new IvParameterSpec(SecretKey.getBytes());

        keyspec = new SecretKeySpec(SecretKey.getBytes(), "AES");

        try {
            cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        } catch (NoSuchAlgorithmException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public static String encrypt(String valueToEncrypt) throws Exception {
        byte[] valueDecoded= new byte[0];
        try {
            valueDecoded = android.util.Base64.encode(valueToEncrypt.getBytes("UTF-8"), android.util.Base64.DEFAULT);
        } catch (UnsupportedEncodingException e) {
        }
        String strencode = new String(valueDecoded, "UTF-8"); // for UTF-8 encoding
        CryptoHelper enc = new CryptoHelper();
        return Base64.encodeBase64String(enc.encryptInternal(strencode));

// Old
        /*
        CryptoHelper enc = new CryptoHelper();
        return Base64.encodeBase64String(enc.encryptInternal(valueToEncrypt));
        */

    }


    public static String decrypt(String valueToDecrypt) throws Exception {
        CryptoHelper enc = new CryptoHelper();
        String Str = new String(enc.decryptInternal(valueToDecrypt));

        byte[] valueDecoded= new byte[0];
        try {
            valueDecoded = android.util.Base64.decode(Str.getBytes("UTF-8"), android.util.Base64.DEFAULT);
        } catch (UnsupportedEncodingException e) {
        }
        String strdecode = new String(valueDecoded, "UTF-8"); // for UTF-8 encoding
        return  strdecode ;
// Old
        /*
        CryptoHelper enc = new CryptoHelper();
        return new String(enc.decryptInternal(valueToDecrypt));
         */
    }

    private byte[] encryptInternal(String text) throws Exception {
        if (text == null || text.length() == 0) {
            throw new Exception("Empty string");
        }

        byte[] encrypted = null;
        try {
            cipher.init(Cipher.ENCRYPT_MODE, keyspec, ivspec);
            encrypted = cipher.doFinal(text.getBytes());
        } catch (Exception e) {
            throw new Exception("[encrypt] " + e.getMessage());
        }
        return encrypted;
    }

    private byte[] decryptInternal(String code) throws Exception {
        if (code == null || code.length() == 0) {
            throw new Exception("Empty string");
        }

        byte[] decrypted = null;
        try {
            cipher.init(Cipher.DECRYPT_MODE, keyspec, ivspec);
            //decrypted = cipher.doFinal(Base64.decode(code,Base64.DEFAULT));
            decrypted = cipher.doFinal(
                    Base64.decodeBase64(code));
        } catch (Exception e) {
            throw new Exception("[decrypt] " + e.getMessage());
        }
        return decrypted;
    }

}
