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
      path: 'project',
      component: () => import('@/views/job/project'),
      name: 'project',
      meta: {
        title: 'project',
        icon: 'project'
      }
    },
    {
      path: 'jobs',
      component: () => import('@/views/job/jobs'),
      name: 'jobs',
      meta: {
        title: 'jobs',
        icon: 'jobs'
      }
    },
    {
      path: 'flow/:id',
      component: () => import('@/views/job/flow'),
      name: 'flow',
      hidden: true,
      meta: {
        title: route => `${route.params.id}`,
        notMenu: true
      }
    }
  ]
}
export default jobRouter
