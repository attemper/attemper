<template>
  <div class="app-container">
    <div class="filter-container">
      <el-input :placeholder="$t('job.columns.jobName')" v-model="page.jobName" style="width: 100px;" class="filter-item" @keyup.enter.native="search"/>
      <el-input :placeholder="$t('job.columns.displayName')" v-model="page.displayName" style="width: 100px;" class="filter-item" @keyup.enter.native="search"/>
      <el-select v-model="page.status" :placeholder="$t('job.columns.status')" clearable class="filter-item" style="width: 130px">
        <el-option v-for="item in jobStatuses" :key="item.value" :label="item.text" :value="item.value"/>
      </el-select>
      <!--<el-select v-model="page.sort" style="width: 140px" class="filter-item" @change="search">
        <el-option v-for="item in sortOptions" :key="item.key" :label="item.label" :value="item.key"/>
      </el-select>-->
      <el-button v-waves class="filter-item" type="primary" icon="el-icon-search" @click="search">{{ $t('actions.search') }}</el-button>
      <el-button class="filter-item" style="margin-left: 10px;" type="success" icon="el-icon-plus" @click="add">{{ $t('actions.add') }}</el-button>
      <el-button :disabled="!selections || !selections.length" class="filter-item" style="margin-left: 10px;" type="danger" icon="el-icon-delete" @click="remove">{{ $t('actions.remove') }}</el-button>
      <!--<el-button v-waves :loading="downloadLoading" class="filter-item" type="primary" icon="el-icon-download" @click="handleDownload">{{ $t('actions.export') }}</el-button>-->
      <el-checkbox v-model="showCreateTime" class="filter-item" style="margin-left:15px;" @change="tableKey=tableKey+1">{{ $t('job.columns.createTime') }}</el-checkbox>
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
      <el-table-column
        type="selection"
        width="40"/>
      <el-table-column :label="$t('job.columns.jobName')" prop="jobName" sortable="custom" align="center" min-width="100px">
        <template slot-scope="scope">
          <span>{{ scope.row.jobName }}</span>
        </template>
      </el-table-column>
      <el-table-column :label="$t('job.columns.displayName')" min-width="150px">
        <template slot-scope="scope">
          <span>{{ scope.row.displayName }}</span>
        </template>
      </el-table-column>
      <el-table-column :label="$t('job.columns.jobType')" align="center" class-name="status-col" width="100">
        <template slot-scope="scope">
          <el-tag :type="scope.row.jobType | jobTypeFilter">{{ formatJobType(scope.row.jobType) }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column :label="$t('job.columns.status')" align="center" class-name="status-col" width="100">
        <template slot-scope="scope">
          <el-tag :type="scope.row.status | statusFilter">{{ formatStatus(scope.row.status) }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column v-if="showCreateTime" :label="$t('job.columns.createTime')" width="110px" align="center">
        <template slot-scope="scope">
          <span>{{ scope.row.createTime }}</span>
        </template>
      </el-table-column>
      <el-table-column :label="$t('job.columns.updateTime')" width="150px" align="center">
        <template slot-scope="scope">
          <span>{{ scope.row.updateTime }}</span>
        </template>
      </el-table-column>
      <el-table-column :label="$t('actions.handle')" align="center" width="230" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <div>
            <el-button type="primary" size="mini" @click="update(scope.row)">{{ $t('actions.update') }}</el-button>
            <el-button style="background-color:	#3CB371; border-color:	#3CB371; color: #fff;" size="mini" @click="openDesignDialog(scope.row)">{{ $t('job.actions.design') }}</el-button>
          </div>
          <div style="padding-top: 6px;">
            <el-button style="background-color:	#A4D3EE; border-color:	#A4D3EE; color: #fff;" size="mini">{{ $t('job.actions.param') }}</el-button>
            <el-button style="background-color:	#FFBBFF; border-color:	#FFBBFF; color: #fff;" size="mini">{{ $t('job.actions.trigger') }}</el-button>
          </div>
        </template>
      </el-table-column>
    </el-table>

    <pagination v-show="page.total>0" :total="page.total" :page.sync="page.currentPage" :limit.sync="page.pageSize" @pagination="search" />

    <el-dialog :title="editDialog.title" :visible.sync="editDialog.base.visible || editDialog.design.visible || editDialog.param.visible || editDialog.trigger.visible" :center="true" :modal="true" :close-on-click-modal="false" :close-on-press-escape="false" :before-close="close">
      <div v-show="editDialog.base.visible">
        <el-form ref="baseForm" :rules="rules.baseRules" :model="job" label-position="left" label-width="150px" class="form-layout">
          <el-form-item :label="$t('job.columns.jobName')" prop="jobName">
            <el-input v-model="job.jobName" :placeholder="$t('job.placeholder.jobName')"/>
          </el-form-item>
          <el-form-item :label="$t('job.columns.displayName')" prop="displayName">
            <el-input v-model="job.displayName" :placeholder="$t('job.placeholder.displayName')"/>
          </el-form-item>
          <el-form-item :label="$t('job.columns.jobType')" prop="jobType">
            <el-select v-model="job.jobType" :placeholder="$t('job.placeholder.jobType')" class="filter-item">
              <el-option v-for="item in jobTypes" :key="item.value" :label="item.label" :value="item.value"/>
            </el-select>
          </el-form-item>
          <el-form-item :label="$t('job.columns.status')" prop="status">
            <el-select v-model="job.status" :placeholder="$t('job.placeholder.status')" class="filter-item">
              <el-option v-for="item in jobStatuses" :key="item.value" :label="item.text" :value="item.value"/>
            </el-select>
          </el-form-item>
          <el-form-item :label="$t('job.columns.remark')">
            <el-input :autosize="{ minRows: 2, maxRows: 4}" v-model="job.remark" :placeholder="$t('job.placeholder.remark')" type="textarea"/>
          </el-form-item>
          <el-form-item>
            <el-button type="info" @click="editDialog.base.visible = false">{{ $t('actions.cancel') }}</el-button>
            <el-button type="success" @click="save">{{ $t('actions.save') }}</el-button>
          </el-form-item>
        </el-form>
      </div>
      <div v-show="editDialog.design.visible">
        <div v-show="job.jobType === 11" >
          <el-form ref="httpJobForm" :rules="rules.httpJobRules" :model="httpJobConfig" label-position="left" label-width="150px" class="form-layout">
            <el-form-item :label="$t('job.httpJob.jobConfig.columns.requestMethod')" prop="requestMethod">
              <el-select v-model="httpJobConfig.requestMethod" :placeholder="$t('job.httpJob.jobConfig.placeholder.requestMethod')" class="filter-item">
                <el-option v-for="item in requestMethods" :key="item.value" :label="item.label" :value="item.value"/>
              </el-select>
            </el-form-item>
            <el-form-item :label="$t('job.httpJob.jobConfig.columns.uri')" prop="uri">
              <el-input v-model="httpJobConfig.uri" :placeholder="$t('job.httpJob.jobConfig.placeholder.uri')"/>
            </el-form-item>
            <el-form-item>
              <el-button type="info" @click="editDialog.design.visible = false">{{ $t('actions.cancel') }}</el-button>
              <el-button type="success" @click="saveDesign">{{ $t('actions.save') }}</el-button>
            </el-form-item>
          </el-form>
        </div>
        <div v-show="job.jobType === 1">
          <el-input :placeholder="$t('job.columns.jobName')" v-model="subJob.page.jobName" style="width: 100px;" class="filter-item" @keyup.enter.native="searchSubJob"/>
          <el-input :placeholder="$t('job.columns.displayName')" v-model="subJob.page.displayName" style="width: 100px;" class="filter-item" @keyup.enter.native="searchSubJob"/>
          <el-select v-model="subJob.page.status" :placeholder="$t('job.columns.status')" clearable class="filter-item" style="width: 130px">
            <el-option v-for="item in jobStatuses" :key="item.value" :label="item.text" :value="item.value"/>
          </el-select>
          <el-button v-waves class="filter-item" type="primary" icon="el-icon-search" @click="searchSubJob">{{ $t('actions.search') }}</el-button>
          <el-button v-waves :disabled="!subJob.selections || !subJob.selections.length" class="filter-item" type="primary" icon="el-icon-edit" @click="subJob.setDialog = true">{{ $t('job.groupJob.subJob.actions.updateSubJobs') }}</el-button>
          <el-button v-waves :disabled="!subJob.selections || !subJob.selections.length || subJob.selections.every(cell => !cell.priority)" class="filter-item" type="danger" icon="el-icon-delete" @click="removeSubJobs">{{ $t('job.groupJob.subJob.actions.removeSubJobs') }}</el-button>
          <el-table
            v-loading="subJob.listLoading"
            ref="subtables"
            :key="subJob.tableKey"
            :data="subJob.list"
            border
            fit
            highlight-current-row
            style="width: 100%; margin-top: 20px;"
            @selection-change="handleSubJobSelectionChange">
            <el-table-column
              type="selection"
              width="40"/>
            <el-table-column :label="$t('job.columns.jobName')" prop="jobName" align="center" min-width="120px">
              <template slot-scope="scope">
                <span>{{ scope.row.jobName }}</span>
              </template>
            </el-table-column>
            <el-table-column :label="$t('job.columns.displayName')" align="center" min-width="150px">
              <template slot-scope="scope">
                <span>{{ scope.row.displayName }}</span>
              </template>
            </el-table-column>
            <el-table-column :label="$t('job.columns.status')" class-name="status-col" width="100px">
              <template slot-scope="scope">
                <el-tag :type="scope.row.status | statusFilter">{{ formatStatus(scope.row.status) }}</el-tag>
              </template>
            </el-table-column>
            <el-table-column :label="$t('job.groupJob.subJob.columns.priority')" class-name="status-col" width="100px">
              <template slot-scope="scope">
                <span>
                  {{ scope.row.priority }}
                </span>
              </template>
            </el-table-column>
          </el-table>
          <pagination v-show="subJob.page.total>0" :total="subJob.page.total" :page.sync="subJob.page.currentPage" :limit.sync="subJob.page.pageSize" @pagination="search" />
          <el-dialog
            :visible.sync="subJob.setDialog"
            :center="true"
            :modal="true"
            :close-on-click-modal="false"
            :title="$t('job.groupJob.subJob.innerDialog.title')"
            width="25%"
            style="padding-top: 50px;"
            append-to-body>
            <el-form label-position="left" label-width="100px">
              <el-form-item :label="$t('job.groupJob.subJob.innerDialog.label')">
                <el-input-number v-model="subJob.tempPriority" :min="1" :precision="0" size="medium"/>
                <el-button type="success" style="margin-left: 20px;" @click="updateSubJobs">{{ $t('actions.ok') }}</el-button>
              </el-form-item>
            </el-form>
          </el-dialog>
        </div>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { listReq, removeReq, addReq, updateReq } from '@/api/job/baseJob'
import { updateJobConfigReq } from '@/api/job/atomJob'
import { atomAndSubJobsListReq, updateSubReq, removeSubReq } from '@/api/job/groupJob'
import waves from '@/directive/waves' // Waves directive
// import { parseTime } from '@/utils'
import Pagination from '@/components/Pagination' // Secondary package based on el-pagination
import { load } from '@/constant'

export default {
  name: 'AtomJob',
  components: { Pagination },
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
    },
    jobTypeFilter(item) {
      const map = {
        0: 'success',
        1: 'primary',
        11: 'warning'
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
        jobType: undefined,
        status: undefined,
        sort: 'JOB_NAME'
      },
      jobStatuses: [],
      jobTypes: [],
      requestMethods: [],
      /* sortOptions: [{ label: this.$t('job.sort.nameAsc'), key: 'JOB_NAME' }, { label: this.$t('job.sort.nameDesc'), key: 'JOB_NAME DESC' }],*/
      showCreateTime: false,
      job: {
        jobName: undefined,
        displayName: '',
        jobType: 11,
        status: 0,
        remark: '',
        jobContent: ''
      },
      httpJobConfig: {
        requestMethod: 'POST',
        uri: ''
      },
      editDialog: {
        oper: undefined,
        title: undefined,
        base: {
          visible: false
        },
        design: {
          visible: false
        },
        param: {
          visible: false
        },
        trigger: {
          visible: false
        }
      },
      subJob: {
        visible: false,
        tableKey: 0,
        list: null,
        listLoading: true,
        selections: [],
        setDialog: false,
        tempPriority: 1,
        page: {
          groupName: undefined,
          currentPage: 1,
          pageSize: 10,
          total: 0,
          jobName: undefined,
          displayName: undefined,
          jobType: 11,
          status: undefined
        }
      },
      rules: {
        baseRules: {
          jobName: [{ required: true, trigger: 'blur' }],
          displayName: [{ required: true, trigger: 'blur' }],
          jobType: [{ required: true, trigger: 'blur' }],
          status: [{ required: true, trigger: 'blur' }]
        },
        httpJobRules: {
          requestMethod: [{ required: true, trigger: 'blur' }],
          uri: [{ required: true, trigger: 'blur' }]
        }
      },
      downloadLoading: false,
      selections: []
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
      this.rules.baseRules.jobType[0].message = this.$t('job.rules.jobType')
      this.rules.httpJobRules.requestMethod[0].message = this.$t('job.httpJob.jobConfig.rules.requestMethod')
      this.rules.httpJobRules.uri[0].message = this.$t('job.httpJob.jobConfig.rules.uri')
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
    searchSubJob() {
      if (this.selections && this.selections.length && this.selections[0].jobName) {
        this.subJob.page.groupName = this.selections[0].jobName
      }
      this.subJob.listLoading = true
      atomAndSubJobsListReq(this.subJob.page).then(res => {
        this.subJob.list = res.data.result.list
        this.subJob.page = res.data.result.page
        setTimeout(() => {
          this.subJob.listLoading = false
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
          jobType: 11,
          status: 0,
          remark: ''
        }
      } else {
        this.job = this.selections[0]
      }
    },
    close() {
      this.editDialog.base.visible =
        this.editDialog.design.visible =
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
    formatJobType(item) {
      for (let i = 0; i < this.jobTypes.length; i++) {
        const option = this.jobTypes[i]
        if (option.value === item) {
          return option.label
        }
      }
      return item
    },
    filterStatus(value, row) {
      return row.status === value
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
    handleSubJobSelectionChange(val) {
      this.subJob.selections = val
    },
    loadConst() {
      load(`./array/${this.$store.state.app.language}.js`).then((array) => {
        this.jobTypes = array.jobTypes
        this.jobStatuses = array.jobStatuses
        this.requestMethods = array.requestMethods
      })
    },
    openDesignDialog(row) {
      this.editDialog.design.visible = true
      this.selectRow(row)
      if (row.jobType === 11) {
        this.resetHttpJob()
      } else if (row.jobType === 1) {
        this.searchSubJob()
      }
    },
    saveDesign() {
      if (this.job.jobType === 11) {
        this.$refs.httpJobForm.validate((valid1) => {
          if (valid1) {
            const data = {
              jobName: this.job.jobName,
              jobContent: JSON.stringify(this.httpJobConfig)
            }
            updateJobConfigReq(data).then(res => {
              this.$message.success(res.data.msg)
              this.designDialog.visible = false
              this.search()
            })
          }
        })
      } // else if other job
    },
    resetHttpJob() {
      if (!this.selections || !this.selections.length || !this.selections[0].jobName || !this.job.jobContent || !this.job.jobContent.length) {
        if (this.job.jobType === 11) {
          this.httpJobConfig = {
            requestMethod: 'POST',
            uri: ''
          }
        }
      } else {
        if (this.job.jobType === 11) { // http job
          this.httpJobConfig = JSON.parse(this.job.jobContent)
        } // else if other job
      }
    },
    updateSubJobs() {
      const jobNames = []
      this.subJob.selections.forEach(sel => {
        jobNames.push(sel.jobName)
      })
      const data = {
        groupName: this.selections[0].jobName,
        jobNames: jobNames,
        priority: this.subJob.tempPriority
      }
      updateSubReq(data).then(res => {
        this.$message.success(res.data.msg)
        this.searchSubJob()
        this.subJob.setDialog = false
      })
    },
    removeSubJobs() {
      this.$confirm('', this.$t('tip.confirm'), { type: 'warning', dangerouslyUseHTMLString: true })
        .then(() => {
          const jobNames = []
          this.subJob.selections.forEach(sel => {
            jobNames.push(sel.jobName)
          })
          const data = {
            groupName: this.selections[0].jobName,
            jobNames: jobNames
          }
          removeSubReq(data).then(res => {
            this.$message.success(res.data.msg)
            this.searchSubJob()
          })
        })
    }
  }
}
</script>

<style lang="scss">
  .form{
    &-layout{
      width: 500px;
      min-height: 350px;
      margin-top: 20px;
      margin-left:50px;
    }
  }
</style>
