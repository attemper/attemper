<template>
  <div class="app-container">
    <div class="filter-container">
      <el-input v-model="page.jobName" :placeholder="$t('dispatch.job.columns.jobName')" class="filter-item search-input" @keyup.enter.native="search" />
      <el-input v-model="page.displayName" :placeholder="$t('columns.displayName')" class="filter-item search-input" @keyup.enter.native="search" />
      <el-button v-waves class="filter-item" type="primary" icon="el-icon-search" @click="search">{{ $t('actions.search') }}</el-button>
      <el-button :disabled="!selections || !selections.length" class="filter-item table-external-button" type="danger" icon="el-icon-delete" @click="remove">{{ $t('actions.remove') }}</el-button>
      <div style="float: right">
        <el-popover
          placement="bottom"
          trigger="hover"
        >
          <el-button v-waves :loading="downloadLoading" class="filter-item" type="primary" icon="el-icon-download" @click="handleDownload">{{ $t('actions.exportList') }}</el-button>
          <el-button slot="reference" class="filter-item table-external-button" type="warning">{{ $t('actions.highOperation') }}</el-button>
        </el-popover>
        <el-button :disabled="!selections || !selections.length" class="filter-item table-external-button" type="primary" @click="manual">
          <svg-icon icon-class="hand" />{{ $t('actions.manual') }}
        </el-button>
      </div>
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
      @cell-click="clickCell"
    >
      <el-table-column type="expand">
        <template slot-scope="props">
          <el-form label-position="left" inline class="table-expand">
            <el-form-item :label="$t('dispatch.delay.columns.requestTime')">
              <span>{{ props.row.requestTime }}</span>
            </el-form-item>
          </el-form>
        </template>
      </el-table-column>
      <el-table-column
        type="selection"
        width="40"
      />
      <el-table-column :label="$t('dispatch.delay.columns.id')" min-width="100px">
        <template slot-scope="scope">
          <span>{{ scope.row.id }}</span>
        </template>
      </el-table-column>
      <el-table-column
        :label="$t('dispatch.job.columns.jobName')"
        prop="jobName"
        align="center"
        min-width="100px"
      >
        <template slot-scope="scope">
          <el-link type="primary" @click="linkJob(scope.row)">{{ scope.row.jobName || '-' }}</el-link>
        </template>
      </el-table-column>
      <el-table-column :label="$t('columns.displayName')" min-width="150px">
        <template slot-scope="scope">
          <span>{{ scope.row.displayName }}</span>
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
      <el-table-column :label="$t('actions.handle')" align="center" width="100" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <div style="padding-top: 6px;">
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
      v-show="page.total>0"
      :total="page.total"
      :page.sync="page.currentPage"
      :limit.sync="page.pageSize"
      @pagination="search"
    />

    <el-dialog
      :title="editDialog.title"
      :visible.sync="editDialog.trigger.visible"
      :center="true"
      :modal="true"
      :close-on-click-modal="false"
      :close-on-press-escape="false"
      :before-close="close"
    >
      <div v-show="editDialog.trigger.visible">
        <el-tabs v-model="triggerTab.timeTrigger.activeTabName" tab-position="left">
          <el-tab-pane :label="$t('dispatch.trigger.tab.time.dailyInterval')" name="2">
            <DailyIntervalTrigger
              ref="dailyIntervalTrigger"
              :milli-second-time-units="milliSecondTimeUnits"
              :in-day-time-units="inDayTimeUnits"
              :days-of-week="daysOfWeek"
              :calendar-groups="calendarGroups"
            />
          </el-tab-pane>
        </el-tabs>
        <el-row style="margin-top: 20px;">
          <el-col :span="4" :offset="8">
            <el-button type="info" @click="editDialog.trigger.visible = false">{{ $t('actions.cancel') }}</el-button>
          </el-col>
          <el-col :span="4" :offset="1">
            <el-button type="success" @click="saveTrigger">{{ $t('actions.use') }}</el-button>
          </el-col>
        </el-row>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { listReq, /* getReq,*/ removeReq, manualReq } from '@/api/dispatch/delay'
import * as calendarApi from '@/api/dispatch/calendar'
import * as triggerApi from '@/api/dispatch/trigger'
import * as toolApi from '@/api/dispatch/tool'
import waves from '@/directive/waves'
import { buildMsg } from '@/utils/tools'
import Pagination from '@/components/Pagination'
import DailyIntervalTrigger from './components/job/dailyTimeIntervalTrigger'
export default {
  name: 'delay',
  components: {
    Pagination,
    DailyIntervalTrigger
  },
  directives: { waves },
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
        sort: 'REQUEST_TIME DESC'
      },
      milliSecondTimeUnits: [],
      inDayTimeUnits: [],
      dayTimeUnit: [],
      overDayTimeUnits: [],
      daysOfWeek: [],
      delay: {},
      editDialog: {
        oper: 'update',
        title: undefined,
        trigger: {
          visible: false
        }
      },
      downloadLoading: false,
      selections: [],
      triggerTab: {
        timeTrigger: {
          activeTabName: '0'
        }
      },
      trigger: {
        dailyIntervalTriggers: []
      },
      timeZones: [],
      calendarGroups: [],
      calendarTypes: []
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
        setTimeout(() => {
          this.listLoading = false
        }, 200)
      })
    },
    reset() {
      this.deplay = Object.assign({}, this.selections[0])
    },
    close() {
      this.editDialog.trigger.visible = false
    },
    update(row) {
      this.editDialog.oper = 'update'
      this.selectRow(row)
      // this.deplay = Object.assign({}, row) // copy obj
      this.editDialog.title = this.$t('actions.update')
      this.editDialog.base.visible = true
      /* if (this.$refs.jobInfoForm) {
        this.$refs.jobInfoForm.clearValidate()
      }*/
    },
    openTriggerDialog(row) {
      this.editDialog.title = this.$t('dispatch.job.actions.trigger')
      this.triggerTab.timeTrigger.activeTabName = '2'
      this.selectRow(row)
      this.editDialog.trigger.visible = true
      this.initCalendarGroups()
      triggerApi.getReq({ jobName: this.deplay.id }).then(res => {
        const result = res.data.result
        if (result.dailyIntervalTriggers && result.dailyIntervalTriggers.length > 0) {
          result.dailyIntervalTriggers.forEach(item => {
            item.daysOfWeekArr = item.daysOfWeek && item.daysOfWeek.length ? item.daysOfWeek.split(',') : []
          })
          this.$refs.dailyIntervalTrigger.triggerArray = result.dailyIntervalTriggers
          this.triggerTab.timeTrigger.activeTabName = '2'
        } else {
          this.$refs.dailyIntervalTrigger.triggerArray = []
          this.$refs.dailyIntervalTrigger.add()
        }
      })
    },
    saveTrigger() {
      const trigger = { jobName: this.deplay.jobName }
      const result2 = this.$refs.dailyIntervalTrigger.validateThenSet(trigger)
      if (result2) {
        triggerApi.updateReq(trigger).then(res => {
          this.$message.success(res.data.msg)
          this.editDialog.trigger.visible = false
          setTimeout(() => {
            this.search()
          }, 200)
        })
      }
    },
    remove() {
      const ids = []
      if (this.selections.length) {
        this.selections.forEach((sel) => {
          ids.push(sel.id)
        })
      } else {
        this.$message.warning(this.$t('tip.selectData'))
        return
      }
      this.$confirm(buildMsg(this, ids), this.$t('tip.confirmMsg'), { type: 'warning' })
        .then(() => {
          removeReq({ ids: ids }).then(res => {
            this.$message.success(res.data.msg)
            this.search()
          })
        })
    },
    selectRow(row) {
      this.$refs.tables.clearSelection()
      if (row && (this.editDialog.oper === 'update' || row.id)) {
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
    handleDownload() {
      /*
      this.downloadLoading = true
        import('@/vendor/Export2Excel').then(excel => {
          const translatedHeader = [
            this.$t('dispatch.job.columns.jobName'),
            this.$t('columns.displayName'),
            this.$t('columns.status'),
            this.$t('dispatch.job.columns.version')
          ]
          const data = this.list
          excel.export_json_to_excel({
            header: translatedHeader,
            data,
            filename: 'job_' + getTimeStr()
          })
          this.downloadLoading = false
        })*/
    },
    loadConst() {
        import(`@/constant/array/${localStorage.getItem('language')}.js`).then((array) => {
          this.milliSecondTimeUnits = array.milliSecondTimeUnits
          this.inDayTimeUnits = array.inDayTimeUnits
          this.dayTimeUnit = array.dayTimeUnit
          this.overDayTimeUnits = array.overDayTimeUnits
          this.daysOfWeek = array.daysOfWeek
          this.calendarTypes = array.calendarTypes
        })
    },
    linkJob() {
      const route = {
        name: 'job',
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
        for (let i = 0; i < this.selections.length; i++) {
          jobNames.push(this.selections[i].jobName)
        }
        this.$confirm(buildMsg(this, jobNames), this.$t('tip.confirmMsg'), { type: 'warning' })
          .then(() => {
            manualReq({ jobNames: jobNames }).then(res => {
              this.$message.success(res.data.msg)
              setTimeout(() => {
                this.$router.push({ name: 'total', replace: true })
              }, 600)
            })
          })
      } else {
        this.$message.warning(this.$t('tip.selectData'))
        return
      }
    },
    initCalendarGroups() {
      this.calendarGroups = []
      calendarApi.listReq().then(res => {
        this.calendarTypes.forEach(type => {
          const options = res.data.result.filter(item => item.type === type.value)
          this.calendarGroups.push({
            label: type.label,
            options: options && options.length > 0 ? options : []
          })
        })
      })
    }
  }
}
</script>

<style rel="stylesheet/scss" lang="scss">
  @import "./styles/jobs.scss";
</style>
