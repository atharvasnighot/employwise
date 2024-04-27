package com.atharva.employwise.service;

import com.atharva.employwise.model.Employee;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class EmailSendingExecutor {

    private final EmailSenderService emailSenderService;

    public EmailSendingExecutor(EmailSenderService emailSenderService) {
        this.emailSenderService = emailSenderService;
    }

    public void sendEmailAsync(String toEmail, Employee employee) {
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.submit(() -> emailSenderService.sendMail(toEmail, employee));
        executorService.shutdown();
    }
}


