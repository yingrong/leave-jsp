package com.tw.archive;

import com.fasterxml.jackson.databind.ObjectMapper;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Random;
import java.util.stream.Collectors;

@WebServlet(value = "/archive/wish-vue")
public class WishVueServlet extends HttpServlet {

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.sendRedirect(response.encodeRedirectURL("/archive/form-submit-vue.jsp"));
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
