package com.greatmrpark.utility;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.InvalidAlgorithmParameterException;
import org.apache.commons.codec.binary.Base64;

public class AES256Cipher {
    private static volatile AES256Cipher INSTANCE;
    
    final static String secretKey   = "0jPfOcAEQvkDM5H609vhcg=="; //32bit
    static String IV                = ""; //16bit
    
    public static AES256Cipher getInstance(){
        if(INSTANCE==null){
            synchronized(AES256Cipher.class){
                if(INSTANCE==null)
                    INSTANCE=new AES256Cipher();
            }
        }
        return INSTANCE;
    }
    
    private AES256Cipher(){
        IV = secretKey.substring(0,16);
    }
    
    /**
     * 암호화
     * @param str
     * @return
     * @throws java.io.UnsupportedEncodingException
     * @throws NoSuchAlgorithmException
     * @throws NoSuchPaddingException
     * @throws InvalidKeyException
     * @throws InvalidAlgorithmParameterException
     * @throws IllegalBlockSizeException
     * @throws BadPaddingException
     */
    public static String AES_Encode(String str) throws java.io.UnsupportedEncodingException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException{
    
        byte[] keyData = secretKey.getBytes();
    
        SecretKey secureKey = new SecretKeySpec(keyData, "AES");
    
        Cipher c = Cipher.getInstance("AES/CBC/PKCS5Padding");
        c.init(Cipher.ENCRYPT_MODE, secureKey, new IvParameterSpec(IV.getBytes()));
    
        byte[] encrypted = c.doFinal(str.getBytes("UTF-8"));
        String enStr = new String(Base64.encodeBase64(encrypted));
    
        return enStr;
    }
    
    public static String AES_Encode(String str, String chrctrset) throws java.io.UnsupportedEncodingException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException{
    
        byte[] keyData = secretKey.getBytes();
    
        SecretKey secureKey = new SecretKeySpec(keyData, "AES");
    
        Cipher c = Cipher.getInstance("AES/CBC/PKCS5Padding");
        c.init(Cipher.ENCRYPT_MODE, secureKey, new IvParameterSpec(IV.getBytes()));
    
        byte[] encrypted = c.doFinal(str.getBytes(chrctrset));
        String enStr = new String(Base64.encodeBase64(encrypted));
    
        return enStr;
    }
    
    /**
     * 복호화
     * @param str
     * @return
     * @throws java.io.UnsupportedEncodingException
     * @throws NoSuchAlgorithmException
     * @throws NoSuchPaddingException
     * @throws InvalidKeyException
     * @throws InvalidAlgorithmParameterException
     * @throws IllegalBlockSizeException
     * @throws BadPaddingException
     */
    public static String AES_Decode(String str) throws java.io.UnsupportedEncodingException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException{
        
        byte[] keyData = secretKey.getBytes();
        SecretKey secureKey = new SecretKeySpec(keyData, "AES");
        Cipher c = Cipher.getInstance("AES/CBC/PKCS5Padding");
        c.init(Cipher.DECRYPT_MODE, secureKey, new IvParameterSpec(IV.getBytes("UTF-8")));
    
        byte[] byteStr = Base64.decodeBase64(str.getBytes());
    
        return new String(c.doFinal(byteStr),"UTF-8");
    }

    public static String AES_Decode(String str, String chrctrset) throws java.io.UnsupportedEncodingException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException{
        
        byte[] keyData = secretKey.getBytes();
        SecretKey secureKey = new SecretKeySpec(keyData, "AES");
        Cipher c = Cipher.getInstance("AES/CBC/PKCS5Padding");
        c.init(Cipher.DECRYPT_MODE, secureKey, new IvParameterSpec(IV.getBytes("UTF-8")));
    
        byte[] byteStr = Base64.decodeBase64(str.getBytes());
    
        return new String(c.doFinal(byteStr),chrctrset);
    }
    
    /**
     * 키생성
     * @return
     * @throws NoSuchAlgorithmException
     */
    public static byte[] generationAES256_KEY() throws NoSuchAlgorithmException{
        KeyGenerator kgen = KeyGenerator.getInstance("AES");
        kgen.init(256);
        SecretKey key = kgen.generateKey();
     
        return key.getEncoded();
     
    }

}
