/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lymytz.service.utils;

import java.security.Key;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

/**
 *
 * @author LYMYTZ
 */
public class EncryptMessage {

    private String message;
    private String key;

    public EncryptMessage() {
    }

    public static String encrypt(String texte, String key) {
        if (Constantes.asString(texte)) {
            try {
                Key clef = new SecretKeySpec(key.getBytes("ISO-8859-2"), "Blowfish");
                Cipher cipher = Cipher.getInstance("Blowfish");
                cipher.init(Cipher.ENCRYPT_MODE, clef);
                return new String(cipher.doFinal(texte.getBytes("ISO-8859-2")),"ISO-8859-2");
            } catch (Exception ex) {
                Logger.getLogger(EncryptMessage.class.getName()).log(Level.SEVERE, "", ex);
            }
        }
        return null;
    }

    public static String decrypt(String texte, String key) {
        if (Constantes.asString(texte)) {
            try {
                Key clef = new SecretKeySpec(key.getBytes("ISO-8859-2"), "Blowfish");
                Cipher cipher = Cipher.getInstance("Blowfish");
                cipher.init(Cipher.DECRYPT_MODE, clef);
                return new String(cipher.doFinal(texte.getBytes("ISO-8859-2")),"ISO-8859-2");
            } catch (Exception ex) {
                Logger.getLogger(EncryptMessage.class.getName()).log(Level.SEVERE, "", ex);
            }
        }
        return null;
    }
}
