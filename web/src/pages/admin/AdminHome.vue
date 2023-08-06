<script setup lang="ts">
import PendingSubmissions from "../../components/PendingSubmissions.vue";
import Pictures from "../../components/Pictures.vue";
import Leaderboard from "../../components/Leaderboard.vue";
import Challenges from "../../components/Challenges.vue";


</script>

<template>
  <main class="admin-grid" aria-label="admin dashboard">
    <section aria-label="pending submissions">
      <h1>Pending</h1>
      <PendingSubmissions />
    </section>
    <section aria-label="leaderboard">
      <h1>Leaderboard</h1>
      <Leaderboard />
    </section>
    <section aria-label="picture locations">
      <h1>Pictures</h1>
      <Pictures />
    </section>
    <section aria-label="challenges">
      <h1>Challenges</h1>
      <Challenges />
    </section>
  </main>
</template>

<style scoped lang="scss">
.admin-grid {
  min-width: 100vw;
  min-height: 100vh;
  display: inline-flex;
  flex-direction: row;
  align-items: center;
  gap: 4rem;
  padding: 0 2rem;

  > section {
    height: 90vh;
    width: 95vw;

    &:deep(> div) {
      height: calc(90vh - 36px - 19.92px - 19.92px);
      background-color: var(--bg-s);

      overflow-x: auto;

      box-shadow: 0px 0px 50px rgba(0, 0, 0, 0.25), inset 0px 2px 2px rgba(255, 255, 255, 0.05), inset 0px -2px 1px rgba(0, 0, 0, 0.15);
      border-radius: 8px;
    }
  }
}
</style>