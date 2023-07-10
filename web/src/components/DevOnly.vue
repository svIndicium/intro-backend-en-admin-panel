<script setup lang="ts">
  const isDev = import.meta.env.MODE === 'development'
</script>

<template>
    <slot v-if="isDev">

    </slot>
</template>

