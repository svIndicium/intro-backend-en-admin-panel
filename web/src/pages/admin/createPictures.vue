<script setup lang="ts">
  function createPictures(e) {
    fetch("/api/pictures", {
      method: "POST",
      headers: {"Authorization": "Bearer " + localStorage.getItem("accessToken")},
      body: new FormData(e.target),
    })
  }
</script>

<template>
  <form @submit.prevent="createPictures">
    <label>
      pictures:
      <input type="file" multiple name="files">
    </label>
    <button>upload pictures</button>
  </form>
</template>

<style scoped>

</style>