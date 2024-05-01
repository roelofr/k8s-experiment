<script setup lang="ts">
type User = {
    id: number;
    name: string;
    createdAt: string;
    updatedAt: string;
}

const toast = useToast()
const {data: users, refresh} = await useFetch(`/api/user`);

usePageTitle().value = "Users";

const dateFormat = new Intl.DateTimeFormat(undefined, {
    hour12: false,
    year: 'numeric',
    month: 'short',
    day: 'numeric',
    hour: '2-digit',
    minute: '2-digit',

})
const rows = computed(() => {
    return Array.from(users?.value).map((user: User) => {
        return {
            id: user.id,
            name: user.name,
            createdAt: dateFormat.format(new Date(user.createdAt)),
            updatedAt: dateFormat.format(new Date(user.updatedAt)),
            actions: 'actions'
        }
    })
})

const columns = [{
    key: 'id',
    label: 'ID'
}, {
    key: 'name',
    label: 'Name'
}, {
    key: 'createdAt',
    label: 'Created at'
}, {
    key: 'updatedAt',
    label: 'Updated at'
}, {
    key: 'actions'
}]

const tableDisabled = ref(false)
const deleteProfile = async (id) => {
    tableDisabled.value = true

    try {
        const res = await fetch(`/api/user/${id}`, {
            method: 'DELETE'
        })

        if (!res.ok)
            throw Error("Failed to delete user!")

        toast.add({
            color: 'green',
            title: 'Success',
            description: 'User deleted successfully!'
        })

        await refresh()
    } catch (err) {
        toast.add({
            color: 'red',
            title: 'Error',
            description: err.message
        })
    } finally {
        tableDisabled.value = false
    }
}
const actionItems = (row: User) => [
    [{
        label: 'View',
        icon: 'i-heroicons-eye-20-solid',
        click: () => navigateTo(`/users/${row.id}`)
    },
        {
            label: 'Edit',
            icon: 'i-heroicons-pencil-square-20-solid',
            click: () => navigateTo(`/users/${row.id}/edit`)
        }], [{
        label: 'Delete',
        icon: 'i-heroicons-trash-20-solid',
        click: () => deleteProfile(row.id)
    }]
]
</script>

<template>
    <div>
        <UTable :columns="columns" :rows="rows" :loading="tableDisabled">
            <template #actions-data="{ row }">
                <UDropdown :items="actionItems(row)" :disabled="tableDisabled">
                    <UButton color="gray" variant="ghost"
                             icon="i-heroicons-ellipsis-horizontal-20-solid"/>
                </UDropdown>
            </template>
        </UTable>

        <div class="mt-8 sm:text-right">
            <UButton @click="() => navigateTo('/users/add')">
                Add User
            </UButton>
        </div>
    </div>
</template>

<style scoped>

</style>
