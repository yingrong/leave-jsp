<%@ page import="com.tw.todo_list.Todo" %>
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
                <input class="toggle" id="todo_toggle_<%=todo.getId()%>" onchange="todoListPage._toggle(this)"
                       type="checkbox" <%if (todo.getCompleted()) {%> checked <%}%> />
                <label><%=todo.getTitle()%>
                </label>
                <button class="destroy" onclick="todoListPage._deleteTodo(this)"></button>
            </div>
            <%--            <input class="edit" value="<%=todo.getTitle()%>">--%>
        </li>
        <%}%>
    </ul>
</section>
<script>
    var todoListPage = (function () {
        function deleteTodo(el) {
            var id = el.parentElement.id.substr(5);
            rootPage.deleteTodo(id);
        }

        function toggle(el) {
            console.log(el)
            var completed = el.checked;
            var sAction = "markDone";
            var id = el.id.substr(12);
            if (!completed) {
                sAction = "markUnfinished";
            }

            rootPage.toggleTodo(id, sAction);
        }

        return {
            _deleteTodo: deleteTodo,
            _toggle: toggle
        }
    })();
</script>
