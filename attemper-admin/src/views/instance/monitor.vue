<template>
  <div class="app-container">
    <div class="filter-container">
      <el-input v-model="page.jobName" :placeholder="$t('dispatch.job.columns.jobName')" class="filter-item search-input" @keyup.enter.native="search" />
      <el-input v-model="page.displayName" :placeholder="$t('columns.displayName')" class="filter-item search-input" @keyup.enter.native="search" />
      <el-select v-model="page.status" :placeholder="$t('columns.status')" multiple clearable collapse-tags class="filter-item search-select">
        <el-option v-for="item in currentJobInstanceStatuses" :key="item.value" :label="item.label" :value="item.value" />
      </el-select>
      <!--<el-select v-model="page.sort" style="width: 140px" class="filter-item" @change="search">
        <el-option v-for="item in sortOptions" :key="item.key" :label="item.label" :value="item.key"/>
      </el-select>-->
      <el-button v-waves class="filter-item" type="primary" icon="el-icon-search" @click="search">{{ $t('actions.search') }}</el-button>
      <span v-if="selections.length === 1">
        <el-button v-if="retryVisible" v-waves class="filter-item" type="warning" icon="el-icon-s-operation" @click="retry">{{ $t('actions.retry') }}</el-button>
        <el-button v-if="terminateVisible" v-waves class="filter-item" type="danger" icon="el-icon-stopwatch" @click="terminate">{{ $t('actions.terminate') }}</el-button>
        <el-button v-if="pauseOrActivateVisible" v-waves :icon="canPause ? 'el-icon-video-pause' : 'el-icon-video-play'" class="filter-item" type="primary" @click="pauseOrActivate">
          {{ canPause ? $t('actions.pause') : $t('actions.activate') }}
        </el-button>
      </span>
      <el-popover
        placement="bottom"
        trigger="click"
      >
        <el-form
          label-position="left"
          label-width="100px"
          style="height: 100%"
        >
          <el-form-item :label="$t('monitor.columns.startTime')" style="margin-bottom: 5px;">
            <date-time-generator @update="page.lowerStartTime = $event" @change="search" />
          </el-form-item>
          <el-form-item>
            <date-time-generator @update="page.upperStartTime = $event" @change="search" />
          </el-form-item>
          <el-form-item :label="$t('monitor.columns.endTime')" style="margin-bottom: 5px;">
            <date-time-generator @update="page.lowerEndTime = $event" @change="search" />
          </el-form-item>
          <el-form-item>
            <date-time-generator @update="page.upperEndTime = $event" @change="search" />
          </el-form-item>
        </el-form>
        <el-button slot="reference" class="filter-item" style="float: right;" type="primary">{{ $t('actions.highSearch') }}</el-button>
      </el-popover>
    </div>

    <el-table
      ref="tables"
      v-loading="listLoading"
      :data="list"
      row-key="rowKey"
      border
      fit
      :tree-props="{children: 'children', hasChildren: 'hasChildren'}"
      highlight-current-row
      style="width: 100%;"
      @selection-change="handleSelectionChange"
      @sort-change="sortChange"
      @cell-click="clickCell"
    >
      <el-table-column
        type="selection"
        width="40"
      />
      <el-table-column :label="$t('dispatch.job.columns.jobName')" show-overflow-tooltip width="110px">
        <template slot-scope="scope">
          <el-link type="primary" @click="openTrace(scope.row)">{{ scope.row.jobName }}</el-link>
        </template>
      </el-table-column>
      <el-table-column :label="$t('columns.displayName')" width="120px">
        <template slot-scope="scope">
          <el-popover trigger="hover" placement="top">
            <p>{{ scope.row.displayName || scope.row.jobName }}</p>
            <div slot="reference">
              <span class="single-line">{{ scope.row.displayName || scope.row.jobName }}</span>
            </div>
          </el-popover>
        </template>
      </el-table-column>
      <el-table-column :label="$t('columns.status')" align="center" class-name="status-col" width="100px">
        <template slot-scope="scope">
          <span v-show="!scope.row.triggerName"><svg-icon icon-class="hand" /></span>
          <el-tag :type="scope.row.status | renderJobInstanceStatus">{{ formatStatus(scope.row.status) }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column :label="$t('monitor.columns.startTime')" width="160px" align="center">
        <template slot-scope="scope">
          <span>{{ scope.row.startTime }}</span>
        </template>
      </el-table-column>
      <el-table-column :label="$t('monitor.columns.endTime')" width="160px" align="center">
        <template slot-scope="scope">
          <span>{{ scope.row.endTime }}</span>
        </template>
      </el-table-column>
      <el-table-column :label="$t('monitor.columns.duration')" min-width="80px" align="center">
        <template slot-scope="scope">
          <span>{{ scope.row.duration === 0 ? 0 : (scope.row.duration | parseDuration) }}</span>
        </template>
      </el-table-column>
      <el-table-column :label="$t('monitor.columns.msg')" min-width="100px">
        <template slot-scope="scope">
          <el-popover trigger="hover" placement="left">
            <p>{{ scope.row.code }}</p>
            <p>{{ scope.row.msg }}</p>
            <div slot="reference">
              <div>{{ scope.row.code }}</div>
              <div class="single-line">{{ scope.row.msg }}</div>
            </div>
          </el-popover>
        </template>
      </el-table-column>
    </el-table>

    <pagination
      v-show="page.total>0"
      :total="page.total"
      :page.sync="page.currentPage"
      :limit.sync="page.pageSize"
      @pagination="search"
    />
  </div>
</template>

<script>
import { listReq, retryReq, terminateReq, pauseReq, activateReq } from '@/api/dispatch/instance'
import { load } from '@/constant'
import Pagination from '@/components/Pagination'
import waves from '@/directive/waves'
import DateTimeGenerator from '@/components/DateTimeGenerator'

export default {
  name: 'Monitor',
  components: {
    DateTimeGenerator,
    Pagination
  },
  directives: { waves },
  props: {
    monitorType: {
      type: String,
      default: 'realTime'
    }
  },
  data() {
    return {
      list: null,
      listLoading: true,
      page: {
        currentPage: 1,
        pageSize: 10,
        total: 0,
        jobName: undefined,
        displayName: undefined,
        status: [],
        sort: 'START_TIME DESC',
        listChildren: true
      },
      currentJobInstanceStatuses: [],
      runningJobInstanceStatus: [],
      pausedJobInstanceStatus: [],
      doingJobInstanceStatuses: [],
      doneJobInstanceStatuses: [],
      jobInstanceStatuses: [],
      jobInstance: {
        id: undefined,
        jobName: null,
        displayName: '',
        status: 0,
        startTime: null,
        duration: 0
      },
      downloadLoading: false,
      selections: []
    }
  },
  computed: {
    retryVisible() {
      return this.doneJobInstanceStatuses.find(item => { return item.value === this.selections[0].status }) !== undefined
    },
    terminateVisible() {
      return this.doingJobInstanceStatuses.find(item => { return item.value === this.selections[0].status }) !== undefined
    },
    pauseOrActivateVisible() {
      return this.runningJobInstanceStatus.find(item => { return item.value === this.selections[0].status }) !== undefined ||
        this.pausedJobInstanceStatus.find(item => { return item.value === this.selections[0].status }) !== undefined
    },
    canPause() {
      return this.runningJobInstanceStatus.find(item => { return item.value === this.selections[0].status }) !== undefined
    }
  },
  created() {
    this.loadConst()
  },
  methods: {
    retry() {
      this.handleRequest(retryReq)
    },
    terminate() {
      this.handleRequest(terminateReq)
    },
    pauseOrActivate() {
      if (this.canPause) {
        this.handleRequest(pauseReq)
      } else {
        this.handleRequest(activateReq)
      }
    },
    handleRequest(request) {
      this.$confirm(this.$t('tip.confirmMsg'), this.$t('tip.confirm'), { type: 'info' })
        .then(() => {
          request({ id: this.jobInstance.id })
            .then(res => {
              this.$message.success(res.data.msg)
              this.search()
            })
            .catch(() => {
              this.search()
            })
        })
    },
    search() {
      this.listLoading = true
      this.initPageStatus()
      setTimeout(() => {
        listReq(this.page).then(res => {
          this.list = res.data.result.list
          Object.assign(this.page, res.data.result.page)
          this.listLoading = false
        })
      }, 300)
    },
    sortChange(data) {
      const { prop, order } = data
      if (prop === 'startTime') {
        this.sortByName(order)
      }
    },
    sortByName(order) {
      if (order === 'ascending') {
        this.page.sort = 'START_TIME'
      } else {
        this.page.sort = 'START_TIME DESC'
      }
      this.search()
    },
    formatStatus(item) {
      for (let i = 0; i < this.currentJobInstanceStatuses.length; i++) {
        const option = this.currentJobInstanceStatuses[i]
        if (option.value === item) {
          return option.label
        }
      }
      return item
    },
    selectRow(row) {
      this.$refs.tables.clearSelection()
      if (row && row.id) {
        this.$refs.tables.toggleRowSelection(row, true)
      }
      this.reset() // get the newest or reset to origin
    },
    reset() {
      this.jobInstance = Object.assign({}, this.selections[0])
    },
    handleSelectionChange(val) {
      this.selections = val
    },
    initPageStatus() {
      if (this.page.status || this.page.status.length === 0) {
        this.page.status = this.currentJobInstanceStatuses.map(item => item.value)
      }
    },
    openTrace(row) {
      const route = {
        name: 'trace',
        params: {
          key: row.jobName,
          rootProcInstId: row.rootProcInstId,
          procDefId: row.procDefId
        }
      }
      this.$router.push(route)
    },
    clickCell(row, column, cell, event) {
      this.selectRow(row)
    },
    loadConst() {
      load(`./array/${localStorage.getItem('language')}.js`).then((array) => {
        this.runningJobInstanceStatus = array.runningJobInstanceStatus
        this.pausedJobInstanceStatus = array.pausedJobInstanceStatus
        this.doingJobInstanceStatuses = array.doingJobInstanceStatuses
        this.doneJobInstanceStatuses = array.doneJobInstanceStatuses
        this.jobInstanceStatuses = array.jobInstanceStatuses
        if (this.monitorType === 'realTime') {
          this.currentJobInstanceStatuses = this.doingJobInstanceStatuses
        } else if (this.monitorType === 'history') {
          this.currentJobInstanceStatuses = this.doneJobInstanceStatuses
        } else if (this.monitorType === 'total') {
          this.currentJobInstanceStatuses = this.jobInstanceStatuses
        } else {
          this.currentJobInstanceStatuses = this.jobInstanceStatuses
        }
        this.search()
      })
    }
  }
}
</script>
