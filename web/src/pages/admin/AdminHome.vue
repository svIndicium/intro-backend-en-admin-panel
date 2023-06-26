<script setup lang="ts">
  import {fetchWithAuth} from "../../lib/fetcher";

  const submissions = await fetchWithAuth<{ id: string, teamName: string, type: string }>("/api/submissions")
</script>

<template>

</template>

<style scoped>

</style>