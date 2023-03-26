package com.company.web.Fileter;

import javax.servlet.*;
import javax.servlet.annotation.*;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebFilter("/index.html")
public class LoginFilter implements Filter {
    public void init(FilterConfig config) throws ServletException {
    }

    public void destroy() {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws ServletException, IOException {

        HttpServletRequest hsRequest = (HttpServletRequest) request;
        Cookie[] cookies = hsRequest.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("account".equals(cookie.getName())) {
                    // 如果有account cookie, 则放行
                    chain.doFilter(hsRequest, response);
                    return;
                }
            }
        }
        // 没有account cookie, 跳转至登录页面
        HttpServletResponse hsResponse = (HttpServletResponse) response;
        hsResponse.sendRedirect(hsRequest.getContextPath() + "/login.html");
        chain.doFilter(hsRequest, hsResponse);


    }
}
