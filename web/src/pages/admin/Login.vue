<script setup lang="ts">
  import {useRouter} from "vue-router";
  import DevOnly from "../../components/DevOnly.vue";

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

  async function loginAsAdmin() {
    const token = await fetch(
        "/api/authenticate",
        {
          method: "POST",
          headers: { "Content-Type": "application/json" },
          body: JSON.stringify({ username: "admin", password: "admin" })
        })
        .then<{ accessToken: string }>(e => e.json())

    localStorage.setItem("accessToken", token.accessToken)

    await router.push({path: '/admin/home'})
  }

  async function loginAsGroup() {
    const token = await fetch(
        "/api/authenticate",
        {
          method: "POST",
          headers: { "Content-Type": "application/json" },
          body: JSON.stringify({ username: "team-1", password: "password" })
        })
        .then<{ accessToken: string }>(e => e.json())

    localStorage.setItem("accessToken", token.accessToken)

    await router.push({path: '/user/home'})
  }
</script>

<template>
  <form @submit.prevent="login">
    <input type="text" name="username">
    <input type="password" name="password">
    <button type="submit">log in</button>
  </form>
  <DevOnly>
      <button @click="loginAsAdmin">log in as admin</button>
    <button @click="loginAsGroup">log in as group</button>
  </DevOnly>
</template>

<style scoped>

</style>