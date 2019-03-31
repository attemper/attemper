/** When your routing system is too long, you can split it into small modules**/

import Layout from '@/layout/Layout'

const sysRouter = {
  name: 'sys',
  path: '/sys',
  component: Layout,
  redirect: '/sys/index',
  alwaysShow: true, // will always show the root menu
  meta: {
    title: 'sys',
    icon: 'system'
  },
  children: [{
    path: 'tenant',
    component: () => import('@/views/sys/tenant'),
    name: 'tenant',
    meta: {
      title: 'tenant',
      icon: 'tenant'
    }
  },
  {
    path: 'user',
    component: () => import('@/views/sys/user'),
    name: 'user',
    meta: {
      title: 'user',
      icon: 'user'
    }
  },
  {
    path: 'tag',
    component: () => import('@/views/sys/tag'),
    name: 'tag',
    meta: {
      title: 'tag',
      icon: 'tag'
    }
  },
  {
    path: 'resource',
    component: () => import('@/views/sys/resource'),
    name: 'resource',
    meta: {
      title: 'resource',
      icon: 'resource'
    }
  }
  ]
}
export default sysRouter
