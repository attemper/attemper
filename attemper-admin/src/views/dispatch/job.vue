<template>
  <div class="app-container">
    <div class="filter-container">
      <el-input v-model="page.jobName" :placeholder="$t('dispatch.job.columns.jobName')" class="filter-item search-input" @keyup.enter.native="search" />
      <el-input v-model="page.displayName" :placeholder="$t('columns.displayName')" class="filter-item search-input" @keyup.enter.native="search" />
      <el-select v-model="page.status" :placeholder="$t('columns.status')" multiple clearable collapse-tags class="filter-item search-select">
        <el-option v-for="item in jobStatuses" :key="item.value" :label="item.label" :value="item.value" />
      </el-select>
      <!--<el-select v-model="page.sort" style="width: 140px" class="filter-item" @change="search">
        <el-option v-for="item in sortOptions" :key="item.key" :label="item.label" :value="item.key"/>
      </el-select>-->
      <el-button class="filter-item" type="primary" icon="el-icon-search" @click="search" />
      <el-button class="filter-item table-external-button" type="success" icon="el-icon-plus" @click="add" />
      <el-button :disabled="!selections || !selections.length" class="filter-item table-external-button" type="danger" icon="el-icon-delete" @click="remove" />
      <el-button :disabled="!selections || !selections.length" class="filter-item table-external-button" type="warning" @click="manualBatch">
        <svg-icon icon-class="hand" />{{ $t('actions.manual') }}
      </el-button>
      <div style="float: right">
        <el-popover placement="bottom" trigger="hover">
          <el-upload style="display: inline;" action="" accept="application/zip" :on-change="importModel" :auto-upload="false" :show-file-list="false">
            <el-button class="high-operation" type="success" icon="el-icon-upload2">{{ $t('actions.importModel') }}</el-button>
          </el-upload>
          <el-button class="high-operation" :disabled="!selections || !selections.length" type="primary" icon="el-icon-download" @click="exportModel">{{ $t('actions.exportModel') }}</el-button><br>
          <el-button class="high-operation" :disabled="!selections || !selections.length" type="success" icon="el-icon-circle-check" @click="enable">{{ $t('actions.enable') }}</el-button>
          <el-button class="high-operation" :disabled="!selections || !selections.length" type="danger" icon="el-icon-circle-close" @click="disable">{{ $t('actions.disable') }}</el-button><br>
          <el-button class="high-operation" :disabled="!page || !page.total || page.total <= 0" :loading="downloadLoading" type="primary" icon="el-icon-download" @click="handleDownload">{{ $t('actions.exportList') }}</el-button><br>
          <el-button class="high-operation" :disabled="!selections || !selections.length" type="primary" @click="publish">
            <svg-icon icon-class="publish" /> {{ $t('actions.publish') }}
          </el-button><br>
          <el-button class="high-operation" :disabled="!selections || selections.length !== 1" type="primary" @click="showChart">
            {{ $t('statistics.chart') }}
          </el-button>
          <el-button slot="reference" class="filter-item table-external-button" type="warning">{{ $t('actions.highOperation') }}</el-button>
        </el-popover>
      </div>
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
      @cell-click="clickCell"
      @sort-change="sortChange"
    >
      <el-table-column type="expand">
        <template slot-scope="props">
          <el-form label-position="left" inline class="table-expand">
            <el-form-item :label="$t('dispatch.job.columns.concurrent')">
              <el-tag :type="props.row.concurrent > 0 ? 'success' : 'info'">{{ props.row.concurrent ? $t('tip.yes') : $t('tip.no') }}</el-tag>
            </el-form-item>
            <el-form-item :label="$t('dispatch.job.columns.once')">
              <el-tag :type="props.row.once > 0 ? 'success' : 'info'">{{ props.row.once ? $t('tip.yes') : $t('tip.no') }}</el-tag>
            </el-form-item>
            <el-form-item :label="$t('columns.updateTime')">
              <span>{{ props.row.updateTime | parseTime }}</span>
            </el-form-item>
          </el-form>
        </template>
      </el-table-column>
      <el-table-column
        type="selection"
        width="45"
      />
      <el-table-column
        :label="$t('dispatch.job.columns.jobName')"
        prop="jobName"
        sortable="custom"
        min-width="100px"
      >
        <template slot-scope="scope">
          <el-link :type="scope.row.status | renderJobStatus" @click="openDesignDialog(scope.row)">{{ scope.row.jobName || '---' }}</el-link>
        </template>
      </el-table-column>
      <el-table-column :label="$t('columns.displayName')" min-width="150px">
        <template slot-scope="scope">
          <el-link type="primary" @click="update(scope.row)">{{ scope.row.displayName || '---' }}</el-link>
        </template>
      </el-table-column>
      <el-table-column :label="$t('columns.status')" align="center" class-name="status-col" width="80">
        <template slot-scope="scope">
          <el-tag :type="scope.row.status | renderJobStatus">{{ formatStatus(scope.row.status) }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column :label="$t('dispatch.job.columns.nextFireTime')" width="160px">
        <template slot-scope="scope">
          <el-popover trigger="hover" placement="left">
            <div v-if="scope.row.nextFireTimes && scope.row.nextFireTimes.length > 0">
              <p v-for="item in scope.row.nextFireTimes" :key="item + Math.random()">
                {{ item }}
              </p>
            </div>
            <div slot="reference">
              <span>{{ scope.row.nextFireTimes && scope.row.nextFireTimes.length > 0 ? scope.row.nextFireTimes[0] : null }}</span>
            </div>
          </el-popover>
        </template>
      </el-table-column>
      <el-table-column :label="$t('actions.handle')" align="center" min-width="240" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <div style="padding-top: 6px;">
            <el-button
              type="info"
              @click="openProjectDialog(scope.row)"
            >
              {{ $t('dispatch.job.actions.project') }}
            </el-button>
            <el-button
              type="primary"
              @click="openArgDialog(scope.row)"
            >
              {{ $t('dispatch.job.actions.param') }}
            </el-button>
            <el-button
              type="warning"
              @click="openConditionDialog(scope.row)"
            >
              {{ $t('dispatch.job.actions.condition') }}
            </el-button>
            <el-button
              type="success"
              @click="openTriggerDialog(scope.row)"
            >
              {{ $t('dispatch.job.actions.trigger') }}
            </el-button>
          </div>
        </template>
      </el-table-column>
    </el-table>

    <pagination
      v-show="page.total > 0"
      :total="page.total"
      :page.sync="page.currentPage"
      :limit.sync="page.pageSize"
      @pagination="search"
    />

    <el-dialog
      :title="editDialog.title"
      :visible.sync="editDialog.base.visible || editDialog.arg.visible || editDialog.trigger.visible || editDialog.condition.visible || editDialog.project.visible || editDialog.param.visible || editDialog.chart.visible"
      :center="true"
      :modal="true"
      :close-on-click-modal="false"
      :close-on-press-escape="false"
      :before-close="close"
      :fullscreen="editDialog.chart.visible"
    >
      <div v-show="editDialog.base.visible">
        <job-info-form ref="jobInfoForm" :job="job" @save="save" />
      </div>
      <div v-show="editDialog.trigger.visible">
        <el-tabs v-model="triggerTab.activeTabName" type="border-card">
          <el-tab-pane :label="$t('dispatch.trigger.tab.cron')" name="0">
            <CronTrigger ref="cronTrigger" :time-zones="timeZones" :calendars="calendars" />
          </el-tab-pane>
          <el-tab-pane :label="$t('dispatch.trigger.tab.calendarOffset')" name="1">
            <CalendarOffsetTrigger ref="calendarOffsetTrigger" :over-day-time-units="overDayTimeUnits" :calendars="calendars" />
          </el-tab-pane>
          <el-tab-pane :label="$t('dispatch.trigger.tab.dailyTimeInterval')" name="2">
            <DailyTimeIntervalTrigger
              ref="dailyTimeIntervalTrigger"
              :milli-second-time-units="milliSecondTimeUnits"
              :in-day-time-units="inDayTimeUnits"
              :days-of-week="daysOfWeek"
              :calendars="calendars"
            />
          </el-tab-pane>
          <el-tab-pane :label="$t('dispatch.trigger.tab.calendarInterval')" name="3">
            <CalendarIntervalTrigger
              ref="calendarIntervalTrigger"
              :day-time-unit="dayTimeUnit"
              :in-day-time-units="inDayTimeUnits"
              :over-day-time-units="overDayTimeUnits"
              :time-zones="timeZones"
              :calendars="calendars"
            />
          </el-tab-pane>
        </el-tabs>
        <el-row style="margin-top: 15px;">
          <el-col :span="4" :offset="8">
            <el-button type="info" @click="editDialog.trigger.visible = false">{{ $t('actions.cancel') }}</el-button>
          </el-col>
          <el-col :span="4" :offset="1">
            <el-button type="success" @click="saveTrigger">{{ $t('actions.use') }}</el-button>
          </el-col>
        </el-row>
      </div>
      <div v-show="editDialog.condition.visible">
        <el-tabs v-model="conditionTab.activeTabName" tab-position="left">
          <el-tab-pane :label="$t('dispatch.condition.tab.sql')" name="0">
            <SqlCondition ref="sqlCondition" />
          </el-tab-pane>
          <el-tab-pane :label="$t('dispatch.condition.tab.ftpFile')" name="10">
            <FtpFileCondition ref="ftpFileCondition" />
          </el-tab-pane>
          <el-tab-pane :label="$t('dispatch.condition.tab.localFile')" name="11">
            <LocalFileCondition ref="localFileCondition" />
          </el-tab-pane>
        </el-tabs>
        <el-row style="margin-top: 15px;">
          <el-col :span="4" :offset="8">
            <el-button type="info" @click="editDialog.condition.visible = false">{{ $t('actions.cancel') }}</el-button>
          </el-col>
          <el-col :span="4" :offset="1">
            <el-button type="success" @click="saveCondition">{{ $t('actions.save') }}</el-button>
          </el-col>
        </el-row>
      </div>
      <div v-show="editDialog.arg.visible">
        <div class="filter-container">
          <el-input v-model="argPage.argName" :placeholder="$t('dispatch.arg.columns.argName')" class="filter-item search-input" @keyup.enter.native="argSearch" />
          <el-select v-model="argPage.argType" :placeholder="$t('dispatch.arg.columns.argType')" clearable collapse-tags class="filter-item search-select">
            <el-option v-for="item in argTypes" :key="item.value" :label="item.label" :value="item.value" />
          </el-select>
          <el-input v-model="argPage.argValue" :placeholder="$t('dispatch.arg.columns.argValue')" class="filter-item search-input" @keyup.enter.native="argSearch" />
          <el-button class="filter-item" type="primary" icon="el-icon-search" @click="argSearch" />
        </div>

        <el-table
          v-loading="argListLoading"
          :data="argList"
          border
          fit
          highlight-current-row
          style="width: 100%;"
        >
          <el-table-column :label="$t('dispatch.arg.columns.argName')" prop="id" sortable="custom" align="center" min-width="100px">
            <template slot-scope="scope">
              <span>{{ scope.row.argName }}</span>
            </template>
          </el-table-column>
          <el-table-column :label="$t('dispatch.arg.columns.argType')" align="center" width="100">
            <template slot-scope="scope">
              <span>{{ formatArgType(scope.row.argType) }}</span>
            </template>
          </el-table-column>
          <el-table-column :label="$t('dispatch.arg.columns.argValue')" min-width="150px">
            <template slot-scope="scope">
              <span>{{ scope.row.argValue }}</span>
            </template>
          </el-table-column>
          <el-table-column :label="$t('actions.handle')" align="center" min-width="120" class-name="small-padding fixed-width">
            <template slot-scope="scope">
              <el-button :type="scope.row.allocated > 0 ? 'danger' : 'primary'" :icon="scope.row.allocated > 0 ? 'el-icon-minus' : 'el-icon-plus'" @click="operateArg(scope.row)" />
            </template>
          </el-table-column>
        </el-table>
        <pagination v-show="argPage.total>0" :total="argPage.total" :page.sync="argPage.currentPage" :limit.sync="argPage.pageSize" @pagination="argSearch" />
      </div>
      <div v-show="editDialog.project.visible">
        <ProjectTree ref="projectTree" :job-name="job.jobName" @cancel="editDialog.project.visible = false" />
      </div>
      <div v-show="editDialog.param.visible">
        <code-editor v-model="jsonData" :read-only="false" extension=".json" />
        <div style="text-align: center; margin-top: 10px">
          <el-button type="danger" @click="manual">
            <svg-icon icon-class="hand" />{{ $t('actions.manual') }}
          </el-button>
        </div>
      </div>
      <div v-show="editDialog.chart.visible">
        <ChartArea ref="chartArea" :job-charts="jobCharts" :job-name="job.jobName" />
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { listReq, /* getReq,*/ removeReq, addReq, updateReq, enableReq, disableReq, publishReq, manualBatchReq, importModelReq, exportModelReq, getTriggerReq, updateTriggerReq, listArgReq, addArgReq, removeArgReq, getJsonArgReq, manualReq, getConditionReq, updateConditionReq } from '@/api/dispatch/job'
import * as calendarApi from '@/api/dispatch/calendar'
import * as toolApi from '@/api/dispatch/tool'
import { buildMsg, getTimeStr, download } from '@/utils/tools'
import Pagination from '@/components/Pagination'
import JobInfoForm from './components/job/jobInfoForm'
import CronTrigger from './components/job/trigger/cronTrigger'
import CalendarOffsetTrigger from './components/job/trigger/calendarOffsetTrigger'
import DailyTimeIntervalTrigger from './components/job/trigger/dailyTimeIntervalTrigger'
import CalendarIntervalTrigger from './components/job/trigger/calendarIntervalTrigger'
import FtpFileCondition from './components/job/condition/ftpFileCondition'
import LocalFileCondition from './components/job/condition/localFileCondition'
import SqlCondition from './components/job/condition/sqlCondition'
import ProjectTree from './components/job/projectTree'
import CodeEditor from '@/components/CodeEditor'
import ChartArea from './components/job/chart/chartArea'

const DEF_OBJ = {
  jobName: undefined,
  displayName: '',
  status: 0,
  concurrent: 0,
  once: 0,
  remark: '',
  content: ''
}
export default {
  name: 'job',
  components: {
    ProjectTree,
    Pagination,
    JobInfoForm,
    CronTrigger,
    CalendarOffsetTrigger,
    DailyTimeIntervalTrigger,
    CalendarIntervalTrigger,
    FtpFileCondition,
    LocalFileCondition,
    SqlCondition,
    CodeEditor,
    ChartArea
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
        status: [],
        sort: 'JOB_NAME'
      },
      argList: null,
      argListLoading: true,
      argPage: {
        jobName: undefined,
        currentPage: 1,
        pageSize: 10,
        total: 0,
        argName: undefined,
        argValue: undefined,
        argType: null,
        sort: 'ARG_NAME'
      },
      argTypes: [],
      jobStatuses: [],
      milliSecondTimeUnits: [],
      inDayTimeUnits: [],
      dayTimeUnit: [],
      overDayTimeUnits: [],
      daysOfWeek: [],
      /* sortOptions: [{ label: this.$t('dispatch.sort.nameAsc'), key: 'JOB_NAME' }, { label: this.$t('dispatch.sort.nameDesc'), key: 'JOB_NAME DESC' }],*/
      showCreateTime: false,
      job: DEF_OBJ,
      editDialog: {
        oper: undefined,
        title: undefined,
        base: {
          visible: false
        },
        arg: {
          visible: false
        },
        trigger: {
          visible: false
        },
        condition: {
          visible: false
        },
        project: {
          visible: false
        },
        param: {
          visible: false
        },
        chart: {
          visible: false
        }
      },
      downloadLoading: false,
      selections: [],
      calendars: [],
      triggerTab: {
        activeTabName: '0'
      },
      conditionTab: {
        activeTabName: '0'
      },
      trigger: {
        cronTriggers: [],
        calendarOffsetTriggers: [],
        dailyTimeIntervalTriggers: [],
        calendarIntervalTriggers: []
      },
      condition: {
        sqlConditions: [],
        ftpFileConditions: [],
        localFileConditions: []
      },
      timeZones: [],
      jsonData: null,
      jobCharts: null
    }
  },
  created() {
    this.loadConst()
    this.initCalendars()
    this.initTimeZones()
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
    reset() {
      if (this.editDialog.oper !== 'update' && (!this.selections || !this.selections.length || !this.selections[0].jobName)) {
        this.job = Object.assign({}, DEF_OBJ)
      } else {
        this.job = Object.assign({}, this.selections[0])
      }
    },
    close() {
      this.editDialog.base.visible =
          this.editDialog.arg.visible =
            this.editDialog.trigger.visible =
              this.editDialog.condition.visible =
              this.editDialog.project.visible =
                this.editDialog.param.visible =
                  this.editDialog.chart.visible = false
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
      const jobNames = this.ofKeys()
      this.$confirm(buildMsg(this, jobNames), this.$t('tip.confirmMsg'), { type: 'warning' })
        .then(() => {
          publishReq({ jobNames: jobNames }).then(res => {
            this.$message.success(res.data.msg)
            this.search()
          })
        })
    },
    enable() {
      const jobNames = this.ofKeys()
      this.$confirm(buildMsg(this, jobNames), this.$t('tip.confirmMsg'), { type: 'warning' })
        .then(() => {
          enableReq({ jobNames: jobNames }).then(res => {
            this.$message.success(res.data.msg)
            this.search()
          })
        })
    },
    disable() {
      const jobNames = this.ofKeys()
      this.$confirm(buildMsg(this, jobNames), this.$t('tip.confirmMsg'), { type: 'warning' })
        .then(() => {
          disableReq({ jobNames: jobNames }).then(res => {
            this.$message.success(res.data.msg)
            this.search()
          })
        })
    },
    ofKeys() {
      return this.selections.map(sel => sel.jobName)
    },
    openTriggerDialog(row) {
      this.editDialog.title = this.$t('dispatch.job.actions.trigger')
      this.triggerTab.activeTabName = '0'
      this.selectRow(row)
      this.editDialog.trigger.visible = true
      getTriggerReq({ jobName: this.job.jobName }).then(res => {
        const result = res.data.result
        if (result && result.cronTriggers && result.cronTriggers.length > 0) {
          this.$refs.cronTrigger.triggerArray = result.cronTriggers
          this.triggerTab.activeTabName = '0'
        } else {
          this.$refs.cronTrigger.triggerArray = []
          this.$refs.cronTrigger.add()
        }
        if (result && result.calendarOffsetTriggers && result.calendarOffsetTriggers.length > 0) {
          this.$refs.calendarOffsetTrigger.triggerArray = result.calendarOffsetTriggers
          this.triggerTab.activeTabName = '1'
        } else {
          this.$refs.calendarOffsetTrigger.triggerArray = []
          this.$refs.calendarOffsetTrigger.add()
        }
        if (result && result.dailyTimeIntervalTriggers && result.dailyTimeIntervalTriggers.length > 0) {
          result.dailyTimeIntervalTriggers.forEach(item => {
            item.daysOfWeekArr = item.daysOfWeek.split(',')
          })
          this.$refs.dailyTimeIntervalTrigger.triggerArray = result.dailyTimeIntervalTriggers
          this.triggerTab.activeTabName = '2'
        } else {
          this.$refs.dailyTimeIntervalTrigger.triggerArray = []
          this.$refs.dailyTimeIntervalTrigger.add()
        }
        if (result && result.calendarIntervalTriggers && result.calendarIntervalTriggers.length > 0) {
          this.$refs.calendarIntervalTrigger.triggerArray = result.calendarIntervalTriggers
          this.triggerTab.activeTabName = '3'
        } else {
          this.$refs.calendarIntervalTrigger.triggerArray = []
          this.$refs.calendarIntervalTrigger.add()
        }
      })
    },
    openConditionDialog(row) {
      this.editDialog.title = this.$t('dispatch.job.actions.condition')
      this.conditionTab.activeTabName = '0'
      this.selectRow(row)
      this.editDialog.condition.visible = true
      getConditionReq({ jobName: this.job.jobName }).then(res => {
        const result = res.data.result
        if (result.sqlConditions && result.sqlConditions.length > 0) {
          this.$refs.sqlCondition.conditionArray = result.sqlConditions
          this.conditionTab.activeTabName = '0'
        } else {
          this.$refs.sqlCondition.conditionArray = []
          this.$refs.sqlCondition.add()
        }
        if (result.ftpFileConditions && result.ftpFileConditions.length > 0) {
          this.$refs.ftpFileCondition.conditionArray = result.ftpFileConditions
          this.conditionTab.activeTabName = '10'
        } else {
          this.$refs.ftpFileCondition.conditionArray = []
          this.$refs.ftpFileCondition.add()
        }
        if (result.localFileConditions && result.localFileConditions.length > 0) {
          this.$refs.localFileCondition.conditionArray = result.localFileConditions
          this.conditionTab.activeTabName = '11'
        } else {
          this.$refs.localFileCondition.conditionArray = []
          this.$refs.localFileCondition.add()
        }
      })
    },
    openArgDialog(row) {
      this.editDialog.title = this.$t('dispatch.job.actions.param')
      this.selectRow(row)
      this.editDialog.arg.visible = true
      this.argSearch()
    },
    argSearch() {
      this.argListLoading = true
      this.argPage.jobName = this.job.jobName
      listArgReq(this.argPage).then(response => {
        this.argList = response.data.result.list
        Object.assign(this.argPage, response.data.result.page)
        setTimeout(() => {
          this.argListLoading = false
        }, 200)
      })
    },
    operateArg(row) {
      const req = (row.allocated === 0 ? addArgReq : removeArgReq)
      const data = {
        jobName: this.job.jobName,
        argName: row.argName
      }
      req(data).then(res => {
        this.$message.success(res.data.msg)
        this.argSearch()
      })
    },
    openProjectDialog(row) {
      this.editDialog.title = this.$t('dispatch.job.actions.project')
      this.selectRow(row)
      this.editDialog.project.visible = true
      this.$nextTick(() => {
        if (this.$refs.projectTree) {
          this.$refs.projectTree.initTreeData()
        }
      })
    },
    saveTrigger() {
      const trigger = { jobName: this.job.jobName }
      const result0 = this.$refs.cronTrigger.validateThenSet(trigger)
      const result1 = this.$refs.calendarOffsetTrigger.validateThenSet(trigger)
      const result2 = this.$refs.dailyTimeIntervalTrigger.validateThenSet(trigger)
      const result3 = this.$refs.calendarIntervalTrigger.validateThenSet(trigger)
      if (result0 && result1 && result2 && result3) {
        updateTriggerReq(trigger).then(res => {
          this.$message.success(res.data.msg)
          this.editDialog.trigger.visible = false
          setTimeout(() => {
            this.search()
          }, 200)
        })
      }
    },
    saveCondition() {
      const condition = { jobName: this.job.jobName }
      const result0 = this.$refs.sqlCondition.validateThenSet(condition)
      const result10 = this.$refs.ftpFileCondition.validateThenSet(condition)
      const result11 = this.$refs.localFileCondition.validateThenSet(condition)
      if (result0 && result10 && result11) {
        updateConditionReq(condition).then(res => {
          this.$message.success(res.data.msg)
          this.editDialog.condition.visible = false
        })
      }
    },
    remove() {
      const jobNames = this.ofKeys()
      this.$confirm(buildMsg(this, jobNames), this.$t('tip.confirmMsg'), { type: 'warning' })
        .then(() => {
          removeReq({ jobNames: jobNames }).then(res => {
            this.$message.success(res.data.msg)
            this.search()
          })
        })
    },
    formatStatus(item) {
      for (let i = 0; i < this.jobStatuses.length; i++) {
        const option = this.jobStatuses[i]
        if (option.value === item) {
          return option.label
        }
      }
      return item
    },
    selectRow(row) {
      if (row && this.selections.length === 1 && this.selections[0].jobName === row.jobName) {
        return
      }
      this.$refs.tables.clearSelection()
      if (row && (this.editDialog.oper === 'update' || row.jobName)) {
        this.$refs.tables.toggleRowSelection(row, true)
      }
      this.reset()
    },
    handleSelectionChange(val) {
      this.selections = val
    },
    clickCell(row, column, cell, event) {
      this.selectRow(row)
    },
    importModel(file) {
      this.$confirm(this.$t('tip.confirm') + ' ' + file.name, this.$t('tip.confirmMsg'), { type: 'warning' })
        .then(() => {
          const data = new FormData()
          data.append('file', file.raw)
          importModelReq(data).then(res => {
            this.$message.success(res.data.msg)
            this.search()
          })
        })
    },
    exportModel() {
      const jobNames = this.ofKeys()
      this.$confirm(buildMsg(this, jobNames), this.$t('tip.confirmMsg'), { type: 'warning' })
        .then(() => {
          const fileName = 'model_' + getTimeStr() + '.zip'
          exportModelReq({ jobNames: jobNames, fileName: fileName }).then((res) => {
            download(res.data, fileName)
          })
        })
    },
    handleDownload() {
      this.downloadLoading = true
      import('@/vendor/Export2Excel').then(excel => {
        const translatedHeader = [
          this.$t('dispatch.job.columns.jobName'),
          this.$t('columns.displayName'),
          this.$t('columns.status')
        ]
        const columnNames = ['jobName', 'displayName', 'status']
        const data = this.formatJson(columnNames, this.selections.length ? this.selections : this.list)
        excel.export_json_to_excel({
          header: translatedHeader,
          data,
          filename: 'job_' + getTimeStr()
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
    loadConst() {
      import(`@/constant/array/${localStorage.getItem('language')}.js`).then((array) => {
        this.jobStatuses = array.jobStatuses
        this.milliSecondTimeUnits = array.milliSecondTimeUnits
        this.inDayTimeUnits = array.inDayTimeUnits
        this.dayTimeUnit = array.dayTimeUnit
        this.overDayTimeUnits = array.overDayTimeUnits
        this.daysOfWeek = array.daysOfWeek
        this.jobCharts = array.jobCharts
        this.search()
      })
      this.initArgTypes()
    },
    initArgTypes() {
      toolApi.argTypesReq().then(res => {
        this.argTypes = res.data.result
      })
    },
    formatArgType(item) {
      for (let i = 0; i < this.argTypes.length; i++) {
        const option = this.argTypes[i]
        if (option.value === item) {
          return option.label
        }
      }
      return item
    },
    openDesignDialog(row) {
      this.editDialog.title = this.$t('dispatch.job.actions.design')
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
    manualBatch() {
      if (this.selections.length === 1) {
        if (this.selections[0].status === 1) {
          this.$message.warning(this.$t('tip.disabledJobError') + ':' + this.selections[0].jobName)
          return
        }
        this.jsonData = null
        getJsonArgReq({ jobName: this.selections[0].jobName }).then(res => {
          if (!res.data.result) {
            this.manual()
          } else {
            this.editDialog.title = this.$t('dispatch.job.actions.param')
            this.editDialog.param.visible = true
            this.jsonData = JSON.parse(res.data.result)
          }
        })
      } else {
        const jobNames = []
        for (let i = 0; i < this.selections.length; i++) {
          const sel = this.selections[i]
          if (sel.status === 1) {
            this.$message.warning(this.$t('tip.disabledJobError') + ':' + sel.jobName)
            return
          }
          jobNames.push(sel.jobName)
        }
        this.$confirm(buildMsg(this, jobNames), this.$t('tip.confirmMsg'), { type: 'warning' })
          .then(() => {
            manualBatchReq({ jobNames: jobNames }).then(res => {
              this.$message.success(res.data.msg)
              setTimeout(() => {
                this.$router.push({ name: 'total', replace: true })
              }, 600)
            })
          })
      }
    },
    manual() {
      this.$confirm(this.$t('tip.confirm'), this.$t('tip.confirmMsg'), { type: 'warning' })
        .then(() => {
          manualReq({
            jobName: this.selections[0].jobName,
            jsonData: (!this.jsonData ? null : (typeof this.jsonData === 'string' ? JSON.parse(this.jsonData) : this.jsonData))
          }).then(res => {
            this.editDialog.param.visible = false
            this.$message.success(res.data.msg)
            setTimeout(() => {
              this.$router.push({ name: 'total', replace: true })
            }, 600)
          })
        })
    },
    showChart() {
      this.editDialog.title = this.$t('statistics.chart') + '-' + this.job.displayName + '(' + this.job.jobName + ')'
      this.editDialog.chart.visible = true
      this.$nextTick(() => {
        if (this.$refs.chartArea) {
          this.$refs.chartArea.chartValue = this.jobCharts[0].value
          this.$refs.chartArea.reRender()
        }
      })
    },
    initCalendars() {
      calendarApi.listReq().then(res => {
        this.calendars = res.data.result
      })
    },
    initPageStatus() {
      if (this.page.status.length === 0) {
        this.page.status = this.jobStatuses.map(item => item.value)
      }
    }
  }
}
</script>

<style rel="stylesheet/scss" lang="scss">
  @import "./styles/jobs.scss";
</style>
