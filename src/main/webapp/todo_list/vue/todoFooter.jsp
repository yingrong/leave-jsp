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
    <div id="todoFooterContainer">
        <todo-footer-component :hasCompleted="hasCompletedJs" />
    </div>
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

    var todoFooterComponent = {
        props: ['hascompleted'],
        template: `
            <button v-show='hascompleted' class="clear-completed" onclick="todoFooterPage._deleteCompletedTodo()">Clear completed</button>
        `
    }

    new Vue({
        el:'#todoFooterContainer',
        data: function () {
            return {
                hasCompletedJs
            }
        },
        components: {
            'todo-footer-component': todoFooterComponent
        }
    })
</script>