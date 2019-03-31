<template>
  <div class="app-container">
    <div class="filter-container">
      <el-input :placeholder="$t('job.columns.jobName')" v-model="page.jobName" style="width: 100px;" class="filter-item" size="mini" @keyup.enter.native="search"/>
      <el-input :placeholder="$t('job.columns.displayName')" v-model="page.displayName" style="width: 100px;" class="filter-item" size="mini" @keyup.enter.native="search"/>
      <el-select v-model="page.status" :placeholder="$t('job.columns.status')" multiple clearable collapse-tags class="filter-item" size="mini" style="width: 160px">
        <el-option v-for="item in jobStatuses" :key="item.value" :label="item.text" :value="item.value"/>
      </el-select>
      <!--<el-select v-model="page.sort" style="width: 140px" class="filter-item" @change="search">
        <el-option v-for="item in sortOptions" :key="item.key" :label="item.label" :value="item.key"/>
      </el-select>-->
      <el-button v-waves class="filter-item" size="mini" type="primary" icon="el-icon-search" @click="search">{{ $t('actions.search') }}</el-button>
      <el-button class="filter-item table-external-button" size="mini" type="success" icon="el-icon-plus" @click="add">{{ $t('actions.add') }}</el-button>
      <el-button :disabled="!selections || !selections.length" class="filter-item table-external-button" size="mini" type="danger" icon="el-icon-delete" @click="remove">{{ $t('actions.remove') }}</el-button>
      <el-button v-waves :disabled="!selections || !selections.length" class="filter-item table-external-button" size="mini" type="primary" @click="publish">
        <svg-icon icon-class="publish"/> {{ $t('table.publish') }}
      </el-button>
      <!--<el-button v-waves :loading="downloadLoading" class="filter-item" type="primary" icon="el-icon-download" @click="handleDownload">{{ $t('actions.export') }}</el-button>
      <el-checkbox v-model="showCreateTime" class="filter-item" style="margin-left:15px;" @change="tableKey=tableKey+1">{{ $t('job.columns.createTime') }}</el-checkbox>-->
    </div>

    <el-table
      v-loading="listLoading"
      ref="tables"
      :key="tableKey"
      :data="list"
      border
      fit
      highlight-current-row
      style="width: 100%;"
      @selection-change="handleSelectionChange"
      @sort-change="sortChange">
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
        width="40"/>
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
        min-width="100px">
        <template slot-scope="scope">
          <el-tag type="primary" @click="update(scope.row)">{{ scope.row.jobName || '-' }}</el-tag>
          <!--<span><a @click="update(scope.row)">{{ scope.row.jobName }}</a></span>-->
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
              @click="openTriggerDialog(scope.row)">{{ $t('job.actions.trigger') }}
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
      @pagination="search"/>

    <el-dialog
      :title="editDialog.title"
      :visible.sync="editDialog.base.visible || editDialog.param.visible || editDialog.trigger.visible"
      :center="true"
      :modal="true"
      :close-on-click-modal="false"
      :close-on-press-escape="false"
      :before-close="close">
      <div v-show="editDialog.base.visible">
        <job-info-form ref="jobInfoForm" :job="job" @save="save" @cancel="editDialog.base.visible = false"/>
      </div>
      <div v-show="editDialog.trigger.visible">
        <el-tabs type="border-card">
          <el-tab-pane>
            <span slot="label">
              <svg-icon icon-class="time"/> {{ $t('job.trigger.tab.time.title') }}
            </span>
            <el-tabs tab-position="left">
              <el-tab-pane :label="$t('job.trigger.tab.time.cron')">
                <div v-for="(item,index) in trigger.cronTriggers" :key="index" style="margin-bottom: 10px;">
                  <el-row>
                    <el-col :span="4">
                      <el-button icon="el-icon-plus" type="success" size="mini" @click="addCronRow"/>
                      <el-button v-show="trigger.cronTriggers.length>1" icon="el-icon-minus" type="danger" size="mini" @click="removeCronRow(index)"/>
                    </el-col>
                    <el-col :span="20">
                      <el-form :model="item" label-width="150px" label-position="left" size="mini">
                        <el-form-item :label="$t('job.trigger.title.triggerName')">
                          <el-input v-model="item.triggerName" :placeholder="$t('job.trigger.placeholder.triggerName')" size="mini"/>
                        </el-form-item>
                        <el-form-item :label="$t('job.trigger.title.expression')">
                          <cron-input v-model="item.expression" @change="changeCron($event, index)" @reset="resetCron($event, index)"/>
                        </el-form-item>
                        <el-form-item :label="$t('job.trigger.title.timeRange')">
                          <el-col :span="11">
                            <el-date-picker
                              v-model="item.startTime"
                              :placeholder="$t('job.trigger.placeholder.startTime')"
                              type="datetime"
                              value-format="yyyy-MM-dd HH:mm:ss"/>
                          </el-col>
                          <el-col :span="11" :offset="1">
                            <el-date-picker
                              v-model="item.endTime"
                              :placeholder="$t('job.trigger.placeholder.endTime')"
                              type="datetime"
                              value-format="yyyy-MM-dd HH:mm:ss"/>
                          </el-col>
                        </el-form-item>
                        <el-form-item :label="$t('job.trigger.title.timeZone')">
                          <el-select v-model="item.timeZoneId" filterable>
                            <el-option
                              v-for="ele in timeZones"
                              :key="ele"
                              :label="ele"
                              :value="ele"/>
                          </el-select>
                        </el-form-item>
                      </el-form>
                    </el-col>
                  </el-row>
                  <hr style="border: 1px dashed #E4E7ED;" color ="#E4E7ED" size = "1">
                </div>
              </el-tab-pane>
              <el-tab-pane :label="$t('job.trigger.tab.time.calendarOffset')">
                <div v-for="(item,index) in trigger.calendarOffsetTriggers" :key="index" style="margin-bottom: 10px;">
                  <el-row>
                    <el-col :span="4">
                      <el-button icon="el-icon-plus" type="success" size="mini" @click="addCalendarOffsetRow"/>
                      <el-button v-show="trigger.calendarOffsetTriggers.length>1" icon="el-icon-minus" type="danger" size="mini" @click="removeCalendarOffsetRow(index)"/>
                    </el-col>
                    <el-col :span="20">
                      <el-form :model="item" label-width="150px" label-position="left" size="mini">
                        <el-form-item :label="$t('job.trigger.title.triggerName')">
                          <el-input v-model="item.triggerName" :placeholder="$t('job.trigger.placeholder.triggerName')"/>
                        </el-form-item>
                        <el-form-item :label="$t('job.trigger.title.timeRange')">
                          <el-col :span="11">
                            <el-date-picker
                              v-model="item.startTime"
                              :placeholder="$t('job.trigger.placeholder.startTime')"
                              type="datetime"
                              value-format="yyyy-MM-dd HH:mm:ss"/>
                          </el-col>
                          <el-col :span="11" :offset="1">
                            <el-date-picker
                              v-model="item.endTime"
                              :placeholder="$t('job.trigger.placeholder.endTime')"
                              type="datetime"
                              value-format="yyyy-MM-dd HH:mm:ss"/>
                          </el-col>
                        </el-form-item>
                        <el-form-item :label="$t('job.trigger.title.startTimeOfDay')">
                          <el-time-picker
                            :placeholder="$t('job.trigger.placeholder.startTimeOfDay')"
                            v-model="item.startTimeOfDay"
                            value-format="HH:mm:ss"/>
                        </el-form-item>
                        <el-form-item :label="$t('job.trigger.title.timeUnit')">
                          <el-select v-model="item.timeUnit" :placeholder="$t('job.trigger.placeholder.timeUnit')" filterable>
                            <el-option
                              v-for="ele in overDayTimeUnits"
                              :key="ele.value"
                              :label="ele.label"
                              :value="ele.value"/>
                          </el-select>
                        </el-form-item>
                        <el-form-item :label="$t('job.trigger.title.repeatCount')">
                          <el-input-number v-model="item.repeatCount" :placeholder="$t('job.trigger.placeholder.repeatCount')" :precision="0" :min="-1" :step="1" controls-position="right"/>
                        </el-form-item>
                        <el-form-item :label="$t('job.trigger.title.reversed')">
                          <el-switch v-model="item.reversed"/>
                        </el-form-item>
                        <el-form-item :label="$t('job.trigger.title.innerOffset')">
                          <el-input-number v-model="item.innerOffset" :placeholder="$t('job.trigger.placeholder.innerOffset')" :precision="0" :min="0" :step="1" controls-position="right"/>
                        </el-form-item>
                        <el-form-item :label="$t('job.trigger.title.outerOffset')">
                          <el-input-number v-model="item.outerOffset" :placeholder="$t('job.trigger.placeholder.outerOffset')" :precision="0" :min="0" :step="1" controls-position="right"/>
                        </el-form-item>
                      </el-form>
                    </el-col>
                  </el-row>
                  <hr style="border: 1px dashed #E4E7ED;" color ="#E4E7ED" size = "1">
                </div>
              </el-tab-pane>
              <el-tab-pane :label="$t('job.trigger.tab.time.dailyInterval')">
                <div v-for="(item,index) in trigger.dailyIntervalTriggers" :key="index" style="margin-bottom: 10px;">
                  <el-row>
                    <el-col :span="4">
                      <el-button icon="el-icon-plus" type="success" size="mini" @click="addDailyIntervalRow"/>
                      <el-button v-show="trigger.dailyIntervalTriggers.length>1" icon="el-icon-minus" type="danger" size="mini" @click="removeDailyIntervalRow(index)"/>
                    </el-col>
                    <el-col :span="20">
                      <el-form :model="item" label-width="150px" label-position="left" size="mini">
                        <el-form-item :label="$t('job.trigger.title.triggerName')">
                          <el-input v-model="item.triggerName" :placeholder="$t('job.trigger.placeholder.triggerName')"/>
                          <!--<el-col :span="10" :offset="2">
                            <el-select v-model="item.calendar" :placeholder="$t('job.trigger.placeholder.calendar')">
                              <el-option
                                v-for="ele in calendars"
                                :key="ele.value"
                                :label="ele.label"
                                :value="ele.value"/>
                            </el-select>
                          </el-col>-->
                        </el-form-item>
                        <el-form-item :label="$t('job.trigger.title.timeRange')">
                          <el-col :span="11">
                            <el-date-picker
                              v-model="item.startTime"
                              :placeholder="$t('job.trigger.placeholder.startTime')"
                              type="datetime"
                              value-format="yyyy-MM-dd HH:mm:ss"/>
                          </el-col>
                          <el-col :span="11" :offset="1">
                            <el-date-picker
                              v-model="item.endTime"
                              :placeholder="$t('job.trigger.placeholder.endTime')"
                              type="datetime"
                              value-format="yyyy-MM-dd HH:mm:ss"/>
                          </el-col>
                        </el-form-item>
                        <el-form-item :label="$t('job.trigger.title.timeRangeOfDay')">
                          <el-col :span="11">
                            <el-time-picker
                              :placeholder="$t('job.trigger.placeholder.startTimeOfDay')"
                              v-model="item.startTimeOfDay"
                              value-format="HH:mm:ss"/>
                          </el-col>
                          <el-col :span="11" :offset="1">
                            <el-time-picker
                              :placeholder="$t('job.trigger.placeholder.endTimeOfDay')"
                              v-model="item.endTimeOfDay"
                              value-format="HH:mm:ss"/>
                          </el-col>
                        </el-form-item>
                        <el-form-item :label="$t('job.trigger.title.interval')">
                          <el-input-number v-model="item.interval" :placeholder="$t('job.trigger.placeholder.interval')" :precision="0" :min="1" :step="1" controls-position="right"/>
                        </el-form-item>
                        <el-form-item :label="$t('job.trigger.title.timeUnit')">
                          <el-select v-model="item.timeUnit" :placeholder="$t('job.trigger.placeholder.timeUnit')" filterable>
                            <el-option
                              v-for="ele in milliSecondTimeUnits.concat(inDayTimeUnits)"
                              :key="ele.value"
                              :label="ele.label"
                              :value="ele.value"/>
                          </el-select>
                        </el-form-item>
                        <el-form-item :label="$t('job.trigger.title.repeatCount')">
                          <el-input-number v-model="item.repeatCount" :placeholder="$t('job.trigger.placeholder.repeatCount')" :precision="0" :min="-1" :step="1" controls-position="right"/>
                        </el-form-item>
                        <el-form-item :label="$t('job.trigger.title.daysOfWeek')">
                          <el-select v-model="item.daysOfWeekArr" :placeholder="$t('job.trigger.placeholder.daysOfWeek')" multiple filterable collapse-tags style="width: 160px;">
                            <el-option
                              v-for="ele in daysOfWeek"
                              :key="ele.value"
                              :label="ele.label"
                              :value="ele.value"/>
                          </el-select>
                        </el-form-item>
                      </el-form>
                    </el-col>
                  </el-row>
                  <hr style="border: 1px dashed #E4E7ED;" color ="#E4E7ED" size = "1">
                </div>
              </el-tab-pane>
              <el-tab-pane :label="$t('job.trigger.tab.time.calendarInterval')">
                <div v-for="(item,index) in trigger.calendarIntervalTriggers" :key="index" style="margin-bottom: 10px;">
                  <el-row>
                    <el-col :span="4">
                      <el-button icon="el-icon-plus" type="success" size="mini" @click="addCalendarIntervalRow"/>
                      <el-button v-show="trigger.calendarIntervalTriggers.length>1" icon="el-icon-minus" type="danger" size="mini" @click="removeCalendarIntervalRow(index)"/>
                    </el-col>
                    <el-col :span="20">
                      <el-form :model="item" label-width="150px" label-position="left" size="mini">
                        <el-form-item :label="$t('job.trigger.title.triggerName')">
                          <el-input v-model="item.triggerName" :placeholder="$t('job.trigger.placeholder.triggerName')"/>
                          <!--<el-col :span="10" :offset="2">
                            <el-select v-model="item.calendar" :placeholder="$t('job.trigger.placeholder.calendar')">
                              <el-option
                                v-for="ele in calendars"
                                :key="ele.value"
                                :label="ele.label"
                                :value="ele.value"/>
                            </el-select>
                          </el-col>-->
                        </el-form-item>
                        <el-form-item :label="$t('job.trigger.title.timeRange')">
                          <el-col :span="11">
                            <el-date-picker
                              v-model="item.startTime"
                              :placeholder="$t('job.trigger.placeholder.startTime')"
                              type="datetime"
                              value-format="yyyy-MM-dd HH:mm:ss"/>
                          </el-col>
                          <el-col :span="11" :offset="1">
                            <el-date-picker
                              v-model="item.endTime"
                              :placeholder="$t('job.trigger.placeholder.endTime')"
                              type="datetime"
                              value-format="yyyy-MM-dd HH:mm:ss"/>
                          </el-col>
                        </el-form-item>
                        <el-form-item :label="$t('job.trigger.title.interval')">
                          <el-input-number v-model="item.interval" :placeholder="$t('job.trigger.placeholder.interval')" :precision="0" :min="1" :step="1" controls-position="right"/>
                        </el-form-item>
                        <el-form-item :label="$t('job.trigger.title.timeUnit')">
                          <el-select v-model="item.timeUnit" :placeholder="$t('job.trigger.placeholder.timeUnit')">
                            <el-option
                              v-for="ele in inDayTimeUnits.concat(dayTimeUnit).concat(overDayTimeUnits)"
                              :key="ele.value"
                              :label="ele.label"
                              :value="ele.value"/>
                          </el-select>
                        </el-form-item>
                        <el-form-item :label="$t('job.trigger.title.repeatCount')">
                          <el-input-number v-model="item.repeatCount" :placeholder="$t('job.trigger.placeholder.repeatCount')" :precision="0" :min="-1" :step="1" controls-position="right"/>
                        </el-form-item>
                        <el-form-item :label="$t('job.trigger.title.preserveDayLight')">
                          <el-switch v-model="item.preserveDayLight"/>
                        </el-form-item>
                        <el-form-item :label="$t('job.trigger.title.skipDayIfNoHour')">
                          <el-switch v-model="item.skipDayIfNoHour"/>
                        </el-form-item>
                        <el-form-item :label="$t('job.trigger.title.timeZone')">
                          <el-select v-model="item.timeZoneId" filterable>
                            <el-option
                              v-for="ele in timeZones"
                              :key="ele"
                              :label="ele"
                              :value="ele"/>
                          </el-select>
                        </el-form-item>
                      </el-form>
                    </el-col>
                  </el-row>
                  <hr style="border: 1px dashed #E4E7ED;" color ="#E4E7ED" size = "1">
                </div>
              </el-tab-pane>
            </el-tabs>
          </el-tab-pane>
          <el-tab-pane>
            <span slot="label">
              <svg-icon icon-class="event"/> {{ $t('job.trigger.tab.event.title') }}
            </span>
            event
          </el-tab-pane>
        </el-tabs>
        <el-row style="margin-top: 20px;">
          <el-col :span="4" :offset="8">
            <el-button type="info" @click="editDialog.trigger.visible = false">{{ $t('actions.cancel') }}</el-button>
          </el-col>
          <el-tooltip :disabled="job.maxVersion" :content="this.$t('job.trigger.tip.jobNotPublished') " placement="right" effect="light">
            <el-col :span="4" :offset="1">
              <el-button :disabled="!job.maxVersion" type="success" @click="saveTrigger">{{ $t('actions.use') }}</el-button>
            </el-col>
          </el-tooltip>
        </el-row>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { listReq, removeReq, addReq, updateReq, publishReq } from '@/api/job/baseJob'
import * as triggerApi from '@/api/job/trigger'
import * as toolApi from '@/api/sys/tool'
import waves from '@/directive/waves' // Waves directive
// import { parseTime } from '@/utils'
import Pagination from '@/components/Pagination' // Secondary package based on el-pagination
import { load } from '@/constant'
import CronInput from 'vue-cron-generator/src/components/cron-input'
import { DEFAULT_CRON_EXPRESSION } from 'vue-cron-generator/src/constant/filed'
import { isBlank, startAfterEndTime } from './scripts/support'
import JobInfoForm from './components/jobInfoForm'

const CRON_OBJ = {
  triggerName: '',
  expression: DEFAULT_CRON_EXPRESSION,
  startTime: null,
  endTime: null,
  timeZoneId: null
}
const CALENDAR_OFFSET_OBJ = {
  triggerName: '',
  startTime: null,
  endTime: null,
  startTimeOfDay: '08:00:00',
  timeUnit: 'WEEK',
  repeatCount: -1,
  reversed: false,
  innerOffset: 0,
  outerOffset: 0
}
const DAILY_INTERVAL_OBJ = {
  triggerName: '',
  startTime: null,
  endTime: null,
  startTimeOfDay: '00:00:00',
  endTimeOfDay: '23:59:59',
  interval: 1,
  timeUnit: 'MINUTE',
  repeatCount: -1,
  daysOfWeekArr: [1, 2, 3, 4, 5, 6, 7]
}
const CALENDAR_INTERVAL_OBJ = {
  triggerName: '',
  // calendar: null,
  startTime: null,
  endTime: null,
  interval: 1,
  timeUnit: 'DAY',
  repeatCount: -1,
  preserveDayLight: false,
  skipDayIfNoHour: false,
  timeZoneId: null
}

export default {
  name: 'Jobs',
  components: {
    Pagination,
    CronInput,
    JobInfoForm
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
        }
      },
      downloadLoading: false,
      selections: [],
      // calendars: [],
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
            this.editDialog.trigger.visible = false
    },
    add() {
      this.editDialog.oper = 'add'
      this.selectRow(null)
      this.editDialog.title = this.$t('actions.add')
      this.editDialog.base.visible = true
      /* this.$nextTick(() => {
        this.$refs['baseForm'].clearValidate()
      })*/
      // this.$refs.jobInfoForm.clearValidate()
    },
    update(row) {
      this.editDialog.oper = 'update'
      this.selectRow(row)
      // this.job = Object.assign({}, row) // copy obj
      this.editDialog.title = this.$t('actions.update')
      this.editDialog.base.visible = true
      /* this.$nextTick(() => {
        this.$refs['baseForm'].clearValidate()
      })*/
      // this.$refs.jobInfoForm.clearValidate()
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
        this.$message.warning(this.$t('tip.publish'))
        return
      }
      const msg = '<p>' + this.$t('tip.publishConfirm') + ':<br><span style="color: red">' + jobNames.join('<br>') + '</span></p>'
      this.$confirm(msg, this.$t('tip.confirm'), { type: 'warning', dangerouslyUseHTMLString: true })
        .then(() => {
          publishReq({ jobNames: jobNames }).then(res => {
            this.$message.success(res.data.msg)
            this.search()
          })
        })
    },
    saveTrigger() {
      const trigger = { jobName: this.job.jobName }
      this.validateThenSetCronTrigger(trigger)
      this.validateThenSetCalendarOffsetTrigger(trigger)
      this.validateThenSetDailyIntervalTrigger(trigger)
      this.validateThenSetCalendarIntervalTrigger(trigger)
      // this.$message.warning(this.$t('job.trigger.tip.noTrigger'))
      triggerApi.updateReq(trigger).then(res => {
        this.$message.success(res.data.msg)
      })
    },
    validateThenSetCronTrigger(trigger) {
      for (let i = 0; i < this.trigger.cronTriggers.length; i++) {
        const item = this.trigger.cronTriggers[i]
        if (JSON.stringify(item) !== JSON.stringify(CRON_OBJ)) {
          if (isBlank(item.triggerName)) {
            this.$message.error(this.$t('job.trigger.tip.triggerNameNotBlank') + ':' + JSON.stringify(item))
            return false
          }
          if (startAfterEndTime(item.startTime, item.endTime)) {
            this.$message.error(this.$t('job.trigger.tip.startAfterEndTime') + ':' + item.triggerName)
            return false
          }
          if (isBlank(item.expression)) { // TODO need regex validation
            this.$message.error(this.$t('job.trigger.tip.cronExpressionInvalid') + ':' + item.triggerName)
            return false
          }
          if (!trigger.cronTriggers) {
            trigger.cronTriggers = []
          }
          trigger.cronTriggers.push(item)
        }
      }
    },
    validateThenSetCalendarOffsetTrigger(trigger) {
      for (let i = 0; i < this.trigger.calendarOffsetTriggers.length; i++) {
        const item = this.trigger.calendarOffsetTriggers[i]
        if (JSON.stringify(item) !== JSON.stringify(CALENDAR_OFFSET_OBJ)) {
          if (isBlank(item.triggerName)) {
            this.$message.error(this.$t('job.trigger.tip.triggerNameNotBlank') + ':' + JSON.stringify(item))
            return false
          }
          if (startAfterEndTime(item.startTime, item.endTime)) {
            this.$message.error(this.$t('job.trigger.tip.startAfterEndTime') + ':' + item.triggerName)
            return false
          }
          if (!trigger.calendarOffsetTriggers) {
            trigger.calendarOffsetTriggers = []
          }
          trigger.calendarOffsetTriggers.push(item)
        }
      }
    },
    validateThenSetDailyIntervalTrigger(trigger) {
      for (let i = 0; i < this.trigger.dailyIntervalTriggers.length; i++) {
        const item = this.trigger.dailyIntervalTriggers[i]
        if (JSON.stringify(item) !== JSON.stringify(DAILY_INTERVAL_OBJ)) {
          if (isBlank(item.triggerName)) {
            this.$message.error(this.$t('job.trigger.tip.triggerNameNotBlank') + ':' + JSON.stringify(item))
            return false
          }
          if (startAfterEndTime(item.startTime, item.endTime)) {
            this.$message.error(this.$t('job.trigger.tip.startAfterEndTime') + ':' + item.triggerName)
            return false
          }
          if (!trigger.dailyIntervalTriggers) {
            trigger.dailyIntervalTriggers = []
          }
          item.daysOfWeek = item.daysOfWeekArr.join(',')
          trigger.dailyIntervalTriggers.push(item)
        }
      }
    },
    validateThenSetCalendarIntervalTrigger(trigger) {
      for (let i = 0; i < this.trigger.calendarIntervalTriggers.length; i++) {
        const item = this.trigger.calendarIntervalTriggers[i]
        if (JSON.stringify(item) !== JSON.stringify(CALENDAR_INTERVAL_OBJ)) {
          if (isBlank(item.triggerName)) {
            this.$message.error(this.$t('job.trigger.tip.triggerNameNotBlank') + ':' + JSON.stringify(item))
            return false
          }
          if (startAfterEndTime(item.startTime, item.endTime)) {
            this.$message.error(this.$t('job.trigger.tip.startAfterEndTime') + ':' + item.triggerName)
            return false
          }
          if (!trigger.calendarIntervalTriggers) {
            trigger.calendarIntervalTriggers = []
          }
          trigger.calendarIntervalTriggers.push(item)
        }
      }
    },
    remove() {
      const jobNames = []
      if (this.selections.length) {
        this.selections.forEach((sel) => {
          jobNames.push(sel.jobName)
        })
      } else {
        this.$message.warning(this.$t('tip.remove'))
        return
      }
      const msg = '<p>' + this.$t('tip.removeConfirm') + ':<br><span style="color: red">' + jobNames.join('<br>') + '</span></p>'
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
      load(`./array/${this.$store.state.app.language}.js`).then((array) => {
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
    openTriggerDialog(row) {
      this.editDialog.title = this.$t('job.actions.trigger')
      this.selectRow(row)
      this.editDialog.trigger.visible = true
      triggerApi.getReq({ jobName: this.job.jobName }).then(res => {
        const result = res.data.result
        if (result.cronTriggers && result.cronTriggers.length > 0) {
          this.trigger.cronTriggers = result.cronTriggers
        } else {
          this.trigger.cronTriggers = []
          this.addCronRow()
        }
        if (result.calendarOffsetTriggers && result.calendarOffsetTriggers.length > 0) {
          this.trigger.calendarOffsetTriggers = result.calendarOffsetTriggers
        } else {
          this.trigger.calendarOffsetTriggers = []
          this.addCalendarOffsetRow()
        }
        if (result.dailyIntervalTriggers && result.dailyIntervalTriggers.length > 0) {
          result.dailyIntervalTriggers.forEach(item => {
            item.daysOfWeekArr = item.daysOfWeek.split(',')
          })
          this.trigger.dailyIntervalTriggers = result.dailyIntervalTriggers
        } else {
          this.trigger.dailyIntervalTriggers = []
          this.addDailyIntervalRow()
        }
        if (result.calendarIntervalTriggers && result.calendarIntervalTriggers.length > 0) {
          this.trigger.calendarIntervalTriggers = result.calendarIntervalTriggers
        } else {
          this.trigger.calendarIntervalTriggers = []
          this.addCalendarIntervalRow()
        }
      })
    },
    openDesignJobFlow() {
      const route = {
        name: 'flow',
        params: {
          id: this.selections[0].jobName
        }
      }
      this.$router.push(route)
    },
    addCronRow() {
      this.trigger.cronTriggers.push(Object.assign({}, CRON_OBJ))
    },
    removeCronRow(index) {
      this.trigger.cronTriggers.splice(index, 1)
    },
    changeCron(newCron, index) {
      this.trigger.cronTriggers[index].expression = newCron
    },
    resetCron(oldCron, index) {
      this.trigger.cronTriggers[index].expression = DEFAULT_CRON_EXPRESSION
    },
    addCalendarOffsetRow() {
      this.trigger.calendarOffsetTriggers.push(Object.assign({}, CALENDAR_OFFSET_OBJ))
    },
    removeCalendarOffsetRow(index) {
      this.trigger.calendarOffsetTriggers.splice(index, 1)
    },
    addDailyIntervalRow() {
      this.trigger.dailyIntervalTriggers.push(Object.assign({}, DAILY_INTERVAL_OBJ))
    },
    removeDailyIntervalRow(index) {
      this.trigger.dailyIntervalTriggers.splice(index, 1)
    },
    addCalendarIntervalRow() {
      this.trigger.calendarIntervalTriggers.push(Object.assign({}, CALENDAR_INTERVAL_OBJ))
    },
    removeCalendarIntervalRow(index) {
      this.trigger.calendarIntervalTriggers.splice(index, 1)
    },
    initTimeZones() {
      toolApi.listTimeZoneReq().then(res => {
        this.timeZones = res.data.result
      })
    }
  }
}
</script>

<style rel="stylesheet/scss" lang="scss">
  @import "./styles/jobs.scss";
</style>
