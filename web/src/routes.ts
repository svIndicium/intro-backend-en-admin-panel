import Home from "./pages/Home.vue";
import Login from "./pages/admin/Login.vue";
import AdminHome from "./pages/admin/AdminHome.vue"
import Pictures from "./pages/Pictures.vue";
import Challenges from "./pages/Challenges.vue";
import Leaderboard from "./pages/Leaderboard.vue";
import {RouteRecordRaw} from "vue-router";
import CreateTeam from "./pages/admin/CreateTeam.vue";
import CreateChallenges from "./pages/admin/CreateChallenges.vue";
import CreatePictures from "./pages/admin/createPictures.vue";
import UserHome from "./pages/UserHome.vue";
import PictureSubmission from "./pages/admin/PictureSubmission.vue";
import ChallengeSubmission from "./pages/admin/ChallengeSubmission.vue";

const routes: RouteRecordRaw[] = [
    { path: '/', component: Login },
    {
        path: '/admin',
        children: [
            { path: 'home', component: AdminHome},
            { path: 'team', component: CreateTeam},
            { path: 'submissions/picture/:id/team/:teamId', component: PictureSubmission},
            { path: 'submissions/challenge/:id/team/:teamId', component: ChallengeSubmission},
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