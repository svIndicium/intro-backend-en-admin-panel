<script setup lang="ts">
import {fetchWithAuth} from "../lib/fetcher";
import {ref} from "vue";

const props = defineProps<{ url: string }>()
const content = ref<HTMLDivElement>()
fetchWithAuth(props.url)
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
        content.value.replaceChildren(video)

        // And load it:
        video.load(); // https://developer.mozilla.org/en-US/docs/Web/API/HTMLMediaElement/load

      } else {
        // image
        const image = document.createElement<HTMLImageElement>("img")
        image.classList.add('submission')
        image.src = URL.createObjectURL(a)
        content.value.replaceChildren(image)
      }
    })
    .catch(() => content.value.innerText = "failed to load")
</script>

<template>
  <div ref="content">content is loading</div>
</template>

<style scoped>

</style>