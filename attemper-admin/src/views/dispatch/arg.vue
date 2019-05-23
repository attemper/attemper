<template>
  <div class="app-container">
    <div class="filter-container">
      <el-input v-model="page.argName" :placeholder="$t('dispatch.arg.columns.argName')" class="filter-item search-input" @keyup.enter.native="search" />
      <el-select v-model="page.argType" :placeholder="$t('dispatch.arg.columns.argType')" clearable collapse-tags class="filter-item search-select">
        <el-option v-for="item in argTypes" :key="item.value" :label="item.label" :value="item.value" />
      </el-select>
      <el-input v-model="page.argValue" :placeholder="$t('dispatch.arg.columns.argValue')" class="filter-item search-input" @keyup.enter.native="search" />
      <el-button v-waves class="filter-item" type="primary" icon="el-icon-search" @click="search">{{ $t('actions.search') }}</el-button>
      <el-button class="filter-item" style="margin-left: 10px;" type="success" icon="el-icon-plus" @click="update(null)">{{ $t('actions.add') }}</el-button>
      <el-button :disabled="!selections || !selections.length" class="filter-item" style="margin-left: 10px;" type="danger" icon="el-icon-delete" @click="remove">{{ $t('actions.remove') }}</el-button>
      <!--<el-button v-waves :loading="downloadLoading" class="filter-item" type="primary" icon="el-icon-download" @click="handleDownload">{{ $t('actions.export') }}</el-button>-->
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
      @sort-change="sortChange"
    >
      <el-table-column
        type="selection"
        width="40"
      />
      <el-table-column :label="$t('dispatch.arg.columns.argName')" prop="id" sortable="custom" align="center" min-width="100px">
        <template slot-scope="scope">
          <span>{{ scope.row.argName }}</span>
        </template>
      </el-table-column>
      <el-table-column :label="$t('dispatch.arg.columns.argType')" align="center" width="100">
        <template slot-scope="scope">
          <span>{{ formatType(scope.row.argType) }}</span>
        </template>
      </el-table-column>
      <el-table-column :label="$t('dispatch.arg.columns.argValue')" min-width="150px">
        <template slot-scope="scope">
          <span>{{ scope.row.argValue }}</span>
        </template>
      </el-table-column>
      <el-table-column :label="$t('columns.remark')" min-width="150px">
        <template slot-scope="scope">
          <span>{{ scope.row.remark }}</span>
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
        <el-form ref="form" :rules="formRules" :model="arg" label-position="right" label-width="150px" class="form-layout">
          <el-form-item :label="$t('dispatch.arg.columns.argName')" prop="argName">
            <el-input v-model="arg.argName" :placeholder="$t('dispatch.arg.placeholder.argName')" />
          </el-form-item>
          <el-form-item :label="$t('dispatch.arg.columns.argType')">
            <el-row>
              <el-col :span="isRaw || isSql ? 15 : 24">
                <el-select v-model="arg.argType" style="width: 100%;" @change="argTypeChanged">
                  <el-option v-for="item in argTypes" :key="item.label" :value="item.value" :label="item.label" />
                </el-select>
              </el-col>
              <el-col v-if="isRaw" :span="8" :offset="1">
                <el-select v-model="arg.genericType" :placeholder="$t('dispatch.arg.placeholder.genericType')">
                  <el-option v-for="item in genericTypes" :key="item.label" :value="item.value" :label="item.label" />
                </el-select>
              </el-col>
              <el-col v-if="isSql" :span="8" :offset="1">
                <el-select v-model="arg.dbName" :placeholder="$t('dispatch.datasource.placeholder.dbName')">
                  <el-option v-for="item in dataSources" :key="item.dbName" :value="item.dbName" :label="item.dbName" />
                </el-select>
              </el-col>
            </el-row>
          </el-form-item>
          <el-form-item :label="$t('dispatch.arg.columns.argValue')" prop="argValue">
            <string-input v-if="arg.argType === 0" v-model="arg.argValue" :placeholder="$t('dispatch.arg.placeholder.argValue')" />
            <boolean-input v-else-if="arg.argType === 1" ref="booleanInput" v-model="arg.argValue" @change="change" />
            <number-input v-else-if="arg.argType === 2" v-model="arg.argValue" :min="-2147483648" :max="2147483647" :step="1" :precision="0" />
            <number-input v-else-if="arg.argType === 3" v-model="arg.argValue" :min="4.9000000e-324" :max="1.797693e+308" :step="1" :precision="5" />
            <number-input v-else-if="arg.argType === 4" v-model="arg.argValue" :min="-9223372036854774808" :max="9223372036854774807" :step="1" :precision="0" />
            <date-input v-else-if="arg.argType === 10" v-model="arg.argValue" />
            <time-input v-else-if="arg.argType === 11" v-model="arg.argValue" />
            <date-time-input v-else-if="arg.argType === 12" v-model="arg.argValue" />
            <list-input v-else-if="arg.argType === 20" ref="listInput" v-model="arg.argValue" :generic-type="arg.genericType" @change="change" />
            <map-input v-else-if="arg.argType === 21" ref="mapInput" v-model="arg.argValue" :generic-type="arg.genericType" @change="change" />
            <string-input v-else v-model="arg.argValue" :placeholder="$t('dispatch.arg.placeholder.argValue')" />
          </el-form-item>
          <el-form-item :label="$t('columns.remark')">
            <el-input v-model="arg.remark" type="textarea" :autosize="{ minRows: 1, maxRows: 5}" :placeholder="$t('placeholders.remark')" />
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
import { listReq, getReq, removeReq, addReq, updateReq } from '@/api/dispatch/arg'
import * as dataSourceApi from '@/api/dispatch/datasource'
import waves from '@/directive/waves'
import Pagination from '@/components/Pagination'
import { load } from '@/constant'
import StringInput from './components/arg/StringInput'
import NumberInput from './components/arg/NumberInput'
import BooleanInput from './components/arg/BooleanInput'
import ListInput from './components/arg/ListInput'
import MapInput from './components/arg/MapInput'
import DateInput from './components/arg/DateInput'
import DateTimeInput from './components/arg/DateTimeInput'
import TimeInput from './components/arg/TimeInput'
const DEF_OBJ = {
  argName: null,
  argType: 0,
  argValue: null,
  genericType: 0,
  remark: null,
  dbName: null
}
export default {
  name: 'arg',
  components: { TimeInput, DateTimeInput, DateInput, MapInput, ListInput, BooleanInput, NumberInput, StringInput, Pagination },
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
        argName: undefined,
        argValue: undefined,
        argType: null,
        sort: 'ARG_NAME'
      },
      editDialog: {
        oper: undefined,
        title: undefined,
        base: {
          visible: false
        }
      },
      formRules: {
        argName: [
          { required: true, trigger: 'blur' }
        ],
        argValue: [
          { required: true, trigger: 'blur' }
        ],
        remark: [
          { required: false, trigger: 'blur', len: 255 }
        ]
      },
      arg: DEF_OBJ,
      argTypes: [],
      genericTypes: [],
      rawTypes: [],
      selections: [],
      dataSources: []
    }
  },
  computed: {
    isRaw() {
      return this.rawTypes.find(cell => cell.value === this.arg.argType)
    },
    isSql() {
      return this.arg.argType === 30
    }
  },
  created() {
    this.loadConst()
    this.setFormRules()
    this.search()
  },
  methods: {
    setFormRules() {
      this.formRules.argName[0].message = this.$t('dispatch.arg.rules.argName')
      this.formRules.argValue[0].message = this.$t('dispatch.arg.rules.argValue')
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
      if (prop === 'argName') {
        this.sort(order)
      }
    },
    sort(order) {
      if (order === 'ascending') {
        this.page.sort = 'ARG_NAME'
      } else {
        this.page.sort = 'ARG_NAME DESC'
      }
      this.search()
    },
    reset() {
      if (!this.selections || !this.selections.length || !this.selections[0].argName) {
        this.arg = DEF_OBJ
      } else {
        getReq({ argName: this.selections[0].argName }).then(res => {
          this.arg = res.data.result
          if (this.$refs.booleanInput) {
            this.$refs.booleanInput.initValue(this.arg.argValue)
          }
          if (this.$refs.listInput) {
            this.$refs.listInput.initValue(this.arg.argValue)
          }
          if (this.$refs.mapInput) {
            this.$refs.mapInput.initValue(this.arg.argValue)
          }
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
          const request = (this.editDialog.oper === 'add' ? addReq(this.arg) : updateReq(this.arg))
          request.then(res => {
            this.$message.success(res.data.msg)
            this.close()
            this.search()
          })
        }
      })
    },
    remove() {
      const argNames = []
      if (this.selections.length) {
        this.selections.forEach((sel) => {
          argNames.push(sel.argName)
        })
      } else {
        this.$message.warning(this.$t('tip.selectData'))
        return
      }
      const msg = '<p>' + this.$t('tip.confirmMsg') + ':<br><span style="color: red">' + argNames.join('<br>') + '</span></p>'
      this.$confirm(msg, this.$t('tip.confirm'), { type: 'warning', dangerouslyUseHTMLString: true })
        .then(() => {
          removeReq({ argNames: argNames }).then(res => {
            this.$message.success(res.data.msg)
            this.search()
          })
        })
    },
    selectRow(row) {
      this.$refs.tables.clearSelection()
      if (row && row.argName) {
        this.$refs.tables.toggleRowSelection(row, true)
      }
      this.reset() // get the newest or reset to origin
    },
    handleSelectionChange(val) {
      this.selections = val
    },
    close() {
      this.editDialog.base.visible = false
    },
    formatType(item) {
      for (let i = 0; i < this.argTypes.length; i++) {
        const option = this.argTypes[i]
        if (option.value === item) {
          return option.label
        }
      }
      return item
    },
    argTypeChanged() {
      if (this.arg.argType === 30) {
        dataSourceApi.listReq().then(res => {
          this.dataSources = res.data.result.list
        })
      }
    },
    change(val) {
      this.arg.argValue = val
    },
    loadConst() {
      load(`./common.js`).then((array) => {
        this.genericTypes = array.genericTypes
        this.rawTypes = array.rawTypes
        this.argTypes = array.argTypes
      })
    }
  }
}
</script>
