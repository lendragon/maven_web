package com.company.web.servlet;

import com.company.util.VerifyCodeUtil;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;

@WebServlet("/verify/*")
public class VerifyServlet extends BaseServlet {

    /**
     * 生成验证码, 并返回图片
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    public void getVerify(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // 生成验证码
        VerifyCodeUtil vc = new VerifyCodeUtil();

        // 获取验证码的结果
        String code = vc.generateCode();;

        HttpSession session = request.getSession();
        // 将新的验证码存入session中
        session.setAttribute("verify", code);

        // 将验证码写回浏览器
        vc.writeCodeToRespone(response);
    }

}
