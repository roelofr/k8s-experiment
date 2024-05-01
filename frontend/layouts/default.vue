<script setup lang="ts">
import {usePageTitle} from "~/composables/states";

const pageTitle = usePageTitle()
</script>

<template>
    <div class="min-h-full">
        <AppNavbar/>

        <header class="bg-white shadow-sm">
            <div class="mx-auto max-w-7xl px-4 py-4 sm:px-6 lg:px-8">
                <h1 class="text-lg font-semibold leading-6 text-gray-900">
                    {{ pageTitle }}</h1>
            </div>
        </header>

        <main class="text-gray-900">
            <div class="mx-auto max-w-7xl py-6 sm:px-6 lg:px-8">
                <slot/>
            </div>
        </main>

        <UNotifications/>
    </div>
</template>
