<template>
  <div>
    <el-row :gutter="10">
      <el-col :span="5">
        <date-time-generator v-model="param.lowerStartTime" :placeholder="$t('columns.startTime')" @update="param.lowerStartTime = $event" @change="search" />
      </el-col>
      <el-col :span="5">
        <date-time-generator v-model="param.upperStartTime" :placeholder="$t('columns.endTime')" @update="param.upperStartTime = $event" @change="search" />
      </el-col>
      <el-col :span="5">
        <el-select v-model="param.firedSource" @change="search">
          <el-option v-for="item in firedSources" :key="item.value" :label="item.label" :value="item.value" />
        </el-select>
      </el-col>
    </el-row>
    <pie-chart v-show="visible" ref="instanceChart" height="500px" />
  </div>
</template>

<script>
import PieChart from '@/components/Chart/PieChart'
import { instanceCountReq } from '@/api/statistics/count'
import CommonChart from '../mixins/commonChart'

export default {
  components: {
    PieChart
  },
  mixins: [CommonChart],
  data() {
    return {
      param: {
        lowerStartTime: null,
        upperStartTime: null,
        firedSource: ''
      },
      statuses: []
    }
  },
  created() {
    this.loadConst()
    this.search()
  },
  methods: {
    search() {
      this.param.jobName = this.jobName
      instanceCountReq(this.param).then(res => {
        const instanceCount = []
        res.data.result.forEach(item => {
          instanceCount.push({
            name: this.statuses.find(cell => cell.value === item.name).label,
            status: item.name,
            value: item.value
          })
        })
        if (instanceCount.length === 0) {
          this.visible = false
          this.$message.warning(this.$t('tip.jobNoInstance'))
          return
        }
        this.visible = true
        if (this.$refs['instanceChart']) {
          this.$refs['instanceChart'].initOption({
            chartName: this.$t('chart.instance'),
            legendData: this.statuses.map(item => item.label),
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
    loadConst() {
      import(`@/constant/array/${localStorage.getItem('language')}.js`).then((array) => {
        this.statuses = array.instanceStatuses
      })
    }
  }
}
</script>

