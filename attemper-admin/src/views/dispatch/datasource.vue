<template>
  <div class="app-container">
    <div class="filter-container">
      <el-input v-model="page.dbName" :placeholder="$t('dispatch.datasource.columns.dbName')" class="filter-item search-input" @keyup.enter.native="search" />
      <el-select v-model="page.driverClassName" :placeholder="$t('dispatch.datasource.columns.driverClassName')" clearable collapse-tags class="filter-item search-select">
        <el-option v-for="item in databases" :key="item.value" :label="item.label" :value="item.value">
          <span style="float: left">{{ item.label }}</span>
          <span style="float: right; color: #8492a6; font-size: 13px; margin-left: 8px;">{{ item.value }}</span>
        </el-option>
      </el-select>
      <el-input v-model="page.jdbcUrl" :placeholder="$t('dispatch.datasource.columns.jdbcUrl')" class="filter-item search-input" @keyup.enter.native="search" />
      <el-input v-model="page.userName" :placeholder="$t('dispatch.datasource.columns.userName')" class="filter-item search-input" @keyup.enter.native="search" />
      <el-button v-waves class="filter-item" type="primary" icon="el-icon-search" @click="search" />
      <el-button class="filter-item" style="margin-left: 10px;" type="success" icon="el-icon-plus" @click="update(null)" />
      <el-button :disabled="!selections || !selections.length" class="filter-item" style="margin-left: 10px;" type="danger" icon="el-icon-delete" @click="remove" />
      <el-button style="float: right" :disabled="!selections || !selections.length" class="filter-item table-external-button" type="warning" @click="testConnection">
        {{ $t('actions.connectTest') }}
      </el-button>
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
      <el-table-column
        type="selection"
        width="45"
      />
      <el-table-column :label="$t('dispatch.datasource.columns.dbName')" prop="id" sortable="custom" align="center" min-width="100px">
        <template slot-scope="scope">
          <span>{{ scope.row.dbName }}</span>
        </template>
      </el-table-column>
      <el-table-column :label="$t('dispatch.datasource.columns.driverClassName')" align="center" min-width="120px">
        <template slot-scope="scope">
          <span>{{ formatType(scope.row.driverClassName) }}</span>
        </template>
      </el-table-column>
      <el-table-column :label="$t('dispatch.datasource.columns.jdbcUrl')" min-width="150px">
        <template slot-scope="scope">
          <span>{{ scope.row.jdbcUrl }}</span>
        </template>
      </el-table-column>
      <el-table-column :label="$t('dispatch.datasource.columns.userName')" width="100px">
        <template slot-scope="scope">
          <span>{{ scope.row.userName }}</span>
        </template>
      </el-table-column>
      <el-table-column :label="$t('actions.handle')" align="center" width="230" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button type="primary" @click="update(scope.row)">{{ $t('actions.update') }}</el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination v-show="page.total>0" :total="page.total" :page.sync="page.currentPage" :limit.sync="page.pageSize" @pagination="search" />

    <el-dialog :title="editDialog.title" :visible.sync="editDialog.base.visible" :center="true" :modal="true" :close-on-click-modal="false" :close-on-press-escape="false" :before-close="close">
      <div v-show="editDialog.base.visible">
        <el-form ref="form" :rules="formRules" :model="datasource" label-position="right" label-width="150px" class="form-layout">
          <el-form-item :label="$t('dispatch.datasource.columns.dbName')" prop="dbName">
            <el-input v-model="datasource.dbName" :placeholder="$t('dispatch.datasource.placeholder.dbName')" />
          </el-form-item>
          <el-form-item :label="$t('dispatch.datasource.columns.driverClassName')">
            <el-select v-model="datasource.driverClassName" style="width: 100%;">
              <el-option v-for="item in databases" :key="item.label" :value="item.value" :label="item.label">
                <span style="float: left">{{ item.label }}</span>
                <span style="float: right; color: #8492a6; font-size: 13px; margin-left: 8px;">{{ item.value }}</span>
              </el-option>
            </el-select>
          </el-form-item>
          <el-form-item :label="$t('dispatch.datasource.columns.jdbcUrl')" prop="jdbcUrl">
            <el-input v-model="datasource.jdbcUrl" :placeholder="$t('dispatch.datasource.placeholder.jdbcUrl')" />
          </el-form-item>
          <el-form-item :label="$t('dispatch.datasource.columns.userName')" prop="userName">
            <el-input v-model="datasource.userName" :placeholder="$t('dispatch.datasource.placeholder.userName')" />
          </el-form-item>
          <el-form-item :label="$t('dispatch.datasource.columns.password')" prop="password">
            <el-input v-model="datasource.password" type="password" :placeholder="$t('dispatch.datasource.placeholder.password')" />
          </el-form-item>
          <el-form-item :label="$t('dispatch.datasource.columns.attribute')" prop="attribute">
            <el-input v-model="datasource.attribute" type="textarea" :autosize="{ minRows: 1, maxRows: 5}" :placeholder="$t('dispatch.datasource.placeholder.attribute')" />
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
import { listReq, getReq, removeReq, addReq, updateReq, testConnectionReq } from '@/api/dispatch/datasource'
import waves from '@/directive/waves'
import Pagination from '@/components/Pagination'
import { buildMsg } from '@/utils/tools'
const DEF_OBJ = {
  dbName: null,
  driverClassName: null,
  jdbcUrl: null,
  userName: null,
  password: null,
  attribute: null
}
export default {
  name: 'datasource',
  components: { Pagination },
  directives: { waves },
  data() {
    return {
      tableKey: 0,
      list: null,
      listLoading: true,
      page: {
        currentPage: 1,
        pageSize: 10,
        total: 0,
        dbName: undefined,
        driverClassName: undefined,
        jdbcUrl: undefined,
        userName: undefined,
        sort: 'DB_NAME'
      },
      editDialog: {
        oper: undefined,
        title: undefined,
        base: {
          visible: false
        }
      },
      formRules: {
        dbName: [
          { required: true, trigger: 'blur' }
        ],
        jdbcUrl: [
          { required: true, trigger: 'blur' }
        ],
        attribute: [
          { required: false, trigger: 'blur', range: { max: 255 }}
        ]
      },
      datasource: DEF_OBJ,
      databases: [],
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
      this.formRules.dbName[0].message = this.$t('dispatch.datasource.rules.dbName')
      this.formRules.jdbcUrl[0].message = this.$t('dispatch.datasource.rules.jdbcUrl')
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
      if (prop === 'dbName') {
        this.sort(order)
      }
    },
    sort(order) {
      if (order === 'ascending') {
        this.page.sort = 'DB_NAME'
      } else {
        this.page.sort = 'DB_NAME DESC'
      }
      this.search()
    },
    testConnection() {
      const dbNames = []
      if (this.selections.length) {
        this.selections.forEach((sel) => {
          dbNames.push(sel.dbName)
        })
      } else {
        this.$message.warning(this.$t('tip.selectData'))
        return
      }
      this.$confirm(buildMsg(this, dbNames), this.$t('tip.confirmMsg'), { type: 'warning' })
        .then(() => {
          testConnectionReq({ dbNames: dbNames }).then(res => {
            const errorDbs = []
            const successDbs = []
            res.data.result.forEach(item => {
              if (item.errorMsg) {
                errorDbs.push(item)
              } else {
                successDbs.push(item)
              }
            })
            const h = this.$createElement
            this.$msgbox({
              title: this.$t('dispatch.datasource.tip.testResult'),
              customClass: 'msgbox-layout',
              message: h('div', null, [
                h('div', { style: 'display : ' + (errorDbs.length > 0 ? 'block' : 'none') }, [
                  h('div', { style: 'color: #F56C6C' }, this.$t('dispatch.datasource.tip.testError')),
                  h('ul', errorDbs.map(item => {
                    return h('li', [
                      h('div', { style: 'color: #F56C6C' }, item.dbName),
                      h('div', null, item.errorMsg)])
                  }))
                ]),
                h('div', { style: 'display : ' + (successDbs.length > 0 ? 'block' : 'none') }, [
                  h('div', { style: 'color: #67C23A' }, this.$t('dispatch.datasource.tip.testSuccess')),
                  h('ul', successDbs.map(item => {
                    return h('li', { style: 'color: #67C23A' }, item.dbName)
                  }))
                ])
              ])
            })
          })
        })
    },
    reset() {
      if (!this.selections || !this.selections.length || !this.selections[0].dbName) {
        this.datasource = Object.assign({}, DEF_OBJ)
      } else {
        getReq({ dbName: this.selections[0].dbName }).then(res => {
          this.datasource = res.data.result
        })
      }
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
          const request = (this.editDialog.oper === 'add' ? addReq(this.datasource) : updateReq(this.datasource))
          request.then(res => {
            this.$message.success(res.data.msg)
            this.editDialog.base.visible = false
            this.search()
          })
        }
      })
    },
    remove() {
      const dbNames = []
      if (this.selections.length) {
        this.selections.forEach((sel) => {
          dbNames.push(sel.dbName)
        })
      } else {
        this.$message.warning(this.$t('tip.selectData'))
        return
      }
      this.$confirm(buildMsg(this, dbNames), this.$t('tip.confirmMsg'), { type: 'warning' })
        .then(() => {
          removeReq({ dbNames: dbNames }).then(res => {
            this.$message.success(res.data.msg)
            this.search()
          })
        })
    },
    selectRow(row) {
      this.$refs.tables.clearSelection()
      if (row && row.dbName) {
        this.$refs.tables.toggleRowSelection(row, true)
      }
      this.reset() // get the newest or reset to origin
    },
    clickCell(row, column, cell, event) {
      this.selectRow(row)
    },
    handleSelectionChange(val) {
      this.selections = val
    },
    close() {
      this.editDialog.base.visible = false
    },
    formatType(item) {
      for (let i = 0; i < this.databases.length; i++) {
        const option = this.databases[i]
        if (option.value === item) {
          return option.label + ' - ' + option.value
        }
      }
      return item
    },
    loadConst() {
      import(`@/constant/common.js`).then((array) => {
        this.databases = array.databases
      })
    }
  }
}
</script>

<style lang="scss">

  .msgbox-layout {
    width: 600px;
  }

</style>
