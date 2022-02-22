package com.tw.todo_list;

import com.fasterxml.jackson.databind.ObjectMapper;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@WebServlet(value = "/todo-list/vue")
public class TodoVueServlet extends HttpServlet {

    TodoRepository todoRepository = new TodoRepository();

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        System.out.println("get new ------");

        String sAction = request.getParameter("sAction");
        if ("get".equals(sAction)) {
            ObjectMapper objectMapper = new ObjectMapper();
            PrintWriter out = response.getWriter();
            response.setContentType("application/json;charset=UTF-8");
            response.setCharacterEncoding("UTF-8");
            response.setStatus(HttpServletResponse.SC_OK);

            List<Todo> todoList = todoRepository.getTodoList();
            out.write(objectMapper.writeValueAsString(todoList));
            out.flush();
        } else {
            request.getRequestDispatcher("/todo_list/vue/index.html")
                    .forward(request, response);
        }
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("sAction");
        System.out.println("post new --------" + action);
        if ("add".equals(action)) {
            String title = request.getParameter("title");
            todoRepository.addTodo(new Todo(title));
        } else if ("delete".equals(action)) {
            String id = request.getParameter("id");
            todoRepository.deleteTodo(id);
        } else if ("markDone".equals(action)) {
            String id = request.getParameter("id");
            todoRepository.markDone(id);
        } else if ("markUnfinished".equals(action)) {
            String id = request.getParameter("id");
            todoRepository.markUnfinished(id);
        } else if ("deleteCompleted".equals(action)) {
            todoRepository.deleteCompleted();
        }
        doGet(request, response);
    }
}