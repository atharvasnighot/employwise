package com.atharva.employwise.service;

import com.atharva.employwise.model.Employee;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailSenderService {

    @Autowired
    private JavaMailSender mailSender;

    public void sendMail(String toEmail, Employee employee) {

        SimpleMailMessage message = new SimpleMailMessage();

        message.setFrom("compliancenexus07@gmail.com");
        message.setTo(toEmail);
        message.setText(employee.getEmployeeName() +
                " will now work under you. Mobile number is " + employee.getPhoneNumber() + " and email is " +
                employee.getEmail());
        message.setSubject("New Employee Added");

        mailSender.send(message);
        System.out.println("Mail Sent Successfully");

    }
}

