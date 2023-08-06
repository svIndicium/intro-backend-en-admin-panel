<script setup lang="ts">
import {ChallengeSubmissionEntry, SubmissionEntry} from "../models";
import {fetchJsonWithAuth} from "../lib/fetcher";
import {onBeforeUnmount, ref} from "vue";

const pictureSubmissions = ref<SubmissionEntry[]>([])
const challengeSubmissions = ref<ChallengeSubmissionEntry[]>([])
const inactive = ref<boolean>(false)

const fetcherFunction = async () => {
  const [newP, newC] = await Promise.all([
    fetchJsonWithAuth<SubmissionEntry[]>("/api/pictures/pending"),
    fetchJsonWithAuth<ChallengeSubmissionEntry[]>("/api/challenges/pending")
  ])

  pictureSubmissions.value = newP
  challengeSubmissions.value = newC
  const total = newP.length + newC.length

  document.title = total > 0 ? `Home (${newP.length + newC.length}) - Speurtocht 88` : `Home - Speurtocht 88`

  if (inactive.value && total > 0) {
    new Audio('/notification.mp3').play()
  }
}

fetcherFunction()
const refresher = setInterval(fetcherFunction, 1000 * 60)

let time = setTimeout(() => inactive.value = true, 1000 * 60 * 5)
const clearInactivity = () => {
  inactive.value = false;
  clearTimeout(time);
  time = setTimeout(() => inactive.value = true, 1000 * 60 * 5)
}

window.onmousemove = clearInactivity;

onBeforeUnmount(() => {
  clearInterval(refresher)
  clearTimeout(time)
})
</script>

<template>
  <div>
    <table v-if="pictureSubmissions.length > 0">
      <thead>
      <tr>
        <th scope="col">type</th>
        <th scope="col">title</th>
        <th scope="col">team</th>
      </tr>
      </thead>
      <tbody>
      <tr v-for="submission in pictureSubmissions">
        <th scope="row">
          <router-link :to="`/admin/submissions/picture/${submission.id}/team/${submission.teamId}`">Picture</router-link>
        </th>
        <td aria-hidden="true"/>
        <td>{{submission.teamName}}</td>
      </tr>
      <tr v-for="submission in challengeSubmissions">
        <th scope="row">
          <router-link :to="`/admin/submissions/challenge/${submission.id}/team/${submission.teamId}`">Challenge
          </router-link>
        </th>
        <td>{{submission.title}}</td>
        <td>{{submission.teamName}}</td>
      </tr>
      </tbody>
    </table>
    <div v-else class="no-submissions">
      <h1>There are no pending submissions</h1>
      <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 220 200" id="logo-icon" data-v-ca40b2dc="">
<!--        <g fill="none">-->

        <g transform="translate(32 8)">
          <g>
            <path class="ring-green rotate" fill="#A3CF9B" d="M115.441 88.078c-.04-21.056-12.753-39.998-32.174-47.936-19.42-7.938-41.694-3.297-56.366 11.746a4.6 4.6 0 0 0-.027 6.487 4.555 4.555 0 0 0 6.46.027c12.055-12.442 30.417-16.304 46.43-9.765 16.014 6.538 26.482 22.173 26.47 39.532 0 2.538 2.048 4.596 4.576 4.596 2.528 0 4.577-2.058 4.577-4.596a.217.217 0 0 0 .054-.09Z"></path>
            <path class="ring-blue-green rotate" fill="#73C3B6" d="M134.286 87.327c.056-28.424-17.141-54.032-43.455-64.706-26.313-10.674-56.465-4.272-76.186 16.175a4.529 4.529 0 0 0 2.382 7.466 4.516 4.516 0 0 0 4.305-1.377c17.17-17.64 43.305-23.107 66.093-13.825 22.789 9.283 37.688 31.464 37.68 56.095v.905a4.594 4.594 0 0 0 4.378 4.8 4.593 4.593 0 0 0 4.794-4.791v-.09c0-.218.01-.408.01-.652Z"></path>
            <path class="ring-blue rotate" fill="#72C9E1" d="M64.038 0C40.558-.019 18.06 9.448 1.61 26.266a4.68 4.68 0 0 0-.369 6.591 4.637 4.637 0 0 0 6.935 0C30.47 10.086 64.27 3.109 93.706 15.203c29.436 12.094 48.665 40.856 48.661 72.786a4.682 4.682 0 0 0 2.266 4.221 4.643 4.643 0 0 0 4.777 0 4.682 4.682 0 0 0 2.266-4.221C151.676 39.397 112.441.005 64.038 0Z"></path>

          </g>
           <g fill="#9D9D9C" transform="translate(32 58)">
            <path d="M50.826 5.408a4.34 4.34 0 0 0-4.207-.144c-3.945 1.56-1.805 6.229-1.805 6.229 7.836 4.561 12.404 13.188 11.23 23.14-1.33 11.027-10.135 19.662-21.2 20.794C23.776 56.56 13.401 49.885 9.86 39.356c-3.543-10.53.693-22.104 10.2-27.872h-.045s1.715-4.002-1.183-6.003a4.646 4.646 0 0 0-5.308.261C2.177 13.954-2.55 28.55 1.832 41.842c4.383 13.29 16.87 22.227 30.884 22.102s26.339-9.284 30.483-22.65c4.144-13.368-.844-27.877-12.337-35.886h-.036Z"></path>
            <path d="M37.862 22.616c0-2.65-2.41-4.795-5.416-4.795-3.006 0-5.417 2.145-5.417 4.795v22.662c0 2.65 2.41 4.795 5.417 4.795 3.006 0 5.416-2.145 5.416-4.795V22.616Z"></path>
            <ellipse cx="32.392" cy="6.166" rx="5.39" ry="5.381"></ellipse>
          </g>
        </g>
      </svg>

    </div>
  </div>

</template>

<style scoped lang="scss">

.no-submissions {
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  height: 90%;

  > svg .rotate {
    transform-box: fill-box;
    transform-origin: bottom;
    animation-duration: 10s;
    animation-name: rotate-element;
    animation-iteration-count: infinite;
    animation-timing-function: ease-in-out;
  }

  > img {
    height: 100%
  }
}

@keyframes rotate-element {
  0%, 30% {
    transform: rotate(0deg);
  }

  100%, 70% {
    transform: rotate(360deg);
  }
}

table {
  width: 100%;
  border-collapse: collapse;
  border: var(--brdr) solid 1px;
  font-size: 1em;

  & > * > tr > td, & > * > tr > th {
    border-bottom: var(--brdr) solid 1px;
    padding: 8px 0px;
    text-align: center;
    text-decoration: none;
    font-weight: bold;

    > a {
      text-decoration: none;
    }
  }
}

thead {
  position: sticky;
  top: 0;
  background-color: var(--bg-s);
}

th > a {
  color: var(--txt-p);

  &:hover {
    text-decoration: underline;
  }
}

</style>