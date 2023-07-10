<script setup lang="ts">
import {fetchJsonWithAuth} from "../../lib/fetcher";
  import {onBeforeUnmount, ref} from "vue";

  const submissions = ref<{ id: string, teamName: string, type: string }[]>([])
  const challenges = ref<string[]>([])
  fetchJsonWithAuth<string[]>("/api/challenges")
      .then(e => challenges.value = e)

  const leaderboard = ref<{teamname: string, challengePoints: number, picturesApproved: number}[]>([])
  fetchJsonWithAuth<{teamname: string, challengePoints: number, picturesApproved: number}[]>("/api/teams/leaderboard")
      .then(e => leaderboard.value = e)

  const fetcherFunction = () => fetchJsonWithAuth<{ id: string, teamName: string, type: string }[]>("/api/submissions").then(response => submissions.value = response);
  fetcherFunction()
  const refresher = setInterval(fetcherFunction, 1000 * 60)

  onBeforeUnmount(() => clearInterval(refresher))

</script>

<template>
  <div class="admin-grid">
    <div>
      <table>
        <thead>
        <tr>
          <th scope="col">team</th>
          <th scope="col">crazy 88</th>
          <th scope="col">speurtocht</th>
        </tr>
        </thead>
        <tbody>
          <tr v-for="team in leaderboard">
            <th scope="row"><router-link :to="`/admin/team/${team.teamname}`">{{team.teamname}}</router-link></th>
            <td>{{team.challengePoints}}</td>
            <td>{{team.picturesApproved}}</td>
          </tr>
        <tr><td colspan="3"><router-link to="/admin/team">create team</router-link></td></tr>
        </tbody>
      </table>
    </div>
    <div class="picture-grid">
      <div class="image-container" v-for="x in 25">
        <img :src="`/nature-${(x % 4) + 1}.jpg`" alt=""/>
      </div>

    </div>
    <div>
      <router-link to="/admin/challenges">create challenges</router-link>
      <ol>
        <li v-for="(challenge, index) in 88">
          Lorem ipsum dolor sit amet, consectetur adipiscing elit. Quisque eget lacus sapien. Aliquam a aliquet.</li>
      </ol>

    </div>
    <div>
      <table>
        <thead>
        <tr>
          <th scope="col">type</th>
          <th scope="col">team</th>
        </tr>
        </thead>
        <tbody>
        <tr v-for="submission in submissions">
          <th scope="row"><router-link :to="`/admin/submission/${submission.id}`">{{submission.type}}</router-link></th>
          <td>{{submission.teamName}}</td>
        </tr>
        </tbody>
      </table>
    </div>
  </div>
</template>

<style scoped lang="scss">
.admin-grid {
  width: 100vw;
  height: 100vh;
  display: grid;
  grid-template-rows: 1fr 1fr;
  grid-template-columns: 1fr 1fr;
  grid-gap: 1rem;

  > div {
    border: white solid 1px;
    margin: 1rem;
    border-radius: 1rem;
    //overflow-x: scroll;
    //overflow-y: hidden;
    overflow-x: scroll;

  }

  > .picture-grid {
    display: grid;
    grid-template-rows: 1fr 1fr 1fr 1fr 1fr;
    grid-template-columns: 1fr 1fr 1fr 1fr 1fr;
    overflow: hidden;

    .image-container {
      position: relative;
      img {
        height: 100%;
        width: 100%;
        object-fit: cover;
        left: 0;
        position: absolute;
        top: 0;
      }
    }


  }
}
table {
  width: 100%;
}
thead {
  position: sticky;
  top: 0;
  background-color: var(--bg-color);
}
</style>