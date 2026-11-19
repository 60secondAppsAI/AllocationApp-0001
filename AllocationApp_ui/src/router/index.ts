import { createRouter, createWebHashHistory } from 'vue-router';
import { useAuth } from '../stores/auth';
import SignIn from '../components/SignIn.vue';
import Settings from '../components/Settings.vue';
import MyDashboard from '../components/MyDashboard.vue';

// Dynamic imports for module components
import Allocations from '../components/Allocations.vue';
import AllocationDetail from '../components/AllocationDetail.vue';
import AllocationTargets from '../components/AllocationTargets.vue';
import AllocationTargetDetail from '../components/AllocationTargetDetail.vue';
import AllocationSnapshots from '../components/AllocationSnapshots.vue';
import AllocationSnapshotDetail from '../components/AllocationSnapshotDetail.vue';
import RebalanceInstructions from '../components/RebalanceInstructions.vue';
import RebalanceInstructionDetail from '../components/RebalanceInstructionDetail.vue';

const routes = [
  {
    path: '/',
    name: 'home',
    redirect: '/dashboard'
  },
  {
    path: '/dashboard',
    name: 'Dashboard',
    component: MyDashboard
  },
  
  // Dynamic module routes
  {
    path: '/allocations',
    name: 'Allocations',
    component: Allocations
  },
  {
    path: '/allocation/:id',
    name: 'AllocationDetail',
    component: AllocationDetail,
    props: true
  },
  {
    path: '/allocationTargets',
    name: 'AllocationTargets',
    component: AllocationTargets
  },
  {
    path: '/allocationTarget/:id',
    name: 'AllocationTargetDetail',
    component: AllocationTargetDetail,
    props: true
  },
  {
    path: '/allocationSnapshots',
    name: 'AllocationSnapshots',
    component: AllocationSnapshots
  },
  {
    path: '/allocationSnapshot/:id',
    name: 'AllocationSnapshotDetail',
    component: AllocationSnapshotDetail,
    props: true
  },
  {
    path: '/rebalanceInstructions',
    name: 'RebalanceInstructions',
    component: RebalanceInstructions
  },
  {
    path: '/rebalanceInstruction/:id',
    name: 'RebalanceInstructionDetail',
    component: RebalanceInstructionDetail,
    props: true
  },

  // Core application routes
  {
    path: '/signin',
    name: 'SignIn',
    component: SignIn
  },
//  {
//    path: '/users',
//    name: 'Users',
//    component: Users,
//    meta: { requiresAuth: true }
//  },
  {
    path: '/settings',
    name: 'Settings',
    component: Settings,
    meta: { requiresAuth: true }
  },
  {
    path: '/:pathMatch(.*)*',
    redirect: '/dashboard'
  }
];

const router = createRouter({
  history: createWebHashHistory(import.meta.env.BASE_URL),
  routes,
  scrollBehavior(to, from, savedPosition) {
    return savedPosition || { top: 0 };
  }
});

// Navigation guard for authentication
router.beforeEach((to, from, next) => {
  const auth = useAuth();
  
  if (to.meta.requiresAuth && !auth.isAuthenticated) {
    next({ name: 'SignIn', query: { redirect: to.fullPath } });
  } else if (to.name === 'SignIn' && auth.isAuthenticated) {
    next({ name: 'Dashboard' });
  } else {
    next();
  }
});

export default router;
