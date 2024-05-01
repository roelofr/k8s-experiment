<script setup lang="ts">
usePageTitle().value = "Homepage";
</script>

<template>
    <div>
        <h1>I am the homepage</h1>

        <NuxtLink to="/users">Go to users</NuxtLink>
    </div>
</template>

<style scoped>

</style>
