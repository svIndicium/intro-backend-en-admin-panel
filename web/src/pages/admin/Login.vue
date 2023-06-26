<script setup lang="ts">
  import {useRouter} from "vue-router";

  const router = useRouter()

  async function login(e) {
    e.preventDefault();
    const entries = new FormData(e.target).entries();
    const data = Object.fromEntries(entries);
    const token = await fetch(
        "/api/authenticate",
        {
          method: "POST",
          headers: { "Content-Type": "application/json" },
          body: JSON.stringify(data)
        })
        .then<{ accessToken: string }>(e => e.json())

    localStorage.setItem("accessToken", token.accessToken)

    await router.push({path: '/admin/home'})
  }
</script>

<template>
  <form @submit.prevent="login">
    <input type="text" name="username">
    <input type="password" name="password">
    <button type="submit">log in</button>
  </form>
</template>

<style scoped>

</style>