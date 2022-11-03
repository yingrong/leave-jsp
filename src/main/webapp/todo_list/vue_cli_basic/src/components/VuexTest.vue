<template>
  <div>
    <h2>Counter</h2>
    <div>
      <button data-testid="decrementer" @click="decrement">-</button>
      <span data-testid="count-value">{{ count }}</span>
      <button data-testid="incrementer" @click="increment">+</button>
    </div>
    <HelloWorld :msg="hix"></HelloWorld>
  </div>
</template>

<script>
import { mapActions, mapState } from 'vuex'
import HelloWorld from "@/components/HelloWorld";
export default {
  data() {
    return {
      hix: " helllo vuex"
    }
  },
  computed: {
    ...mapState(['count'])
  },
  components: {
    HelloWorld,
  },
  methods: {
    ...mapActions(['decrement', 'increment'])
  }
}
</script>