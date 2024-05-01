<script setup lang="ts">
import Joi from 'joi'
import type {FormSubmitEvent} from '#ui/types'

usePageTitle().value = "Add User";
const toast = useToast()

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
        const response = await fetch(`/api/user`, {
            method: 'POST',
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
