<script setup lang="ts">
import {fetchJsonWithAuth, fetchWithAuth} from "../../lib/fetcher";
  import {onBeforeUnmount, ref} from "vue";

  const pictureSubmissions = ref<{ id: string, teamName: string, teamId: string }[]>([])
  const challengeSubmissions = ref<{ id: string, teamName: string, teamId: string }[]>([])
  const challenges = ref<{ id: string, title: string, challenge: string, points: number}[]>([])
  const pictureIds = ref<{data: string, id: string }[]>([])
  fetchJsonWithAuth<{ id: string, title: string, challenge: string, points: number}[]>("/api/challenges")
      .then(e => challenges.value = e)

  const leaderboard = ref<{id: string, teamname: string, points: { challengePoints: number, picturesApproved: number }}[]>([])
  fetchJsonWithAuth<{id: string, teamname: string, points: { challengePoints: number, picturesApproved: number }}[]>("/api/teams/leaderboard")
      .then(e => leaderboard.value = e)

  fetchJsonWithAuth<{ id: string }[]>("/api/pictures")
      .then(async (e) => {
        const temp = e.map(async (obj) => {
          const data = await fetchWithAuth(`/api/pictures/${obj.id}/file`)
              .then(r => r.blob())
              .then(a => URL.createObjectURL(a))
          return { id: obj.id, data }
        })
        pictureIds.value = await Promise.all(temp)
      })


  const fetcherFunction = () => {
    fetchJsonWithAuth<{
      id: string,
      teamName: string,
      teamId: string,
    }[]>("/api/pictures/pending")
        .then(e => pictureSubmissions.value = e)
    fetchJsonWithAuth<{
      id: string,
      teamName: string,
      teamId: string,
    }[]>("/api/challenges/pending")
        .then(e => challengeSubmissions.value = e)

  }
  fetcherFunction()
  const refresher = setInterval(fetcherFunction, 1000 * 60)

  onBeforeUnmount(() => clearInterval(refresher))

</script>

<template>
  <div class="admin-grid">
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
            <th scope="row"><router-link :to="`/admin/team/${team.id}`">{{team.teamname}}</router-link></th>
            <td>{{team.points.challengePoints}}</td>
            <td>{{team.points.picturesApproved}}<span>/{{pictureIds.length}}</span></td>
          </tr>
        <tr class="create-new"><td colspan="4"><router-link to="/admin/team">create team</router-link></td></tr>
        </tbody>
      </table>
    </div>
    <div class="picture-grid">
      <router-link to="/admin/pictures" class="image-container" v-for="x in 25">
        <img :src="`/nature-${(x % 4) + 1}.jpg`" alt=""/>
      </router-link>
    </div>
    <div>
      <table id="challenge-table">
        <thead>
        <tr>
          <th scope="col">Nr.</th>
          <th scope="col">Challenge</th>
          <th scope="col">Score</th>
        </tr>
        </thead>
        <tbody>
        <template v-for="(challenge, index) in challenges">
          <tr>
            <td>#{{index+1}}</td>
            <th scope="row"><router-link :to="`/admin/challenges/${challenge.id}`">{{challenge.title}}</router-link></th>
            <td>{{challenge.points}}</td>
          </tr>
          <tr>
            <td colspan="3">{{challenge.challenge}}</td>
          </tr>
        </template>
        <tr class="create-new"><td colspan="3"><router-link to="/admin/challenges">create challenges</router-link></td></tr>
        </tbody>
      </table>
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
        <tr v-for="submission in pictureSubmissions">
          <th scope="row"><router-link :to="`/admin/submissions/picture/${submission.id}/team/${submission.teamId}`">Picture</router-link></th>
          <td>{{submission.teamName}}</td>
        </tr>
        <tr v-for="submission in challengeSubmissions">
          <th scope="row"><router-link :to="`/admin/submissions/challenge/${submission.id}/team/${submission.teamId}`">Challenge</router-link></th>
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
  //padding: 1rem;

  > * {
    max-height: 50vh;
    //border: white solid 1px;
    margin: 1rem;
    background-color: var(--bg-s);
    //border: white solid 1px;
    //margin: 1rem;
    //border-radius: 1rem;
    overflow-x: scroll;
    //overflow-y: hidden;

    //overflow-x: scroll;
    box-shadow: 0px 0px 50px rgba(0, 0, 0, 0.25), inset 0px 2px 2px rgba(255, 255, 255, 0.05), inset 0px -2px 1px rgba(0, 0, 0, 0.15);
    border-radius: 8px;

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
  border-collapse: collapse;
  border: var(--brdr) solid 1px;
  font-size: 1em;

  &> * > tr > td, &> * > tr > th {
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

#challenge-table {
  thead > tr > th:nth-child(2) {
    text-align: left;
  }
  tbody {
    tr:nth-of-type(2n) {
      background-color: var(--bg-p);
      td {
        text-align: left;
        padding-left: 16px;
        padding-right: 16px;
        font-weight: unset;
      }



    }

    tr > th {
      text-align: left;
      font-weight: unset;
    }

    tr:nth-of-type(2n+1) {
      border-bottom: 1px solid rgba(0, 0, 0, 0.25);
      box-shadow: 0px 0px 8px rgba(0, 0, 0, 0.25);

    }
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