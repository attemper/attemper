<template>
  <div class="app-container">
    <div class="filter-container">
      <el-input v-model="page.jobName" :placeholder="$t('dispatch.job.columns.jobName')" class="filter-item search-input" @keyup.enter.native="search" />
      <el-input v-model="page.displayName" :placeholder="$t('columns.displayName')" class="filter-item search-input" @keyup.enter.native="search" />
      <el-select v-model="page.status" :placeholder="$t('columns.status')" multiple clearable collapse-tags class="filter-item search-select">
        <el-option v-for="item in jobInstanceStatuses" :key="item.value" :label="item.label" :value="item.value" />
      </el-select>
      <!--<el-select v-model="page.sort" style="width: 140px" class="filter-item" @change="search">
        <el-option v-for="item in sortOptions" :key="item.key" :label="item.label" :value="item.key"/>
      </el-select>-->
      <el-button v-waves class="filter-item" type="primary" icon="el-icon-search" @click="search">{{ $t('actions.search') }}</el-button>
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
      border
      fit
      highlight-current-row
      style="width: 100%;"
      @selection-change="handleSelectionChange"
      @sort-change="sortChange"
    >
      <el-table-column
        type="selection"
        width="40"
      />
      <el-table-column :label="$t('dispatch.job.columns.jobName')" :show-overflow-tooltip="true" width="100px">
        <template slot-scope="scope">
          <el-link type="primary" @click="openTrace(scope.row)">{{ scope.row.jobName || '-' }}</el-link>
        </template>
      </el-table-column>
      <el-table-column :label="$t('columns.displayName')" :show-overflow-tooltip="true" width="120px">
        <template slot-scope="scope">
          <span>{{ scope.row.displayName }}</span>
        </template>
      </el-table-column>
      <el-table-column :label="$t('columns.status')" align="center" class-name="status-col" width="100px">
        <template slot-scope="scope">
          <el-tag :type="scope.row.status | renderJobInstanceStatus">{{ formatStatus(scope.row.status) }}</el-tag>
          <span v-show="!scope.row.triggerName"><svg-icon icon-class="hand" /></span>
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
          <span>{{ scope.row.duration | parseDuration }}</span>
        </template>
      </el-table-column>
      <el-table-column :label="$t('monitor.columns.msg')" :show-overflow-tooltip="true" min-width="100px">
        <template slot-scope="scope">
          <div>{{ scope.row.code }}</div>
          <div>{{ scope.row.msg }}</div>
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
import { listReq } from '@/api/dispatch/instance'
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
        sort: 'START_TIME DESC'
      },
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
  created() {
    this.loadConst()
  },
  methods: {
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
      for (let i = 0; i < this.jobInstanceStatuses.length; i++) {
        const option = this.jobInstanceStatuses[i]
        if (option.value === item) {
          return option.label
        }
      }
      return item
    },
    selectRow(row) {
      this.$refs.tables.clearSelection()
      if (row && row.jobName) {
        this.$refs.tables.toggleRowSelection(row, true)
      }
      this.reset() // get the newest or reset to origin
    },
    handleSelectionChange(val) {
      this.selections = val
    },
    initPageStatus() {
      if (this.page.status || this.page.status.length === 0) {
        this.page.status = this.jobInstanceStatuses.map(item => item.value)
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
    search() {
      this.listLoading = true
      this.initPageStatus()
      listReq(this.page).then(response => {
        this.list = response.data.result.list
        Object.assign(this.page, response.data.result.page)
        // Just to simulate the time of the request
        setTimeout(() => {
          this.listLoading = false
        }, 200)
      })
    },
    loadConst() {
      load(`./array/${localStorage.getItem('language')}.js`).then((array) => {
        if (this.monitorType === 'realTime') {
          this.jobInstanceStatuses = array.todoJobInstanceStatuses
        } else if (this.monitorType === 'history') {
          this.jobInstanceStatuses = array.doneJobInstanceStatuses
        } else if (this.monitorType === 'total') {
          this.jobInstanceStatuses = array.jobInstanceStatuses
        } else {
          this.jobInstanceStatuses = array.jobInstanceStatuses
        }
        this.search()
      })
    }
  }
}
</script>
