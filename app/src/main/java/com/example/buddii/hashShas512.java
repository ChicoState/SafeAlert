package com.example.buddii;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import java.security.SecureRandom;

class hashSha512 {

    static String hashPaswordSHA512(String hashthisWord, byte[] salt) {
        String hashedPword = null;
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-512");
            md.update(salt);
            byte[] bytes = md.digest(hashthisWord.getBytes());
            StringBuilder sb = new StringBuilder();
            //standard method for java hashing
            for(int i=0; i< bytes.length ;i++)
            {
                sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
            }
            hashedPword = sb.toString();
        }
        catch (NoSuchAlgorithmException e)
        {
            e.printStackTrace();
        }
        return hashedPword;
    }
    //unique salt is created whenever a user is created and requires one
    static byte[] getSalt()

    {

        //SHA1PRNG is a pure Java implementation which is not as strong as the algorithms used
        // by approved DRBG mechanisms in NIST SP800-90.
        SecureRandom sr = null;
        try {
            sr = SecureRandom.getInstance("SHA1PRNG");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        byte[] salt = new byte[16];
        sr.nextBytes(salt);
        return salt;

    }



}
