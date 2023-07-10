import { createApp } from 'vue'
import {createRouter, createWebHistory} from 'vue-router'
import './style.css'
import App from './App.vue'
import routes from "./routes";

const router = createRouter({
    // 4. Provide the history implementation to use. We are using the hash history for simplicity here.
    history: createWebHistory(),
    routes, // short for `routes: routes`
})
const app = createApp(App);
app.use(router)
app.mount('#app')
