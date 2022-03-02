<template>
  <section class="todoapp">
    <div id="app">
      <TodoHeader v-on:add-todo="saveTodo"></TodoHeader>
      <TodoList :todos="todos" v-on:delete-todo="deleteTodo" ></TodoList>
      <TodoFooter :hasCompleted="hasCompletedJs" v-on:delete-completed-todo="deleteCompletedTodo">
      </TodoFooter>
    </div>
  </section>
</template>

<script>
import TodoHeader from './components/TodoHeader';
import TodoList from './components/TodoList';
import TodoFooter from './components/TodoFooter';
import $ from 'jquery';

export default {
  name: 'App',
  components: {
    TodoHeader,
    TodoList,
    TodoFooter
  },
  data: function () {
    return {
      todos: []
    }
  },
  computed: {
    hasCompletedJs: function () {
      return this.todos.filter(function(todo){return todo.completed}).length > 0
    }
  },
  created: function () {
    var _this = this;
    $.ajax({
      url: "/todo-list/ajax?sAction=get",
      dataType: "json",
      success: function (data) {
        _this.todos = data;
      }
    })
  },
  methods: {
      saveTodo: function (todo) {
          this.todos.push(todo);
      },
      deleteTodo: function (id) {
          var newTodos = this.todos.filter(todo => todo.id != id);
          this.todos = newTodos;
      },
      deleteCompletedTodo: function () {
          var _this = this;
          $.ajax({
              url: "/todo-list/ajax?sAction=deleteCompleted",
              method: 'post',
              success: function () {
                  var newTodos = _this.todos.filter(todo => !todo.completed);
                  _this.todos = newTodos;
              }
          })
      }
  }
}
</script>

<style>
</style>
