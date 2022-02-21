<%@ page import="com.tw.todo_list.Todo" %>
<%@ page import="java.util.List" %>
<%@ page import="com.fasterxml.jackson.databind.ObjectMapper" %>

<%
    List<Todo> todoList = (List<Todo>) request.getAttribute("todoList");
    ObjectMapper objectMapper = new ObjectMapper();
    String todoListString = objectMapper.writeValueAsString(todoList);
%>
<div id="todoListContainer"></div>
<script>
    (function () {
        var todos = JSON.parse('<%=todoListString%>');

        new Vue({
            el: "#todoListContainer",
            data: function () {
                return {
                    todos
                }
            },
            template:`
<section class="main" v-show="todos.length">
    <ul class="todo-list">
        <li v-for="todo in todos" :key="todo.id" :class="{completed: todo.completed}">
            <div class="view">
                <input class="toggle" v-model="todo.completed" type="checkbox" @change="toggleComplted(todo)"/>
                <label>{{todo.title}}</label>
                <button class="destroy" @click="deleteTodo(todo)"></button>
            </div>
        </li>
    </ul>
</section>
            `,
            methods: {
                toggleComplted: function (todo) {
                    var sAction = "markDone";
                    if (!todo.completed) {
                        sAction = "markUnfinished";
                    }
                    rootPage.toggleTodo(todo.id, sAction);
                },
                deleteTodo: function (todo) {
                    rootPage.deleteTodo(todo.id);
                }
            }
        });
    })();
</script>
