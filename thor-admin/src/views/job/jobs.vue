<template>
  <div class="app-container">
    <div class="filter-container">
      <el-input
        :placeholder="$t('job.columns.jobName')"
        v-model="page.jobName"
        style="width: 100px;"
        class="filter-item"
        @keyup.enter.native="search"/>
      <el-input
        :placeholder="$t('job.columns.displayName')"
        v-model="page.displayName"
        style="width: 100px;"
        class="filter-item"
        @keyup.enter.native="search"/>
      <el-select
        v-model="page.status"
        :placeholder="$t('job.columns.status')"
        multiple
        clearable
        collapse-tags
        class="filter-item"
        style="width: 160px">
        <el-option v-for="item in jobStatuses" :key="item.value" :label="item.text" :value="item.value"/>
      </el-select>
      <!--<el-select v-model="page.sort" style="width: 140px" class="filter-item" @change="search">
        <el-option v-for="item in sortOptions" :key="item.key" :label="item.label" :value="item.key"/>
      </el-select>-->
      <el-button v-waves class="filter-item" type="primary" icon="el-icon-search" @click="search">{{
      $t('actions.search') }}
      </el-button>
      <el-button class="filter-item" style="margin-left: 10px;" type="success" icon="el-icon-plus" @click="add">{{
      $t('actions.add') }}
      </el-button>
      <el-button
        :disabled="!selections || !selections.length"
        class="filter-item"
        style="margin-left: 10px;"
        type="danger"
        icon="el-icon-delete"
        @click="remove">{{ $t('actions.remove') }}
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
          </el-form>
        </template>
      </el-table-column>
      <el-table-column
        type="selection"
        width="40"/>
      <el-table-column
        :label="$t('job.columns.jobName')"
        prop="jobName"
        sortable="custom"
        align="center"
        min-width="100px">
        <template slot-scope="scope">
          <span>{{ scope.row.jobName }}</span>
        </template>
      </el-table-column>
      <el-table-column :label="$t('job.columns.displayName')" min-width="150px">
        <template slot-scope="scope">
          <span>{{ scope.row.displayName }}</span>
        </template>
      </el-table-column>
      <el-table-column :label="$t('job.columns.version')" sortable="custom" align="center" min-width="70px">
        <template slot-scope="scope">
          <el-popover trigger="hover" placement="top">
            <p>{{ $t('tip.clickToSeeDetail') }}</p>
            <div slot="reference" class="name-wrapper">
              <el-button v-show="scope.row.version" type="primary" size="mini">{{ scope.row.version }}</el-button>
            </div>
          </el-popover>
        </template>
      </el-table-column>
      <el-table-column :label="$t('job.columns.reversion')" sortable="custom" align="center" min-width="70px">
        <template slot-scope="scope">
          <el-popover trigger="hover" placement="top">
            <p>{{ $t('tip.clickToSeeDetail') }}</p>
            <div slot="reference" class="name-wrapper">
              <el-button v-show="scope.row.reversion" type="primary" size="mini">{{ scope.row.reversion }}</el-button>
            </div>
          </el-popover>
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
          <div>
            <el-button type="primary" size="mini" @click="update(scope.row)">{{ $t('actions.update') }}</el-button>
            <el-button
              style="background-color:	#3CB371; border-color:	#3CB371; color: #fff;"
              size="mini"
              @click="openDesignDialog(scope.row)">{{ $t('job.actions.design') }}
            </el-button>
          </div>
          <div style="padding-top: 6px;">
            <el-button style="background-color:	#A4D3EE; border-color:	#A4D3EE; color: #fff;" size="mini">{{
            $t('job.actions.param') }}
            </el-button>
            <el-button
              style="background-color:	#FFBBFF; border-color:	#FFBBFF; color: #fff;"
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
        <el-form
          ref="baseForm"
          :rules="rules.baseRules"
          :model="job"
          label-position="left"
          label-width="150px"
          class="form-layout">
          <el-form-item :label="$t('job.columns.jobName')" prop="jobName">
            <el-input v-model="job.jobName" :placeholder="$t('job.placeholder.jobName')"/>
          </el-form-item>
          <el-form-item :label="$t('job.columns.displayName')" prop="displayName">
            <el-input v-model="job.displayName" :placeholder="$t('job.placeholder.displayName')"/>
          </el-form-item>
          <el-form-item :label="$t('job.columns.status')" prop="status">
            <el-select v-model="job.status" :placeholder="$t('job.placeholder.status')" class="filter-item">
              <el-option v-for="item in jobStatuses" :key="item.value" :label="item.text" :value="item.value"/>
            </el-select>
          </el-form-item>
          <el-form-item :label="$t('job.columns.remark')">
            <el-input
              :autosize="{ minRows: 2, maxRows: 4}"
              v-model="job.remark"
              :placeholder="$t('job.placeholder.remark')"
              type="textarea"/>
          </el-form-item>
          <el-form-item>
            <el-button type="info" @click="editDialog.base.visible = false">{{ $t('actions.cancel') }}</el-button>
            <el-button type="success" @click="save">{{ $t('actions.save') }}</el-button>
          </el-form-item>
        </el-form>
      </div>
      <div v-show="editDialog.trigger.visible">
        <el-tabs type="border-card">
          <el-tab-pane>
            <span slot="label">
              <svg-icon icon-class="time"/> {{ $t('job.trigger.tab.time.title') }}
            </span>
            <el-tabs tab-position="left">
              <el-tab-pane :label="$t('job.trigger.tab.time.cron')">
                <div v-for="(item,index) in cronTriggers" :key="index" style="margin-bottom: 10px;">
                  <el-row>
                    <el-col :span="4">
                      <el-button icon="el-icon-plus" type="success" size="mini" @click="addCronRow"/>
                      <el-button v-show="cronTriggers.length>1" icon="el-icon-minus" type="danger" size="mini" @click="removeCronRow(index)"/>
                    </el-col>
                    <el-col :span="20">
                      <cron-input v-model="item.cron" @change="changeCron" @reset="resetCron"/>
                    </el-col>
                  </el-row>
                </div>
              </el-tab-pane>
              <el-tab-pane :label="$t('job.trigger.tab.time.real')">
                <div v-for="(item,index) in realTimeTriggers" :key="index" style="margin-bottom: 10px;">
                  <el-row>
                    <el-col :span="4">
                      <el-button icon="el-icon-plus" type="success" size="mini" @click="addRealRow"/>
                      <el-button v-show="realTimeTriggers.length>1" icon="el-icon-minus" type="danger" size="mini" @click="removeRealRow(index)"/>
                    </el-col>
                    <el-col :span="20">
                      <el-form :model="item" label-width="150px" label-position="left" size="mini">
                        <el-form-item :label="$t('job.trigger.title.triggerInfo')">
                          <el-col :span="10">
                            <el-input v-model="item.triggerName" :placeholder="$t('job.trigger.placeholder.triggerName')"/>
                          </el-col>
                          <el-col :span="10" :offset="2">
                            <el-select v-model="item.calendar" :placeholder="$t('job.trigger.placeholder.calendar')">
                              <el-option
                                v-for="ele in calendars"
                                :key="ele.value"
                                :label="ele.label"
                                :value="ele.value"/>
                            </el-select>
                          </el-col>
                        </el-form-item>
                        <el-form-item :label="$t('job.trigger.title.timeRange')">
                          <el-col :span="11">
                            <el-date-picker
                              v-model="item.startTime"
                              :placeholder="$t('job.trigger.placeholder.startTime')"
                              type="datetime"/>
                          </el-col>
                          <el-col :span="1">-</el-col>
                          <el-col :span="11">
                            <el-date-picker
                              v-model="item.endTime"
                              :placeholder="$t('job.trigger.placeholder.endTime')"
                              type="datetime"/>
                          </el-col>
                        </el-form-item>
                        <el-form-item :label="$t('job.trigger.title.period')">
                          <el-col :span="6">
                            <el-input-number v-model="item.period" :precision="0" :min="1" :step="1" controls-position="right"/>
                          </el-col>
                          <el-col :span="8" :offset="1">
                            <el-select v-model="item.timeUnit" :placeholder="$t('job.trigger.placeholder.timeUnit')">
                              <el-option
                                v-for="ele in realTimeUnits"
                                :key="ele.value"
                                :label="ele.label"
                                :value="ele.value"/>
                            </el-select>
                          </el-col>
                          <el-col :span="6" :offset="2">
                            <el-input-number v-model="item.repeatCount" :precision="0" :min="1" :step="1" controls-position="right"/>
                          </el-col>
                        </el-form-item>
                      </el-form>
                    </el-col>
                  </el-row>
                  <hr style="border: 1px dashed #E4E7ED;" color ="#E4E7ED" size = "1">
                </div>
              </el-tab-pane>
              <el-tab-pane :label="$t('job.trigger.tab.time.fixed')">
                <div v-for="(item,index) in fixedTimeTriggers" :key="index" style="margin-bottom: 10px;">
                  <el-row>
                    <el-col :span="4">
                      <el-button icon="el-icon-plus" type="success" size="mini" @click="addFixedRow"/>
                      <el-button v-show="fixedTimeTriggers.length>1" icon="el-icon-minus" type="danger" size="mini" @click="removeFixedRow(index)"/>
                    </el-col>
                    <el-col :span="20">
                      <el-form :model="item" label-width="150px" label-position="left" size="mini">
                        <el-form-item :label="$t('job.trigger.title.triggerInfo')">
                          <el-col :span="10">
                            <el-input v-model="item.triggerName" :placeholder="$t('job.trigger.placeholder.triggerName')"/>
                          </el-col>
                          <el-col :span="10" :offset="2">
                            <el-select v-model="item.calendar" :placeholder="$t('job.trigger.placeholder.calendar')">
                              <el-option
                                v-for="ele in calendars"
                                :key="ele.value"
                                :label="ele.label"
                                :value="ele.value"/>
                            </el-select>
                          </el-col>
                        </el-form-item>
                        <el-form-item :label="$t('job.trigger.title.timeRange')">
                          <el-col :span="11">
                            <el-date-picker
                              v-model="item.startTime"
                              :placeholder="$t('job.trigger.placeholder.startTime')"
                              type="datetime"/>
                          </el-col>
                          <el-col :span="1">-</el-col>
                          <el-col :span="11">
                            <el-date-picker
                              v-model="item.endTime"
                              :placeholder="$t('job.trigger.placeholder.endTime')"
                              type="datetime"/>
                          </el-col>
                        </el-form-item>
                        <el-form-item :label="$t('job.trigger.title.timeRangeOfDay')">
                          <el-col :span="11">
                            <el-time-select
                              :placeholder="$t('job.trigger.placeholder.startTimeOfDay')"
                              v-model="item.startTimeOfDay"
                              :picker-options="{start: '00:00', step: '00:05',end: '23:55'}"/>
                          </el-col>
                          <el-col :span="1">-</el-col>
                          <el-col :span="11">
                            <el-time-select
                              :placeholder="$t('job.trigger.placeholder.endTimeOfDay')"
                              v-model="item.endTimeOfDay"
                              :picker-options="{start: '00:00', step: '00:05',end: '23:55', minTime: item.startTimeOfDay}"/>
                          </el-col>
                        </el-form-item>
                        <el-form-item :label="$t('job.trigger.title.period')">
                          <el-col :span="6">
                            <el-input-number v-model="item.period" :precision="0" :min="1" :step="1" controls-position="right"/>
                          </el-col>
                          <el-col :span="12" :offset="1">
                            <el-select v-model="item.timeUnit" :placeholder="$t('job.trigger.placeholder.timeUnit')">
                              <el-option
                                v-for="ele in fixedTimeUnits"
                                :key="ele.value"
                                :label="ele.label"
                                :value="ele.value"/>
                            </el-select>
                          </el-col>
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
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { listReq, removeReq, addReq, updateReq } from '@/api/job/baseJob'
import waves from '@/directive/waves' // Waves directive
// import { parseTime } from '@/utils'
import Pagination from '@/components/Pagination' // Secondary package based on el-pagination
import { load } from '@/constant'
import CronInput from 'vue-cron-generator/src/components/cron-input'
import { DEFAULT_CRON_EXPRESSION } from 'vue-cron-generator/src/constant/filed'

export default {
  name: 'Jobs',
  components: {
    Pagination,
    CronInput
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
      realTimeUnits: [],
      fixedTimeUnits: [],
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
      rules: {
        baseRules: {
          jobName: [{ required: true, trigger: 'blur' }],
          displayName: [{ required: true, trigger: 'blur' }],
          status: [{ required: true, trigger: 'blur' }]
        }
      },
      downloadLoading: false,
      selections: [],
      calendars: [],
      cronTriggers: [
        {
          expression: DEFAULT_CRON_EXPRESSION
        }
      ],
      realTimeTriggers: [
        {
          calendar: null,
          startTime: null
        }
      ],
      fixedTimeTriggers: [
        {
          calendar: null,
          startTime: null
        }
      ],
      cron: this.resetCron()
    }
  },
  created() {
    this.loadConst()
    this.setFormRules()
    this.search()
  },
  methods: {
    setFormRules() {
      this.rules.baseRules.jobName[0].message = this.$t('job.rules.jobName')
      this.rules.baseRules.displayName[0].message = this.$t('job.rules.displayName')
      this.rules.baseRules.status[0].message = this.$t('job.rules.status')
    },
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
      if (!this.selections || !this.selections.length || !this.selections[0].jobName) {
        this.job = {
          jobName: undefined,
          displayName: '',
          status: 0,
          remark: ''
        }
      } else {
        this.job = this.selections[0]
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
      this.$nextTick(() => {
        this.$refs['baseForm'].clearValidate()
      })
    },
    update(row) {
      this.selectRow(row)
      this.editDialog.oper = 'update'
      // this.job = Object.assign({}, row) // copy obj
      this.editDialog.title = this.$t('actions.update')
      this.editDialog.base.visible = true
      this.$nextTick(() => {
        this.$refs['baseForm'].clearValidate()
      })
    },
    save() {
      this.$refs.baseForm.validate((valid) => {
        if (valid) {
          const request = (this.editDialog.oper === 'add' ? addReq(this.job) : updateReq(this.job))
          // this.job.jobContent = JSON.stringify(this.httpJobConfig)
          request.then(res => {
            this.$message.success(res.data.msg)
            this.editDialog.base.visible = false
            this.search()
          })
        }
      })
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
      if (row && row.jobName) {
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
        this.realTimeUnits = array.realTimeUnits
        this.fixedTimeUnits = array.fixedTimeUnits
        this.calendars = array.calendars
      })
    },
    openDesignDialog(row) {
      this.editDialog.title = this.$t('job.actions.design')
      this.selectRow(row)
      this.openDesignJobFlow()
    },
    openTriggerDialog(row) {
      // this.editDialog.title = this.$t('job.actions.design')
      this.selectRow(row)
      this.editDialog.trigger.visible = true
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
      const obj = {
        cron: DEFAULT_CRON_EXPRESSION
      }
      this.cronTriggers.push(obj)
    },
    removeCronRow(index) {
      this.cronTriggers.splice(index, 1)
    },
    addRealRow() {
      const obj = {
        calendar: null
      }
      this.realTimeTriggers.push(obj)
    },
    removeRealRow(index) {
      this.realTimeTriggers.splice(index, 1)
    },
    addFixedRow() {
      const obj = {
        calendar: null
      }
      this.fixedTimeTriggers.push(obj)
    },
    removeFixedRow(index) {
      this.fixedTimeTriggers.splice(index, 1)
    },
    changeCron(cron) {
      this.cron = cron
    },
    resetCron(cron) {
      this.cron = DEFAULT_CRON_EXPRESSION
    }
  }
}
</script>

<style lang="scss">
  .el-form-item--mini.el-form-item, .el-form-item--small.el-form-item {
    margin-bottom: 8px;
  }

  .form {
    &-layout {
      width: 500px;
      min-height: 350px;
      margin-top: 20px;
      margin-left: 50px;
    }
  }

  .table-expand {
    font-size: 0;
  }

  .table-expand label {
    width: 150px;
    color: #99a9bf;
  }

  .table-expand .el-form-item {
    margin-right: 0;
    margin-bottom: 0;
    width: 25%;
  }

  .trigger{
    &-row{
      &-top{
        margin-top: 5px;
      }
    }
  }
</style>
