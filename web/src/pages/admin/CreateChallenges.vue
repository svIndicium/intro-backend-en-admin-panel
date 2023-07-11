<script setup lang="ts">
import {sendJson} from "../../lib/fetcher";

function createChallenges(e) {
  const entries = new FormData(e.target).entries();
  const data = Object.fromEntries(entries);
  const content = data.challenges as string

  const challenges = content.split("\n").map((e) => {
    const split = e.split(":")
    const challenge = split[0]
    const points = Number(split[1])
    return { challenge, points }
  })

  sendJson("/api/challenges", challenges, "POST")
}
</script>

<template>
<!--  <input type="text">-->
  <form @submit.prevent="createChallenges">
    <label for="challenges-text">challenges:</label>
    <br>
    <textarea id="challenges-text" name="challenges" placeholder="challenge:points"></textarea>
    <br>
    <button>create challenges</button>
  </form>

</template>

<style scoped>
#challenges-text {
  width: 80vw;
  height: 80vh;
}
</style>