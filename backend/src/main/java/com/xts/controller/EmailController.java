package com.xts.controller;

import com.xts.pojo.Result;
import com.xts.service.EmailService;
import com.xts.service.impl.EmailServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@CrossOrigin(origins = "*")
public class EmailController {

    @Autowired
    private EmailService emailService;

    @RequestMapping("/send-email")
    public Result sendEmail() {
        emailService.sendSimpleEmail(
                "2269235516@qq.com",
                "哈哈哈哈",
                "<h1>我是帅哥</h1>"
        );
        return Result.success("邮件发送成功！");
    }
}

//"2370528328@qq.com",xlj
//"2269235516@qq.com",wjl
