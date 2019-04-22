/** When your routing monitor is too long, you can split it into small modules**/

import Layout from '@/layout/Layout'

const monitorRouter = {
  name: 'monitor',
  path: '/monitor',
  component: Layout,
  redirect: '/monitor/index',
  alwaysShow: true, // will always show the root menu
  meta: {
    title: 'monitor',
    icon: 'monitor'
  },
  children: [
    {
      path: 'realTime',
      component: () => import('@/views/monitor/realTime'),
      name: 'realTime',
      meta: {
        title: 'realTime',
        icon: 'realTime',
        noCache: false
      }
    },
    {
      path: 'history',
      component: () => import('@/views/monitor/history'),
      name: 'history',
      meta: {
        title: 'history',
        icon: 'history',
        noCache: false
      }
    },
    {
      path: 'total',
      component: () => import('@/views/monitor/total'),
      name: 'total',
      meta: {
        title: 'total',
        icon: 'total',
        noCache: false
      }
    }
  ]
}
export default monitorRouter
