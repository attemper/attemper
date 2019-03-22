import access from './access'

const install = function(Vue) {
  Vue.directive('access', access)
}

if (window.Vue) {
  window['access'] = access
  Vue.use(install); // eslint-disable-line
}

access.install = install
export default access
