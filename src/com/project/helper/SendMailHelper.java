/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.project.helper;

import java.util.Properties;
import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

/**
 *
 * @author addie
 */
public class SendMailHelper {

    public static void sendMail(String fileName, String userName, String subject, String email, Boolean isFeedbackMail) {
        // Recipient's email ID needs to be mentioned.
        String to = email;//change accordingly

        // Sender's email ID needs to be mentioned
        String from = Queries.APPLICATION_NAME + " <todaysupreme@gmail.com>";//change accordingly
//        final char[] usernameChar = "todaysupreme@gmail.com".toCharArray();
//        final char[] passwordChar = "st#5845@256".toCharArray();//change accordingly
//        final String username = "todaysupreme@gmail.com";//change accordingly
//        final String password = "st#5845@256";//change accordingly

        // Assuming you are sending email through relay.jangosmtp.net
        String host = "smtp.sendgrid.net";

        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.starttls.required", "true");
        props.put("mail.smtp.host", host);
        props.put("mail.smtp.port", "587");

        // Get the Session object.
        Session session = Session.getInstance(props,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(String.valueOf(Queries.EMAIL_USER_NAME_IN_CHAR), String.valueOf(Queries.EMAIL_PASSWORD_IN_CHAR));
                    }
                });

        try {

            //            // Create a default MimeMessage object.
            MimeMessage message = new MimeMessage(session);

            // Set From: header field of the header.
            message.setFrom(new InternetAddress(from));

            // Set To: header field of the header.
            message.addRecipient(Message.RecipientType.TO,
                    new InternetAddress(to));

            // Set Subject: header field
            if (isFeedbackMail)
            {
                message.setSubject(subject + " - Feedback from " + userName);
            }
            else
            {
                message.setSubject(Queries.APPLICATION_NAME + ": A Case Has been referred to You");
            }

            // Create the message part
            BodyPart messageBodyPart = new MimeBodyPart();

            // Fill the message

            if (isFeedbackMail)
            {
                String htmlText = fileName + "<br/><br/><img src=\"http://www.supreme-today.com:8080/assets/theme/img/logo.png\">";
                messageBodyPart.setContent(htmlText, "text/html");
            }else
            {
                String data = "Dear User,<br/><br/>" + "This Case has been referred by " + userName + "<br/><br/>Regards,<br/><br/> Vikas Info Solutions Pvt. Ltd.," + "Delhi";
                String htmlText = data + "<br/><br/><img src=\"http://www.supreme-today.com:8080/assets/theme/img/logo.png\">";
                messageBodyPart.setContent(htmlText, "text/html");
            }

            // Create a multipar message
            Multipart multipart = new MimeMultipart();

            // Set text message part
            multipart.addBodyPart(messageBodyPart);

            // Part two is attachment

            if (isFeedbackMail==false)
            {
                messageBodyPart = new MimeBodyPart();
                DataSource source = new FileDataSource(fileName);
                messageBodyPart.setDataHandler(new DataHandler(source));
                messageBodyPart.setFileName(fileName);
                multipart.addBodyPart(messageBodyPart);
            }

            // Send the complete message parts
            message.setContent(multipart);

            // Send message
            Transport.send(message);
            //System.out.println("Sent message successfully....");

        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }

//    public static void sendMail(String fileName, String userName, String subject, String email, Boolean isFeedbackMail) {
//        // Recipient's email ID needs to be mentioned.
//        String to = email;//change accordingly
//
//        // Sender's email ID needs to be mentioned
//        String from = "Supreme Today <todaysupreme@gmail.com>";//change accordingly
////        final char[] usernameChar = "todaysupreme@gmail.com".toCharArray();
////        final char[] passwordChar = "st#5845@256".toCharArray();//change accordingly
////        final String username = "todaysupreme@gmail.com";//change accordingly
////        final String password = "st#5845@256";//change accordingly
//
//        // Assuming you are sending email through relay.jangosmtp.net
//        String host = "smtp.gmail.com";
//
//        Properties props = new Properties();
//        props.put("mail.smtp.auth", "true");
//        props.put("mail.smtp.starttls.enable", "true");
//        props.put("mail.smtp.host", host);
//        props.put("mail.smtp.port", "587");
//
//        // Get the Session object.
//        Session session = Session.getInstance(props,
//                new javax.mail.Authenticator() {
//                    protected PasswordAuthentication getPasswordAuthentication() {
//                        return new PasswordAuthentication(String.valueOf(Queries.EMAIL_USER_NAME_IN_CHAR), String.valueOf(Queries.EMAIL_PASSWORD_IN_CHAR));
//                    }
//                });
//
//        try {
//
//            //            // Create a default MimeMessage object.
//            MimeMessage message = new MimeMessage(session);
//
//            // Set From: header field of the header.
//            message.setFrom(new InternetAddress(from));
//
//            // Set To: header field of the header.
//            message.addRecipient(Message.RecipientType.TO,
//                    new InternetAddress(to));
//
//            // Set Subject: header field
//            if (isFeedbackMail)
//            {
//                message.setSubject(subject + " - Feedback from " + userName);
//            }
//            else
//            {
//                message.setSubject("Supreme Today: A Case Has been referred to You");
//            }
//
//            // Create the message part
//            BodyPart messageBodyPart = new MimeBodyPart();
//
//            // Fill the message
//
//            if (isFeedbackMail)
//            {
//                String htmlText = fileName + "<br/><br/><img src=\"http://www.supreme-today.com:8080/assets/theme/img/logo.png\">";
//                messageBodyPart.setContent(htmlText, "text/html");
//            }else
//            {
//                String data = "Dear User,<br/><br/>" + "This Case has been referred by " + userName + "<br/><br/>Regards,<br/><br/> Vikas Info Solutions Pvt. Ltd.," + "Delhi";
//                String htmlText = data + "<br/><br/><img src=\"http://www.supreme-today.com:8080/assets/theme/img/logo.png\">";
//                messageBodyPart.setContent(htmlText, "text/html");
//            }
//
//            // Create a multipar message
//            Multipart multipart = new MimeMultipart();
//
//            // Set text message part
//            multipart.addBodyPart(messageBodyPart);
//
//            // Part two is attachment
//
//            if (isFeedbackMail==false)
//            {
//                messageBodyPart = new MimeBodyPart();
//                DataSource source = new FileDataSource(fileName);
//                messageBodyPart.setDataHandler(new DataHandler(source));
//                messageBodyPart.setFileName(fileName);
//                multipart.addBodyPart(messageBodyPart);
//            }
//
//            // Send the complete message parts
//            message.setContent(multipart);
//
//            // Send message
//            Transport.send(message);
//            //System.out.println("Sent message successfully....");
//
//        } catch (MessagingException e) {
//            throw new RuntimeException(e);
//        }
//    }


//    public static void main(String[] args) {
//
//        // Recipient's email ID needs to be mentioned.
//        String to = "ronakkoradiya@gmail.com";
//
//        // Sender's email ID needs to be mentioned
//        String from = "ronakkoradiya102@gmail.com";
//
//        // Assuming you are sending email from localhost
//        String host = "localhost";
//
//        // Get system properties
//        Properties properties = System.getProperties();
//
//        // Setup mail server
//        properties.setProperty("mail.smtp.host", host);
//        properties.setProperty("mail.user", "ronakkoradiya102");
//        properties.setProperty("mail.password", "ronak102");
//        // Get the default Session object.
//        Session session = Session.getDefaultInstance(properties);
//
//        try {
//            // Create a default MimeMessage object.
//            MimeMessage message = new MimeMessage(session);
//
//            // Set From: header field of the header.
//            message.setFrom(new InternetAddress(from));
//
//            // Set To: header field of the header.
//            message.addRecipient(Message.RecipientType.TO,
//                    new InternetAddress(to));
//
//            // Set Subject: header field
//            message.setSubject("This is the Subject Line!");
//
//            // Create the message part 
//            BodyPart messageBodyPart = new MimeBodyPart();
//
//            // Fill the message
//            messageBodyPart.setText("This is message body");
//
//            // Create a multipar message
//            Multipart multipart = new MimeMultipart();
//
//            // Set text message part
//            multipart.addBodyPart(messageBodyPart);
//
//         // Part two is attachment
////         messageBodyPart = new MimeBodyPart();
////         String filename = "file.txt";
////         DataSource source = new FileDataSource(filename);
////         messageBodyPart.setDataHandler(new DataHandler(source));
////         messageBodyPart.setFileName(filename);
////         multipart.addBodyPart(messageBodyPart);
//            // Send the complete message parts
//            message.setContent(multipart);
//
//            // Send message
//            Transport.send(message);
//            //System.out.println("Sent message successfully....");
//        } catch (MessagingException mex) {
//            mex.printStackTrace();
//        }
//    }
}
