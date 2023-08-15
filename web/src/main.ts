import { createApp } from 'vue'
import {createRouter, createWebHistory} from 'vue-router'
import './style.css'
import App from './App.vue'
import routes from "./routes";
import router from "./routes";


const app = createApp(App);
app.use(router)
app.mount('#app')
