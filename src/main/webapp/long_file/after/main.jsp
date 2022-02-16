<%@ page import="com.tw.long_file.Todo" %>
<%@ page import="java.util.List" %>

<% List<Todo> todoList = (List<Todo>) request.getAttribute("todoList"); %>
<section class="main">
    <input id="toggle-all" class="toggle-all" type="checkbox">
    <label for="toggle-all">Mark all as complete</label>
    <ul class="todo-list">
        <% for (int i = 0; i < todoList.size(); i++) {
            Todo todo = todoList.get(i);
        %>
        <li <%if (todo.getCompleted()) {%> class="completed"<%}%> data-id="<%=todo.getId()%>">
            <div class="view" id="todo_<%=todo.getId()%>">
                <input class="toggle" id="todo_toggle_<%=todo.getId()%>" onchange="toogle(this)"
                       type="checkbox" <%if (todo.getCompleted()) {%> checked <%}%> />
                <label><%=todo.getTitle()%>
                </label>
                <button class="destroy" onclick="deleteTodo(this)"></button>
            </div>
            <%--            <input class="edit" value="<%=todo.getTitle()%>">--%>
        </li>
        <%}%>
    </ul>
</section>
