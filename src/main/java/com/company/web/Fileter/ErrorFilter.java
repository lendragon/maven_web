package com.company.web.Fileter;

import javax.servlet.*;
import javax.servlet.annotation.*;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebFilter(urlPatterns = "/*", dispatcherTypes = DispatcherType.ERROR)
public class ErrorFilter implements Filter {
    public void init(FilterConfig config) throws ServletException {
    }

    public void destroy() {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws ServletException, IOException {
        // 如果出现了错误则重定向至error.html
        HttpServletResponse hsResponse = (HttpServletResponse) response;
        hsResponse.sendRedirect("/error.html");
        chain.doFilter(request, hsResponse);
    }
}
