package kh.gov.treasury.utils;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CryptoUtils {	
	

    /**
     * Encrypt data
     * 
     * @param
     * Return
     */
    public static String encrypt(String data, String keyPass, String keyIV, String algorithm, String keyPadding) {
        try {
            SecretKeySpec secretKey = new SecretKeySpec(keyPass.getBytes(StandardCharsets.UTF_8), algorithm);
            IvParameterSpec ivParams = new IvParameterSpec(keyIV.getBytes(StandardCharsets.UTF_8));
            Cipher cipher = Cipher.getInstance(keyPadding);
            cipher.init(Cipher.ENCRYPT_MODE, secretKey, ivParams);
            byte[] encryptedBytes = cipher.doFinal(data.getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(encryptedBytes);            
		} catch (Exception e) {
			log.error("Inside encrypt - Error Message={}", e.getMessage());
			return null;
		}
    }

    /**
     * Un-Encrypt data
     * 
     * @param
     * Return
     */
    public static String decrypt(String encryptedData, String keyPass, String keyIV, String algorithm, String keyPadding) {
        try {
	        SecretKeySpec secretKey = new SecretKeySpec(keyPass.getBytes(StandardCharsets.UTF_8), algorithm);
	        IvParameterSpec ivParams = new IvParameterSpec(keyIV.getBytes(StandardCharsets.UTF_8));
	        Cipher cipher = Cipher.getInstance(keyPadding);
	        cipher.init(Cipher.DECRYPT_MODE, secretKey, ivParams);
	        byte[] decryptedBytes = cipher.doFinal(Base64.getDecoder().decode(encryptedData));
            return new String(decryptedBytes, StandardCharsets.UTF_8);
        } catch (Exception e) {
			log.error("Inside decrypt - Error Message={}", e.getMessage());
			return null;
		}
    }
    

//    algorithm: AES
//    keyPadding: AES/CBC/PKCS5PADDING
//    keyPass: gdntassetskey168
//    keyIV: gdntassekeyiv168
    
    public static void main(String[] args) {
      System.out.println(decrypt("rgS+S/48zp6KT1OQYuoKUw==", "gdntassetskey168", "gdntassekeyiv168", "AES", "AES/CBC/PKCS5PADDING"));
    }
    
}
