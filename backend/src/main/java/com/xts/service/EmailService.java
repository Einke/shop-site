package com.xts.service;

public interface EmailService {


    boolean sendSimpleEmail(String to, String subject, String body);
}
