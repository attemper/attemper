<template>
  <div class="app-container">
    <div class="filter-container">
      <el-input :placeholder="$t('job.columns.jobName')" v-model="page.jobName" style="width: 100px;" class="filter-item" @keyup.enter.native="handleFilter"/>
      <el-input :placeholder="$t('job.columns.displayName')" v-model="page.displayName" style="width: 100px;" class="filter-item" @keyup.enter.native="handleFilter"/>
      <el-select v-model="page.status" :placeholder="$t('job.columns.status')" clearable class="filter-item" style="width: 130px">
        <el-option v-for="item in statusOptions" :key="item.value" :label="item.text" :value="item.value"/>
      </el-select>
      <!--<el-select v-model="page.sort" style="width: 140px" class="filter-item" @change="handleFilter">
        <el-option v-for="item in sortOptions" :key="item.key" :label="item.label" :value="item.key"/>
      </el-select>-->
      <el-button v-waves class="filter-item" type="primary" icon="el-icon-search" @click="handleFilter">{{ $t('actions.search') }}</el-button>
      <el-button class="filter-item" style="margin-left: 10px;" type="success" icon="el-icon-plus" @click="handleAdd">{{ $t('actions.add') }}</el-button>
      <el-button class="filter-item" style="margin-left: 10px;" type="danger" icon="el-icon-delete" @click="handleRemove">{{ $t('actions.remove') }}</el-button>
      <el-button v-waves :loading="downloadLoading" class="filter-item" type="primary" icon="el-icon-download" @click="handleDownload">{{ $t('actions.export') }}</el-button>
      <el-checkbox v-model="showCreateTime" class="filter-item" style="margin-left:15px;" @change="tableKey=tableKey+1">{{ $t('job.columns.createTime') }}</el-checkbox>
    </div>

    <el-table
      v-loading="listLoading"
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
      <el-table-column :label="$t('job.columns.status')" :filters="statusOptions" :filter-method="filterStatus" class-name="status-col" width="100">
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

    <pagination v-show="total>0" :total="total" :page.sync="page.currentPage" :limit.sync="page.pageSize" @pagination="search" />

    <el-dialog :title="editDialog.title" :visible.sync="editDialog.visible">
      <el-form ref="form" :rules="rules" :model="temp" label-position="left" label-width="150px" style="width: 500px; margin-left:50px;">
        <el-form-item :label="$t('job.columns.jobName')" prop="jobName">
          <el-input v-model="temp.jobName" :placeholder="$t('job.placeholder.jobName')"/>
        </el-form-item>
        <el-form-item :label="$t('job.columns.displayName')" prop="displayName">
          <el-input v-model="temp.displayName" :placeholder="$t('job.placeholder.displayName')"/>
        </el-form-item>
        <el-form-item :label="$t('job.columns.status')">
          <el-select v-model="temp.status" :placeholder="$t('job.placeholder.status')" class="filter-item">
            <el-option v-for="item in statusOptions" :key="item.value" :label="item.text" :value="item.value"/>
          </el-select>
        </el-form-item>
        <el-form-item :label="$t('job.columns.remark')">
          <el-input :autosize="{ minRows: 2, maxRows: 4}" v-model="temp.remark" :placeholder="$t('job.placeholder.remark')" type="textarea"/>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="editDialog.visible = false">{{ $t('actions.cancel') }}</el-button>
        <el-button type="primary" @click="save">{{ $t('actions.save') }}</el-button>
      </div>
    </el-dialog>

  </div>
</template>

<script>
import { listReq/* , getReq*/, removeReq, addReq, updateReq } from '@/api/job/baseJob'
import waves from '@/directive/waves' // Waves directive
import { parseTime } from '@/utils'
import Pagination from '@/components/Pagination' // Secondary package based on el-pagination

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
        status: undefined,
        sort: 'JOB_NAME'
      },
      importanceOptions: [1, 2, 3],
      statusOptions: [
        {
          text: this.$t('job.status.enable'),
          value: 0
        },
        {
          text: this.$t('job.status.disable'),
          value: 1
        },
        {
          text: this.$t('job.status.transient'),
          value: 2
        },
        {
          text: this.$t('job.status.disposible'),
          value: 3
        }
      ],
      sortOptions: [{ label: this.$t('job.sort.nameAsc'), key: 'JOB_NAME' }, { label: this.$t('job.sort.nameDesc'), key: 'JOB_NAME DESC' }],
      showCreateTime: false,
      temp: {
        jobName: undefined,
        displayName: '',
        jobType: 10,
        status: 0,
        remark: ''
      },
      editDialog: {
        oper: undefined,
        title: undefined,
        visible: false
      },
      rules: {
        jobName: [{ required: true, trigger: 'blur' }],
        displayName: [{ required: true, trigger: 'blur' }],
        status: [{ required: true, trigger: 'blur' }]
      },
      downloadLoading: false,
      selections: []
    }
  },
  created() {
    this.rules.jobName[0].message = this.$t('job.rules.jobName')
    this.rules.displayName[0].message = this.$t('job.rules.displayName')
    this.rules.status[0].message = this.$t('job.rules.status')
    this.search()
  },
  methods: {
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
    resetTemp() {
      this.temp = {
        jobName: undefined,
        displayName: '',
        status: 0,
        remark: ''
      }
    },
    handleAdd() {
      this.resetTemp()
      this.editDialog.oper = 'add'
      this.editDialog.title = this.$t('actions.add')
      this.editDialog.visible = true
      this.$nextTick(() => {
        this.$refs['form'].clearValidate()
      })
    },
    save() {
      this.$refs.form.validate((valid) => {
        if (valid) {
          const request = (this.editDialog.oper === 'add' ? addReq(this.temp) : updateReq(this.temp))
          request.then(res => {
            this.$message.success(res.data.msg)
            this.editDialog.visible = false
            this.search()
          })
        }
      })
    },
    handleUpdate(row) {
      this.editDialog.oper = 'update'
      this.temp = Object.assign({}, row) // copy obj
      this.editDialog.title = this.$t('actions.update')
      this.editDialog.visible = true
      this.$nextTick(() => {
        this.$refs['form'].clearValidate()
      })
    },
    handleRemove(row) {
      const jobNames = []
      if (row && row.jobName) {
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
    handleDownload() {
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
    },
    formatStatus(status) {
      for (let i = 0; i < this.statusOptions.length; i++) {
        const statusOption = this.statusOptions[i]
        if (statusOption.value === status) {
          return statusOption.text
        }
      }
      return status
    },
    filterStatus(value, row) {
      return row.status === value
    },
    handleSelectionChange(val) {
      this.selections = val
    }
  }
}
</script>
