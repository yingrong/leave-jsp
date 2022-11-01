<template>
  <div>
    <p>{{ msg }}</p>
    <p>{{ info }}</p>
    <p>Times clicked: {{ count }}</p>
    <button @click="increment">increment</button>
  </div>
</template>

<script>
import axios from "axios";


export default {
  name: "HelloWorld",
  props: ['msg'],
  data() {
    return {
      info: 10,
      count: 0,
    }
  },
  methods: {
    increment() {
      this.count++
    },
  },
  async mounted() {
    try {
      let resp = await axios
          .get('http://localhost:8080/todo-list/ajax?sAction=get');
      this.info = resp;
    } catch (e) {
      this.info = "1"
    }


  }
}
</script>

<style scoped>

</style>