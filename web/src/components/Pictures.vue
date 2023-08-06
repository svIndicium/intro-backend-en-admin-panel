<script setup lang="ts">
import {fetchJsonWithAuth} from "../lib/fetcher";
import {Picture} from "../models";
import {ref} from "vue";

const pictureIds = ref<Picture[]>([])

fetchJsonWithAuth<Picture[]>("/api/pictures")
    .then((e) => pictureIds.value = e)
</script>

<template>
  <div>
    <ul class="picture-grid" aria-label="list of pictures">
      <li v-for="picture in pictureIds">
        <router-link :to="`/admin/picture/${picture.id}`" class="image-container">
          <img :src="`/api/pictures/${picture.id}/file`" :alt="`navigate to picture location number ${picture.id}`"/>
        </router-link>
      </li>
    </ul>
  </div>
</template>

<style scoped lang="scss">

ul.picture-grid {
  list-style: none;
  display: grid;
  grid-template-rows: 1fr 1fr 1fr 1fr 1fr;
  grid-template-columns: 1fr 1fr 1fr 1fr 1fr;
  overflow: hidden;
  height: 100%;
  width: 100%;
  margin: 0;
  padding: 0;

  > li {
    position: relative;

    > * > img {
      height: 100%;
      width: 100%;
      object-fit: cover;
      left: 0;
      position: absolute;
      top: 0;
    }
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