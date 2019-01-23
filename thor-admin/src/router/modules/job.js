/** When your routing job is too long, you can split it into small modules**/

import Layout from '@/views/layout/Layout'

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
  children: [{
    path: 'jobs',
    component: () => import('@/views/job/jobs'),
    name: 'jobs',
    meta: {
      title: 'jobs',
      icon: 'atom-job'
    }
  },
  {
    path: 'atom-job',
    component: () => import('@/views/job/atomJob'),
    name: 'atom-job',
    meta: {
      title: 'atomJob',
      icon: 'atom-job'
    }
  },
  {
    path: 'group-job',
    component: () => import('@/views/job/groupJob'),
    name: 'group-job',
    meta: {
      title: 'groupJob',
      icon: 'group-job'
    }
  },
  {
    path: 'flow-job',
    component: () => import('@/views/job/flowJob'),
    name: 'flow-job',
    meta: {
      title: 'flowJob',
      icon: 'flow-job'
    }
  }
  ]
}
export default jobRouter
