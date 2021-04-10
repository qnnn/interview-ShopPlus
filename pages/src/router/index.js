import Vue from 'vue'
import Router from 'vue-router'
import {getToken, setToken, removeToken} from '@/utils/auth'
import store from '@/store'
import {buildMenus} from '@/api/system/menu'
import {filterAsyncRouter} from '@/store/modules/permission'
import {Message} from 'element-ui'

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
      meta: {title: 'Dashboard', icon: 'dashboard'}
    }]
  }

  // {
  //   path: '/profile',
  //   component: Layout,
  //   redirect: '/profile/info',
  //   name: 'Profile',
  //   meta: {title: '个人信息', icon: 'user'},
  //   children: [
  //     {
  //       path: 'info',
  //       name: 'ProfileInfo',
  //       component: () => import('@/views/profile/info'),
  //       meta: {title: '修改信息', icon: 'table'}
  //     },
  //     {
  //       path: 'password',
  //       name: 'ProfilePassword',
  //       component: () => import('@/views/profile/password'),
  //       meta: {title: '修改密码', icon: 'tree'}
  //     },
  //     {
  //       path: 'icon',
  //       name: 'ProfileIcon',
  //       component: () => import('@/views/profile/icon'),
  //       meta: {title: '修改头像', icon: 'tree'}
  //     }
  //   ]
  // },

  // {
  //   path: '/system',
  //   component: Layout,
  //   redirect: '/system/user',
  //   name: 'UserControl',
  //   meta: {title: '用户管理', icon: 'user'},
  //   children: [
  //     {
  //       path: 'user',
  //       name: 'UserInfo',
  //       component: () => import('@/views/system/user/index'),
  //       meta: {title: '用户列表', icon: 'table'}
  //     },
  //     {
  //       path: 'role',
  //       name: 'RoleInfo',
  //       component: () => import('@/views/system/role/index'),
  //       meta: {title: '角色管理', icon: 'tree'}
  //     },
  //     {
  //       path: 'menu',
  //       name: 'MenuInfo',
  //       component: () => import('@/views/system/menu/index'),
  //       meta: {title: '菜单管理', icon: 'tree'}
  //     }
  //   ]
  // },

  // {
  //   path: '/logs',
  //   component: Layout,
  //   redirect: '/logs/user',
  //   name: 'Logs',
  //   meta: {title: '日志管理', icon: 'user'},
  //   children: [
  //     {
  //       path: 'user',
  //       name: 'LogsInfo',
  //       component: () => import('@/views/logging/index'),
  //       meta: {title: '查看日志', icon: 'table'}
  //     }
  //   ]
  // },
  //
  // {
  //   path: '/form',
  //   component: Layout,
  //   children: [
  //     {
  //       path: 'index',
  //       name: 'Form',
  //       component: () => import('@/views/form/index'),
  //       meta: {title: 'Form', icon: 'form'}
  //     }
  //   ]
  // },
  // 404 page must be placed at the end !!!
  // {path: '*', redirect: '/404', hidden: true}
]

const createRouter = () => new Router({
  // mode: 'history', // require service support
  scrollBehavior: () => ({y: 0}),
  routes: constantRoutes
})


const router = createRouter()

const whiteList = ['/login']

router.beforeEach(function (to, from, next) {
  if (to.meta.title) {
    document.title = to.meta.title + ' - ' + 'kimiAdmin'
  }

  if (getToken()) {
    // 已登录且要跳转的页面是登录页
    if (to.path === '/login') {
      next({path: '/'})
    }
    else {
      // 登录时未拉取 菜单，在此处拉取
      if (store.getters.loadMenus) {
        // 修改成false，防止死循环
        store.dispatch('user/updateLoadMenus')
        loadMenus(next, to)
      }
      else if (store.getters.roles.length === 0 ) { // 判断当前用户是否已拉取完user_info信息
        store.dispatch('user/getInfo').then(() => { // 拉取user_info
          // 动态路由，拉取菜单
          loadMenus(next, to)
        }).catch(() => {
          Message({
            message: '网络异常！请检查网络是否可用！',
            type: 'warning'
          })
          next()
          // store.dispatch('user/logout').then(() => {
          //   location.reload() // 为了重新实例化vue-router对象 避免bug
          // })
        })
      }
      else {
        next() // 否则全部重定向到登录页
      }
    }
  }
  else {
    /* has no token*/
    if (whiteList.indexOf(to.path) !== -1) { // 在免登录白名单，直接进入
      next()
    } else {
      next(`/login?redirect=${to.fullPath}`) // 否则全部重定向到登录页
    }
  }
})

export const loadMenus = (next, to) => {
  buildMenus().then(res => {
    const asyncRouter = filterAsyncRouter(res.data)
    asyncRouter.push({path: '*', redirect: '/404', hidden: true})

    store.dispatch('GenerateRoutes', asyncRouter).then(() => { // 存储路由
      router.addRoutes(asyncRouter) // 动态添加可访问路由表
      next({...to, replace: true})
    })
  })
}

export function resetRouter() {
  const newRouter = createRouter()
  router.matcher = newRouter.matcher // reset router
}

export default router
