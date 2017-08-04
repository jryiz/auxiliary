package com.auxiliary.service;

import com.auxiliary.model.MailFlow;
import com.auxiliary.repository.MailFlowRepository;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.mail.MailProperties;
import org.springframework.boot.autoconfigure.mail.MailSenderAutoConfiguration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.File;
import java.util.Date;

/**
 * @auther ucmed Wenjun Choi
 * @create 2017/7/17
 */

@Service
public class MailService {
    private final Logger LOG = Logger.getLogger(this.getClass());
    @Autowired
    private JavaMailSender javaMailSender;
    @Autowired
    private  MailProperties properties;

    @Autowired
    private MailFlowRepository mailFlowRepository;

    @Value("${spring.mail.username}")
    private String mailFrom;

    /**
     * 发送纯文本的简单邮件
     * @param to
     * @param subject
     * @param content
     */
    public void sendSimpleMail(String to,String subject,String content) throws MessagingException {
        SimpleMailMessage smm = new SimpleMailMessage();
        smm.setFrom(mailFrom);
        smm.setTo(to);
        smm.setSubject(subject);
        smm.setText(content);
        javaMailSender.send(smm);
//        LOG.info(properties.getProperties().toString());
        save(to,subject,content,null,null);
        LOG.info("send mail success");
//        LOG.error("send mail failed",e);
    }

    public void sendSimpleMail(MailFlow mailFlow) throws MessagingException{
        this.sendSimpleMail(mailFlow.getMailTo(), mailFlow.getSubject(), mailFlow.getContent());
    }
    /**
     *
     * @param to
     * @param subject
     * @param content
     */
    public void sendHtmlMail(String to, String subject, String content){
        MimeMessage message = javaMailSender.createMimeMessage();
        try{
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setFrom(mailFrom);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(content, true);

            javaMailSender.send(message);
            LOG.info("send html mail success");
        } catch (Exception e){
            LOG.error("send mail failed",e);
        }
    }
    /**
     * 发送带附件的邮件
     * @param to
     * @param subject
     * @param content
     * @param filePath
     */
    public void sendAttachmentsMail(String to, String subject, String content, String filePath){
        MimeMessage message = javaMailSender.createMimeMessage();

        try {
            //true表示需要创建一个multipart message
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setFrom(mailFrom);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(content, true);

            FileSystemResource file = new FileSystemResource(new File(filePath));
            String fileName = filePath.substring(filePath.lastIndexOf(File.separator));
            helper.addAttachment(fileName, file);

            javaMailSender.send(message);
            LOG.info("带附件的邮件已经发送。");
        } catch (MessagingException e) {
            LOG.error("发送带附件的邮件时发生异常！", e);
        }
    }

    /**
     * 发送嵌入静态资源（一般是图片）的邮件
     * @param to
     * @param subject
     * @param content 邮件内容，需要包括一个静态资源的id，比如：<img src=\"cid:rscId01\" >
     * @param rscPath 静态资源路径和文件名
     * @param rscId 静态资源id
     */
    public void sendInlineResourceMail(String to, String subject, String content, String rscPath, String rscId){
        MimeMessage message = javaMailSender.createMimeMessage();

        try {
            //true表示需要创建一个multipart message
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setFrom(mailFrom);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(content, true);

            FileSystemResource res = new FileSystemResource(new File(rscPath));
            helper.addInline(rscId, res);

            javaMailSender.send(message);
            LOG.info("嵌入静态资源的邮件已经发送。");
        } catch (MessagingException e) {
            LOG.error("发送嵌入静态资源的邮件时发生异常！", e);
        }
    }

    private void save(String to, String subject, String content, String rscPath, String rscId){
        MailFlow mail = new MailFlow();
        mail.setMailFrom(mailFrom);
        mail.setMailTo(to);
        mail.setSubject(subject);
        mail.setContent(content);
        mail.setPath(rscPath);
        mail.setRscId(rscId);
        mail.setSendDate(new Date());
        mailFlowRepository.save(mail);
    }
}
