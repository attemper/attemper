<template>
  <div class="app-container">
    <div class="filter-container">
      <el-input v-model="page.jobName" :placeholder="$t('job.columns.jobName')" style="width: 100px;" class="filter-item" size="mini" @keyup.enter.native="search" />
      <el-input v-model="page.displayName" :placeholder="$t('job.columns.displayName')" style="width: 100px;" class="filter-item" size="mini" @keyup.enter.native="search" />
      <el-select v-model="page.status" :placeholder="$t('job.columns.status')" multiple clearable collapse-tags class="filter-item" size="mini" style="width: 160px">
        <el-option v-for="item in jobStatuses" :key="item.value" :label="item.text" :value="item.value" />
      </el-select>
      <!--<el-select v-model="page.sort" style="width: 140px" class="filter-item" @change="search">
        <el-option v-for="item in sortOptions" :key="item.key" :label="item.label" :value="item.key"/>
      </el-select>-->
      <el-button v-waves class="filter-item" size="mini" type="primary" icon="el-icon-search" @click="search">{{ $t('actions.search') }}</el-button>
      <el-button class="filter-item table-external-button" size="mini" type="success" icon="el-icon-plus" @click="add">{{ $t('actions.add') }}</el-button>
      <el-button :disabled="!selections || !selections.length" class="filter-item table-external-button" size="mini" type="danger" icon="el-icon-delete" @click="remove">{{ $t('actions.remove') }}</el-button>
      <el-button v-waves :disabled="!selections || !selections.length" class="filter-item table-external-button" size="mini" type="primary" @click="publish">
        <svg-icon icon-class="publish" /> {{ $t('table.publish') }}
      </el-button>
      <el-button style="float: right" :disabled="!selections || !selections.length" class="filter-item table-external-button" size="mini" type="danger" @click="manual">
        <svg-icon icon-class="hand" />{{ $t('actions.manual') }}
      </el-button>
      <!--<el-button v-waves :loading="downloadLoading" class="filter-item" type="primary" icon="el-icon-download" @click="handleDownload">{{ $t('actions.export') }}</el-button>
      <el-checkbox v-model="showCreateTime" class="filter-item" style="margin-left:15px;" @change="tableKey=tableKey+1">{{ $t('job.columns.createTime') }}</el-checkbox>-->
    </div>

    <el-table
      ref="tables"
      :key="tableKey"
      v-loading="listLoading"
      :data="list"
      border
      fit
      highlight-current-row
      style="width: 100%;"
      @selection-change="handleSelectionChange"
      @sort-change="sortChange"
    >
      <el-table-column type="expand">
        <template slot-scope="props">
          <el-form label-position="left" inline class="table-expand">
            <el-form-item :label="$t('job.columns.createTime')">
              <span>{{ props.row.createTime }}</span>
            </el-form-item>
            <el-form-item :label="$t('job.columns.updateTime')">
              <span>{{ props.row.updateTime }}</span>
            </el-form-item>
            <el-form-item :label="$t('job.columns.deploymentTime')">
              <span>{{ props.row.deploymentTime }}</span>
            </el-form-item>
          </el-form>
        </template>
      </el-table-column>
      <el-table-column
        type="selection"
        width="40"
      />
      <el-table-column :label="$t('job.columns.version')" align="center" width="80px">
        <template slot-scope="scope">
          <el-button :type="scope.row.maxVersion ? 'success' : 'info'" size="mini" @click="openDesignDialog(scope.row)">{{ scope.row.maxVersion || '-' }}</el-button>
        </template>
      </el-table-column>
      <el-table-column
        :label="$t('job.columns.jobName')"
        prop="jobName"
        sortable="custom"
        align="center"
        min-width="100px"
      >
        <template slot-scope="scope">
          <el-tag type="primary" @click="update(scope.row)">{{ scope.row.jobName || '-' }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column :label="$t('job.columns.displayName')" min-width="150px">
        <template slot-scope="scope">
          <span>{{ scope.row.displayName }}</span>
        </template>
      </el-table-column>
      <el-table-column :label="$t('job.columns.status')" align="center" class-name="status-col" width="100">
        <template slot-scope="scope">
          <el-tag :type="scope.row.status | statusFilter">{{ formatStatus(scope.row.status) }}</el-tag>
        </template>
      </el-table-column>
      <!--<el-table-column v-if="showCreateTime" :label="$t('job.columns.createTime')" width="110px" align="center">
        <template slot-scope="scope">
          <span>{{ scope.row.createTime }}</span>
        </template>
      </el-table-column>-->
      <el-table-column :label="$t('actions.handle')" align="center" width="230" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <div style="padding-top: 6px;">
            <el-button type="primary" size="mini">{{
              $t('job.actions.param') }}
            </el-button>
            <el-button
              type="success"
              size="mini"
              @click="openTriggerDialog(scope.row)"
            >{{ $t('job.actions.trigger') }}
            </el-button>
            <el-button
              type="info"
              size="mini"
              @click="openProjectDialog(scope.row)"
            >{{ $t('job.actions.project') }}
            </el-button>
          </div>
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
      :visible.sync="editDialog.base.visible || editDialog.param.visible || editDialog.trigger.visible || editDialog.project.visible"
      :center="true"
      :modal="true"
      :close-on-click-modal="false"
      :close-on-press-escape="false"
      :before-close="close"
    >
      <div v-show="editDialog.base.visible">
        <job-info-form ref="jobInfoForm" :job="job" @save="save" @cancel="editDialog.base.visible = false" />
      </div>
      <div v-show="editDialog.trigger.visible">
        <!--
        <el-tabs type="border-card">
          <el-tab-pane>
            <span slot="label">
              <svg-icon icon-class="time" /> {{ $t('job.trigger.tab.time.title') }}
            </span>
            -->
        <el-tabs v-model="triggerTab.timeTrigger.activeTabName" tab-position="left">
          <el-tab-pane :label="$t('job.trigger.tab.time.cron')" name="0">
            <CronTrigger ref="cronTrigger" :init-trigger-array="trigger.cronTriggers" :time-zones="timeZones" />
          </el-tab-pane>
          <el-tab-pane :label="$t('job.trigger.tab.time.calendarOffset')" name="1">
            <CalendarOffsetTrigger ref="calendarOffsetTrigger" :init-trigger-array="trigger.calendarOffsetTriggers" :over-day-time-units="overDayTimeUnits" />
          </el-tab-pane>
          <el-tab-pane :label="$t('job.trigger.tab.time.dailyInterval')" name="2">
            <DailyIntervalTrigger
              ref="dailyIntervalTrigger"
              :init-trigger-array="trigger.dailyIntervalTriggers"
              :milli-second-time-units="milliSecondTimeUnits"
              :in-day-time-units="inDayTimeUnits"
              :days-of-week="daysOfWeek"
            />
          </el-tab-pane>
          <el-tab-pane :label="$t('job.trigger.tab.time.calendarInterval')" name="3">
            <CalendarIntervalTrigger
              ref="calendarIntervalTrigger"
              :init-trigger-array="trigger.calendarIntervalTriggers"
              :day-time-unit="dayTimeUnit"
              :in-day-time-units="inDayTimeUnits"
              :over-day-time-units="overDayTimeUnits"
              :time-zones="timeZones"
            />
          </el-tab-pane>
        </el-tabs>
        <!--
        </el-tab-pane>
        <el-tab-pane>
          <span slot="label">
            <svg-icon icon-class="event" /> {{ $t('job.trigger.tab.event.title') }}
          </span>
          event
        </el-tab-pane>
      </el-tabs>
      -->
        <el-row style="margin-top: 20px;">
          <el-col :span="4" :offset="8">
            <el-button type="info" @click="editDialog.trigger.visible = false">{{ $t('actions.cancel') }}</el-button>
          </el-col>
          <el-tooltip :disabled="job.maxVersion > 0" :content="this.$t('job.trigger.tip.jobNotPublished') " placement="right" effect="light">
            <el-col :span="4" :offset="1">
              <el-button :disabled="!job.maxVersion" type="success" @click="saveTrigger">{{ $t('actions.use') }}</el-button>
            </el-col>
          </el-tooltip>
        </el-row>
      </div>
      <div v-show="editDialog.project.visible">
        <ProjectTree ref="projectTree" :job-name="job.jobName" @cancel="editDialog.project.visible = false" />
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { listReq, removeReq, addReq, updateReq, publishReq, manualReq } from '@/api/job/flowJob'
import * as triggerApi from '@/api/job/trigger'
import * as toolApi from '@/api/sys/tool'
import waves from '@/directive/waves' // Waves directive
// import { parseTime } from '@/utils'
import Pagination from '@/components/Pagination' // Secondary package based on el-pagination
import { load } from '@/constant'
import JobInfoForm from './components/jobInfoForm'
import Cookies from 'js-cookie'
import CronTrigger from './components/cronTrigger'
import CalendarOffsetTrigger from './components/calendarOffsetTrigger'
import DailyIntervalTrigger from './components/dailyIntervalTrigger'
import CalendarIntervalTrigger from './components/calendarIntervalTrigger'
import ProjectTree from './components/projectTree'

export default {
  name: 'flowJob',
  components: {
    ProjectTree,
    Pagination,
    JobInfoForm,
    CronTrigger,
    CalendarOffsetTrigger,
    DailyIntervalTrigger,
    CalendarIntervalTrigger
  },
  directives: { waves },
  filters: {
    statusFilter(item) {
      const map = {
        0: 'success',
        1: 'danger',
        2: 'warning',
        3: ''
      }
      return map[item]
    }
  },
  data() {
    return {
      tableKey: 0,
      list: null,
      listLoading: true,
      page: {
        currentPage: 1,
        pageSize: 10,
        total: 0,
        jobName: undefined,
        displayName: undefined,
        status: [0],
        sort: 'JOB_NAME'
      },
      jobStatuses: [],
      milliSecondTimeUnits: [],
      inDayTimeUnits: [],
      dayTimeUnit: [],
      overDayTimeUnits: [],
      daysOfWeek: [],
      /* sortOptions: [{ label: this.$t('job.sort.nameAsc'), key: 'JOB_NAME' }, { label: this.$t('job.sort.nameDesc'), key: 'JOB_NAME DESC' }],*/
      showCreateTime: false,
      job: {
        jobName: undefined,
        displayName: '',
        status: 0,
        remark: '',
        jobContent: ''
      },
      editDialog: {
        oper: undefined,
        title: undefined,
        base: {
          visible: false
        },
        param: {
          visible: false
        },
        trigger: {
          visible: false
        },
        project: {
          visible: false
        }
      },
      downloadLoading: false,
      selections: [],
      // calendars: [],
      triggerTab: {
        timeTrigger: {
          activeTabName: '0'
        }
      },
      trigger: {
        cronTriggers: [],
        calendarOffsetTriggers: [],
        dailyIntervalTriggers: [],
        calendarIntervalTriggers: []
      },
      timeZones: []
    }
  },
  created() {
    this.loadConst()
    this.search()
    this.initTimeZones()
  },
  methods: {
    search() {
      this.listLoading = true
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
    reset() {
      if (this.editDialog.oper !== 'update' && (!this.selections || !this.selections.length || !this.selections[0].jobName)) {
        this.job = {
          jobName: undefined,
          displayName: '',
          status: 0,
          remark: ''
        }
      } else {
        this.job = Object.assign({}, this.selections[0])
      }
    },
    close() {
      this.editDialog.base.visible =
          this.editDialog.param.visible =
            this.editDialog.trigger.visible =
              this.editDialog.project.visible = false
    },
    add() {
      this.editDialog.oper = 'add'
      this.selectRow(null)
      this.editDialog.title = this.$t('actions.add')
      this.editDialog.base.visible = true
      if (this.$refs.jobInfoForm) {
        this.$refs.jobInfoForm.clearValidate()
      }
    },
    update(row) {
      this.editDialog.oper = 'update'
      this.selectRow(row)
      // this.job = Object.assign({}, row) // copy obj
      this.editDialog.title = this.$t('actions.update')
      this.editDialog.base.visible = true
      if (this.$refs.jobInfoForm) {
        this.$refs.jobInfoForm.clearValidate()
      }
    },
    save(job) {
      this.job = job
      const request = (this.editDialog.oper === 'add' ? addReq(this.job) : updateReq(this.job))
      request.then(res => {
        this.$message.success(res.data.msg)
        this.editDialog.base.visible = false
        this.search()
      })
    },
    publish() {
      const jobNames = []
      if (this.selections.length) {
        this.selections.forEach((sel) => {
          jobNames.push(sel.jobName)
        })
      } else {
        this.$message.warning(this.$t('tip.selectData'))
        return
      }
      const msg = '<p>' + this.$t('tip.confirmMsg') + ':<br><span style="color: red">' + jobNames.join('<br>') + '</span></p>'
      this.$confirm(msg, this.$t('tip.confirm'), { type: 'warning', dangerouslyUseHTMLString: true })
        .then(() => {
          publishReq({ jobNames: jobNames }).then(res => {
            this.$message.success(res.data.msg)
            this.search()
          })
        })
    },
    openTriggerDialog(row) {
      this.editDialog.title = this.$t('job.actions.trigger')
      this.triggerTab.timeTrigger.activeTabName = '0'
      this.selectRow(row)
      this.editDialog.trigger.visible = true
      triggerApi.getReq({ jobName: this.job.jobName }).then(res => {
        const result = res.data.result
        if (result.cronTriggers && result.cronTriggers.length > 0) {
          this.trigger.cronTriggers = result.cronTriggers
          this.$refs.cronTrigger.triggerArray = this.trigger.cronTriggers
          this.triggerTab.timeTrigger.activeTabName = '0'
        } else {
          this.trigger.cronTriggers = this.$refs.cronTrigger.triggerArray = []
          this.$refs.cronTrigger.add()
        }
        if (result.calendarOffsetTriggers && result.calendarOffsetTriggers.length > 0) {
          this.trigger.calendarOffsetTriggers = result.calendarOffsetTriggers
          this.$refs.calendarOffsetTrigger.triggerArray = this.trigger.calendarOffsetTriggers
          this.triggerTab.timeTrigger.activeTabName = '1'
        } else {
          this.trigger.calendarOffsetTriggers = this.$refs.calendarOffsetTrigger.triggerArray = []
          this.$refs.calendarOffsetTrigger.add()
        }
        if (result.dailyIntervalTriggers && result.dailyIntervalTriggers.length > 0) {
          result.dailyIntervalTriggers.forEach(item => {
            item.daysOfWeekArr = item.daysOfWeek.split(',')
          })
          this.trigger.dailyIntervalTriggers = result.dailyIntervalTriggers
          this.$refs.dailyIntervalTrigger.triggerArray = this.trigger.dailyIntervalTriggers
          this.triggerTab.timeTrigger.activeTabName = '2'
        } else {
          this.trigger.dailyIntervalTriggers = this.$refs.dailyIntervalTrigger.triggerArray = []
          this.$refs.dailyIntervalTrigger.add()
        }
        if (result.calendarIntervalTriggers && result.calendarIntervalTriggers.length > 0) {
          this.trigger.calendarIntervalTriggers = result.calendarIntervalTriggers
          this.$refs.calendarIntervalTrigger.triggerArray = this.trigger.calendarIntervalTriggers
          this.triggerTab.timeTrigger.activeTabName = '3'
        } else {
          this.trigger.calendarIntervalTriggers = this.$refs.calendarIntervalTrigger.triggerArray = []
          this.$refs.calendarIntervalTrigger.add()
        }
      })
    },
    openProjectDialog(row) {
      this.editDialog.title = this.$t('job.actions.project')
      this.selectRow(row)
      this.editDialog.project.visible = true
      if (this.$refs.projectTree) {
        this.$refs.projectTree.initTreeData()
      }
    },
    saveTrigger() {
      const trigger = { jobName: this.job.jobName }
      const result0 = this.$refs.cronTrigger.validateThenSet(trigger)
      const result1 = this.$refs.calendarOffsetTrigger.validateThenSet(trigger)
      const result2 = this.$refs.dailyIntervalTrigger.validateThenSet(trigger)
      const result3 = this.$refs.calendarIntervalTrigger.validateThenSet(trigger)
      // this.$message.warning(this.$t('job.trigger.tip.noTrigger'))
      if (result0 && result1 && result2 && result3) {
        triggerApi.updateReq(trigger).then(res => {
          this.$message.success(res.data.msg)
          this.editDialog.trigger.visible = false
        })
      }
    },
    remove() {
      const jobNames = []
      if (this.selections.length) {
        this.selections.forEach((sel) => {
          jobNames.push(sel.jobName)
        })
      } else {
        this.$message.warning(this.$t('tip.selectData'))
        return
      }
      const msg = '<p>' + this.$t('tip.confirmMsg') + ':<br><span style="color: red">' + jobNames.join('<br>') + '</span></p>'
      this.$confirm(msg, this.$t('tip.confirm'), { type: 'warning', dangerouslyUseHTMLString: true })
        .then(() => {
          removeReq({ jobNames: jobNames }).then(res => {
            this.$message.success(res.data.msg)
            this.search()
          })
        })
    },
    /* handleDownload() {
          this.downloadLoading = true
            import('@/vendor/Export2Excel').then(excel => {
              const tHeader = ['timestamp', 'title', 'type', 'importance', 'status']
              const filterVal = ['timestamp', 'title', 'type', 'importance', 'status']
              const data = this.formatJson(filterVal, this.list)
              excel.export_json_to_excel({
                header: tHeader,
                data,
                filename: 'table-list'
              })
              this.downloadLoading = false
            })
        },
        formatJson(filterVal, jsonData) {
          return jsonData.map(v => filterVal.map(j => {
            if (j === 'timestamp') {
              return parseTime(v[j])
            } else {
              return v[j]
            }
          }))
        }, */
    formatStatus(item) {
      for (let i = 0; i < this.jobStatuses.length; i++) {
        const option = this.jobStatuses[i]
        if (option.value === item) {
          return option.text
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
        this.jobStatuses = array.jobStatuses
        this.milliSecondTimeUnits = array.milliSecondTimeUnits
        this.inDayTimeUnits = array.inDayTimeUnits
        this.dayTimeUnit = array.dayTimeUnit
        this.overDayTimeUnits = array.overDayTimeUnits
        this.daysOfWeek = array.daysOfWeek
        // this.calendars = array.calendars
      })
    },
    openDesignDialog(row) {
      this.editDialog.title = this.$t('job.actions.design')
      this.selectRow(row)
      this.openDesignJobFlow()
    },
    openDesignJobFlow() {
      const route = {
        name: 'flow',
        params: {
          key: this.selections[0].jobName
        }
      }
      this.$router.push(route)
    },
    initTimeZones() {
      toolApi.listTimeZoneReq().then(res => {
        this.timeZones = res.data.result
      })
    },
    manual() {
      const jobNames = []
      if (this.selections.length) {
        this.selections.forEach((sel) => {
          if (!sel.maxVersion) {
            this.$message.warning(this.$t('tip.manualWithNoVersion') + ':' + sel.jobName)
            return
          }
          jobNames.push(sel.jobName)
        })
      } else {
        this.$message.warning(this.$t('tip.selectData'))
        return
      }
      const msg = '<p>' + this.$t('tip.confirmMsg') + ':<br><span style="color: red">' + jobNames.join('<br>') + '</span></p>'
      this.$confirm(msg, this.$t('tip.confirm'), { type: 'warning', dangerouslyUseHTMLString: true })
        .then(() => {
          manualReq({ jobNames: jobNames }).then(res => {
            this.$message.success(res.data.msg)
          })
        })
    }
  }
}
</script>

<style rel="stylesheet/scss" lang="scss">
  @import "./styles/jobs.scss";
</style>
