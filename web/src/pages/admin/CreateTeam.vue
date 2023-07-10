<script setup lang="ts">
  import {sendForm} from "../../lib/fetcher";

  function createTeam(e) {
    // e.preventDefault()
    sendForm("/api/teams", e.target, "POST")
  }

</script>

<template>
  <form @submit.prevent="createTeam" >
    <label>teamname:<input type="text" name="teamname"></label>
    <br>
    <label>password:<input type="password" name="password"></label>
    <br>
    <button type="submit">create team</button>
  </form>
</template>

<style scoped>

</style>