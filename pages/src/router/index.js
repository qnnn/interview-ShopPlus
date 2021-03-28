import Vue from 'vue'
import Router from 'vue-router'

Vue.use(Router)

/* Layout */
import Layout from '@/layout'
export const constantRoutes = [
  {
    path: '/login',
    component: () => import('@/views/login/index'),
    hidden: true
  },

  {
    path: '/404',
    component: () => import('@/views/404'),
    hidden: true
  },

  {
    path: '/',
    component: Layout,
    redirect: '/dashboard',
    children: [{
      path: 'dashboard',
      name: 'Dashboard',
      component: () => import('@/views/dashboard/index'),
      meta: { title: 'Dashboard', icon: 'dashboard' }
    }]
  },

  {
    path: '/example',
    component: Layout,
    redirect: '/example/table',
    name: 'Example',
    meta: { title: 'Example', icon: 'el-icon-s-help' },
    children: [
      {
        path: 'table',
        name: 'Table',
        component: () => import('@/views/table/index'),
        meta: { title: 'Table', icon: 'table' }
      },
      {
        path: 'tree',
        name: 'Tree',
        component: () => import('@/views/tree/index'),
        meta: { title: 'Tree', icon: 'tree' }
      }
    ]
  },

  {
    path: '/profile',
    component: Layout,
    redirect: '/profile/info',
    name: 'Profile',
    meta: { title: '个人信息', icon: 'user' },
    children: [
      {
        path: 'info',
        name: 'ProfileInfo',
        component: () => import('@/views/profile/info'),
        meta: { title: '修改信息', icon: 'table' }
      },
      {
        path: 'password',
        name: 'ProfilePassword',
        component: () => import('@/views/profile/password'),
        meta: { title: '修改密码', icon: 'tree' }
      },
      {
        path: 'icon',
        name: 'ProfileIcon',
        component: () => import('@/views/profile/icon'),
        meta: { title: '修改头像', icon: 'tree' }
      }
    ]
  },

  {
    path: '/system',
    component: Layout,
    redirect: '/system/user',
    name: 'UserControl',
    meta: { title: '用户管理', icon: 'user' },
    children: [
      {
        path: 'user',
        name: 'UserInfo',
        component: () => import('@/views/system/user/index'),
        meta: { title: '用户列表', icon: 'table' }
      },
      {
        path: 'role',
        name: 'RoleInfo',
        component: () => import('@/views/system/role/index'),
        meta: { title: '角色管理', icon: 'tree' }
      },
      {
        path: 'menu',
        name: 'MenuIfo',
        component: () => import('@/views/system/menu/index'),
        meta: {title: '菜单管理',icon: 'tree'}
      }
    ]
  },

  {
    path: '/logs',
    component: Layout,
    redirect: '/logs/user',
    name: 'Logs',
    meta: { title: '日志管理', icon: 'user' },
    children: [
      {
        path: 'user',
        name: 'LogsInfo',
        component: () => import('@/views/logging/index'),
        meta: { title: '查看日志', icon: 'table' }
      }
    ]
  },

  {
    path: '/form',
    component: Layout,
    children: [
      {
        path: 'index',
        name: 'Form',
        component: () => import('@/views/form/index'),
        meta: { title: 'Form', icon: 'form' }
      }
    ]
  },
  // 404 page must be placed at the end !!!
  { path: '*', redirect: '/404', hidden: true }
]

const createRouter = () => new Router({
  // mode: 'history', // require service support
  scrollBehavior: () => ({ y: 0 }),
  routes: constantRoutes
})

const router = createRouter()

export function resetRouter() {
  const newRouter = createRouter()
  router.matcher = newRouter.matcher // reset router
}

export default router
