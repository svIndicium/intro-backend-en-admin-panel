<script setup lang="ts">
import {fetchJsonWithAuth, fetchWithAuth} from "../../lib/fetcher";
  import {useRouter} from "vue-router";
  import {ref} from "vue";

  const router = useRouter();

  const original = ref<HTMLDivElement>()
  const submission = ref<HTMLDivElement>()

fetchWithAuth(`/api/pictures/${router.currentRoute.value.params.id}/file`)
    .then(r => r.blob())
    .then(a => {

      if (['video/mp4', 'video/webm', 'video/ogg'].includes(a.type)) {
        // video
        const newObjectUrl = URL.createObjectURL( a );

        const video = document.createElement<HTMLVideoElement>("video")

        // URLs created by `URL.createObjectURL` always use the `blob:` URI scheme: https://w3c.github.io/FileAPI/#dfn-createObjectURL
        const oldObjectUrl = video.currentSrc;
        if( oldObjectUrl && oldObjectUrl.startsWith('blob:') ) {
          // It is very important to revoke the previous ObjectURL to prevent memory leaks. Un-set the `src` first.
          // See https://developer.mozilla.org/en-US/docs/Web/API/URL/createObjectURL

          video.src = ''; // <-- Un-set the src property *before* revoking the object URL.
          URL.revokeObjectURL( oldObjectUrl );
        }

        // Then set the new URL:
        video.src = newObjectUrl;
        video.controls = true;
        video.classList.add('submission')
        original.value.replaceChildren(video)

        // And load it:
        video.load(); // https://developer.mozilla.org/en-US/docs/Web/API/HTMLMediaElement/load

      } else {
        // image
        const image = document.createElement<HTMLImageElement>("img")
        image.classList.add('submission')
        image.src = URL.createObjectURL(a)
        original.value.replaceChildren(image)
      }
    })
    .catch(() => submission.value.innerText = "failed to load")

  fetchWithAuth(`/api/pictures/${router.currentRoute.value.params.id}/teams/${router.currentRoute.value.params.teamId}/file`)
      .then(r => r.blob())
      .then(a => {

        if (['video/mp4', 'video/webm', 'video/ogg'].includes(a.type)) {
          // video
          const newObjectUrl = URL.createObjectURL( a );

          const video = document.createElement<HTMLVideoElement>("video")

          // URLs created by `URL.createObjectURL` always use the `blob:` URI scheme: https://w3c.github.io/FileAPI/#dfn-createObjectURL
          const oldObjectUrl = video.currentSrc;
          if( oldObjectUrl && oldObjectUrl.startsWith('blob:') ) {
            // It is very important to revoke the previous ObjectURL to prevent memory leaks. Un-set the `src` first.
            // See https://developer.mozilla.org/en-US/docs/Web/API/URL/createObjectURL

            video.src = ''; // <-- Un-set the src property *before* revoking the object URL.
            URL.revokeObjectURL( oldObjectUrl );
          }

          // Then set the new URL:
          video.src = newObjectUrl;
          video.controls = true;
          video.classList.add('submission')
          submission.value.replaceChildren(video)

          // And load it:
          video.load(); // https://developer.mozilla.org/en-US/docs/Web/API/HTMLMediaElement/load

        } else {
          // image
          const image = document.createElement<HTMLImageElement>("img")
          image.classList.add('submission')
          image.src = URL.createObjectURL(a)
          submission.value.replaceChildren(image)
        }
      })
  .catch(() => submission.value.innerText = "failed to load")

async function deny() {
  await fetch(`/api/pictures/${router.currentRoute.value.params.id}/teams/${router.currentRoute.value.params.teamId}/deny`, {
    method: "PATCH",
    headers: { "Authorization": "Bearer " + localStorage.getItem("accessToken") },
  })
  await router.push({path: '/admin/home'})
}

async function approve() {
  await fetch(`/api/pictures/${router.currentRoute.value.params.id}/teams/${router.currentRoute.value.params.teamId}/approve`, {
    method: "PATCH",
    headers: { "Authorization": "Bearer " + localStorage.getItem("accessToken") },
  })
  await router.push({path: '/admin/home'})
}
</script>

<template>
  <main>
<!--    <h1>{{ teamName }}</h1>-->
    <div class="submission-grid">
      <div>
        <div class="submission-top-bar">
          <p>Original</p>
        </div>
        <div ref="original">content is loading</div>
      </div>

      <div>
        <div class="submission-top-bar">
          <p style="background-color: white;color: black;margin: 0">Submission</p>
          <button type="button" @click="deny">deny</button>
          <button type="button" @click="approve">approve</button>
        </div>
        <div ref="submission">content is loading</div>
      </div>
    </div>
  </main>
</template>

<style scoped lang="scss">
.submission-grid {
  display: grid;
  grid-template-rows: 1fr;
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
.submission-top-bar {
  position: absolute;
  padding: 1rem;
  display: flex;
  justify-content: flex-start;
  gap: 1rem;

  > p, button {
    background-color: var(--bg-s);
    color: var(--txt-p);
    margin: 0;
    padding: .25rem;
    font-size: 1em;
    border: 1px solid var(--brdr);
    border-radius: 1rem;
  }

  > button:hover {
    background-color: var(--bg-p);
  }
}
</style>