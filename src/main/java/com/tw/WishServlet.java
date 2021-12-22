package com.tw;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(value = "/wish")
public class WishServlet extends HttpServlet {

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String name = request.getParameter("name");
        String believe = request.getParameter("believe");
        boolean promised = believe.equalsIgnoreCase("yes");
        if (promised) {
            request.getRequestDispatcher("/receive-wish.jsp")
                    .forward(request, response);
        } else {
            response.sendRedirect(response.encodeRedirectURL("/from-submit.jsp" + "?name" + name));
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        this.doGet(req, resp);
    }
}
