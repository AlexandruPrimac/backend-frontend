import js from '@eslint/js'
import { defineConfig, globalIgnores } from 'eslint/config'
import globals from 'globals'

export default defineConfig([
    { files: [ '**/*.{js,mjs,cjs}' ], plugins: { js }, extends: [ 'js/recommended' ] },
    { files: [ '**/*.{js,mjs,cjs}' ], languageOptions: { globals: globals.browser } },
    globalIgnores([ 'src/main/resources/static/js/**', 'build/resources/main/static/js/**' ])
])
