<script setup lang="ts">
import Joi from 'joi'
import type {FormSubmitEvent} from '#ui/types'

const route = useRoute();
const toast = useToast();

const {data: user} = await useFetch(`/api/user/${route.params.id}`, {
    onResponseError({request, response, options}) {
        if (response.status === 404) {
            showError({
                statusCode: 404,
                statusMessage: "Page Not Found"
            })
        }
    },
});

usePageTitle().value = "Edit user";

const schema = Joi.object({
    name: Joi.string().required(),
})

const loading = ref(false)
const state = reactive({
    name: undefined,
})

async function onSubmit(event: FormSubmitEvent<any>) {
    loading.value = true

    try {
        const response = await fetch(`/api/user/${user?.id}`, {
            method: 'PUT',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({name: state.name})
        });

        if (!response.ok)
            throw Error("Failed to create user!")

        const user = await response.json()
        if (!user.id)
            throw Error("Failed to create user!")

        toast.add({
            color: 'green',
            title: 'Success',
            description: 'User created successfully!'
        })

        navigateTo(`/users/${user.id}`)
    } catch (err) {
        loading.value = false
        toast.add({
            color: 'red',
            title: 'Error',
            description: err.message
        })
    }
}

</script>

<template>
    <UForm :schema="schema" :state="state" class="space-y-4" @submit="onSubmit">
        <UFormGroup label="Name" name="name">
            <UInput v-model="state.name"/>
        </UFormGroup>

        <UButton type="submit" :disabled="loading">
            Create User
        </UButton>
    </UForm>
</template>

<style scoped>

</style>
