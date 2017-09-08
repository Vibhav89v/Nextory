package common;

import java.io.IOException;
import java.util.Date;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import generics.Property;

public class EmailAttachmentSender      //NEXTORY
{
	public static String[] attachFiles;

	public static void sendEmailWithAttachments() throws AddressException, MessagingException 
	{
		String host = Property.getPropertyValue(AutomationConstants.CONFIG_PATH + AutomationConstants.CONFIG_FILE,
				"HOST");
		String port = Property.getPropertyValue(AutomationConstants.CONFIG_PATH + AutomationConstants.CONFIG_FILE,
				"PORT");
		String userName = Property.getPropertyValue(AutomationConstants.CONFIG_PATH + AutomationConstants.CONFIG_FILE,
				"MAILSENDERID");
		String password = Property.getPropertyValue(AutomationConstants.CONFIG_PATH + AutomationConstants.CONFIG_FILE,
				"MAILSENDERPASSWORD");
		String subject = Property.getPropertyValue(AutomationConstants.CONFIG_PATH + AutomationConstants.CONFIG_FILE,
				"MAILSUBJECT");
		String message = Property.getPropertyValue(AutomationConstants.CONFIG_PATH + AutomationConstants.CONFIG_FILE,
				"MAILMESSAGE");
		String recipient = Property.getPropertyValue(AutomationConstants.CONFIG_PATH + AutomationConstants.CONFIG_FILE,
				"MAILRECIPIENT");
		String attachments = Property
				.getPropertyValue(AutomationConstants.CONFIG_PATH + AutomationConstants.CONFIG_FILE, "ATTACHMENT");
		
		attachFiles = attachments.split(",");
		int count = 0;

		for (String attaching : attachFiles) 
		{
			attachFiles[count] = attaching.trim();
			count++;
		}

		// attachFiles[0] = "./result/result.html";
		// attachFiles[1] = "./result/result.log";
		// attachFiles[2] = "./test-output/emailable-report.html";

		// sets SMTP server properties
		java.util.Properties props = null;
		props = System.getProperties();

		props.put("mail.smtp.host", host);
		props.put("mail.smtp.socketFactory.port", port);
		props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.user", userName);
		props.put("mail.password", password);

		// creates a new session with an authenticator
		Authenticator auth = new Authenticator() 
		{
			public PasswordAuthentication getPasswordAuthentication() 
			{
				return new PasswordAuthentication(userName, password);
			}
		};
		Session session = Session.getInstance(props, auth);
		session.setDebug(true);

		try 
		{

			// creates a new e-mail message
			Message msg = new MimeMessage(session);

			String[] recipientList = recipient.split(",");
			InternetAddress[] recipientAddress = new InternetAddress[recipientList.length];
			int counter = 0;
			for (String recipients : recipientList) 
			{
				recipientAddress[counter] = new InternetAddress(recipients.trim());
				counter++;
			}
			msg.setRecipients(Message.RecipientType.TO, recipientAddress);
			msg.setSubject(subject);
			msg.setSentDate(new Date());

			// creates message part
			MimeBodyPart messageBodyPart = new MimeBodyPart();
			messageBodyPart.setContent(message, "text/html");

			// creates multi-part
			Multipart multipart = new MimeMultipart();
			multipart.addBodyPart(messageBodyPart);

			// adds attachments
			if (attachFiles != null && attachFiles.length > 0) 
			{
				for (String filePath : attachFiles)
				{
					MimeBodyPart attachPart = new MimeBodyPart();

					try 
					{
						attachPart.attachFile(filePath);
					} 
					catch (IOException ex) 
					{
						ex.printStackTrace();
					}

					multipart.addBodyPart(attachPart);
				}
			}

			// sets the multi-part as e-mail's content
			msg.setContent(multipart);

			Transport transport = session.getTransport("smtp");
			transport.connect(host, userName, password);
			transport.sendMessage(msg, msg.getAllRecipients());
			transport.close();
			// sends the e-mail

		} 
		catch (Exception e)
		{
			throw new RuntimeException(e);
		}
	}

	/**
	 * Test sending e-mail with attachments
	 */
	public static void main(String[] args) 
	{
		// SMTP info
		String host = "smtp.gmail.com";
		String port = "587";
		String mailFrom = "nextoryautomation130@gmail.com";
		String password = "nextory12345";

		// message info
		// String mailTo = "vibhav89.v@gmail.com";
		String subject = " EMAIL DEMO ";
		String message = "I have some attachments for you.";

		try 
		{
			sendEmailWithAttachments();
			System.out.println("Email sent.");
		} 
		catch (Exception ex)
		{
			System.out.println("Could not send email.");
			ex.printStackTrace();
		}
	}
}