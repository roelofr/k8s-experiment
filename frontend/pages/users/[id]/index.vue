<script setup lang="ts">
const route = useRoute();
const {data: user} = await useFetch(`/api/user/${route.params.id}`, {
    onResponseError({request, response, options}) {
        console.warn("Error fetching user", response.status, response.statusText)

        if (response.status === 404) {
            showError({
                statusCode: 404,
                statusMessage: "Page Not Found"
            })
        }
    }
});

const pageTitle = usePageTitle();
pageTitle.value = user ? `User ${user.name}` : `User #${route.params.id}`;
</script>

<template>
    <div>
        <template v-if="user != null">
            <h1>User #{{ user.id }}</h1>

            <table>
                <tbody>
                <tr>
                    <th>ID</th>
                    <td>{{ user.id }}</td>
                </tr>
                <tr>
                    <th>Name</th>
                    <td>{{ user.name }}</td>
                </tr>
                <tr>
                    <th>Created</th>
                    <td>{{ user.createdAt }}</td>
                </tr>
                <tr>
                    <th>Last updated</th>
                    <td>{{ user.updatedAt }}</td>
                </tr>
                </tbody>
            </table>
        </template>
        <template v-else>
            <NuxtLoadingIndicator/>
        </template>

        <UButton @click="() => navigateTo('/users')">Back to users</UButton>
    </div>
</template>

<style scoped>

</style>
