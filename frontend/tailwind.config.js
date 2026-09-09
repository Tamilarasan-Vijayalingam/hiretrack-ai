/** @type {import('tailwindcss').Config} */
export default {
  content: [
    "./index.html",
    "./src/**/*.{js,ts,jsx,tsx}",
  ],
  darkMode: 'class', // Enable dark mode
  theme: {
    extend: {
      colors: {
        primary: {
          light: '#eff6ff',
          DEFAULT: '#3b82f6',
          dark: '#1d4ed8',
        },
        navy: {
          light: '#1e293b',
          DEFAULT: '#0f172a',
          dark: '#020617',
        }
      }
    },
  },
  plugins: [],
}
