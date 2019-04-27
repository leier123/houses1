package com.feihong.utils;


import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMessage.RecipientType;
import java.util.Properties;

public class MailUtils {
    public static void main(String[] args) throws MessagingException {
        // 1.创建一个程序与邮件服务器会话对象 Session
        String email="1203894285@qq.com";
        String emailMsg = "liuyonglei123";
        Properties props = new Properties();
        props.setProperty("mail.transport.protocol", "SMTP");
        props.setProperty("mail.host", "smtp.163.com");
        props.setProperty("mail.smtp.auth", "true");// 指定验证为true是否需要身份验证

        // 创建验证器
        Authenticator auth = new Authenticator() {
            public PasswordAuthentication getPasswordAuthentication() {
                // 密码验证
                return new PasswordAuthentication("yuranran520@yeah.net", "liuyonglei123");

            }
        };

        Session session = Session.getInstance(props, auth);
        // 开启Session的debug模式，这样就可以查看到程序发送Email的运行状态
        // 2.创建一个Message，它相当于是邮件内容
        Message message = new MimeMessage(session);

        message.setFrom(new InternetAddress("yuranran520@yeah.net")); // 设置发送者

        message.setRecipient(RecipientType.TO, new InternetAddress(email)); // 设置发送方式与接收者

        message.setSubject("用户激活");
        message.setText("这是一封激活邮件，请<a href='#'>点击</a>");

        message.setContent(emailMsg, "text/html;charset=utf-8");

        // 3.创建 Transport用于将邮件发送

        Transport.send(message);

    }
}
