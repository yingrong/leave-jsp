<footer class="footer">
    <%
        boolean hasCompleted = false;
        for (Todo todo : todoList) {
            if (todo.getCompleted()) {
                hasCompleted = true;
                break;
            }
        }
    %>
    <%if(hasCompleted) {%>
    <button class="clear-completed" onclick="deleteCompletedTodo()">Clear completed</button>
    <%}%>
</footer>
<script>
    function deleteCompletedTodo() {
        todoForm.sAction.value = "deleteCompleted"
        todoForm.submit();
    }
</script>