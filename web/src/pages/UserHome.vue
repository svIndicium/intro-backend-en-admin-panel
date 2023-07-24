<script setup lang="ts">

import BingoCard from "../components/BingoCard.vue";
</script>

<template>
  <main>
    <div class="bingo-wrapper">
      <div class="bingo-container">
        <bingo-card />
      </div>
    </div>

  </main>


</template>

<style scoped lang="scss">
.bingo-wrapper {
  display: grid;
  place-items: center;
}
.bingo-container {
  width: 80vw;
  height: 80vw;
  display: grid;
  //align-content: center;
  //justify-content: center;
  //place-items: center;
}
</style>