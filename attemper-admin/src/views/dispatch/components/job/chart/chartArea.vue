<template>
  <div>
    <el-select v-model="chartValue">
      <el-option v-for="item in jobCharts" :key="item.value" :label="item.label" :value="item.value" />
    </el-select>
    <component :is="chartValue" :ref="chartValue" :job-name="jobName" style="margin-top: 10px;" />
  </div>
</template>

<script>
import DurationChart from './durationChart'
import StatusChart from './statusChart'

export default {
  components: {
    DurationChart,
    StatusChart
  },
  props: {
    jobName: {
      type: String,
      default: null
    },
    jobCharts: {
      type: Array,
      default: null
    }
  },
  data() {
    return {
      chartValue: null
    }
  },
  methods: {
    reRender() {
      this.$nextTick(() => {
        if (this.$refs[this.chartValue]) {
          this.$refs[this.chartValue].search()
        }
      })
    }
  }
}
</script>
