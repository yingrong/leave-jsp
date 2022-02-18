<%@ page import="com.tw.todo_list.Todo" %>
<%@ page import="java.util.List" %>
<%
    List<Todo> todoList = (List<Todo>) request.getAttribute("todoList");
    boolean hasCompleted = false;
    for (Todo todo : todoList) {
        if (todo.getCompleted()) {
            hasCompleted = true;
            break;
        }
    }
%>
<footer class="footer">
    <div id="todoFooterContainer"></div>
</footer>
<script>
    var hasCompletedJs = <%=hasCompleted%>;
    var todoFooterPage = (function () {

        function deleteCompletedTodo() {
            rootPage.deleteCompleted();
        }

        return {
            _deleteCompletedTodo: deleteCompletedTodo
        }
    })();

    new Vue({
        el:'#todoFooterContainer',
        data: function () {
            return {
                hasCompletedJs: hasCompletedJs
            }
        },
        template: `
            <button v-show='hasCompletedJs' class="clear-completed" onclick="todoFooterPage._deleteCompletedTodo()">Clear completed</button>
        `
    })
</script>