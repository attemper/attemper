/** When your routing job is too long, you can split it into small modules**/

import Layout from '@/layout'

const dispatchRouter = {
  name: 'dispatch',
  path: '/dispatch',
  component: Layout,
  redirect: '/dispatch/index',
  alwaysShow: true, // will always show the root menu
  meta: {
    title: 'dispatch',
    icon: 'dispatch'
  },
  children: [
    {
      path: 'job',
      component: () => import('@/views/dispatch/job'),
      name: 'job',
      meta: {
        title: 'job',
        icon: 'job',
        noCache: false
      }
    },
    {
      path: 'delay',
      component: () => import('@/views/dispatch/delay'),
      name: 'delay',
      meta: {
        title: 'delay',
        icon: 'delay',
        noCache: false
      }
    },
    {
      path: 'arg',
      component: () => import('@/views/dispatch/arg'),
      name: 'arg',
      meta: {
        title: 'arg',
        icon: 'arg',
        noCache: false
      }
    },
    {
      path: 'datasource',
      component: () => import('@/views/dispatch/datasource'),
      name: 'datasource',
      meta: {
        title: 'datasource',
        icon: 'datasource',
        noCache: false
      }
    },
    {
      path: 'calendar',
      component: () => import('@/views/dispatch/calendar'),
      name: 'calendar',
      meta: {
        title: 'calendar',
        icon: 'calendar',
        noCache: false,
        buttons: ['saveDate']
      }
    },
    {
      path: 'flow/:key',
      component: () => import('@/views/dispatch/flow'),
      name: 'flow',
      hidden: true,
      meta: {
        title: route => `${route.params.key}`,
        notMenu: true,
        noCache: false
      }
    }
  ]
}
export default dispatchRouter
