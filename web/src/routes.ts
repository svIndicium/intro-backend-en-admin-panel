import Login from "./pages/admin/Login.vue";
import AdminHome from "./pages/admin/AdminHome.vue"
import {createRouter, createWebHistory, RouteRecordRaw} from "vue-router";
import CreateTeam from "./pages/admin/CreateTeam.vue";
import CreateChallenges from "./pages/admin/CreateChallenges.vue";
import CreatePictures from "./pages/admin/createPictures.vue";
import PictureSubmission from "./pages/admin/PictureSubmission.vue";
import ChallengeSubmission from "./pages/admin/ChallengeSubmission.vue";
import TeamOverview from "./pages/admin/TeamOverview.vue";

const routes: RouteRecordRaw[] = [
    { path: '/', component: Login },
    {
        path: '/admin',
        children: [
            { path: 'home', component: AdminHome},
            { path: 'team', component: CreateTeam},
            { path: 'team/:id', component: TeamOverview},
            { path: 'submissions/picture/:id/team/:teamId', component: PictureSubmission},
            { path: 'submissions/challenge/:id/team/:teamId', component: ChallengeSubmission},
            { path: 'challenges', component: CreateChallenges },
            { path: 'pictures', component: CreatePictures },
        ]
    }
]

const router = createRouter({
    // 4. Provide the history implementation to use. We are using the hash history for simplicity here.
    history: createWebHistory(),
    routes, // short for `routes: routes`
})

export default router