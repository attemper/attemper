/** When your routing job is too long, you can split it into small modules**/

import Layout from '@/layout'

const applicationRouter = {
  name: 'application',
  path: '/application',
  component: Layout,
  redirect: '/application/index',
  alwaysShow: true,
  meta: {
    title: 'application',
    icon: 'application'
  },
  children: [
    {
      path: 'project',
      component: () => import('@/views/application/project'),
      name: 'project',
      meta: {
        title: 'project',
        icon: 'project',
        noCache: false
      }
    },
    {
      path: 'gist',
      component: () => import('@/views/application/gist'),
      name: 'gist',
      meta: {
        title: 'gist',
        icon: 'gist',
        noCache: false
      }
    }
  ]
}
export default applicationRouter
