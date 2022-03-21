<template>
  <footer class="footer">
    <button v-show='hasCompleted' class="clear-completed" @click="deleteCompletedTodo">Clear completed</button>
  </footer>
</template>
<script>
export default {
  name: "TodoFooter",
  props: {
    hasCompleted: {
      type: Boolean
    }
  },
  methods: {
    deleteCompletedTodo: function () {
      this.$emit('delete-completed-todo');
    }
  }
}
</script>
<style scoped>

</style>
