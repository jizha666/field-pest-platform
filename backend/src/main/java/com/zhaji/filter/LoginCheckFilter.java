package com.zhaji.filter;

import com.zhaji.utils.JwtUtils;
import io.jsonwebtoken.Claims;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

import java.io.IOException;

@Slf4j
@WebFilter(urlPatterns = "/*")
public class LoginCheckFilter implements Filter {

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
            throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) servletRequest;
        HttpServletResponse resp = (HttpServletResponse) servletResponse;

        String url = req.getRequestURL().toString();
        log.info("请求的URL:{}", url);

        if(url.contains("login")) {
            log.info("登录操作，放行");
            filterChain.doFilter(servletRequest, servletResponse);
            return;
        }

        String jwt = req.getHeader("token");

        if (!StringUtils.hasLength(jwt)) {
            log.info("请求头token为空，返回未登录的信息");
            resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
//            Result error = Result.error("请求头token为空");
//            String nologin = JSONObject.toJSONString(error);
//            resp.getWriter().write(nologin);
            return;
        }

        try{
            JwtUtils.parseJWT(jwt);
        }catch (Exception e){
            e.printStackTrace();
            log.info("令牌解析失败，返回未登录错误信息");
            resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        log.info("令牌合法，放行");
        filterChain.doFilter(servletRequest, servletResponse);
    }
}
