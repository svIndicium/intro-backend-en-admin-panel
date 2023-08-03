<script setup lang="ts">
  import {useRouter} from "vue-router";
  import DevOnly from "../../components/DevOnly.vue";

  const router = useRouter()

  async function login(e) {
    e.preventDefault();
    const entries = new FormData(e.target).entries();
    const data = Object.fromEntries(entries);
    const token = await fetch(
        "/api/authenticate",
        {
          method: "POST",
          headers: { "Content-Type": "application/json" },
          body: JSON.stringify(data)
        })
        .then<{ accessToken: string }>(e => e.json())

    localStorage.setItem("accessToken", token.accessToken)

    await router.push({path: '/admin/home'})
  }

  async function loginAsAdmin() {
    const token = await fetch(
        "/api/authenticate",
        {
          method: "POST",
          headers: { "Content-Type": "application/json" },
          body: JSON.stringify({ username: "admin", password: "admin" })
        })
        .then<{ accessToken: string }>(e => e.json())

    localStorage.setItem("accessToken", token.accessToken)

    await router.push({path: '/admin/home'})
  }
</script>

<template>
  <div class="login-wrapper">
    <header>
      <img src="/logo.svg" alt=""/>
      <h1>Speurtocht 88</h1>
    </header>
    <main>
      <form @submit.prevent="login">
        <div>
          <label for="password">username</label>
          <input type="text" name="username">
        </div>
        <div>
          <label for="password">password</label>
          <input type="password" name="password">
        </div>

        <div class="button-group">
          <DevOnly>
            <button @click="loginAsAdmin">Quick dev login</button>
          </DevOnly>
          <button type="submit">Log in</button>
        </div>
      </form>

    </main>
  </div>

</template>

<style scoped lang="scss">
.login-wrapper {
  background-image: url("/background.svg");
  min-width: 100vw;
  min-height: 100vh;
  background-position: center;
  background-size: cover;
}
header {
  position: absolute;
  display: flex;
  height: 4rem;
  align-items: center;
  img {
    height: 100%;
  }
  color: white;
  gap: 1rem;
  padding: 1rem;
}
main {
  place-items: center;
  display: grid;
  min-width: 100vw;
  min-height: 100vh;
}
form {
  display: flex;
  flex-direction: column;
  gap: .5rem;
  background-color: white;
  border-radius: 1rem;
  //border-color: #001220;
  border: solid #001220 4px;
  padding: 1rem;
  min-height: 340px;
  min-width: 604px;

  div {
    flex-direction: column;
    display: flex;
  }

  div.button-group {
    flex-direction: row;
    justify-content: space-evenly;
    display: flex;
    margin-top: auto;
  }
}

input, button, label {
  font-size: 2em;
}

button {
  padding: 0.5rem;
  border-radius: .5rem;
  background: #73C671;
  border-color: var(--brdr);
}
</style>