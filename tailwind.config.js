/** @type {import('tailwindcss').Config} */
module.exports = {
    content: [
        "./src/main/resources/templates/**/*.html",
        "./src/main/resources/static/**/*.js" // If you use JS files with Tailwind classes
    ],
    theme: {
        extend: {},
    },
    plugins: [],
}