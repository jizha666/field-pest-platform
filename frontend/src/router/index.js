import { createRouter, createWebHistory } from 'vue-router'

// import IndexView from '@/views/index/index.vue';
// import LayoutView from '@/views/layout/index.vue'
// import LoginView from '@/views/login/index.vue'
// import PestView from '@/views/pest/index.vue'
import AdminIndexView from '@/views/adminIndex/index.vue'
import AdminLayoutView from '@/views/adminLayout/index.vue'
//import EnvCollectView from '@/views/envCollect/index.vue'
import EquipmentView from '@/views/equipment/index.vue'
import FieldView from '@/views/field/index.vue'
import PestCount from '@/views/pestCount/index.vue'
import AreaView from '@/views/area/index.vue'
import PestQuery from '@/views/pestQuery/index.vue'
import LoginView from '@/views/login/index.vue'
//import PestCollectView from '@/views/pestCollect/index.vue'
import UserView from '@/views/user/index.vue'
import VisitorIndexView from '@/views/visitorIndex/index.vue'
import VisitorLayoutView from '@/views/visitorLayout/index.vue'
import Camera from '@/views/camera/index.vue'


const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    { path: '/', redirect: '/login' },
    {path: '/login', name: 'login', component: LoginView},
    {
     path: '/1', 
     name: '',
     component: VisitorLayoutView,
    //  redirect: '/visitorIndex', //重定向
     children: [
      { path: '/layout', name: 'layout', component: VisitorLayoutView },
      { path: '/visitorIndex', name: 'visitorIndex', component: VisitorIndexView },
      { path: '/pestQuery', name: 'pestQuery', component: PestQuery },
      { path: '/pestCount', name: 'pestCount', component: PestCount },
      { path: '/camera', name: 'camera', component: Camera },
      { path: '/user', name: 'user', component: UserView },
     ]
    },
    {
      path: '/2', 
      name: '',
      component: AdminLayoutView,
      // redirect: '/adminIndex', //重定向
      children: [
       { path: '/layout', name: 'layout', component: AdminLayoutView },
       { path: '/adminIndex', name: 'adminIndex', component: AdminIndexView },
       { path: '/area', name: 'area', component: AreaView },
       { path: '/field', name: 'field', component: FieldView },
       { path: '/equipment', name: 'equipment', component: EquipmentView },
       //{ path: '/envCollect', name: 'envCollect', component: EnvCollectView },
      ]
     },
    { path: '/:pathMatch(.*)*', redirect: '/login' },
  ]
})

export default router

