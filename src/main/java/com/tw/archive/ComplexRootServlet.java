package com.tw.archive;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(value = "/archive/complex-root")
public class ComplexRootServlet extends HttpServlet {

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        request.setAttribute("complexObjectVO", ComplexObjectVO.getInstance());

        request.getRequestDispatcher("/archive/complex/complex-1.jsp")
                .forward(request, response);
    }

}