package com.example.demo.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.nio.file.Path;

import org.springframework.core.io.FileSystemResource;

import java.util.Map;

@Service
public class EmailService {

    private final JavaMailSender mailSender;
    private final SpringTemplateEngine templateEngine;

    public EmailService(
            JavaMailSender mailSender,
            SpringTemplateEngine templateEngine) {

        this.mailSender = mailSender;
        this.templateEngine = templateEngine;
    }

    // Normal email
    public void sendEmail(
        String to,
        String subject,
        String templateName,
        Map<String, Object> variables) throws MessagingException {

        MimeMessage mail = mailSender.createMimeMessage();

        MimeMessageHelper helper =
                new MimeMessageHelper(mail, true);

        Context context = new Context();
        context.setVariables(variables);

        String htmlContent =
                templateEngine.process(templateName, context);

        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(htmlContent, true);

        mailSender.send(mail);
    }

    // Email with PDF attachment
    public void sendEmailWithAttachment(
        String to,
        String subject,
        String templateName,
        Map<String, Object> variables,
        String attachmentPath) throws MessagingException {

        MimeMessage mail = mailSender.createMimeMessage();

        MimeMessageHelper helper =
                new MimeMessageHelper(mail, true);

        // Thymeleaf context
        Context context = new Context();
        context.setVariables(variables);

        // Process HTML template
        String htmlContent =
                templateEngine.process(templateName, context);

        // Configure email
        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(htmlContent, true);

        // Load PDF from resources
        ClassPathResource resource =
                new ClassPathResource(attachmentPath);

        // Check whether PDF exists
        if (!resource.exists()) {
            throw new MessagingException(
                    "Attachment not found: " + attachmentPath
            );
        }

        // Attach PDF
        helper.addAttachment(
                resource.getFilename(),
                resource
        );

        // Send email
        mailSender.send(mail);
    }
    public void sendEmailWithAttachment(
        String to,
        String subject,
        String templateName,
        Map<String, Object> variables,
        Path attachmentPath) throws MessagingException {

    MimeMessage mail =
            mailSender.createMimeMessage();

    MimeMessageHelper helper =
            new MimeMessageHelper(mail, true);

    // Thymeleaf context
    Context context = new Context();
    context.setVariables(variables);

    // Process HTML template
    String htmlContent =
            templateEngine.process(
                    templateName,
                    context
            );

    // Configure email
    helper.setTo(to);
    helper.setSubject(subject);
    helper.setText(htmlContent, true);

    // Load generated file
    FileSystemResource resource =
            new FileSystemResource(attachmentPath);

    // Check whether file exists
    if (!resource.exists()) {
        throw new MessagingException(
                "Attachment not found: "
                        + attachmentPath
        );
    }

    // Attach generated certificate
    helper.addAttachment(
            resource.getFilename(),
            resource
    );

    // Send email
    mailSender.send(mail);
}
public void sendCustomEmail(
        String to,
        String subject,
        String message)
        throws MessagingException {

    MimeMessage mail =
            mailSender.createMimeMessage();

    MimeMessageHelper helper =
            new MimeMessageHelper(mail, true);

    helper.setTo(to);
    helper.setSubject(subject);

    String htmlMessage = message
            .replace("&", "&amp;")
            .replace("<", "&lt;")
            .replace(">", "&gt;")
            .replace("\n", "<br>");

    helper.setText(htmlMessage, true);

    mailSender.send(mail);
}
}