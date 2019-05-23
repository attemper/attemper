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
      <el-table-column :label="$t('dispatch.job.columns.jobName')" min-width="100px">
        <template slot-scope="scope">
          <el-link type="primary" @click="openTrace(scope.row)">{{ scope.row.jobName || '-' }}</el-link>
        </template>
      </el-table-column>
      <el-table-column :label="$t('columns.displayName')" min-width="150px">
        <template slot-scope="scope">
          <span>{{ scope.row.displayName }}</span>
        </template>
      </el-table-column>
      <el-table-column :label="$t('columns.status')" align="center" class-name="status-col" width="100px">
        <template slot-scope="scope">
          <el-tag :type="scope.row.status | renderJobInstanceStatus">{{ formatStatus(scope.row.status) }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column :label="$t('monitor.columns.startTime')" min-width="90px" align="center">
        <template slot-scope="scope">
          <span>{{ scope.row.startTime }}</span>
        </template>
      </el-table-column>
      <el-table-column :label="$t('monitor.columns.endTime')" min-width="90px" align="center">
        <template slot-scope="scope">
          <span>{{ scope.row.endTime }}</span>
        </template>
      </el-table-column>
      <el-table-column :label="$t('monitor.columns.duration')" min-width="80px" align="center">
        <template slot-scope="scope">
          <span>{{ scope.row.duration | parseDuration }}</span>
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
import { listReq } from '@/api/monitor/monitor'
import { load } from '@/constant'
import common from './mixins/common'

export default {
  name: 'total',
  mixins: [common],
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
    loadConst() {
      load(`./array/${localStorage.getItem('language')}.js`).then((array) => {
        this.jobInstanceStatuses = array.jobInstanceStatuses
        this.search()
      })
    }
  }
}
</script>
