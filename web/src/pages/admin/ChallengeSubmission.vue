<script setup lang="ts">
import {fetchJsonWithAuth, fetchWithAuth} from "../../lib/fetcher";
import {useRouter} from "vue-router";
import {ref} from "vue";
import SubmissionContent from "../../components/SubmissionContent.vue";
import {ChallengeSubmission} from "../../models";

const router = useRouter();

const submission = ref<ChallengeSubmission>()
const deniedReason = ref<string>("")

fetchJsonWithAuth<ChallengeSubmission>(`/api/challenges/${router.currentRoute.value.params.id}/teams/${router.currentRoute.value.params.teamId}`)
    .then(e => submission.value = e)

async function deny() {
  await fetch(`/api/challenges/${router.currentRoute.value.params.id}/teams/${router.currentRoute.value.params.teamId}/deny`, {
    method: "PATCH",
    headers: { "Authorization": "Bearer " + localStorage.getItem("accessToken"), "Content-Type": "application/json" },
    body: JSON.stringify({ deniedReason: deniedReason.value })
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
  <main v-if="submission" class="challenge-grid">
    <section style="padding: 1rem">
      <h1>{{ submission.submittedBy }}: {{ submission.title }}</h1>
      <p>{{ submission.challenge }}</p>
      <div class="action-group">
        <button type="button" @click="approve" class="affirmative-action">approve</button>
        <label for="reason">Reason for denial:</label>
        <br>
        <textarea style="width: 100%; resize: none;  height: 5rem" v-model="deniedReason" id="reason"/>
        <button type="button" @click="deny" :disabled="deniedReason === ''">deny</button>
      </div>
    </section>

    <section class="submission-grid">
      <SubmissionContent :url="`/api/challenges/submissions/${id}/file`" v-for="id in submission.files" />
    </section>
  </main>
  <main v-else>
    <h1>Loading...</h1>
  </main>
</template>

<style scoped lang="scss">
.challenge-grid {
  display: grid;
  //grid-template-rows: auto;
  grid-template-columns: 30% 70%;
  //gap: 1em;
  //padding: 1em;
}

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

.action-group {
  > * {
    margin-bottom: 1rem;
  }
  button {
    width: 100%;
  }
}

.affirmative-action {
  background-color: #73C671;
}

button {
  border-radius: 1em;
  font-weight: bolder;
  padding: .5rem;
  font-size: 1.5rem;
  border: none;
}
</style>