package com.company.web.Fileter;

import javax.servlet.*;
import javax.servlet.annotation.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebFilter("/*")
public class CharsetFilter implements Filter {
    public void init(FilterConfig config) throws ServletException {
    }

    public void destroy() {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws ServletException, IOException {
        HttpServletResponse hsResponse = (HttpServletResponse) response;
        HttpServletRequest hsRequest = (HttpServletRequest) request;


        hsRequest.setCharacterEncoding("utf-8");
        hsResponse.setContentType("text/html;charset=utf-8");
        chain.doFilter(hsRequest, hsResponse);
    }
}
