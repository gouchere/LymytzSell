/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package lymytz.service.utils;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author GOUCHERE YVES
 */
public class MdpUtil implements Serializable {

    public static String charArray = "abcdefghijklmnopqrstuvwxyz"
            + "ABCDEFGHIJKLMNOPQRSTUVWXTZ0123456789/-*@_-()";

    public String randomString(int length) {
        Random rd = new Random();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < charArray.length(); i++) {
            sb.append(charArray.charAt(rd.nextInt(70)));
        }
        return sb.toString();
    }

    public static String hashString(String mdp) {
        byte[] tabBytes = null;
        try {
            try {                
                tabBytes = MessageDigest.getInstance("MD5").digest(mdp.getBytes("UTF-8"));
            } catch (UnsupportedEncodingException ex) {
                Logger.getLogger(MdpUtil.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(MdpUtil.class.getName()).log(Level.SEVERE, null, ex);
        }
        return tabByteToString(tabBytes);
    }

    private static String tabByteToString(byte[] tabByte) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < tabByte.length; i++) {
            sb.append(tabByte[i]);
        }
        return sb.toString();
    }
}
