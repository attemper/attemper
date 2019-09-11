import Vue from 'vue'

import 'normalize.css/normalize.css' // A modern alternative to CSS resets
import Element from 'element-ui'
import './styles/element-variables.scss'
import 'vue-cron-generator/src/styles/global.less'
import VCalendar from 'v-calendar'
import '@/styles/index.scss' // global css
// https://github.com/apache/incubator-echarts/blob/master/index.js
import ECharts from 'vue-echarts/components/ECharts.vue'
import 'echarts/lib/chart/pie'
import 'echarts/lib/component/title'
import 'echarts/lib/component/tooltip'
import 'echarts/lib/chart/scatter'
import 'echarts/lib/component/singleAxis'

import App from './App'
import store from './store'
import router from './router'

import i18n from './lang' // Internationalization
import './icons' // icon
import './permission' // permission control
import './utils/error-log' // error log
import * as filters from './filters' // global filters

/**
 * If you don't want to use mock-server
 * you want to use mockjs for request interception
 * you can execute:
 *
 * import { mockXHR } from '../mock'
 * mockXHR()
 */

Vue.use(Element, {
  size: localStorage.getItem('size') || 'small', // set element-ui default size
  i18n: (key, value) => i18n.t(key, value)
})

Vue.use(VCalendar)
Vue.component('v-chart', ECharts)

// register global utility filters.
Object.keys(filters).forEach(key => {
  Vue.filter(key, filters[key])
})

Vue.config.productionTip = false

new Vue({
  el: '#app',
  router,
  store,
  i18n,
  render: h => h(App)
})
