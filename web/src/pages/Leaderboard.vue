<script setup lang="ts">
import {fetchJsonWithAuth} from "../lib/fetcher";

  const teams = await fetchJsonWithAuth<{ teamname: string, challengePoints: number, approvedPictures: number}>("/api/teams/leaderboard")
</script>

<template>

</template>

<style scoped>

</style>