<script setup lang="ts">
import {fetchJsonWithAuth, fetchWithAuth} from "../../lib/fetcher";
import {useRouter} from "vue-router";
import {ref} from "vue";
import SubmissionContent from "../../components/SubmissionContent.vue";
import {ChallengeSubmission} from "../../models";

const router = useRouter();

const original = ref<HTMLDivElement>()

const submission = ref<ChallengeSubmission>()

fetchJsonWithAuth<ChallengeSubmission>(`/api/challenges/${router.currentRoute.value.params.id}/teams/${router.currentRoute.value.params.teamId}`)
    .then(e => submission.value = e)

async function deny() {
  await fetch(`/api/challenges/${router.currentRoute.value.params.id}/teams/${router.currentRoute.value.params.teamId}/deny`, {
    method: "PATCH",
    headers: { "Authorization": "Bearer " + localStorage.getItem("accessToken") },
  })
  await router.push({path: '/admin/home'})
}

async function approve() {
  await fetch(`/api/challenges/${router.currentRoute.value.params.id}/teams/${router.currentRoute.value.params.teamId}/approve`, {
    method: "PATCH",
    headers: { "Authorization": "Bearer " + localStorage.getItem("accessToken") },
  })
  await router.push({path: '/admin/home'})
}
</script>

<template>
  <main>
    <h1>{{ submission.submittedBy }}: {{ submission.title }}</h1>
    <p>{{ submission.challenge }}</p>
    <div>
      <button type="button" @click="deny">deny</button>
      <button type="button" @click="approve">approve</button>
    </div>
    <div class="submission-grid">
      <SubmissionContent :url="`/api/challenges/submissions/${id}/file`" v-for="id in submission.files" />
    </div>
  </main>
</template>

<style scoped lang="scss">
.submission-grid {
  display: grid;
  grid-template-rows: auto;
  grid-template-columns: 1fr 1fr;
  gap: 1em;
  padding: 1em;

  &> div {
    width: 100%;


    & > :global(.submission) {
      //position: relative;
      width:100%;
      border-radius: 1rem;
      overflow: hidden;
    }
  }
}
</style>