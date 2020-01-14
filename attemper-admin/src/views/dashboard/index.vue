<template>
  <div>
    <div class="dashboard-editor-container">
      <el-row :gutter="32" class="panel-group">
        <el-col :xs="12" :sm="12" :lg="cellNumber" class="card-panel-col">
          <div class="card-panel">
            <div class="card-panel-icon-wrapper icon-money">
              <svg-icon icon-class="monitor" class-name="card-panel-icon" />
            </div>
            <div class="card-panel-description">
              <div>
                <span class="card-panel-text">
                  {{ $t('statistics.instance') }}
                </span>
                <count-to :start-val="0" :end-val="totalInstanceCount" :duration="3000" class="card-panel-num" />
              </div>
            </div>
          </div>
        </el-col>
        <el-col :xs="12" :sm="12" :lg="cellNumber" class="card-panel-col">
          <div class="card-panel">
            <div class="card-panel-icon-wrapper icon-message">
              <svg-icon icon-class="job" class-name="card-panel-icon" />
            </div>
            <div class="card-panel-description">
              <div>
                <span class="card-panel-text">
                  {{ $t('statistics.job') }}
                </span>
                <count-to :start-val="0" :end-val="totalJobCount" :duration="3000" class="card-panel-num" />
              </div>
            </div>
          </div>
        </el-col>
        <el-col v-if="superAdmin > 0" :xs="12" :sm="12" :lg="cellNumber" class="card-panel-col">
          <div class="card-panel">
            <div class="card-panel-icon-wrapper icon-people">
              <svg-icon icon-class="tenant" class-name="card-panel-icon" />
            </div>
            <div class="card-panel-description">
              <span class="card-panel-text">
                {{ $t('statistics.tenant') }}
              </span>
              <count-to :start-val="0" :end-val="totalTenantCount" :duration="3000" class="card-panel-num" />
            </div>
          </div>
        </el-col>
      </el-row>
    </div>
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
      <el-col v-if="superAdmin > 0" :xs="24" :sm="24" :lg="cellNumber">
        <div class="chart-wrapper">
          <pie-chart ref="tenantChart" />
        </div>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import { mapGetters } from 'vuex'
import { instanceCountReq, jobCountReq, tenantCountReq } from '@/api/statistics/count'
import CountTo from 'vue-count-to'
import PieChart from '@/components/Chart/PieChart'
const INSTANCE_COLORS = [
  '#409EFF',
  '#67C23A',
  '#F56C6C',
  '#E6A23C',
  '#909399'
]

const JOB_COLORS = [
  '#67C23A',
  '#E6A23C'
]

const TENANT_COLORS = [
  '#67C23A',
  '#E6A23C',
  '#F56C6C'
]

export default {
  name: 'Dashboard',
  components: {
    CountTo,
    PieChart
  },
  data() {
    return {
      visible: true,
      totalInstanceCount: 0,
      totalJobCount: 0,
      totalTenantCount: 0
    }
  },
  computed: {
    ...mapGetters([
      'superAdmin'
    ]),
    cellNumber() {
      return this.superAdmin > 0 ? 8 : 12
    }
  },
  created() {
    this.init()
  },
  methods: {
    renderChart(req, statuses, refName, chartName, colorList) {
      return new Promise((resolve) => {
        req().then(res => {
          let totalCount = 0
          const dataArray = []
          res.data.result.forEach(item => {
            dataArray.push({
              name: statuses.find(cell => cell.value === item.name).label,
              status: item.name,
              value: item.value
            })
            totalCount += item.value
          })
          if (this.$refs[refName]) {
            this.$refs[refName].initOption({
              chartName: chartName,
              legendData: statuses.map(item => item.label),
              seriesData: dataArray,
              colorList: colorList
            })
          }
          resolve(totalCount)
        })
      })
    },
    init() {
      import(`@/constant/array/${localStorage.getItem('language')}.js`).then((array) => {
        this.renderChart(instanceCountReq,
          array.instanceStatuses,
          'instanceChart',
          this.$t('chart.instance'),
          INSTANCE_COLORS
        ).then(res => { this.totalInstanceCount = res })

        this.renderChart(jobCountReq,
          array.jobStatuses, 'jobChart',
          this.$t('chart.job'),
          JOB_COLORS
        ).then(res => { this.totalJobCount = res })

        this.renderChart(tenantCountReq,
          array.tenantStatuses,
          'tenantChart',
          this.$t('chart.tenant'),
          TENANT_COLORS
        ).then(res => { this.totalTenantCount = res })
      })
    }
  }
}
</script>

<style lang="scss" scoped>
  .dashboard-editor-container {
    padding: 10px;
    background-color: rgb(240, 242, 245);
    position: relative;

  .github-corner {
    position: absolute;
    top: 0px;
    border: 0;
    right: 0;
  }

  .chart-wrapper {
    background: #fff;
    padding: 16px 16px 0;
    margin-bottom: 32px;
  }

  }

  @media (max-width: 1024px) {
    .chart-wrapper {
      padding: 8px;
    }
  }

  .panel-group {
    margin-top: 5px;

  .card-panel-col {
    margin-bottom: 32px;
  }

  .card-panel {
    height: 150px;
    cursor: pointer;
    font-size: 12px;
    position: relative;
    overflow: hidden;
    color: #666;
    background: #fff;
    box-shadow: 4px 4px 40px rgba(0, 0, 0, .05);
    border-color: rgba(0, 0, 0, .05);

  &
  :hover {

  .card-panel-icon-wrapper {
    color: #fff;
  }

  .icon-people {
    background: #40c9c6;
  }

  .icon-message {
    background: #36a3f7;
  }

  .icon-money {
    background: #f4516c;
  }

  .icon-shopping {
    background: #34bfa3
  }

  }
  .icon-people {
    color: #40c9c6;
  }

  .icon-message {
    color: #36a3f7;
  }

  .icon-money {
    color: #f4516c;
  }

  .icon-shopping {
    color: #34bfa3
  }

  .card-panel-icon-wrapper {
    float: left;
    margin: 14px 0 0 14px;
    padding: 16px;
    transition: all 0.38s ease-out;
    border-radius: 6px;
  }

  .card-panel-icon {
    float: left;
    font-size: 48px;
  }

  .card-panel-instance-description {
    float: right;
    font-weight: bold;
    margin: 3px 26px 26px 0px;
  }

  .card-panel-job-description {
    float: right;
    font-weight: bold;
    margin: 30px 26px 26px 0px;
  }

  .card-panel-description {
    float: right;
    font-weight: bold;
    margin: 60px 26px 26px 0px;
  }

  .card-panel-text {
    line-height: 18px;
    color: rgba(0, 0, 0, 0.45);
    font-size: 16px;
    margin: 0 5px 12px 0;
  }

  .card-panel-num {
    font-size: 20px;
  }

  }
  }

  @media (max-width: 550px) {
    .card-panel-description {
      display: none;
    }

    .card-panel-icon-wrapper {
      float: none !important;
      width: 100%;
      height: 100%;
      margin: 0 !important;

    .svg-icon {
      display: block;
      margin: 14px auto !important;
      float: none !important;
    }
  }

  }
</style>
