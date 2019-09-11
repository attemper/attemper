<template>
  <div class="app-container">
    <div class="filter-container">
      <el-input v-model="page.jobName" :placeholder="$t('dispatch.job.columns.jobName')" class="filter-item search-input" @keyup.enter.native="search" />
      <el-input v-model="page.displayName" :placeholder="$t('columns.displayName')" class="filter-item search-input" @keyup.enter.native="search" />
      <el-select v-model="page.status" :placeholder="$t('columns.status')" multiple clearable collapse-tags class="filter-item search-select">
        <el-option v-for="item in instanceStatuses" :key="item.value" :label="item.label" :value="item.value" />
      </el-select>
      <el-button v-waves class="filter-item" type="primary" icon="el-icon-search" @click="search" />
      <span v-if="selections.length === 1">
        <el-button v-if="retryVisible" v-waves class="filter-item" type="warning" icon="el-icon-s-operation" @click="openParam">{{ $t('actions.retry') }}</el-button>
        <el-button v-if="terminateVisible" v-waves class="filter-item" type="danger" icon="el-icon-stopwatch" @click="terminate">{{ $t('actions.terminate') }}</el-button>
      </span>
      <div style="float: right">
        <el-popover
          placement="bottom"
          trigger="hover"
        >
          <el-button v-waves :loading="downloadLoading" class="filter-item" type="primary" icon="el-icon-download" @click="handleDownload">{{ $t('actions.exportList') }}</el-button>
          <el-button slot="reference" class="filter-item table-external-button" type="warning">{{ $t('actions.highOperation') }}</el-button>
        </el-popover>
        <el-popover
          placement="bottom"
          trigger="hover"
        >
          <el-form
            label-position="left"
            label-width="100px"
            style="height: 100%"
          >
            <el-form-item :label="$t('columns.startTime')" style="margin-bottom: 5px;">
              <date-time-generator @update="page.lowerStartTime = $event" @change="search" />
            </el-form-item>
            <el-form-item>
              <date-time-generator @update="page.upperStartTime = $event" @change="search" />
            </el-form-item>
            <el-form-item :label="$t('columns.endTime')" style="margin-bottom: 5px;">
              <date-time-generator @update="page.lowerEndTime = $event" @change="search" />
            </el-form-item>
            <el-form-item>
              <date-time-generator @update="page.upperEndTime = $event" @change="search" />
            </el-form-item>
          </el-form>
          <el-button slot="reference" class="filter-item table-external-button" type="primary">{{ $t('actions.highSearch') }}</el-button>
        </el-popover>
      </div>
    </div>

    <el-table
      ref="tables"
      v-loading="listLoading"
      :data="list"
      row-key="id"
      border
      fit
      :tree-props="{children: 'children', hasChildren: 'hasChildren'}"
      highlight-current-row
      style="width: 100%;"
      lazy
      :load="load"
      @selection-change="handleSelectionChange"
      @sort-change="sortChange"
      @cell-click="clickCell"
    >
      <el-table-column
        type="selection"
        width="45"
      />
      <el-table-column :label="$t('dispatch.job.columns.jobName')" show-overflow-tooltip min-width="120px">
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
          <el-tag :type="scope.row.status | renderInstanceStatus">{{ formatStatus(scope.row.status) }}</el-tag>
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
      <el-table-column :label="$t('monitor.columns.duration')" min-width="80px" align="center">
        <template slot-scope="scope">
          <span>{{ scope.row.duration | parseDuration }}</span>
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

    <el-dialog
      :title="editDialog.title"
      :visible.sync="editDialog.param.visible "
      :center="true"
      :modal="true"
      :close-on-click-modal="false"
      :close-on-press-escape="false"
      :before-close="close"
    >
      <div v-show="editDialog.param.visible">
        <code-editor v-model="jsonData" :read-only="false" extension=".json" />
        <div style="text-align: center; margin-top: 10px">
          <el-button type="danger" @click="retry">
            <svg-icon icon-class="hand" />{{ $t('actions.manual') }}
          </el-button>
        </div>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { listReq, listRetryReq, listChildrenReq, listRetryChildrenReq, retryReq, terminateReq, getArgsReq } from '@/api/instance/instance'
import { getTimeStr } from '@/utils/tools'
import Pagination from '@/components/Pagination'
import waves from '@/directive/waves'
import DateTimeGenerator from '@/components/DateTimeGenerator'
import CodeEditor from '@/components/CodeEditor'

export default {
  name: 'Monitor',
  components: {
    DateTimeGenerator,
    CodeEditor,
    Pagination
  },
  directives: { waves },
  props: {
    monitorType: {
      type: String,
      default: 'total'
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
      doingInstanceStatuses: [],
      doneInstanceStatuses: [],
      instanceStatuses: [],
      instance: null,
      downloadLoading: false,
      selections: [],
      jsonData: null,
      editDialog: {
        title: undefined,
        param: {
          visible: false
        }
      }
    }
  },
  computed: {
    retryVisible() {
      return this.doneInstanceStatuses.find(item => { return item.value === this.selections[0].status }) !== undefined
    },
    terminateVisible() {
      return this.doingInstanceStatuses.find(item => { return item.value === this.selections[0].status }) !== undefined
    }
  },
  created() {
    this.loadConst()
  },
  methods: {
    openParam() {
      this.jsonData = null
      getArgsReq({ procInstId: this.instance.procInstId, actInstId: this.instance.procInstId }).then(res => {
        if (!res.data.result) {
          this.retry()
        } else {
          this.editDialog.title = this.$t('dispatch.job.actions.param')
          this.editDialog.param.visible = true
          this.jsonData = JSON.parse(res.data.result)
        }
      })
    },
    retry() {
      this.$confirm(this.$t('tip.confirmMsg'), this.$t('tip.confirm'), { type: 'info' })
        .then(() => {
          retryReq({
            procInstId: this.instance.procInstId,
            jsonData: (!this.jsonData ? null : (typeof this.jsonData === 'string' ? JSON.parse(this.jsonData) : this.jsonData))
          })
            .then(res => {
              this.editDialog.param.visible = false
              this.$message.success(res.data.msg)
              this.search()
            })
        })
    },
    terminate() {
      this.$confirm(this.$t('tip.confirmMsg'), this.$t('tip.confirm'), { type: 'info' })
        .then(() => {
          terminateReq({ procInstId: this.instance.procInstId })
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
      this.list = []
      this.listLoading = true
      this.initPageStatus()
      let request
      if (this.monitorType === 'retry') {
        request = listRetryReq
      } else {
        request = listReq
      }
      setTimeout(() => {
        request(this.page).then(res => {
          this.list = res.data.result.list
          Object.assign(this.page, res.data.result.page)
          this.listLoading = false
        })
      }, 300)
    },
    load(row, treeNode, resolve) {
      this.selectRow(row)
      let request
      if (this.monitorType === 'retry') {
        request = listRetryChildrenReq
      } else {
        request = listChildrenReq
      }
      request(this.instance).then(res => {
        resolve(res.data.result)
      })
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
      for (let i = 0; i < this.instanceStatuses.length; i++) {
        const option = this.instanceStatuses[i]
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
      this.instance = Object.assign({}, this.selections[0])
    },
    handleSelectionChange(val) {
      this.selections = val
      if (this.selections.length === 1) {
        this.reset()
      }
    },
    initPageStatus() {
      if (this.page.status.length === 0) {
        this.page.status = this.instanceStatuses.map(item => item.value)
      }
    },
    openTrace(row) {
      if (!row.procInstId) {
        this.$message.info(this.$t('tip.flowNotStart'))
        return
      }
      const route = {
        name: 'trace',
        params: {
          key: row.jobName,
          procInstId: row.procInstId,
          procDefId: row.procDefId
        }
      }
      this.$router.push(route)
    },
    clickCell(row, column, cell, event) {
      this.selectRow(row)
    },
    handleDownload() {
      this.downloadLoading = true
      import('@/vendor/Export2Excel').then(excel => {
        const translatedHeader = [
          this.$t('dispatch.job.columns.jobName'),
          this.$t('columns.displayName'),
          this.$t('columns.status'),
          this.$t('columns.startTime'),
          this.$t('columns.endTime'),
          this.$t('monitor.columns.duration'),
          'code',
          'msg'
        ]
        const columnNames = ['jobName', 'displayName', 'status', 'startTime', 'endTime', 'duration', 'code', 'msg']
        const data = this.formatJson(columnNames, this.list)
        excel.export_json_to_excel({
          header: translatedHeader,
          data,
          filename: 'instance_' + getTimeStr()
        })
        this.downloadLoading = false
      })
    },
    formatJson(filterVal, jsonData) {
      return jsonData.map(v => filterVal.map(j => {
        if (j === 'status') {
          return this.formatStatus(v[j])
        } else {
          return v[j]
        }
      }))
    },
    close() {
      this.editDialog.param.visible = false
    },
    loadConst() {
      import(`@/constant/array/${localStorage.getItem('language')}.js`).then((array) => {
        this.doingInstanceStatuses = array.doingInstanceStatuses
        this.doneInstanceStatuses = array.doneInstanceStatuses
        this.instanceStatuses = array.instanceStatuses
        this.search()
      })
    }
  }
}
</script>
