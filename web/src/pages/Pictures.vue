<script setup lang="ts">


</script>

<template>
  <div class="grid">
    <img src="vite.svg" v-for="x in 25" alt=""/>
  </div>

</template>

<style scoped lang="scss">
.grid {
  display: grid;
  grid-template-columns: auto auto auto auto auto;
  grid-template-rows: auto auto auto auto auto;
  img {
    width: 100%;
  }
}
</style>