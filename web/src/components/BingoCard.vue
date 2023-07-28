<script setup lang="ts">
import {fetchJsonWithAuth, fetchWithAuth} from "../lib/fetcher";
import {ref} from "vue";
fetchJsonWithAuth<number[]>("/api/challenges")
const pictureIds = ref<{data: string, id: string, state: string}[]>([])
fetchJsonWithAuth<{id:string, state: string}[]>("/api/pictures")
    .then(async (e) => {
      const temp = e.map(async (obj) => {
        const data = await fetch(`/api/pictures/${obj.id}/file`)
            .then(r => r.blob())
            .then(a => URL.createObjectURL(a))
        return { id: obj.id, state: obj.state, data }
      })
      pictureIds.value = await Promise.all(temp)
    })
</script>

<template>
  <div class="bingo-grid">
    <div class="image-container" v-for="picture in pictureIds">
      <img :src="picture.data" alt=""/>
    </div>
  </div>
</template>

<style scoped lang="scss">
.bingo-grid {
  display: grid;
  grid-template-rows: 1fr 1fr 1fr 1fr 1fr;
  grid-template-columns: 1fr 1fr 1fr 1fr 1fr;
  overflow: hidden;

  .image-container {
    position: relative;
    img {
      height: 100%;
      width: 100%;
      object-fit: cover;
      left: 0;
      position: absolute;
      top: 0;
    }

    filter: grayscale(1);
  }


}
</style>