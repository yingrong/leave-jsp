<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Todo List, 重构后</title>
    <script type="text/javascript" src="../../js/jquery.js"></script>
    <script type="text/javascript" src="../../js/vue.js"></script>
    <link rel="stylesheet" href="/todo_list/common/index.css"/>
    <script type="text/javascript" src="/todo_list/vue/todoForm.js"></script>
    <script type="text/javascript" src="/todo_list/vue/todoHeader.js"></script>
    <script type="text/javascript" src="/todo_list/vue/todoList.js"></script>
    <script type="text/javascript" src="/todo_list/vue/todoFooter.js"></script>
</head>

<body>
<section class="todoapp">
    <div id="app">
        <todo-form-component :s-action="sAction" :id="id" :title="title" v-on:submit="submitForm"></todo-form-component>
        <todo-header-component v-on:add-todo="saveTodo"></todo-header-component>
        <todo-list-component :todos="todos" v-on:toggle-todo="toggleCompleted" v-on:delete-todo="deleteTodo" >
        </todo-list-component>
        <todo-footer-component :hasCompleted="hasCompletedJs" v-on:delete-completed-todo="deleteCompletedTodo">
        </todo-footer-component>
    </div>
</section>
<script>
    new Vue({
        el: '#app',
        data: function () {
            return {
                todos: [],
                sAction: '',
                id: '',
                title: ''
            }
        },
        computed: {
            hasCompletedJs: function () {
                return this.todos.filter(function(todo){return todo.completed}).length > 0
            }
        },
        components: {
            'todo-form-component': todoFormComponent,
            'todo-header-component': todoHeaderComponent,
            'todo-list-component': todoListComponent,
            'todo-footer-component': todoFooterComponent
        },
        created: function () {
            var _this = this;
            $.ajax({
                url: "/todo-list/vue?sAction=get",
                dataType: "json",
                success: function (data) {
                    _this.todos = data;
                }
            })
        },
        methods: {
            saveTodo: function (title) {
                this.sAction = "add";
                this.title = title;
            },
            toggleCompleted: function (id, sAction) {
                this.sAction = sAction;
                this.id=id;
            },
            deleteTodo: function (id) {
                this.sAction="delete"
                this.id=id;
            },
            deleteCompletedTodo: function () {
                this.sAction="deleteCompleted"
            },
            submitForm: function () {
                this.$nextTick(function () {
                    todoForm.submit();
                });
            }
        }
    })
</script>
</body>
</html>
