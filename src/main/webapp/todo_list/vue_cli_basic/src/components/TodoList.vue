<template>
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
</template>

<script>
import $ from 'jquery';
export default {
  name: "TodoList",
  props:{
    todos: {
      type: Array
    }
  },
  methods: {
    toggleComplted: function (todo) {
      console.log("toggleComplted ")
      console.log(todo)
      var sAction = "markDone";
      if (!todo.completed) {
        sAction = "markUnfinished";
      }
      $.ajax({
        url: "/todo-list/ajax?sAction=" + sAction,
        method: 'post',
        data: {
          id: todo.id
        },
        error: function () {
          todo.completed = !todo.completed;
        }
      })
    },
    deleteTodo: function (todo) {
      var _this = this;
      $.ajax({
        url: "/todo-list/ajax?sAction=delete",
        method: 'post',
        data: {
          id: todo.id
        },
        success: function () {
          _this.$emit('delete-todo', todo.id);
        }
      })
    }
  }
}
</script>
<style scoped>

</style>
