<template>
  <div>
    <el-row :gutter="10">
      <el-col :span="10">
        <el-card>
          <div slot="header">
            <span>{{ $t('application.program.title.programList') }}</span>
          </div>
          <div class="filter-container">
            <el-input v-model="page.programName" :placeholder="$t('application.program.columns.programName')" class="filter-item search-input" @keyup.enter.native="search" />
            <el-button class="filter-item" type="primary" icon="el-icon-search" @click="search" />
            <el-button class="filter-item" style="margin-left: 10px;" type="success" icon="el-icon-plus" @click="update(null)" />
            <el-button :disabled="!selections || !selections.length" class="filter-item" style="margin-left: 10px;" type="danger" icon="el-icon-delete" @click="remove" />
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
          >
            <el-table-column type="expand">
              <template slot-scope="props">
                <el-form label-position="left" inline class="table-expand">
                  <el-form-item :label="$t('columns.createTime')">
                    <span>{{ props.row.createTime | parseTime }}</span>
                  </el-form-item>
                </el-form>
              </template>
            </el-table-column>
            <el-table-column
              type="selection"
              width="45"
            />
            <el-table-column :label="$t('application.program.columns.programName')" align="center" min-width="100px">
              <template slot-scope="scope">
                <el-link type="primary" @click="update(scope.row)">{{ scope.row.programName }}</el-link>
              </template>
            </el-table-column>
            <el-table-column :label="$t('application.program.columns.injectOrder')" align="center" min-width="100px">
              <template slot-scope="scope">
                <span>{{ scope.row.injectOrder }}</span>
              </template>
            </el-table-column>
          </el-table>

          <pagination v-show="page.total>0" :total="page.total" :page.sync="page.currentPage" :limit.sync="page.pageSize" @pagination="search" />

        </el-card>
      </el-col>
      <el-col v-if="selections && selections.length === 1" :span="14">
        <el-card>
          <div slot="header">
            <span>{{ selections && selections.length === 1 ? selections[0].programName : '' }}{{ $t('application.program.title.packageList') }}</span>
          </div>
          <div class="filter-container">
            <el-input v-model="packagePage.packageName" :placeholder="$t('application.program.columns.packageName')" class="filter-item search-input" @keyup.enter.native="searchPackage" />
            <el-button class="filter-item" type="primary" icon="el-icon-search" @click="searchPackage" />
            <el-button :disabled="!packageSelections || !packageSelections.length" class="filter-item" type="danger" icon="el-icon-delete" @click="removePackage" />
            <el-upload style="display: inline; margin-left: 10px;" action="" accept=".jar" :on-change="upload" :auto-upload="false" :show-file-list="false">
              <el-button class="filter-item" type="success" icon="el-icon-upload2">{{ $t('actions.upload') }}</el-button>
            </el-upload>
          </div>

          <el-table
            ref="packageTables"
            v-loading="packageListLoading"
            :data="packageList"
            border
            fit
            highlight-current-row
            style="width: 100%;"
            @selection-change="handlePackageSelectionChange"
            @cell-click="clickPackageCell"
          >
            <el-table-column
              type="selection"
              width="45"
            />
            <el-table-column :label="$t('application.program.columns.packageName')" align="center" min-width="100px">
              <template slot-scope="scope">
                <el-link type="info" @click="openPackageCategory(scope.row)">{{ scope.row.packageName }}</el-link>
              </template>
            </el-table-column>
            <el-table-column :label="$t('application.program.columns.uploadTime')" align="center" min-width="100px">
              <template slot-scope="scope">
                <span>{{ scope.row.uploadTime | parseTime }}</span>
              </template>
            </el-table-column>
            <el-table-column :label="$t('application.program.columns.loadTime')" align="center" min-width="100px">
              <template slot-scope="scope">
                <span>{{ scope.row.loadTime | parseTime }}</span>
              </template>
            </el-table-column>
            <el-table-column :label="$t('application.program.columns.unloadTime')" align="center" min-width="100px">
              <template slot-scope="scope">
                <span>{{ scope.row.unloadTime | parseTime }}</span>
              </template>
            </el-table-column>
            <el-table-column :label="$t('actions.handle')" min-width="150px">
              <template slot-scope="scope">
                <el-button type="primary" @click="downloadPackage(scope.row)">{{ $t('actions.download') }}</el-button>
                <el-button v-if="scope.row.loadTime && !scope.row.unloadTime" type="danger" @click="unloadPackage(scope.row)">{{ $t('actions.unload') }}</el-button>
                <el-button v-if="(!scope.row.loadTime && !scope.row.unloadTime) || (scope.row.loadTime && scope.row.unloadTime)" type="success" @click="loadPackage(scope.row)">{{ $t('actions.load') }}</el-button>
              </template>
            </el-table-column>
          </el-table>

          <pagination v-show="packagePage.total>0" :total="packagePage.total" :page.sync="packagePage.currentPage" :limit.sync="packagePage.pageSize" @pagination="searchPackage" />
        </el-card>
      </el-col>
    </el-row>

    <el-dialog :title="editDialog.title" :visible.sync="editDialog.base.visible" :center="true" :modal="true" :close-on-click-modal="false" :close-on-press-escape="false" :before-close="close">
      <div v-if="editDialog.base.visible">
        <el-form ref="form" :rules="formRules" :model="program" label-position="right" label-width="150px" class="form-layout">
          <el-form-item :label="$t('application.program.columns.programName')" prop="programName">
            <el-input v-model="program.programName" :placeholder="$t('application.program.placeholder.programName')" />
          </el-form-item>
          <el-form-item :label="$t('application.program.columns.injectOrder')" prop="injectOrder">
            <el-input-number v-model="program.injectOrder" :precision="0" :min="0" :step="1" />
          </el-form-item>
          <el-form-item>
            <el-button type="success" @click="save">{{ $t('actions.save') }}</el-button>
          </el-form-item>
        </el-form>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { listReq, removeReq, addReq, updateReq, listPackageReq, removePackageReq, uploadPackageReq, loadPackageReq, unloadPackageReq, downloadPackageReq } from '@/api/application/program'
import Pagination from '@/components/Pagination'
import { buildMsg, download } from '@/utils/tools'

const DEF_OBJ = {
  programName: null,
  injectOrder: 0
}
export default {
  name: 'program',
  components: { Pagination },
  data() {
    return {
      tableKey: 0,
      list: null,
      packageList: null,
      listLoading: true,
      packageListLoading: true,
      page: {
        currentPage: 1,
        pageSize: 10,
        total: 0,
        programName: undefined
      },
      packagePage: {
        currentPage: 1,
        pageSize: 10,
        total: 0,
        programName: null
      },
      editDialog: {
        oper: undefined,
        title: undefined,
        base: {
          visible: false
        }
      },
      formRules: {
        programName: [
          { required: true, trigger: 'blur' }
        ],
        injectOrder: [
          { required: true, trigger: 'blur' }
        ]
      },
      program: DEF_OBJ,
      programPackage: null,
      selections: [],
      packageSelections: []
    }
  },
  watch: {
    searchKey(val) {
      this.$refs.tree.filter(val)
    }
  },
  created() {
    this.setFormRules()
    this.search()
  },
  methods: {
    upload(file) {
      this.$confirm(this.$t('tip.confirm') + ' ' + file.name, this.$t('tip.confirmMsg'), { type: 'warning' })
        .then(() => {
          const currentSize = Math.ceil(file.size / 1024 / 1024)
          const maxSize = 20
          if (currentSize > maxSize) {
            this.$message.error(this.$t('tip.fileSizeOutOfBound') + maxSize + 'M,' + this.$t('tip.currentFileSize') + currentSize + 'M')
            return
          }
          const data = new FormData()
          data.append('file', file.raw)
          data.append('programName', this.program.programName)
          uploadPackageReq(data).then(res => {
            this.$message.success(res.data.msg)
            this.searchPackage()
          })
        })
    },
    downloadPackage(row) {
      this.$confirm(this.$t('tip.confirm'), this.$t('tip.confirmMsg'), { type: 'warning' })
        .then(() => {
          downloadPackageReq({ id: row.id }).then((res) => {
            download(res.data, row.packageName)
          })
        })
    },
    loadPackage(row) {
      this.$confirm(this.$t('tip.confirm'), this.$t('tip.confirmMsg'), { type: 'warning' })
        .then(() => {
          loadPackageReq({ id: row.id }).then(res => {
            this.$message.success(res.data.msg)
            this.searchPackage()
          })
        })
    },
    unloadPackage(row) {
      this.$confirm(this.$t('tip.confirm'), this.$t('tip.confirmMsg'), { type: 'warning' })
        .then(() => {
          unloadPackageReq({ id: row.id }).then(res => {
            this.$message.success(res.data.msg)
            this.searchPackage()
          })
        })
    },
    openPackageCategory(row) {
      const route = {
        name: 'package',
        params: {
          key: row.id
        }
      }
      this.$router.push(route)
    },
    setFormRules() {
      this.formRules.programName[0].message = this.$t('application.program.rules.programName')
      this.formRules.injectOrder[0].message = this.$t('application.program.rules.injectOrder')
    },
    search() {
      this.listLoading = true
      listReq(this.page).then(response => {
        this.list = response.data.result.list
        Object.assign(this.page, response.data.result.page)
        this.listLoading = false
      })
    },
    searchPackage() {
      this.packageListLoading = true
      this.packagePage.programName = this.program.programName
      listPackageReq(this.packagePage).then(response => {
        this.packageList = response.data.result.list
        Object.assign(this.packagePage, response.data.result.page)
        this.packageListLoading = false
      })
    },
    update(row) {
      if (row == null) {
        this.editDialog.oper = 'add'
        this.editDialog.title = this.$t('actions.add')
      } else {
        this.editDialog.oper = 'update'
        this.editDialog.title = this.$t('actions.update')
      }
      this.selectRow(row)
      this.editDialog.base.visible = true
      this.$nextTick(() => {
        this.$refs['form'].clearValidate()
      })
    },
    save() {
      this.$refs.form.validate((valid) => {
        if (valid) {
          const request = (this.editDialog.oper === 'add' ? addReq(this.program) : updateReq(this.program))
          request.then(res => {
            this.$message.success(res.data.msg)
            this.editDialog.base.visible = false
            this.search()
          })
        }
      })
    },
    remove() {
      const programNames = []
      if (this.selections.length) {
        this.selections.forEach((sel) => {
          programNames.push(sel.programName)
        })
      } else {
        this.$message.warning(this.$t('tip.selectData'))
        return
      }
      this.$confirm(buildMsg(this, programNames), this.$t('tip.confirmMsg'), { type: 'warning' })
        .then(() => {
          removeReq({ programNames: programNames }).then(res => {
            this.$message.success(res.data.msg)
            this.search()
          })
        })
    },
    removePackage() {
      const ids = []
      if (this.packageSelections.length) {
        this.packageSelections.forEach((sel) => {
          ids.push(sel.id)
        })
      } else {
        this.$message.warning(this.$t('tip.selectData'))
        return
      }
      this.$confirm(this.$t('tip.confirm'), this.$t('tip.confirmMsg'), { type: 'warning' })
        .then(() => {
          removePackageReq({ ids: ids }).then(res => {
            this.$message.success(res.data.msg)
            this.searchPackage()
          })
        })
    },
    clickCell(row, column, cell, event) {
      this.selectRow(row)
    },
    handleSelectionChange(val) {
      this.selections = val
    },
    selectRow(row) {
      this.$refs.tables.clearSelection()
      if (row && row.programName) {
        this.$refs.tables.toggleRowSelection(row, true)
      }
      this.reset() // get the newest or reset to origin
    },
    reset() {
      if (!this.selections || !this.selections.length || !this.selections[0].programName) {
        this.program = Object.assign({}, DEF_OBJ)
      } else {
        this.program = Object.assign({}, this.selections[0])
        this.searchPackage()
      }
    },
    clickPackageCell(row, column, cell, event) {
      this.selectPackageRow(row)
    },
    handlePackageSelectionChange(val) {
      this.packageSelections = val
    },
    selectPackageRow(row) {
      this.$refs.packageTables.clearSelection()
      if (row && row.id) {
        this.$refs.packageTables.toggleRowSelection(row, true)
      }
      this.resetPackage() // get the newest or reset to origin
    },
    resetPackage() {
      if (!this.packageSelections || !this.packageSelections.length || !this.packageSelections[0].id) {
        this.programPackage = null
      } else {
        this.programPackage = this.packageSelections[0]
      }
    },
    close() {
      this.editDialog.base.visible = false
    }
  }
}
</script>
