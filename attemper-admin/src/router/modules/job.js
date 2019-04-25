/** When your routing job is too long, you can split it into small modules**/

import Layout from '@/layout/Layout'

const jobRouter = {
  name: 'job',
  path: '/job',
  component: Layout,
  redirect: '/job/index',
  alwaysShow: true, // will always show the root menu
  meta: {
    title: 'job',
    icon: 'job'
  },
  children: [
    {
      path: 'flowJob',
      component: () => import('@/views/job/flowJob'),
      name: 'flowJob',
      meta: {
        title: 'flowJob',
        icon: 'flowJob',
        noCache: false
      }
    },
    {
      path: 'arg',
      component: () => import('@/views/job/arg'),
      name: 'arg',
      meta: {
        title: 'arg',
        icon: 'arg',
        noCache: false
      }
    },
    {
      path: 'project',
      component: () => import('@/views/job/project'),
      name: 'project',
      meta: {
        title: 'project',
        icon: 'project',
        noCache: false
      }
    },
    {
      path: 'flow/:key',
      component: () => import('@/views/job/flow'),
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
