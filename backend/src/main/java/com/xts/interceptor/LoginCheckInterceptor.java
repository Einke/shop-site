package com.xts.interceptor;

import com.alibaba.fastjson2.JSON;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import com.xts.pojo.Result;
import com.xts.utils.JwtUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;

@Slf4j
@Component
public class LoginCheckInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest req, HttpServletResponse resp, Object handler) throws Exception {

        //1.获取请求url
        String url = req.getRequestURI();
        log.info("请求的url:{}", url);

        //2.判断请求url是否包含login，如果包含，说明是登录操作，放行
        if(url.contains("login")){
            log.info("登录操作，放行！");
            return true;
        }

        //3.获取请求头中的令牌（token）
        String jwt = req.getHeader("token");

        //4.判断令牌是否存在，若不存在返回错误结果
        if(!StringUtils.hasLength(jwt)){
            log.info("token为空，返回未登录的信息");
            Result error = Result.error("NOT_LOGIN");
            //手动转换json
            String notLogin = JSON.toJSONString(error);
            resp.getWriter().write(notLogin);
            return false;
        }

        //5.解析token，如果解析失败，返回错误结果
        try {
            JwtUtils.parseJWT(jwt);
        } catch (Exception e) {
            e.printStackTrace();
            log.info("解析令牌失败，返回未登录信息");
            Result error = Result.error("NOT_LOGIN");
            String notLogin = JSON.toJSONString(error);
            resp.getWriter().write(notLogin);
            return false;
        }

        //6.放行
        log.info("令牌合法，放行");
        return true;
    }
}
