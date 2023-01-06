/*
 * Envoi de mail avec pièce jointe
 */
package lymytz.service.application.service.mail;

import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import lymytz.service.utils.Constantes;
import lymytz.service.utils.LymytzService;
import lymytz.service.utils.UtilsProject;

/**
 *
 * @author LYMYTZ
 */
public class SendMail {

    private String emeteur;
    private String destinataire;

    Properties p = new Properties();
    Session session;

    public SendMail() {
        p.put("mail.transport.protocol", "smtp");
        p.put("mail.smtp.host", "mail.lymytz.com");
//        p.put("mail.smtp.socketFactory.port", "465");
//        p.put("mail.smtp.user", "goucherey@lymytz.com");
        p.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        p.put("mail.smtp.auth", "true");
        p.put("mail.smtp.port", "587");

        session = Session.getDefaultInstance(p, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication("goucherey@lymytz.com", "yVES/1910#");
            }
        });
//        session=Session.getDefaultInstance(p);
    }

    public SendMail(String emeteur, String destinataire) {
        this();
        this.emeteur = emeteur;
        this.destinataire = destinataire;
    }

    public String getEmeteur() {
        return emeteur;
    }

    public void setEmeteur(String emeteur) {
        this.emeteur = emeteur;
    }

    public String getDestinataire() {
        return destinataire;
    }

    public void setDestinataire(String destinataire) {
        this.destinataire = destinataire;
    }

    public void sendMessage() {
        try {
            Message msg = new MimeMessage(session);
            msg.addRecipient(Message.RecipientType.TO, new InternetAddress("gouchere@yahoo.fr", " M. Admin"));
            msg.setSubject("Rapport d'activité de la société " + UtilsProject.currentSociete.getName() + " [" + UtilsProject.currentAgence.getCodeagence() + "]" + " du " + Constantes.dfD.format(new Date()));
            msg.setText("Example de message");
            Transport.send(msg);
            LymytzService.success();
        } catch (MessagingException | UnsupportedEncodingException ex) {
            Logger.getLogger(SendMail.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
