<template>
  <div class="app-container">
    <div class="filter-container">
      <el-input :placeholder="$t('job.columns.jobName')" v-model="page.jobName" style="width: 100px;" class="filter-item" @keyup.enter.native="handleFilter"/>
      <el-input :placeholder="$t('job.columns.displayName')" v-model="page.displayName" style="width: 100px;" class="filter-item" @keyup.enter.native="handleFilter"/>
      <el-select v-model="page.status" :placeholder="$t('job.columns.status')" clearable class="filter-item" style="width: 130px">
        <el-option v-for="item in jobStatuses" :key="item.value" :label="item.text" :value="item.value"/>
      </el-select>
      <!--<el-select v-model="page.sort" style="width: 140px" class="filter-item" @change="handleFilter">
        <el-option v-for="item in sortOptions" :key="item.key" :label="item.label" :value="item.key"/>
      </el-select>-->
      <el-button v-waves class="filter-item" type="primary" icon="el-icon-search" @click="handleFilter">{{ $t('actions.search') }}</el-button>
      <el-button class="filter-item" style="margin-left: 10px;" type="success" icon="el-icon-plus" @click="handleAdd">{{ $t('actions.add') }}</el-button>
      <el-button class="filter-item" style="margin-left: 10px;" type="danger" icon="el-icon-delete" @click="handleRemove">{{ $t('actions.remove') }}</el-button>
      <!--<el-button v-waves :loading="downloadLoading" class="filter-item" type="primary" icon="el-icon-download" @click="handleDownload">{{ $t('actions.export') }}</el-button>-->
      <el-checkbox v-model="showCreateTime" class="filter-item" style="margin-left:15px;" @change="tableKey=tableKey+1">{{ $t('job.columns.createTime') }}</el-checkbox>
    </div>

    <el-table
      v-loading="listLoading"
      ref="jobTable"
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
      <el-table-column :label="$t('job.columns.displayName')" align="center" min-width="150px">
        <template slot-scope="scope">
          <span>{{ scope.row.displayName }}</span>
        </template>
      </el-table-column>
      <el-table-column :label="$t('job.columns.status')" :filters="jobStatuses" :filter-method="filterStatus" class-name="status-col" width="100">
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
          <el-button type="primary" size="mini" icon="el-icon-edit" @click="handleUpdate(scope.row)"/>
          <el-button type="danger" size="mini" icon="el-icon-delete" @click="handleRemove(scope.row)"/>
        </template>
      </el-table-column>
    </el-table>

    <pagination v-show="page.total>0" :total="page.total" :page.sync="page.currentPage" :limit.sync="page.pageSize" @pagination="search" />

    <el-dialog :title="editDialog.title" :visible.sync="editDialog.visible" :center="true" :modal="true" :close-on-click-modal="false" :close-on-press-escape="false">
      <el-steps :active="editDialog.currentStep">
        <el-step :title="$t('job.steps.base')"/>
        <el-step :title="$t('job.steps.job')"/>
        <el-step :title="$t('job.steps.arg')"/>
        <el-step :title="$t('job.steps.trigger')"/>
      </el-steps>
      <div class="setting">
        <el-form v-show="editDialog.currentStep === 1" ref="baseForm" :rules="baseRules" :model="job" label-position="left" label-width="150px" class="form-layout">
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
            <el-input :autosize="{ minRows: 2, maxRows: 4}" v-model="job.remark" :placeholder="$t('job.placeholder.remark')" type="textarea"/>
          </el-form-item>
        </el-form>
        <div v-show="editDialog.currentStep === 2" style="text-align: center" >
          <el-input :placeholder="$t('job.columns.jobName')" v-model="subJob.page.jobName" style="width: 100px;" class="filter-item" @keyup.enter.native="handleSubJobFilter"/>
          <el-input :placeholder="$t('job.columns.displayName')" v-model="subJob.page.displayName" style="width: 100px;" class="filter-item" @keyup.enter.native="handleSubJobFilter"/>
          <el-select v-model="subJob.page.status" :placeholder="$t('job.columns.status')" clearable class="filter-item" style="width: 130px">
            <el-option v-for="item in jobStatuses" :key="item.value" :label="item.text" :value="item.value"/>
          </el-select>
          <el-button v-waves class="filter-item" type="primary" icon="el-icon-search" @click="handleSubJobFilter">{{ $t('actions.search') }}</el-button>
          <el-button v-waves :disabled="!subJob.selections || !subJob.selections.length" class="filter-item" type="primary" icon="el-icon-edit" @click="subJob.setDialog = true">{{ $t('job.groupJob.subJob.actions.updateSubJobs') }}</el-button>
          <el-button v-waves :disabled="!subJob.selections || !subJob.selections.length || subJob.selections.every(cell => !cell.priority)" class="filter-item" type="danger" icon="el-icon-delete" @click="removeSubJobs">{{ $t('job.groupJob.subJob.actions.removeSubJobs') }}</el-button>
          <el-table
            v-loading="subJob.listLoading"
            ref="subJobTable"
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
      <div slot="footer" class="dialog-footer">
        <el-row>
          <el-col :span="4" :offset="2">
            <el-button :disabled="editDialog.currentStep === 1" type="primary" @click="last">{{ $t('actions.last') }}</el-button>
          </el-col>
          <el-col :span="4" :offset="3">
            <el-button type="info" @click="editDialog.visible = false">{{ $t('actions.cancel') }}</el-button>
          </el-col>
          <el-col :span="4">
            <el-button type="success" @click="save">{{ $t('actions.save') }}</el-button>
          </el-col>
          <el-col :span="4" :offset="3">
            <el-button :disabled="editDialog.currentStep === 4" type="primary" @click="next">{{ $t('actions.next') }}</el-button>
          </el-col>
        </el-row>
      </div>
    </el-dialog>

  </div>
</template>

<script>
import { listReq, getReq, removeReq, addReq, updateReq } from '@/api/job/baseJob'
import { atomAndSubJobsListReq, updateSubReq, removeSubReq } from '@/api/job/groupJob'
import waves from '@/directive/waves' // Waves directive
import Pagination from '@/components/Pagination' // Secondary package based on el-pagination
import { load } from '@/constant'

export default {
  name: 'GroupJob1',
  components: { Pagination },
  directives: { waves },
  filters: {
    statusFilter(status) {
      const statusMap = {
        0: 'success',
        1: 'danger',
        2: 'warning',
        3: ''
      }
      return statusMap[status]
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
        jobType: 1,
        status: undefined,
        sort: 'JOB_NAME'
      },
      selections: [],
      subJob: {
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
          lowerJobType: 10,
          jobName: undefined,
          displayName: undefined,
          jobType: 11,
          status: undefined
        },
        subJobDatas: [] // struct like {jobName: 'myJob', priority: 1}
      },
      jobStatuses: [],
      showCreateTime: false,
      job: {
        jobName: undefined,
        displayName: '',
        jobType: 1,
        status: 0,
        remark: '',
        jobContent: ''
      },
      editDialog: {
        oper: undefined,
        title: undefined,
        visible: false,
        currentStep: 1
      },
      baseRules: {
        jobName: [{ required: true, trigger: 'blur' }],
        displayName: [{ required: true, trigger: 'blur' }],
        status: [{ required: true, trigger: 'blur' }]
      },
      downloadLoading: false
    }
  },
  created() {
    this.loadConst()
    this.setFormRules()
    this.search()
  },
  methods: {
    setFormRules() {
      this.baseRules.jobName[0].message = this.$t('job.rules.jobName')
      this.baseRules.displayName[0].message = this.$t('job.rules.displayName')
      this.baseRules.status[0].message = this.$t('job.rules.status')
    },
    search() {
      this.listLoading = true
      listReq(this.page).then(response => {
        this.list = response.data.result.list
        this.page.total = response.data.result.page.total
        this.listLoading = false
        // Just to simulate the time of the request
        /* setTimeout(() => {
          this.listLoading = false
        }, 200)*/
      })
    },
    handleFilter() {
      this.page.currentPage = 1
      this.search()
    },
    handleSubJobFilter() {
      this.subJob.page.currentPage = 1
      this.restJob()
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
      this.handleFilter()
    },
    handleAdd() {
      this.editDialog.oper = 'add'
      this.selectRow(null)
      this.reset() // to set to origin
      this.editDialog.title = this.$t('actions.add')
      this.editDialog.visible = true
      this.$nextTick(() => {
        this.$refs['baseForm'].clearValidate()
      })
    },
    save() {
      this.$refs.baseForm.validate((valid) => {
        if (valid) {
          if (this.job.jobType === 11) {
            this.$refs.subJobForm.validate((valid1) => {
              if (valid1) {
                const request = (this.editDialog.oper === 'add' ? addReq(this.job) : updateReq(this.job))
                this.job.jobContent = JSON.stringify(this.subJobConfig)
                request.then(res => {
                  this.$message.success(res.data.msg)
                  this.editDialog.visible = false
                  this.search()
                })
              }
            })
          } // else if other job
        }
      })
    },
    handleUpdate(row) {
      this.selectRow(row)
      this.editDialog.oper = 'update'
      this.reset() // get the newest
      // this.job = Object.assign({}, row) // copy obj
      this.editDialog.title = this.$t('actions.update')
      this.editDialog.visible = true
      this.$nextTick(() => {
        this.$refs['baseForm'].clearValidate()
      })
    },
    handleRemove(row) {
      const jobNames = []
      if (row && row.jobName) {
        this.selectRow(row) // just select the row
        jobNames.push(row.jobName)
      } else if (this.selections.length) {
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
            this.handleFilter()
          })
        })
    },
    formatStatus(status) {
      for (let i = 0; i < this.jobStatuses.length; i++) {
        const statusOption = this.jobStatuses[i]
        if (statusOption.value === status) {
          return statusOption.text
        }
      }
      return status
    },
    filterStatus(value, row) {
      return row.status === value
    },
    selectRow(row) {
      this.$refs.jobTable.clearSelection()
      if (row && row.jobName) {
        this.$refs.jobTable.toggleRowSelection(row, true)
      }
    },
    handleSelectionChange(val) {
      this.selections = val
    },
    loadConst() {
      load(`./array/${this.$store.state.app.language}.js`).then((array) => {
        this.jobTypes = array.jobTypes
        this.jobStatuses = array.jobStatuses
        this.allocateTitles = array.allocateTitles
        this.transferTitles = array.transferTitles
      })
    },
    last() {
      this.editDialog.currentStep--
      this.reset()
    },
    next() {
      this.editDialog.currentStep++
      this.reset()
    },
    reset() {
      if (this.editDialog.currentStep === 1) {
        this.resetBase()
      } else if (this.editDialog.currentStep === 2) {
        this.restJob()
      }
    },
    resetBase() {
      if (!this.selections || !this.selections.length || !this.selections[0].jobName) {
        this.job = {
          jobName: undefined,
          displayName: '',
          jobType: 1,
          status: 0,
          remark: ''
        }
      } else {
        getReq({ jobName: this.selections[0].jobName }).then(res => {
          this.job = res.data.result
        })
      }
    },
    restJob() {
      // this.subJobDatas = []
      if (this.selections && this.selections.length && this.selections[0].jobName) {
        this.subJob.page.groupName = this.selections[0].jobName
      }
      this.subJob.listLoading = true
      atomAndSubJobsListReq(this.subJob.page).then(res => {
        this.subJob.list = res.data.result.list
        this.subJob.page.total = res.data.result.page.total
        setTimeout(() => {
          this.subJob.listLoading = false
        }, 200)
      })
    },
    handleSubJobSelectionChange(val) {
      this.subJob.selections = val
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
        this.handleSubJobFilter()
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
            this.handleSubJobFilter()
          })
        })
    }
  }
}
</script>

<style lang="scss">
  .setting{
    height: 500px;
    margin-top: 30px;
  }
  .form{
    &-layout{
      // width: 500px;
      // min-height: 350px;
      margin-left:50px;
    }
  }
</style>
