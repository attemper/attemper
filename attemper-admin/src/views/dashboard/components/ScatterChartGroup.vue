<template>
  <div>
    <scatter-chart v-if="allPlanVisible" ref="allPlanChart" height="150px" />

    <scatter-chart v-if="dailyPlanVisible" ref="dailyPlanChart" height="150px" />

    <scatter-chart v-if="realTimePlanVisible" ref="realTimePlanChart" height="150px" />
  </div>
</template>

<script>
import ScatterChart from './ScatterChart'
import { appPlanReq } from '@/api/statistics/analysis'

export default {
  name: 'ScatterChartGroup',
  components: {
    ScatterChart
  },
  data() {
    return {
      allPlanVisible: true,
      realTimePlanVisible: true,
      dailyPlanVisible: true
    }
  },
  created() {
    this.initPlan()
  },
  methods: {
    initPlan() {
      appPlanReq().then(res => {
        if (res.data.result.length === 0) {
          this.allPlanVisible =
              this.realTimePlanVisible =
                this.dailyPlanVisible = false
          return
        }
        const allAxisObj = {}; const dailyAxisObj = {}; const realTimeAxisObj = {}
        const allSeriesObj = {}; const dailySeriesObj = {}; const realTimeSeriesObj = {}
        for (let i = 0; i < res.data.result.length; i++) {
          const appPlan = res.data.result[i]
          this.toObj(allAxisObj, allSeriesObj, appPlan)
          if (appPlan.prevFireTime) {
            if (new Date(appPlan.nextFireTime) - new Date(appPlan.prevFireTime) > 86300000) { // daily
              this.toObj(dailyAxisObj, dailySeriesObj, appPlan)
            } else { // real time
              this.toObj(realTimeAxisObj, realTimeSeriesObj, appPlan)
            }
          }
        }
        const allAxisData = []; const dailyAxisData = {}; const realTimeAxisData = []
        const allSeriesData = []; const dailySeriesData = []; const realTimeSeriesData = []
        this.toData(allAxisObj, allSeriesObj, allAxisData, allSeriesData)
        this.toData(dailyAxisObj, dailySeriesObj, dailyAxisData, dailySeriesData)
        this.toData(realTimeAxisObj, realTimeSeriesObj, realTimeAxisData, realTimeSeriesData)
        if (this.$refs['allPlanChart']) {
          this.$refs['allPlanChart'].initOption({
            text: this.$t('chart.total'),
            axisData: allAxisData,
            seriesData: allSeriesData
          })
        }
        if (!dailyAxisData.length || dailyAxisData.length === 0) {
          this.dailyPlanVisible = false
        } else {
          if (this.$refs['dailyPlanChart']) {
            this.$refs['dailyPlanChart'].initOption({
              text: this.$t('chart.daily'),
              axisData: dailyAxisData,
              seriesData: dailySeriesData
            })
          }
        }
        if (!realTimeAxisData.length || realTimeAxisData.length === 0) {
          this.realTimePlanVisible = false
        } else {
          if (this.$refs['realTimePlanChart']) {
            this.$refs['realTimePlanChart'].initOption({
              text: this.$t('chart.realTime'),
              axisData: realTimeAxisData,
              seriesData: realTimeSeriesData
            })
          }
        }
      })
    },
    toObj(axisObj, seriesObj, appPlan) {
      if (!axisObj[appPlan.nextFireTime]) {
        axisObj[appPlan.nextFireTime] = appPlan.jobName
        seriesObj[appPlan.nextFireTime] = 1
      } else {
        axisObj[appPlan.nextFireTime] += '\n' + appPlan.jobName
        seriesObj[appPlan.nextFireTime] += 1
      }
    },
    toData(axisObj, seriesObj, axisData, seriesData) {
      let index = 0
      for (const key in axisObj) {
        axisData.push(key + '\n\n' + axisObj[key])
        seriesData.push([index, seriesObj[key]])
        index++
      }
    }
  }
}
</script>
