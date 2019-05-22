/** When your routing job is too long, you can split it into small modules**/

import Layout from '@/layout'

const jobRouter = {
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
      path: 'project',
      component: () => import('@/views/dispatch/project'),
      name: 'project',
      meta: {
        title: 'project',
        icon: 'project',
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
        noCache: false
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
export default jobRouter
