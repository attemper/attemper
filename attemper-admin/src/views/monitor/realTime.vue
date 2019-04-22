<template>
  <div class="app-container">
    <div class="filter-container">
      <el-input v-model="page.jobName" :placeholder="$t('job.columns.jobName')" style="width: 100px;" class="filter-item" size="mini" @keyup.enter.native="search" />
      <el-input v-model="page.displayName" :placeholder="$t('job.columns.displayName')" style="width: 100px;" class="filter-item" size="mini" @keyup.enter.native="search" />
      <el-select v-model="page.status" :placeholder="$t('monitor.columns.status')" multiple clearable collapse-tags class="filter-item" size="mini" style="width: 160px">
        <el-option v-for="item in jobInstanceStatuses" :key="item.value" :label="item.label" :value="item.value" />
      </el-select>
      <!--<el-select v-model="page.sort" style="width: 140px" class="filter-item" @change="search">
        <el-option v-for="item in sortOptions" :key="item.key" :label="item.label" :value="item.key"/>
      </el-select>-->
      <el-button v-waves class="filter-item" size="mini" type="primary" icon="el-icon-search" @click="search">{{ $t('actions.search') }}</el-button>
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
      <el-table-column :label="$t('job.columns.jobName')" min-width="100px">
        <template slot-scope="scope">
          <span>{{ scope.row.jobName }}</span>
        </template>
      </el-table-column>
      <el-table-column :label="$t('job.columns.displayName')" min-width="150px">
        <template slot-scope="scope">
          <span>{{ scope.row.displayName }}</span>
        </template>
      </el-table-column>
      <el-table-column :label="$t('monitor.columns.status')" align="center" class-name="status-col" width="100px">
        <template slot-scope="scope">
          <el-tag :type="scope.row.status | statusFilter">{{ formatStatus(scope.row.status) }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column :label="$t('monitor.columns.startTime')" min-width="110px" align="center">
        <template slot-scope="scope">
          <span>{{ scope.row.startTime }}</span>
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
import { listReq } from '@/api/monitor/realTime'
import waves from '@/directive/waves' // Waves directive
// import { parseTime } from '@/utils'
import Pagination from '@/components/Pagination' // Secondary package based on el-pagination
import { load } from '@/constant'
import Cookies from 'js-cookie'

export default {
  name: 'RealTimeMonitor',
  components: {
    Pagination
  },
  directives: { waves },
  filters: {
    statusFilter(item) {
      const map = {
        0: 'primary',
        1: 'success',
        2: 'danger',
        3: 'warning',
        4: 'info'
      }
      return map[item]
    }
  },
  data() {
    return {
      list: null,
      listLoading: true,
      page: {
        currentPage: 1,
        pageSize: 20,
        total: 0,
        jobName: undefined,
        displayName: undefined,
        status: [],
        sort: 'JOB_NAME'
      },
      jobInstanceStatuses: [],
      /* sortOptions: [{ label: this.$t('job.sort.nameAsc'), key: 'JOB_NAME' }, { label: this.$t('job.sort.nameDesc'), key: 'JOB_NAME DESC' }],*/
      showCreateTime: false,
      jobInstance: {
        id: undefined,
        jobName: null,
        displayName: '',
        status: 0,
        startTime: null,
        duration: '0'
      },
      downloadLoading: false,
      selections: []
    }
  },
  created() {
    this.loadConst()
    this.search()
  },
  methods: {
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
    sortChange(data) {
      const { prop, order } = data
      if (prop === 'jobName') {
        this.sortByName(order)
      }
    },
    sortByName(order) {
      if (order === 'ascending') {
        this.page.sort = 'JOB_NAME'
      } else {
        this.page.sort = 'JOB_NAME DESC'
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
      if (row && (this.editDialog.oper === 'update' || row.jobName)) {
        this.$refs.tables.toggleRowSelection(row, true)
      }
      this.reset() // get the newest or reset to origin
    },
    handleSelectionChange(val) {
      this.selections = val
    },
    loadConst() {
      load(`./array/${Cookies.get('language')}.js`).then((array) => {
        this.jobInstanceStatuses = array.todoJobInstanceStatuses
        this.initPageStatus()
      })
    },
    initPageStatus() {
      if (this.page.status && this.page.status.length === 0) {
        this.page.status = this.jobInstanceStatuses.map(item => item.value)
      }
    }
  }
}
</script>
