package com.toyfactory.pcb.service;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * password hash 및 accecc token hash를 위한 암호 관련 기능
 * 참고 사이트 
 * http://howtodoinjava.com/security/how-to-generate-secure-password-hash-md5-sha-pbkdf2-bcrypt-examples/
 * @author hikiro
 *
 */

@Service("crpytoService")
public class CrpytoService {
	
	//private static String _salt = "jdhe34fhid$&i38e";

    @Value("${pcbservice.crypto.salt}")
    private String _salt;

	private static String get_SHA_1_SecureHashValue(String plainText, byte[] salt)
    {
        //Use MessageDigest md = MessageDigest.getInstance("SHA-256");
		
        String generatedHashValue = null;
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-1");
            md.update(salt);
            byte[] bytes = md.digest(plainText.getBytes());
            StringBuilder sb = new StringBuilder();
            for(int i=0; i< bytes.length ;i++)
            {
                sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
            }
            generatedHashValue = sb.toString();
        } 
        catch (NoSuchAlgorithmException e) 
        {
            e.printStackTrace();
        }
        return generatedHashValue;
    }
	
    //Add salt
    private static byte[] getSalt() throws NoSuchAlgorithmException
    {
        SecureRandom sr = SecureRandom.getInstance("SHA1PRNG");
        byte[] salt = new byte[16];
        sr.nextBytes(salt);
        return salt;
    }
    
    public String generateSHA1Hash(String plainText){    	
        return get_SHA_1_SecureHashValue(plainText, _salt.getBytes());    		
    }
}
