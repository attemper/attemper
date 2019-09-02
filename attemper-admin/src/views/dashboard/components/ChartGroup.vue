<template>
  <el-row v-if="visible" :gutter="32">
    <el-col :xs="24" :sm="24" :lg="cellNumber">
      <div class="chart-wrapper">
        <pie-chart ref="instanceChart" />
      </div>
    </el-col>
    <el-col :xs="24" :sm="24" :lg="cellNumber">
      <div class="chart-wrapper">
        <pie-chart ref="jobChart" />
      </div>
    </el-col>
  </el-row>
</template>

<script>
import PieChart from './PieChart'
import { instanceCountReq, jobCountReq } from '@/api/statistics/count'

export default {
  components: {
    PieChart
  },
  props: {
    cellNumber: {
      type: Number,
      default: 8
    }
  },
  data() {
    return {
      visible: true,
      instance: {
        statuses: [],
        chartName: '',
        legendData: [],
        seriesData: [],
        colorList: []
      },
      job: {
        statuses: [],
        chartName: '',
        legendData: [],
        seriesData: [],
        colorList: []
      }
    }
  },
  created() {
    this.loadConst()
    this.initInstance()
    this.initJob()
  },
  methods: {
    initInstance() {
      instanceCountReq().then(res => {
        const instanceCount = []
        res.data.result.forEach(item => {
          instanceCount.push({
            name: this.instance.statuses.find(cell => cell.value === item.status).label,
            status: item.status,
            value: item.count
          })
        })
        if (instanceCount.length === 0) {
          this.visible = false
          return
        }
        if (this.$refs['instanceChart']) {
          this.$refs['instanceChart'].initOption({
            chartName: this.$t('chart.instance'),
            legendData: this.instance.statuses.map(item => item.label),
            seriesData: instanceCount,
            colorList: [
              '#409EFF',
              '#67C23A',
              '#F56C6C',
              '#E6A23C',
              '#909399'
            ]
          })
        }
      })
    },
    initJob() {
      jobCountReq().then(res => {
        const jobCount = []
        res.data.result.forEach(item => {
          jobCount.push({
            name: this.job.statuses.find(cell => cell.value === item.status).label,
            status: item.status,
            value: item.count
          })
        })
        if (this.$refs['jobChart']) {
          this.$refs['jobChart'].initOption({
            chartName: this.$t('chart.job'),
            legendData: this.job.statuses.map(item => item.label),
            seriesData: jobCount,
            colorList: [
              '#67C23A',
              '#E6A23C'
            ]
          })
        }
      })
    },
    loadConst() {
      import(`@/constant/array/${localStorage.getItem('language')}.js`).then((array) => {
        this.instance.statuses = array.instanceStatuses
        this.job.statuses = array.jobStatuses
      })
    }
  }
}
</script>

