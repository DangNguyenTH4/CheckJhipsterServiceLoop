package HTTPREQUEST.HTTREQUEST;

import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class SendMail {
	private String fromEmail;
	private String passWord;

	public SendMail() {
		this.fromEmail = ReadProperties.getProperty("mail.smtp.host");
		System.out.println(fromEmail);
		this.passWord = ReadProperties.getProperty("mail.smtp.password");
		System.out.println(passWord);
	}

	public boolean sendMail(String toEmail, String message) {
		Properties props = ReadProperties.getInstance();
		Session session = Session.getInstance(props, new javax.mail.Authenticator() {

			protected PasswordAuthentication getPasswordAuthentication() {

				return new PasswordAuthentication(fromEmail, passWord);

			}

		});
		session.setDebug(true);

		try {
			// Create a default MimeMessage object.
			MimeMessage mimeMessage = new MimeMessage(session);

			// Set From: header field of the header.
			mimeMessage.setFrom(new InternetAddress(this.fromEmail));

			// Set To: header field of the header.
			mimeMessage.addRecipient(Message.RecipientType.TO, new InternetAddress(toEmail));

			// Set Subject: header field
			mimeMessage.setSubject("Some service were die");

			// Now set the actual message
			mimeMessage.setText(message);

			System.out.println("sending...");
			// Send message
			Transport.send(mimeMessage);
			System.out.println("Sent message successfully....");
		} catch (MessagingException mex) {
			mex.printStackTrace();
		}

		return true;
	}
}
