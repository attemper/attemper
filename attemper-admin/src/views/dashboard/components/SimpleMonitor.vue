<template>
  <el-table
    :data="list"
    border
    fit
    highlight-current-row
    style="width: 100%;"
  >
    <el-table-column :label="$t('dispatch.job.columns.jobName')" show-overflow-tooltip width="110px">
      <template slot-scope="scope">
        <el-link type="primary" @click="openTrace(scope.row)">{{ scope.row.jobName }}</el-link>
      </template>
    </el-table-column>
    <el-table-column :label="$t('columns.displayName')" min-width="120px">
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
    <el-table-column :label="$t('columns.startTime')" width="160px" align="center">
      <template slot-scope="scope">
        <span>{{ scope.row.startTime }}</span>
      </template>
    </el-table-column>
    <el-table-column :label="$t('columns.endTime')" width="160px" align="center">
      <template slot-scope="scope">
        <span>{{ scope.row.endTime }}</span>
      </template>
    </el-table-column>
    <el-table-column :label="$t('monitor.columns.duration')" width="100px" align="center">
      <template slot-scope="scope">
        <span>{{ scope.row.duration | parseDuration }}</span>
      </template>
    </el-table-column>
  </el-table>
</template>

<script>
import { listReq } from '@/api/dispatch/instance'
export default {
  data() {
    return {
      list: null,
      listLoading: false,
      page: {
        currentPage: 1,
        pageSize: 5,
        total: 0,
        status: [],
        sort: 'START_TIME DESC'
      },
      jobInstanceStatuses: [],
      currentJobInstanceStatuses: []
    }
  },
  created() {
    this.loadConst()
  },
  methods: {
    search() {
      this.listLoading = true
      this.initPageStatus()
      setTimeout(() => {
        listReq(this.page).then(res => {
          this.list = res.data.result.list
          this.listLoading = false
        })
      }, 300)
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
    loadConst() {
      import(`@/constant/array/${localStorage.getItem('language')}.js`).then((array) => {
        this.jobInstanceStatuses = array.jobInstanceStatuses
        this.currentJobInstanceStatuses = this.jobInstanceStatuses
        this.search()
      })
    }
  }
}
</script>

