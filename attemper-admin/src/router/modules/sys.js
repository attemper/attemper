/** When your routing system is too long, you can split it into small modules**/

import Layout from '@/layout'

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
      icon: 'tenant',
      noCache: false
    }
  },
  {
    path: 'tag',
    component: () => import('@/views/sys/tag'),
    name: 'tag',
    meta: {
      title: 'tag',
      icon: 'tag',
      noCache: false
    }
  },
  {
    path: 'resource',
    component: () => import('@/views/sys/resource'),
    name: 'resource',
    meta: {
      title: 'resource',
      icon: 'resource',
      noCache: false
    }
  }
  ]
}
export default sysRouter
