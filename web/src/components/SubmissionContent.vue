<script setup lang="ts">
import { streamWithAuth } from "../lib/fetcher";
import { onMounted, ref, onBeforeUnmount } from "vue";

const props = defineProps<{ url: string }>();
// const videoContainer = ref<HTMLDivElement>();
// const status = ref("Preparing video...");
// const error = ref("");
// const videoElement = ref<HTMLVideoElement | null>(null);
//
// // Controllers and cleanup references
// let abortController: AbortController | null = null;
//
// onBeforeUnmount(() => {
//   // Clean up resources when component is unmounted
//   if (abortController) {
//     abortController.abort();
//   }
// });
//
// const streamVideo = async () => {
//   try {
//     // Create abort controller
//     abortController = new AbortController();
//
//     // Try the simple approach first - use the browser's native video streaming capabilities
//     // by creating a blob URL that points to the video data
//     status.value = "Fetching video...";
//
//     // Fetch the entire video as a blob
//     const response = await streamWithAuth(
//         props.url,
//         {}, // No range header - get the entire file
//         // { signal: abortController.signal }
//     );
//
//     if (!response.ok) {
//       throw new Error(`Failed to access video: ${response.status} ${response.statusText}`);
//     }
//
//     // Convert response to blob
//     const blob = await response.blob();
//     const blobUrl = URL.createObjectURL(blob);
//
//     // Create video element
//     videoElement.value = document.createElement("video");
//     videoElement.value.controls = true;
//     videoElement.value.style.width = "100%";
//     videoElement.value.style.maxHeight = "80vh";
//     videoElement.value.src = blobUrl;
//
//     // Clear the container and append the video
//     if (videoContainer.value) {
//       videoContainer.value.innerHTML = "";
//       videoContainer.value.appendChild(videoElement.value);
//     }
//
//     status.value = "";
//
//     // Set up error handling for video element
//     videoElement.value.addEventListener("error", (e) => {
//       error.value = `Video playback error: ${videoElement.value?.error?.message || "Unknown error"}`;
//       console.error("Video error:", videoElement.value?.error);
//     });
//
//     // Clean up blob URL when video is unmounted
//     onBeforeUnmount(() => {
//       URL.revokeObjectURL(blobUrl);
//     });
//
//   } catch (e) {
//     if (e.name !== 'AbortError') {
//       error.value = e.message || "Failed to load video";
//       status.value = "";
//       console.error("Video streaming error:", e);
//     }
//   }
// };
//
// // Alternative function that uses a simple progressive download approach
// const downloadAndPlayVideo = async () => {
//   try {
//     // Create video element
//     videoElement.value = document.createElement("video");
//     videoElement.value.controls = true;
//     videoElement.value.style.width = "100%";
//     videoElement.value.style.maxHeight = "80vh";
//
//     // Create a download link as fallback
//     const downloadLink = document.createElement("a");
//     downloadLink.textContent = "Download video";
//     downloadLink.style.display = "block";
//     downloadLink.style.marginTop = "10px";
//
//     // Clear the container and append the video
//     if (videoContainer.value) {
//       videoContainer.value.innerHTML = "";
//       videoContainer.value.appendChild(videoElement.value);
//       videoContainer.value.appendChild(downloadLink);
//     }
//
//     // Create abort controller
//     abortController = new AbortController();
//
//     // Get authentication headers from your streamWithAuth method
//     const response = await streamWithAuth(
//         props.url,
//         {}, // Empty headers object
//         { signal: abortController.signal }
//     );
//
//     if (!response.ok) {
//       throw new Error(`Failed to access video: ${response.status} ${response.statusText}`);
//     }
//
//     // Get the blob and create object URL
//     const blob = await response.blob();
//     const videoUrl = URL.createObjectURL(blob);
//
//     // Set the video source
//     videoElement.value.src = videoUrl;
//
//     // Set up the download link
//     downloadLink.href = videoUrl;
//     downloadLink.download = props.url.split('/').pop() || "video";
//
//     status.value = "";
//
//     // Clean up when the component is unmounted
//     onBeforeUnmount(() => {
//       URL.revokeObjectURL(videoUrl);
//     });
//
//   } catch (e) {
//     if (e.name !== 'AbortError') {
//       error.value = e.message || "Failed to load video";
//       status.value = "";
//       console.error("Video loading error:", e);
//     }
//   }
// };
//
// onMounted(() => {
//   // Try the direct method first
//   downloadAndPlayVideo();
// });
</script>

<template>
  <div class="video-container">
<!--    <div ref="videoContainer" class="video-element"></div>-->
<!--    <div v-if="status" class="status-message">{{ status }}</div>-->
<!--    <div v-if="error" class="error-message">{{ error }}</div>-->
    <video
        ref="videoPlayer"
        controls
        width="800"
        height="450"
        class="video-player">
      <source :src="url" />
      Your browser does not support the video tag.
    </video>
  </div>
</template>

<style scoped>
.video-container {
  width: 100%;
}

.video-element {
  background-color: #f5f5f5;
  min-height: 200px;
  display: flex;
  align-items: center;
  justify-content: center;
}

.status-message {
  margin-top: 0.5rem;
  padding: 0.5rem;
  background-color: #f8f9fa;
  border-radius: 4px;
  text-align: center;
}

.error-message {
  margin-top: 0.5rem;
  padding: 0.5rem;
  background-color: #fff3f3;
  color: #d32f2f;
  border-radius: 4px;
  text-align: center;
}
</style>