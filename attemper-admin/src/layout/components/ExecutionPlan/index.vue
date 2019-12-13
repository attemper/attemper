<template>
  <el-popover placement="bottom" width="100%" trigger="hover" @show="showPlan">
    <job-plan v-if="visible" v-model="planData" />
    <span slot="reference" style="font-size: 17px;">{{ currentTimeStr }}</span>
  </el-popover>

</template>

<script>
import { appPlanReq } from '@/api/statistics/analysis'
import { currentTimeReq } from '@/api/dispatch/tool'
import { parseTime } from '@/utils'
import JobPlan from './JobPlan'

export default {
  name: 'ExecutionPlan',
  components: {
    JobPlan
  },
  data() {
    return {
      timer: null,
      now: null,
      currentTimeStr: null,
      visible: true,
      planData: []
    }
  },
  created() {
    currentTimeReq().then(res => {
      this.now = new Date(res.data.result)
      this.timer = setInterval(() => {
        this.now = new Date(this.now.getTime() + 1000)
        this.currentTimeStr = parseTime(this.now)
      }, 1000)
    })
  },
  beforeDestroy() {
    if (this.timer) {
      clearInterval(this.timer)
    }
  },
  methods: {
    showPlan() {
      appPlanReq().then(res => {
        if (res.data.result.length === 0) {
          this.visible = false
          return
        }
        this.planData = []
        const allObj = {}
        for (let i = 0; i < res.data.result.length; i++) {
          const appPlan = res.data.result[i]
          this.toObj(allObj, appPlan)
        }
        this.toData(allObj, this.planData)
      })
    },
    toObj(obj, appPlan) {
      const key = parseTime(appPlan.nextFireTime)
      if (!obj[key]) {
        obj[key] = []
      }
      obj[key].push(appPlan)
    },
    toData(obj, data) {
      for (const key in obj) {
        data.push({
          nextFireTime: key,
          plans: obj[key]
        })
      }
    }
  }
}
</script>
