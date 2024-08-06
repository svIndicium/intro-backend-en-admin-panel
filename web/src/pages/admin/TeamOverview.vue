<script setup lang="ts">
import {fetchJsonWithAuth, fetchWithAuth} from "../../lib/fetcher";
  import {useRouter} from "vue-router";
  import {TeamMeta} from "../../models";
  import {ref} from "vue";

  const router = useRouter();

  const team = ref<TeamMeta>()
  fetchJsonWithAuth<TeamMeta>(`/api/teams/${router.currentRoute.value.params.id}`)
    .then(async e => {
      for (let picture of e.pictures) {
        if (picture.state === 'APPROVED') {
          await fetchWithAuth(`/api/pictures/${picture.id}/teams/${router.currentRoute.value.params.id}/file`)
              .then(r => r.blob())
              .then(a => {
                // image
                const image = document.createElement<HTMLImageElement>("img")
                image.classList.add('submission')
                picture.content = URL.createObjectURL(a)
              })
        }
      }
      e.challenges = e.challenges.filter(challenge => challenge.state === 'APPROVED')
      team.value = e
    })
</script>

<template>
  <main>
    <header class="top-section">
      <router-link to="/admin/home">&lt;</router-link>
      <h1>{{team.meta.teamName}}</h1>
      <h2 style="margin-left: auto">join code: <u>{{team.joinCode}}</u></h2>
    </header>
    <div class="submissions-grid">
      <section aria-label="picture locations" >
        <h1>Bingo {{team.meta.points.picturesApproved}}/25</h1>
        <div class="pictures">
          <ul class="picture-grid" aria-label="list of pictures">
            <li v-for="picture in team.pictures">
              <template v-if="picture.state === 'APPROVED'">
                <!--            <router-link :to="`/admin/picture/${picture.id}`" class="image-container" >-->
                <img :src="picture.content"/>
                <!--            </router-link>-->
              </template>
              <template v-else>
                <div class="image-container" />
              </template>

            </li>
          </ul>
        </div>

      </section>
      <section aria-label="challenges">
        <h1>Crazy 88: {{team.meta.points.challengePoints}}</h1>
        <table id="challenge-table">
          <thead>
          <tr>
            <th scope="col">Nr.</th>
            <th scope="col">Challenge</th>
            <th scope="col">Score</th>
          </tr>
          </thead>
          <tbody>
          <template v-for="(challenge, index) in team.challenges">
            <tr>
              <td>#{{index+1}}</td>
              <th scope="row"><router-link :to="`/admin/submissions/challenge/${challenge.id}/team/${router.currentRoute.value.params.id}`">{{challenge.title}}</router-link></th>
              <td>{{challenge.points}}</td>
            </tr>
            <tr>
              <td colspan="3">{{challenge.challenge}}</td>
            </tr>
          </template>
          </tbody>
        </table>
      </section>
    </div>

  </main>
  </template>

<style scoped lang="scss">
main {
  margin: 1rem;
}

.top-section {
  display: flex;
  align-items: baseline;
  background-color: #73C671;
  border-radius: 1rem;
  padding: 1rem;

  a {
    font-size: 32px;
    color: black;
    text-decoration: none;
    font-weight: 700;
    padding-right: 1rem;
  }

  * {
    margin: 0;
  }
}


ul.picture-grid {
  list-style: none;
  display: grid;
  grid-template-rows: 1fr 1fr 1fr 1fr 1fr;
  grid-template-columns: 1fr 1fr 1fr 1fr 1fr;
  //height: 95vh;
  width: 100%;
  aspect-ratio: 1;
  border: black solid 1px;
  border-radius: 1rem;
  overflow: hidden;
  margin: 0;
  padding: 0;
  > li {
    position: relative;
    > img {
      height: 100%;
      width: 100%;
      object-fit: cover;
      left: 0;
      position: absolute;
      top: 0;
    }
  }
}

.submissions-grid {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 1rem;
  //padding: 0 1rem;
  //width: 95vw;
  //grid-template-rows: 1fr 1fr;
}

.pictures {
  width: inherit;
  //height: 80vw;
  //width: 80vw;
  display: grid;
  //place-items: center;

  //border: black solid 1px;
  //border-radius: 1rem;
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

#challenge-table {
  //margin-top: 4rem;
  background-color: var(--bg-s);
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
</style>