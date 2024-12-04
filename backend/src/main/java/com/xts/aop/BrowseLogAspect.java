package com.xts.aop;

import com.xts.mapper.BrowseLogMapper;
import com.xts.pojo.BrowseLog;
import com.xts.utils.JwtUtils;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Slf4j
@Component
@Aspect
public class BrowseLogAspect {

    @Autowired
    private HttpServletRequest request;

    @Autowired
    private BrowseLogMapper browseLogMapper;

    @Around("@annotation(com.xts.anno.BrowseLog)")
    public Object recordBrowseLog(ProceedingJoinPoint joinPoint) throws Throwable {
        //操作人id
        String jwt = request.getHeader("token");
        Claims claims = JwtUtils.parseJWT(jwt);
        Integer BrowseUser = (Integer) claims.get("id");

        //商品id
        Object[] args = joinPoint.getArgs();
        Integer shopId = (Integer) args[0];

        //操作时间
        LocalDateTime BrowseTime = LocalDateTime.now();

        long begin = System.currentTimeMillis();
        //调用原始方法运行
        Object result = joinPoint.proceed();
        long end = System.currentTimeMillis();
        //浏览总时长
        long costTime = end - begin;

        BrowseLog browseLog = new BrowseLog();
        browseLog.setUserId(BrowseUser);
        browseLog.setShopId(shopId);
        browseLog.setOperateTime(BrowseTime);
        browseLog.setCostTime(costTime);

        browseLogMapper.insert(browseLog);

        log.info("AOP操作浏览日志：{}",browseLog);
        return result;
    }

}
