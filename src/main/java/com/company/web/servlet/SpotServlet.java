package com.company.web.servlet;

import com.company.domain.Page;
import com.company.service.Impl.CollectServiceImpl;
import com.company.service.Impl.SpotServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.util.Set;

@WebServlet("/spot/*")
public class SpotServlet extends BaseServlet {
    private SpotServiceImpl ssi = new SpotServiceImpl();

    public void spot(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json;charset=utf-8");
        String page = request.getParameter("currentPage");
        page = page == null ? "1" : page;

        // 获取到所有景点的json信息
        Page p = new Page();
        String spotInfo = ssi.findByPage(page, p.getOnePageRecord());
        int allRecord = ssi.findAllRecord();

        p.setAllRecord(allRecord);
        p.setCurrentPage(Integer.parseInt(page));
        String pageInfo = new ObjectMapper().writeValueAsString(p) ;

        // 获取用户的收藏列表
        String account = null;
        Cookie[] cookies = request.getCookies();
        for (Cookie cookie : cookies) {
            if ("account".equals(cookie.getName())) {
                account = cookie.getValue();
                break;
            }
        }

        Set<String> set;
        String setStr = null;
        if (account != null) {
            CollectServiceImpl csi = new CollectServiceImpl();
            set = csi.findByAccount(account);
            if (set != null) {
                setStr = new ObjectMapper().writeValueAsString(set);
            }
        }

        // 拼接json
        String json = "[" + spotInfo + ", " + pageInfo + ", " + setStr + "]";

        // 以json形式返回给前台
        response.getWriter().write(json);
    }

}
