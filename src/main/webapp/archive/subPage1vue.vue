<template>
  <p>sub page 1 vue start</p>
  <div id="subPage1Container"> sub1.subPage1Str = {{ subPage1Str }}</div>
  <p>sub page 1 vue end</p>
</template>

<script>
export default {
  props: {
    value: {
      type: String,
      required: true
    }
  },
}
</script>
