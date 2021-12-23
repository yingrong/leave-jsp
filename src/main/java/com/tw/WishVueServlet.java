package com.tw;

import com.fasterxml.jackson.databind.ObjectMapper;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Random;
import java.util.stream.Collectors;

@WebServlet(value = "/wish-vue")
public class WishVueServlet extends HttpServlet {

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String body = request.getReader().lines().collect(Collectors.joining());
        ObjectMapper objectMapper = new ObjectMapper();
        HashMap hashMap = objectMapper.readValue(body, HashMap.class);
        String name = (String) hashMap.get("name");
        request.setAttribute("wish", hashMap.get("wish"));
        request.getParameterMap().put("wish", new String[]{(String) hashMap.get("wish")});
//        String name = request.getParameter("name");
        String believe = (String) hashMap.get("believe");
        boolean promised = believe.equalsIgnoreCase("yes");
        if (promised) {
            request.getRequestDispatcher("/receive-wish.jsp")
                    .forward(request, response);
        } else {
            response.sendRedirect(response.encodeRedirectURL("/form-submit-vue.jsp") + "?name=" + name);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String body = req.getReader().lines().collect(Collectors.joining());
        ObjectMapper objectMapper = new ObjectMapper();
        WishRequestVO wishRequestVO = objectMapper.readValue(body, WishRequestVO.class);
        boolean promised = wishRequestVO.getBelieve().equalsIgnoreCase("yes");

        PrintWriter out = resp.getWriter();
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        if (promised) {
            resp.setStatus(HttpServletResponse.SC_OK);

            int wishNumber = new Random().nextInt();
            if (wishNumber == 0) {
                wishNumber = 100;
            }
            WishResponseVO wishResponseVO = new WishResponseVO(wishNumber);
            out.write(objectMapper.writeValueAsString(wishResponseVO));
        } else {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            ErrorResponseVO buCheng = new ErrorResponseVO(1, "xin bu cheng");
            out.print(objectMapper.writeValueAsString(buCheng));
        }
        out.flush();
    }
}
