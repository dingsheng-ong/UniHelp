package my.edu.um.fsktm.unihelp.util;

import android.util.Log;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Hash {

    public static String compute(String message, String algorithm) {
        String hash = null;
        try {
            MessageDigest messageDigest = MessageDigest.getInstance(algorithm);
            messageDigest.update(message.getBytes());
            byte[] bytes = messageDigest.digest();
            StringBuilder sb= new StringBuilder();
            for (byte b: bytes)
                sb.append(Integer.toString((b & 0xff) + 0x100, 16).substring(1));
            hash = sb.toString();
        } catch (NoSuchAlgorithmException e) {
            Log.e("Hash.java, Line 15", e.toString());
        }
        return hash;
    }
}
