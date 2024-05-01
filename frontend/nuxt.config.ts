// https://nuxt.com/docs/api/configuration/nuxt-config
export default defineNuxtConfig({
    devtools: {enabled: true},
    modules: ["@nuxt/ui", "@nuxt/eslint", "@nuxt/image"],
    colorMode: {
        preference: 'light'
    },
    routeRules: {
        '/api/**': {proxy: 'http://localhost:8080/api/**'},
    }
})