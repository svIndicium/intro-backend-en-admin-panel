<script setup lang="ts">
  import {fetchWithAuth} from "../lib/fetcher";

  const teams = await fetchWithAuth<{ teamname: string, challengePoints: number, approvedPictures: number}>("/api/teams/leaderboard")
</script>

<template>

</template>

<style scoped>

</style>