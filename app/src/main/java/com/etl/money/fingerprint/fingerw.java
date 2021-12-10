package com.etl.money.fingerprint;


import android.annotation.TargetApi;
import android.security.keystore.KeyGenParameterSpec.Builder;

import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.KeyPairGenerator;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.spec.ECGenParameterSpec;
import java.security.spec.X509EncodedKeySpec;

@TargetApi(25)
public class fingerw {
    private static KeyStore a = null;
    public static KeyStore a() {
        if (a == null) {
            try {
                a = KeyStore.getInstance("AndroidKeyStore");
                a.load(null);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return a;
    }

    public static void b() {
        try {
            KeyPairGenerator instance = KeyPairGenerator.getInstance("EC", "AndroidKeyStore");
            instance.initialize(new Builder("fingerprint", 4).setDigests(new String[]{"SHA-256"}).setAlgorithmParameterSpec(new ECGenParameterSpec("secp256r1")).setUserAuthenticationRequired(true).build());
            instance.generateKeyPair();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static boolean c() {
        try {
            return fingerw.a().containsAlias("fingerprint");
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }


    //  private nw() {}

    public static String g() {
        try {
            try {

                PrivateKey privateKey = (PrivateKey) a().getKey("fingerprint", null);
                Signature instance = Signature.getInstance("SHA256withECDSA");
                instance.initSign(privateKey);
                // PublicKey publicKey = MainActivity.a().getCertificate("fingerprint").getPublicKey();
                String Str = ""+a().getCertificate("fingerprint").getPublicKey();if (Str!=""){ Str=     Str.substring((Str.length()-12),Str.length()); }
                // TextView txt   =(TextView) findViewById(R.id.txt);
                //  txt.setText(""+ Str);

                return Str;
            } catch (InvalidKeyException e) {
                a().deleteEntry("fingerprint");
                b();
                return"b" ;
            }
        } catch (Exception e2) {
            e2.printStackTrace();
            return"c" ;
        }
    }

    public static byte[] d() {
        try {
            if (!fingerw.c()) {
                fingerw.b();
            }
            PublicKey publicKey = fingerw.a().getCertificate("fingerprint").getPublicKey();

            return KeyFactory.getInstance(publicKey.getAlgorithm()).generatePublic(new X509EncodedKeySpec(publicKey.getEncoded())).getEncoded();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    public static Signature e() {
        try {
            if (!fingerw.c()) {
                return null;
            }
            try {
                PrivateKey privateKey = (PrivateKey) fingerw.a().getKey("fingerprint", null);
                Signature instance = Signature.getInstance("SHA256withECDSA");
                instance.initSign(privateKey);
                return instance;
            } catch (InvalidKeyException e) {
                fingerw.a().deleteEntry("fingerprint");
                fingerw.b();
                return fingerw.e();
            }
        } catch (Exception e2) {
            e2.printStackTrace();
            return null;
        }
    }
}