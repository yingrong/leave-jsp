<template>
  <section class="todoapp">
    <div id="app">
      <todo-header-component v-on:add-todo="saveTodo"></todo-header-component>
      <TodoHeader></TodoHeader>
      <!--    <img alt="Vue logo" src="./assets/logo.png">-->
    </div>
  </section>
</template>

<script>
import TodoHeader from './components/TodoHeader';

export default {
  name: 'App',
  components: {
    TodoHeader
  }
}
</script>

<style>
#app {
  font-family: Avenir, Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  text-align: center;
  color: #2c3e50;
  margin-top: 60px;
}
</style>
