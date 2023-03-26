package com.company.web.servlet;

import com.company.domain.ResultInfo;
import com.company.domain.Spot;
import com.company.domain.User;
import com.company.service.Impl.CollectServiceImpl;
import com.company.service.Impl.SpotServiceImpl;
import com.company.service.Impl.UserServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.beanutils.BeanUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.*;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Map;
import java.util.Set;

@WebServlet("/user/*")
public class UserServlet extends BaseServlet {
    private UserServiceImpl usi = new UserServiceImpl();

    /**
     * 判断用户是否登录
     *
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    public void isLogin(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // 返回json格式
        response.setContentType("application/json;charset=utf-8");

        Cookie[] cookies = request.getCookies();
        for (Cookie cookie : cookies) {
            // 查看是否有cookie
            if ("account".equals(cookie.getName())) {
                // 有, 通过session获取到用户信息
                /*User user = (User) request.getSession().getAttribute(cookie.getValue());*/

                User user = usi.findByAccount(cookie.getValue());
                String userStr = new ObjectMapper().writeValueAsString(user);
                response.getWriter().write(userStr);
            }
        }
    }


    /**
     * 登录操作
     *
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    public void login(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json;charset=utf-8");
        // 获取用户输入的账号, 密码和验证码
        String checkCodeIn = request.getParameter("checkCode");

        // 从session中获取验证码
        String verify = (String) request.getSession().getAttribute("verify");
        request.getSession().removeAttribute("verify");

        ResultInfo ri = new ResultInfo();
        ObjectMapper om = new ObjectMapper();

        // 查看用户输入的验证码是否正确
        if (verify == null || !verify.equalsIgnoreCase(checkCodeIn)) {
            // 不正确, 直接返回验证码错误
            ri.setResult(false);
            ri.setInfo("验证码错误");
            response.getWriter().write(om.writeValueAsString(ri));
            return;
        }

        // 正确在判断用户名和密码是否正确
        String accountIn = request.getParameter("account");
        String passwordIn = request.getParameter("password");
        // 查看用户输入的账号和密码是否正确
        User user = usi.findUser(accountIn, passwordIn);
        if (user == null) {
            // 错误, 返回错误信息
            ri.setResult(false);
            ri.setInfo("用户名或密码错误");
            response.getWriter().write(om.writeValueAsString(ri));
            return;
        }
        // 正确, 写入cookie, 3分钟内不用重复登录
        Cookie cookie = new Cookie("account", accountIn);
        cookie.setPath(request.getContextPath());
        cookie.setHttpOnly(true);
        response.addCookie(cookie);

        request.getSession().setAttribute(accountIn, user);

        ri.setResult(true);
        ri.setInfo("登录成功");
        response.getWriter().write(om.writeValueAsString(ri));
    }


    /**
     * 注册操作
     *
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    public void regist(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json;charset=utf-8");

        // 获取前台数据
        Map<String, String[]> map = request.getParameterMap();
        User user = new User();
        try {
            BeanUtils.populate(user, map);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }

        // 判断是否存在一样的account的用户
        ResultInfo ri = new ResultInfo();
        ObjectMapper om = new ObjectMapper();

        if (usi.findByAccount(user.getAccount()) != null) {
            // 如果有, 返回错误信息
            ri.setResult(false);
            ri.setInfo("该账号已存在");
            response.getWriter().write(om.writeValueAsString(ri));

            return;
        }

        // 如果没有, 将数据写入数据库, 返回成功
        usi.insert(user);
        ri.setResult(true);
        ri.setInfo("注册成功");
        response.getWriter().write(om.writeValueAsString(ri));
    }


    /**
     * 收藏操作
     *
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    public void collect(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json;charset=utf-8");

        // 获取用户的account参数和景点的sid参数
        Cookie[] cookies = request.getCookies();
        String account = null;
        for (Cookie cookie : cookies) {
            if ("account".equals(cookie.getName())) {
                account = cookie.getValue();
                break;
            }
        }

        ResultInfo ri = new ResultInfo();

        if (account == null) {
            // 提示用户登录
            ri.setResult(false);
            ri.setInfo("请先登录");
            response.getWriter().write(new ObjectMapper().writeValueAsString(ri));
            return;
        }

        String sidStr = request.getParameter("sid");
        Integer sid = null;
        try {
            sid = Integer.parseInt(sidStr);
        } catch (Exception e) {
        }

        // 更新用户的数据, 收藏该景点
        Integer result = usi.collectSpot(account, sid);

        if (result != null && result == 1) {
            // 更新成功则返回
            ri.setResult(true);
            ri.setInfo("收藏成功");
        } else {
            // 更新失败
            ri.setResult(false);
            ri.setInfo("收藏失败");
        }

        response.getWriter().write(new ObjectMapper().writeValueAsString(ri));
    }

    /**
     * 取消收藏操作
     *
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    public void unCollect(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json;charset=utf-8");

        // 获取用户的account和景点的sid参数
        String sidStr = request.getParameter("sid");
        int sid;
        try {
            sid = Integer.parseInt(sidStr);
        } catch (Exception e) {
            return;
        }

        // 更新用户的数据, 取消收藏该景点
        Cookie[] cookies = request.getCookies();
        String account = null;
        for (Cookie cookie : cookies) {
            if ("account".equals(cookie.getName())) {
                account = cookie.getValue();
                break;
            }
        }
        Integer result = usi.unCollectSpot(account, sid);

        ResultInfo ri = new ResultInfo();
        if (result != null && result == 1) {
            // 更新成功则返回
            ri.setResult(true);
            ri.setInfo("取消收藏成功");
        } else {
            // 更新失败
            ri.setResult(false);
            ri.setInfo("取消收藏失败");
        }

        response.getWriter().write(new ObjectMapper().writeValueAsString(ri));
    }


    /**
     * 获取用户的收藏集合并返回
     *
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    public void getCollect(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json;charset=utf-8");

        // 获取用户的account参数和景点的sid参数
        Cookie[] cookies = request.getCookies();
        String account = null;
        for (Cookie cookie : cookies) {
            if ("account".equals(cookie.getName())) {
                account = cookie.getValue();
                break;
            }
        }

        CollectServiceImpl csi = new CollectServiceImpl();
        Set<String> set = csi.findByAccount(account);

        // 通过sid获取到每一个spot对象
        SpotServiceImpl ssi = new SpotServiceImpl();
        ArrayList<Spot> arrayList = new ArrayList<>();
        for (String s : set) {
            arrayList.add(ssi.findBySid(Integer.parseInt(s)));
        }

        response.getWriter().write(new ObjectMapper().writeValueAsString(arrayList));

    }


    /**
     * 退出登录
     *
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    public void unLogin(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json;charset=utf-8");
        Cookie[] cookies = request.getCookies();
        Cookie newCookie = null;
        String account = null;

        // 查找是否有该用户的cookie信息(实际开发中不能这样, 因为用户可能禁用cookie功能)
        for (Cookie cookie : cookies) {
            if ("account".equals(cookie.getName())) {
                account = cookie.getValue();
                newCookie = new Cookie("account", "");
                newCookie.setPath(request.getContextPath());
                newCookie.setMaxAge(0);
            }
        }
        ResultInfo ri = new ResultInfo();

        // 如果没有获取到cookie
        if (newCookie == null) {
            ri.setResult(false);
            ri.setInfo("退出失败");
            new ObjectMapper().writeValue(response.getOutputStream(), ri);
            return;
        }

        // 发送新的立即销毁的cookie
        response.addCookie(newCookie);
        // 销毁session
        if (account != null) {
            request.getSession().removeAttribute(account);
        }

        ri.setResult(true);
        ri.setInfo("退出成功");
        new ObjectMapper().writeValue(response.getOutputStream(), ri);
    }
}

