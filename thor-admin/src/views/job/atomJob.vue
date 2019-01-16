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
      <el-table-column :label="$t('job.columns.displayName')" min-width="150px">
        <template slot-scope="scope">
          <span>{{ scope.row.displayName }}</span>
        </template>
      </el-table-column>
      <el-table-column :label="$t('job.columns.status')" :filters="jobStatuses" :filter-method="filterStatus" align="center" class-name="status-col" width="100">
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
          <el-button type="primary" size="mini" @click="openDesignDialog(scope.row)">{{ $t('job.atomJob.actions.design') }}</el-button>
          <el-button type="primary" size="mini" icon="el-icon-edit" @click="handleUpdate(scope.row)"/>
          <el-button type="danger" size="mini" icon="el-icon-delete" @click="handleRemove(scope.row)"/>
        </template>
      </el-table-column>
    </el-table>

    <pagination v-show="total>0" :total="total" :page.sync="page.currentPage" :limit.sync="page.pageSize" @pagination="search" />

    <el-dialog :title="editDialog.title" :visible.sync="editDialog.visible" :center="true" :modal="true" :close-on-click-modal="false" :close-on-press-escape="false">
      <el-steps :active="editDialog.currentStep">
        <el-step :title="$t('job.steps.base')"/>
        <el-step :title="$t('job.steps.arg')"/>
        <el-step :title="$t('job.steps.trigger')"/>
      </el-steps>
      <el-form v-show="editDialog.currentStep === 1" ref="baseForm" :rules="baseRules" :model="job" label-position="left" label-width="150px" class="form-layout">
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
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-row>
          <el-col :span="4" :offset="2">
            <el-button :disabled="editDialog.currentStep === 1" type="primary" @click="editDialog.currentStep--">{{ $t('actions.last') }}</el-button>
          </el-col>
          <el-col :span="4" :offset="3">
            <el-button type="info" @click="editDialog.visible = false">{{ $t('actions.cancel') }}</el-button>
          </el-col>
          <el-col :span="4">
            <el-button type="success" @click="save">{{ $t('actions.save') }}</el-button>
          </el-col>
          <el-col :span="4" :offset="3">
            <el-button :disabled="editDialog.currentStep === 4" type="primary" @click="editDialog.currentStep++">{{ $t('actions.next') }}</el-button>
          </el-col>
        </el-row>
      </div>
    </el-dialog>

    <el-dialog :title="$t('job.atomJob.designDialog.title')" :visible.sync="designDialog.visible" :center="true" :modal="true" :close-on-click-modal="false" :close-on-press-escape="false">
      <el-form v-show="job.jobType === 11" ref="httpJobForm" :rules="httpJobRules" :model="httpJobConfig" label-position="left" label-width="150px" class="form-layout">
        <el-form-item :label="$t('job.atomJob.httpJob.jobConfig.columns.requestMethod')" prop="requestMethod">
          <el-select v-model="httpJobConfig.requestMethod" :placeholder="$t('job.atomJob.httpJob.jobConfig.placeholder.requestMethod')" class="filter-item">
            <el-option v-for="item in requestMethods" :key="item.value" :label="item.label" :value="item.value"/>
          </el-select>
        </el-form-item>
        <el-form-item :label="$t('job.atomJob.httpJob.jobConfig.columns.uri')" prop="uri">
          <el-input v-model="httpJobConfig.uri" :placeholder="$t('job.atomJob.httpJob.jobConfig.placeholder.uri')"/>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-row>
          <el-col :span="4" :offset="6">
            <el-button type="info" @click="designDialog.visible = false">{{ $t('actions.cancel') }}</el-button>
          </el-col>
          <el-col :span="4" :offset="3">
            <el-button type="success" @click="saveDesign">{{ $t('actions.save') }}</el-button>
          </el-col>
        </el-row>
      </div>
    </el-dialog>

  </div>
</template>

<script>
import { listReq, removeReq, addReq, updateReq } from '@/api/job/baseJob'
import { updateJobConfigReq } from '@/api/job/atomJob'
import waves from '@/directive/waves' // Waves directive
// import { parseTime } from '@/utils'
import Pagination from '@/components/Pagination' // Secondary package based on el-pagination
import { load } from '@/constant'

export default {
  name: 'AtomJob',
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
      total: 0,
      listLoading: true,
      page: {
        lowerJobType: 10,
        currentPage: 1,
        pageSize: 10,
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
        visible: false,
        currentStep: 1
      },
      designDialog: {
        visible: false
      },
      baseRules: {
        jobName: [{ required: true, trigger: 'blur' }],
        displayName: [{ required: true, trigger: 'blur' }],
        jobType: [{ required: true, trigger: 'blur' }],
        status: [{ required: true, trigger: 'blur' }]
      },
      httpJobRules: {
        requestMethod: [{ required: true, trigger: 'blur' }],
        uri: [{ required: true, trigger: 'blur' }]
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
      this.baseRules.jobName[0].message = this.$t('job.rules.jobName')
      this.baseRules.displayName[0].message = this.$t('job.rules.displayName')
      this.baseRules.status[0].message = this.$t('job.rules.status')
      this.baseRules.jobType[0].message = this.$t('job.rules.jobType')
      this.httpJobRules.requestMethod[0].message = this.$t('job.atomJob.httpJob.jobConfig.rules.requestMethod')
      this.httpJobRules.uri[0].message = this.$t('job.atomJob.httpJob.jobConfig.rules.uri')
    },
    search() {
      this.listLoading = true
      listReq(this.page).then(response => {
        this.list = response.data.result.list
        this.total = response.data.result.page.total
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
    handleAdd() {
      this.editDialog.oper = 'add'
      this.selectRow(null)
      this.editDialog.title = this.$t('actions.add')
      this.editDialog.visible = true
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
            this.editDialog.visible = false
            this.search()
          })
        }
      })
    },
    handleUpdate(row) {
      this.selectRow(row)
      this.editDialog.oper = 'update'
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
      this.reset() // get the newest or reset to origin
    },
    handleSelectionChange(val) {
      this.selections = val
    },
    loadConst() {
      load(`./array/${this.$store.state.app.language}.js`).then((array) => {
        this.jobTypes = array.jobTypes
        this.jobStatuses = array.jobStatuses
        this.requestMethods = array.requestMethods
      })
    },
    openDesignDialog(row) {
      this.designDialog.visible = true
      this.selectRow(row)

      this.resetJobConfig()
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
              this.handleFilter()
            })
          }
        })
      } // else if other job
    },
    resetJobConfig() {
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
