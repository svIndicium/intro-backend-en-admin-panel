<script setup lang="ts">
import {fetchJsonWithAuth} from "../lib/fetcher";
import {LeaderboardEntry} from "../models";
import {ref} from "vue";

const leaderboard = ref<LeaderboardEntry[]>([])
fetchJsonWithAuth<LeaderboardEntry[]>("/api/teams/leaderboard")
    .then(e => leaderboard.value = e)
</script>

<template>
  <div>
    <table id="leaderboard-table">
      <thead>
      <tr>
        <th scope="col">Rank</th>
        <th scope="col">Team Name</th>
        <th scope="col">Crazy 88 Score</th>
        <th scope="col">Pictures</th>
      </tr>
      </thead>
      <tbody>
      <tr v-for="(team, index) in leaderboard">
        <td>#{{index+1}}</td>
        <th scope="row">
          <router-link :to="`/admin/team/${team.id}`">{{team.teamname}}</router-link>
        </th>
        <td>{{team.points.challengePoints}}</td>
        <td>{{team.points.picturesApproved}}<span>/25</span></td>
      </tr>
      <tr class="create-new">
        <td colspan="4">
          <router-link to="/admin/team">create team</router-link>
        </td>
      </tr>
      </tbody>
    </table>
  </div>
</template>

<style scoped lang="scss">

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

.create-new {
  background-color: #73C671;

  td > a {
    color: var(--bg-s);
  }

}

#leaderboard-table {
  thead > tr > th:nth-child(2) {
    text-align: left;
  }

  tbody > tr > th {
    text-align: left;
    font-weight: unset;
  }

  tbody > tr > td > span {
    font-size: .5em;
    font-weight: normal;
  }
}
</style>