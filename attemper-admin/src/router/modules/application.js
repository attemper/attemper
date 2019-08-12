/** When your routing job is too long, you can split it into small modules**/

import Layout from '@/layout'

const applicationRouter = {
  name: 'application',
  path: '/application',
  component: Layout,
  redirect: '/application/index',
  alwaysShow: true, // will always show the root menu
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
      path: 'program',
      component: () => import('@/views/application/program'),
      name: 'program',
      meta: {
        title: 'program',
        icon: 'program',
        noCache: false
      }
    },
    {
      path: 'package/:key',
      component: () => import('@/views/application/package'),
      name: 'package',
      hidden: true,
      meta: {
        title: route => `${route.params.key}`,
        notMenu: true,
        noCache: false
      }
    }
  ]
}
export default applicationRouter
