/** When your routing monitor is too long, you can split it into small modules**/

import Layout from '@/layout'

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
      component: () => import('@/views/instance/monitor'),
      name: 'realTime',
      props: {
        monitorType: 'realTime'
      },
      meta: {
        title: 'realTime',
        icon: 'realTime'
      }
    },
    {
      path: 'history',
      component: () => import('@/views/instance/monitor'),
      name: 'history',
      props: {
        monitorType: 'history'
      },
      meta: {
        title: 'history',
        icon: 'history'
      }
    },
    {
      path: 'total',
      component: () => import('@/views/instance/monitor'),
      name: 'total',
      props: {
        monitorType: 'total'
      },
      meta: {
        title: 'total',
        icon: 'total'
      }
    },
    {
      path: 'trace/:key',
      component: () => import('@/views/instance/trace'),
      name: 'trace',
      hidden: true,
      meta: {
        title: route => `${route.params.key}`,
        notMenu: true
      }
    }
  ]
}
export default monitorRouter
