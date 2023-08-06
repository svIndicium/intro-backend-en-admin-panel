<script setup lang="ts">
import {fetchJsonWithAuth} from "../lib/fetcher";
import {Challenge} from "../models";
import {ref} from "vue";
const challenges = ref<Challenge[]>([])
fetchJsonWithAuth<Challenge[]>("/api/challenges")
    .then(e => challenges.value = e)
</script>
<template>
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
          <th scope="row">
            <router-link :to="`/admin/challenges/${challenge.id}`">{{challenge.title}}</router-link>
          </th>
          <td>{{challenge.points}}</td>
        </tr>
        <tr>
          <td colspan="3">{{challenge.challenge}}</td>
        </tr>
      </template>
      <tr class="create-new">
        <td colspan="3">
          <router-link to="/admin/challenges">create challenges</router-link>
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
</style>