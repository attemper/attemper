<template>
  <div>
    <el-row :gutter="10">
      <el-col :span="5">
        <date-time-generator v-model="page.lowerStartTime" :placeholder="$t('columns.startTime')" @update="param.lowerStartTime = $event" @change="search" />
      </el-col>
      <el-col :span="5">
        <date-time-generator v-model="page.upperStartTime" :placeholder="$t('columns.endTime')" @update="param.upperStartTime = $event" @change="search" />
      </el-col>
      <el-col :span="5">
        <el-select v-model="page.firedSource" @change="search">
          <el-option v-for="item in firedSources" :key="item.value" :label="item.label" :value="item.value" />
        </el-select>
      </el-col>
      <el-col :span="5">
        <el-select v-model="page.sort" @change="search">
          <el-option v-for="item in durationOrders" :key="item.value" :label="item.label" :value="item.value" />
        </el-select>
      </el-col>
    </el-row>

    <line-chart v-show="visible" ref="durationChart" height="500px" />

    <pagination
      v-show="visible && page.total > 0"
      :total="page.total"
      :page.sync="page.currentPage"
      :limit.sync="page.pageSize"
      @pagination="search"
    />

  </div>
</template>

<script>
import LineChart from '@/components/Chart/LineChart'
import { instanceDurationReq } from '@/api/statistics/analysis'
import Pagination from '@/components/Pagination'
import CommonChart from '../mixins/commonChart'

export default {
  components: {
    LineChart,
    Pagination
  },
  mixins: [CommonChart],
  data() {
    return {
      page: {
        currentPage: 1,
        pageSize: 10,
        total: 0,
        lowerStartTime: null,
        upperStartTime: null,
        firedSource: '',
        sort: 'START_TIME'
      },
      durationOrders: []
    }
  },
  created() {
    this.loadConst()
    this.search()
  },
  methods: {
    search() {
      this.page.jobName = this.jobName
      instanceDurationReq(this.page).then(res => {
        Object.assign(this.page, res.data.result.page)
        if (res.data.result.durations.length === 0) {
          this.visible = false
          this.$message.warning(this.$t('tip.jobNoInstance'))
          return
        }
        this.visible = true
        if (this.$refs['durationChart']) {
          this.$refs['durationChart'].initOption({
            chartName: this.$t('chart.instanceDuration'),
            xAxisData: res.data.result.startTimes,
            seriesData: res.data.result.durations
          })
        }
      })
    },
    loadConst() {
      const lang = localStorage.getItem('language')
      import('@/lang/dict.js').then(array => {
        this.durationOrders = array['durationOrders_' + lang]
      })
    }
  }
}
</script>

