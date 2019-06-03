<template>
  <el-row :gutter="40" class="panel-group">
    <el-col :xs="12" :sm="12" :lg="6" class="card-panel-col">
      <div class="card-panel" @click="handleSetLineChartData('purchases')">
        <div class="card-panel-icon-wrapper icon-money">
          <svg-icon icon-class="monitor" class-name="card-panel-icon" />
        </div>
        <div class="card-panel-instance-description">
          <div v-for="item in jobInstanceCount" :key="item.label" style="margin-bottom: 5px;">
            <span class="card-panel-text">
              {{ item.label }}
            </span>
            <el-tag :type="item.status | renderJobInstanceStatus">
              <count-to :start-val="0" :end-val="item.value" :duration="3000" class="card-panel-num" />
            </el-tag>
          </div>
        </div>
        <div class="card-panel-description">
          <div>
            <span class="card-panel-text">
              {{ $t('statistics.instance') }}
            </span>
            <count-to :start-val="0" :end-val="totalJobInstanceCount" :duration="3000" class="card-panel-num" />
          </div>
        </div>
      </div>
    </el-col>
    <el-col :xs="12" :sm="12" :lg="6" class="card-panel-col">
      <div class="card-panel" @click="handleSetLineChartData('messages')">
        <div class="card-panel-icon-wrapper icon-message">
          <svg-icon icon-class="job" class-name="card-panel-icon" />
        </div>
        <div class="card-panel-job-description">
          <div v-for="item in jobCount" :key="item.label" style="margin-bottom: 5px;">
            <span class="card-panel-text">
              {{ item.label }}
            </span>
            <el-tag :type="item.status | renderJobStatus">
              <count-to :start-val="0" :end-val="item.value" :duration="3000" class="card-panel-num" />
            </el-tag>
          </div>
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
    <el-col v-if="admin" :xs="12" :sm="12" :lg="4" class="card-panel-col">
      <div class="card-panel" @click="handleSetLineChartData('newVisitis')">
        <div class="card-panel-icon-wrapper icon-people">
          <svg-icon icon-class="tenant" class-name="card-panel-icon" />
        </div>
        <div class="card-panel-description">
          <span class="card-panel-text">
            {{ $t('statistics.tenant') }}
          </span>
          <count-to :start-val="0" :end-val="tenantCount" :duration="3000" class="card-panel-num" />
        </div>
      </div>
    </el-col>
  </el-row>
</template>

<script>
import CountTo from 'vue-count-to'
import { jobInstanceCountReq, jobCountReq, tenantCountReq } from '../../../api/statistics/count'
import { load } from '@/constant'
import { mapGetters } from 'vuex'

export default {
  components: {
    CountTo
  },
  data() {
    return {
      totalJobInstanceCount: 0,
      jobInstanceCount: [],
      totalJobCount: 0,
      jobCount: [],
      tenantCount: 0,
      jobStatuses: [],
      jobInstanceStatuses: []
    }
  },
  computed: {
    ...mapGetters([
      'admin'
    ])
  },
  created() {
    this.loadConst()
    this.getJobInstanceCount()
    this.getJobCount()
    this.getTenantCount()
  },
  methods: {
    handleSetLineChartData(type) {
      this.$emit('handleSetLineChartData', type)
    },
    getJobInstanceCount() {
      this.totalJobInstanceCount = 0
      this.jobInstanceCount = []
      jobInstanceCountReq().then(res => {
        res.data.result.forEach(item => {
          this.jobInstanceCount.push({
            label: this.jobInstanceStatuses.find(cell => cell.value === item.status).label,
            status: item.status,
            value: item.count
          })
          this.totalJobInstanceCount += item.count
        })
      })
    },
    getJobCount() {
      this.totalJobCount = 0
      this.jobCount = []
      jobCountReq().then(res => {
        res.data.result.forEach(item => {
          this.jobCount.push({
            label: this.jobStatuses.find(cell => cell.value === item.status).label,
            status: item.status,
            value: item.count
          })
          this.totalJobCount += item.count
        })
      })
    },
    getTenantCount() {
      this.tenantCount = 0
      tenantCountReq().then(res => {
        this.tenantCount = res.data.result
      })
    },
    loadConst() {
      load(`./array/${localStorage.getItem('language')}.js`).then((array) => {
        this.jobStatuses = array.jobStatuses
        this.jobInstanceStatuses = array.jobInstanceStatuses
      })
    }
  }
}
</script>

<style lang="scss" scoped>
.panel-group {
  margin-top: 5px;
  .card-panel-col{
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
    &:hover {
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

@media (max-width:550px) {
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
