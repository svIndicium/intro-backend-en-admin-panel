<script setup lang="ts">
import QrcodeVue from 'qrcode.vue'
  import {sendForm} from "../../lib/fetcher";
  import {ref} from "vue";

  const joinUrl = 'https://localhost:5173/login?joinCode='
  const joinCode = ref<null | string>(null)
  async function createTeam(e) {
    // e.preventDefault()
    const r = await sendForm("/api/teams", e.target, "POST")

    joinCode.value = await r.text()
  }

</script>

<template>
  <div>
    <main>
      <form v-if="joinCode === null" @submit.prevent="createTeam" >
        <label>teamname:<input type="text" name="teamname"></label>
        <br>
        <button type="submit">create team</button>
      </form>
      <section v-else>
        <header>
          <qrcode-vue :value="`${joinUrl}${joinCode}`" :size="600" level="M"></qrcode-vue>
        </header>
        <section>
          <h1>Or join with the following code: <u>{{joinCode}}</u></h1>
        </section>
      </section>
    </main>

  </div>

</template>

<style scoped lang="scss">
div {
  display: grid;
  place-items: center;
  width: 100vw;
  height: 100vh;

  main {
    width: 80vh;
    aspect-ratio: 1;
    background: #73C671;
    padding: 1rem;
    border-radius: 1rem;

    section {
      display: grid;
      place-items: center;
    }
  }
}
</style>