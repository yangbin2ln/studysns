package com.eeesns.tshow.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.Message.RecipientType;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.drools.core.io.impl.ClassPathResource;

public class MailUtil {
	public static void sendMain(String subject, String content, String toAccount)
			throws MessagingException {
		Map map = getMailUserInfo();
		final String account = (String) map.get("account");
		final String password = (String) map.get("password");
		/*
		 * 1. 得到session
		 */
		Properties props = new Properties();
		props.setProperty("mail.smtp.host", "smtp.qq.com");
		props.setProperty("mail.smtp.auth", "true");

		Authenticator auth = new Authenticator() {
			@Override
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(account, password);
			}
		};

		Session session = Session.getInstance(props, auth);

		/*
		 * 2. 创建MimeMessage
		 */
		MimeMessage msg = new MimeMessage(session);
		msg.setFrom(new InternetAddress(account));// 设置发件人
		msg.setRecipients(RecipientType.TO, toAccount);// 设置收件人
		// msg.setRecipients(RecipientType.CC, "itcast_cxf@sohu.com");//设置抄送
		// msg.setRecipients(RecipientType.BCC, "itcast_cxf@sina.com");//设置暗送
		msg.setSubject(subject);

		msg.setContent(content, "text/html;charset=utf-8");

		/*
		 * 3. 发
		 */
		Transport.send(msg);

	}

	/**
	 * 使用模块发送
	 * 
	 * @param subject
	 * @param content
	 * @param toAccount
	 * @throws MessagingException
	 */
	public static void sendMainTemplete(String subject, Object[] params, String path,
			String toAccount) throws MessagingException {
		Map map = getMailUserInfo();
		final String account = (String) map.get("account");
		final String password = (String) map.get("password");
		/*
		 * 1. 得到session
		 */
		Properties props = new Properties();
		props.setProperty("mail.smtp.host", "smtp.qq.com");
		props.setProperty("mail.smtp.auth", "true");

		Authenticator auth = new Authenticator() {
			@Override
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(account, password);
			}
		};

		Session session = Session.getInstance(props, auth);

		/*
		 * 2. 创建MimeMessage
		 */
		MimeMessage msg = new MimeMessage(session);
		msg.setFrom(new InternetAddress(account));// 设置发件人
		msg.setRecipients(RecipientType.TO, toAccount);// 设置收件人
		// msg.setRecipients(RecipientType.CC, "itcast_cxf@sohu.com");//设置抄送
		// msg.setRecipients(RecipientType.BCC, "itcast_cxf@sina.com");//设置暗送
		msg.setSubject(subject);
		String content = reader(path);
		content = MessageFormat.format(content, params);
		msg.setContent(content, "text/html;charset=utf-8");

		/*
		 * 3. 发
		 */
		Transport.send(msg);

	}

	public static void main(String[] args) throws IOException, MessagingException {
		/*
		 * String invationCode = "tShow-0002"; String toAccount =
		 * "1819995223@qq.com"; sendMain("tShow用户向您发送了邀请码", "  邀请码：" +
		 * invationCode +
		 * " \r 您的邮箱：1819995223@qq.com \r 点击下方链接激活邮箱http://juyibuluo.com",
		 * toAccount);
		 */
		Object[] object = new Object[2];
		object[0] = "杨斌";
		object[1] = "00021";
		sendMainTemplete(
				"这是一封T-Show用户的邀请函",
				object,
				"E:\\MyEclipse\\MyEclipse 10\\f\\Workspaces\\MyEclipse 10\\tShow\\WebRoot\\templete\\mailTemplete.xml",
				"1819995223@qq.com");
	}

	public static Map getMailUserInfo() {
		Map map = new HashMap();
		try {
			ClassPathResource fis = new ClassPathResource("properties/mail.properties");
			InputStream inputStream;

			inputStream = fis.getInputStream();

			Properties p = new Properties();
			p.load(inputStream);
			inputStream.close();
			String account = p.getProperty("account");
			String password = p.getProperty("password");
			System.out.println(account);
			System.out.println(password);
			map.put("account", account);
			map.put("password", password);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return map;
	}

	public static String reader(String path) {
		SAXReader reader = new SAXReader();
		String str = null;
		try {
			Document d = reader.read(new File(path));
			Element root = d.getRootElement();
			Element html = root.element("html");
			str = html.asXML();
		} catch (DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return str;
	}
}
