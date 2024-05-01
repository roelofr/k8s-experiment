import pluginVue from 'eslint-plugin-vue'
import eslintPluginPrettierRecommended
  from 'eslint-plugin-prettier/recommended';

export default [
  // Vue with all it's rules
  ...pluginVue.configs['flat/recommended'],

  // Prettier
  eslintPluginPrettierRecommended,

  // Local
  {
    rules: {
      'no-console': 'off',
      'no-debugger': 'off',
      'vue/no-multiple-template-root': 'off',
    }
  }
]
