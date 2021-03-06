package fileapp.crypt;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import javax.crypto.spec.SecretKeySpec;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.security.MessageDigest;

public class Crypter {

    private static final String AES = "AES";

    public SecretKeySpec keyGen(String str) throws Exception {
        byte[] key = str.getBytes();
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        key = md.digest(key);
        return new SecretKeySpec(key, AES);
    }

    public StreamObject encrypt(File in, File out, SecretKeySpec key) throws Exception {
        Cipher cipher = Cipher.getInstance(AES);
        cipher.init(Cipher.ENCRYPT_MODE, key);
        FileInputStream is = new FileInputStream(in);
        CipherOutputStream os = new CipherOutputStream(new FileOutputStream(out), cipher);
        return new StreamObject(is, os, in.length());
    }

    public StreamObject decrypt(File in, File out, SecretKeySpec key) throws Exception {
        Cipher cipher = Cipher.getInstance(AES);
        cipher.init(Cipher.DECRYPT_MODE, key);
        CipherInputStream is = new CipherInputStream(new FileInputStream(in), cipher);
        FileOutputStream os = new FileOutputStream(out);
        return new StreamObject(is, os, in.length());
    }

}
