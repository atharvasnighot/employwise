package com.atharva.employwise.config;

import com.atharva.employwise.service.EmailSenderService;
import com.atharva.employwise.service.EmailSendingExecutor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {

    @Bean
    public EmailSendingExecutor emailSendingExecutor(EmailSenderService emailSenderService) {
        return new EmailSendingExecutor(emailSenderService);
    }
}

