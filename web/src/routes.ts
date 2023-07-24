import Home from "./pages/Home.vue";
import Login from "./pages/admin/Login.vue";
import AdminHome from "./pages/admin/AdminHome.vue"
import Pictures from "./pages/Pictures.vue";
import Challenges from "./pages/Challenges.vue";
import Leaderboard from "./pages/Leaderboard.vue";
import {RouteRecordRaw} from "vue-router";
import CreateTeam from "./pages/admin/CreateTeam.vue";
import Submission from "./pages/admin/Submission.vue";
import CreateChallenges from "./pages/admin/CreateChallenges.vue";
import CreatePictures from "./pages/admin/createPictures.vue";
import UserHome from "./pages/UserHome.vue";

const routes: RouteRecordRaw[] = [
    { path: '/', component: Home },
    { path: '/pictures', component: Pictures },
    { path: '/challenges', component: Challenges },
    { path: '/leaderboard', component: Leaderboard },
    {
        path: '/admin',
        children: [
            { path: 'login', component: Login },
            { path: 'home', component: AdminHome},
            { path: 'team', component: CreateTeam},
            { path: 'submission/:id', component: Submission},
            { path: 'challenges', component: CreateChallenges },
            { path: 'pictures', component: CreatePictures },
        ]
    },
    {
        path: '/user',
        children: [
            { path: 'home', component: UserHome },
        ]
    }
]

export default routes