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
            <el-button v-waves class="filter-item" type="primary" icon="el-icon-search" @click="search">{{ $t('actions.search') }}</el-button>
            <el-button class="filter-item" style="margin-left: 10px;" type="success" icon="el-icon-plus" @click="update(null)">{{ $t('actions.add') }}</el-button>
            <el-button :disabled="!selections || !selections.length" class="filter-item" style="margin-left: 10px;" type="danger" icon="el-icon-delete" @click="remove">{{ $t('actions.remove') }}</el-button>
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
                    <span>{{ props.row.createTime }}</span>
                  </el-form-item>
                </el-form>
              </template>
            </el-table-column>
            <el-table-column
              type="selection"
              width="40"
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
      <el-col v-show="selections && selections.length === 1" :span="14">
        <el-card>
          <div slot="header">
            <span>{{ selections && selections.length === 1 ? selections[0].programName : '' }}{{ $t('application.program.title.packageList') }}</span>
          </div>
          <div class="filter-container">
            <el-input v-model="packagePage.packageName" :placeholder="$t('application.program.columns.packageName')" class="filter-item search-input" @keyup.enter.native="searchPackage" />
            <el-button v-waves class="filter-item" type="primary" icon="el-icon-search" @click="searchPackage">{{ $t('actions.search') }}</el-button>
            <el-button :disabled="!packageSelections || !packageSelections.length" class="filter-item" type="danger" icon="el-icon-delete" @click="removePackage">{{ $t('actions.remove') }}</el-button>
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
              width="40"
            />
            <el-table-column :label="$t('application.program.columns.packageName')" align="center" min-width="100px">
              <template slot-scope="scope">
                <el-link type="info" @click="showPackageCategory(scope.row)">{{ scope.row.packageName }}</el-link>
              </template>
            </el-table-column>
            <el-table-column :label="$t('application.program.columns.uploadTime')" align="center" min-width="100px">
              <template slot-scope="scope">
                <span>{{ scope.row.uploadTime }}</span>
              </template>
            </el-table-column>
            <el-table-column :label="$t('application.program.columns.loadTime')" align="center" min-width="100px">
              <template slot-scope="scope">
                <span>{{ scope.row.loadTime }}</span>
              </template>
            </el-table-column>
            <el-table-column :label="$t('application.program.columns.unloadTime')" align="center" min-width="100px">
              <template slot-scope="scope">
                <span>{{ scope.row.unloadTime }}</span>
              </template>
            </el-table-column>
            <el-table-column :label="$t('actions.handle')" align="center" width="200">
              <template slot-scope="scope">
                <el-button type="primary" @click="downloadPackage(scope.row)">{{ $t('actions.download') }}</el-button>
                <el-button v-if="scope.row.loadTime && !scope.row.unloadTime" type="danger" @click="unloadPackage(scope.row)">{{ $t('actions.unload') }}</el-button>
                <el-button v-if="!scope.row.loadTime && !scope.row.unloadTime" type="success" @click="loadPackage(scope.row)">{{ $t('actions.load') }}</el-button>
              </template>
            </el-table-column>
          </el-table>

          <pagination v-show="packagePage.total>0" :total="packagePage.total" :page.sync="packagePage.currentPage" :limit.sync="packagePage.pageSize" @pagination="searchPackage" />
        </el-card>
      </el-col>
    </el-row>

    <el-dialog :title="editDialog.title" :visible.sync="editDialog.base.visible || editDialog.category.visible" :center="true" :modal="true" :close-on-click-modal="false" :close-on-press-escape="false" :before-close="close">
      <div v-if="editDialog.base.visible">
        <el-form ref="form" :rules="formRules" :model="program" label-position="right" label-width="150px" class="form-layout">
          <el-form-item :label="$t('application.program.columns.programName')" prop="dbName">
            <el-input v-model="program.programName" :placeholder="$t('application.program.placeholder.programName')" />
          </el-form-item>
          <el-form-item :label="$t('application.program.columns.injectOrder')">
            <el-input-number v-model="program.injectOrder" :precision="0" :min="0" :step="1" />
          </el-form-item>
          <el-form-item>
            <el-button type="success" @click="save">{{ $t('actions.save') }}</el-button>
          </el-form-item>
        </el-form>
      </div>
      <div v-if="editDialog.category.visible">
        <el-input v-model="searchKey" style="margin-bottom: 10px; width: 200px;">
          <i v-show="searchKey.length > 0" slot="suffix" class="el-input__icon el-icon-close" @click="searchKey = ''" />
        </el-input>
        <el-tree
          ref="tree"
          :data="treeData"
          :filter-node-method="filterNode"
          node-key="fileName"
          :default-expand-all="canExpandAll"
        >
          <span slot-scope="{ node, data }" class="custom-tree-node">
            <span>
              <i :class="data.dir ? (node.expanded ? 'el-icon-folder-opened' : 'el-icon-folder') : (node.isCurrent ? 'el-icon-notebook-2' : 'el-icon-notebook-1')" />
              {{ data.fileName }}
              <span v-show="node.isCurrent && !data.dir" style="margin-left: 30px;">
                <i class="el-icon-view" @click="viewFile(data)" />
                <i style="margin-left: 10px;" class="el-icon-download" @click="downloadFile(data)" />
              </span>
            </span>
          </span>
        </el-tree>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { listReq, removeReq, addReq, updateReq, listPackageReq, removePackageReq, uploadPackageReq, listPackageCategoryReq, downloadPackageReq, viewFileReq, downloadFileReq } from '@/api/application/program'
import waves from '@/directive/waves'
import Pagination from '@/components/Pagination'
import { buildMsg, download } from '@/utils/tools'
const DEF_OBJ = {
  programName: null,
  injectOrder: 0
}
export default {
  name: 'program',
  components: { Pagination },
  directives: { waves },
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
        },
        category: {
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
      packageSelections: [],
      treeData: [],
      searchKey: '',
      canExpandAll: false
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
          const maxSize = 50
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
      //
    },
    unloadPackage(row) {
      //
    },
    viewFile(fileInfo) {
      viewFileReq({ filePath: fileInfo.filePath }).then((res) => {
        console.log(res.data.result)
      })
    },
    downloadFile(fileInfo) {
      this.$confirm(this.$t('tip.confirm'), this.$t('tip.confirmMsg'), { type: 'warning' })
        .then(() => {
          downloadFileReq({ filePath: fileInfo.filePath }).then((res) => {
            download(res.data, fileInfo.fileName)
          })
        })
    },
    showPackageCategory(row) {
      this.searchKey = ''
      listPackageCategoryReq({ id: row.id }).then(res => {
        const sourceList = res.data.result
        let root
        for (let i = 0; i < sourceList.length; i++) {
          const item = sourceList[i]
          if (!item.parentFileName) {
            root = item
            break
          }
        }
        this.createTree(sourceList, root)
        this.treeData = [root]
        this.canExpandAll = (sourceList.length < 30)
        this.editDialog.category.visible = true
      })
    },
    createTree(sourceList, cell) {
      for (let i = 0; i < sourceList.length; i++) {
        const item = sourceList[i]
        if (item.parentFileName === cell.fileName) {
          if (!cell.children) {
            cell.children = []
          }
          cell.children.push(item)
          this.createTree(sourceList, item)
        }
      }
    },
    filterNode(value, data) {
      if (!value) {
        return true
      }
      return data.fileName.indexOf(value) !== -1
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
      this.editDialog.base.visible =
        this.editDialog.category.visible = false
    }
  }
}
</script>
