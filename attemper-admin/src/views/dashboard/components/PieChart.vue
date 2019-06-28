<template>
  <div :class="className" :style="{height:height,width:width}" />
</template>

<script>
import echarts from 'echarts'
require('echarts/theme/macarons') // echarts theme
import resize from './mixins/resize'
import { jobInstanceCountReq } from '@/api/statistics/count'
const COLOR_LIST = [
  '#409EFF',
  '#67C23A',
  '#F56C6C',
  '#E6A23C',
  '#909399'
]
export default {
  mixins: [resize],
  props: {
    className: {
      type: String,
      default: 'chart'
    },
    width: {
      type: String,
      default: '100%'
    },
    height: {
      type: String,
      default: '300px'
    }
  },
  data() {
    return {
      chart: null,
      jobInstanceStatuses: []
    }
  },
  created() {
    this.init()
  },
  beforeDestroy() {
    if (!this.chart) {
      return
    }
    this.chart.dispose()
    this.chart = null
  },
  methods: {
    init() {
      this.loadConst()
      const jobInstanceCount = []
      jobInstanceCountReq().then(res => {
        res.data.result.forEach(item => {
          jobInstanceCount.push({
            name: this.jobInstanceStatuses.find(cell => cell.value === item.status).label,
            status: item.status,
            value: item.count
          })
        })
        this.chart = echarts.init(this.$el, 'macarons')
        this.chart.setOption({
          tooltip: {
            trigger: 'item',
            formatter: '{a} <br/>{b} : {c} ({d}%)'
          },
          legend: {
            left: 'center',
            bottom: '10',
            data: this.jobInstanceStatuses.map(item => item.label)
          },
          series: [
            {
              name: this.$t('chart.instance'),
              type: 'pie',
              // roseType: 'radius',
              // radius: [15, 95],
              // center: ['50%', '38%'],
              data: jobInstanceCount,
              animationEasing: 'cubicInOut',
              animationDuration: 2600,
              itemStyle: {
                normal: {
                  color: function(params) {
                    return COLOR_LIST[params.data.status]
                  }
                }
              }
            }
          ]
        })
      })
    },
    loadConst() {
      import(`@/constant/array/${localStorage.getItem('language')}.js`).then((array) => {
        this.jobInstanceStatuses = array.jobInstanceStatuses
      })
    }
  }
}
</script>
