package com.rmd.task.util;

import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 邮件发送工具类
 * @author zuoguodong
 */
public class MailUtil {
	
	static Logger log = LoggerFactory.getLogger(MailUtil.class.getClass());
	
	private static String host = PropsUtil.readProps("mail.host");
	private static String from = PropsUtil.readProps("mail.from");
	private static String pwd = PropsUtil.readProps("mail.pwd");
	private static String to = PropsUtil.readProps("mail.to");
	
	public static void sendMail(String subject,String text){
		Properties props = System.getProperties();
        props.put("mail.smtp.host", host);
        props.put("mail.smtp.auth", "true");
        Session session = Session.getDefaultInstance(props, new Authenticator() {
            @Override
            public PasswordAuthentication getPasswordAuthentication(){
                return new PasswordAuthentication(from, pwd);
            }
        });
        try {
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(from));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
            message.setSubject(subject);
            message.setText(text);
            Transport.send(message);
        } catch (AddressException e) {
            log.error("邮件地址错误",e);
        } catch (MessagingException e) {
        	log.error("发送邮件出错",e);
        }
	}
	
	public static void main(String args[]){
		MailUtil.sendMail("hello", "这是一封测试邮件");
	}
	
}
