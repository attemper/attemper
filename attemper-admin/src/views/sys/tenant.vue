<template>
  <div class="app-container">
    <div class="filter-container">
      <el-input v-model="page.userName" :placeholder="$t('sys.tenant.columns.userName')" class="filter-item search-input" @keyup.enter.native="search" />
      <el-input v-model="page.displayName" :placeholder="$t('columns.displayName')" class="filter-item search-input" @keyup.enter.native="search" />
      <el-button v-waves class="filter-item" type="primary" icon="el-icon-search" @click="search">{{ $t('actions.search') }}</el-button>
      <el-button v-access="'tenant-add'" class="filter-item" style="margin-left: 10px;" type="success" icon="el-icon-plus" @click="update(null)">{{ $t('actions.add') }}</el-button>
      <el-button v-access="'tenant-remove'" :disabled="!selections || !selections.length" class="filter-item" style="margin-left: 10px;" type="danger" icon="el-icon-delete" @click="remove">{{ $t('actions.remove') }}</el-button>
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
      <el-table-column :label="$t('sys.tenant.columns.userName')" prop="userName" sortable="custom" align="center" min-width="100px">
        <template slot-scope="scope">
          <span>{{ scope.row.userName }}</span>
        </template>
      </el-table-column>
      <el-table-column :label="$t('columns.displayName')" min-width="150px">
        <template slot-scope="scope">
          <el-popover trigger="hover" placement="top">
            <p>{{ scope.row.displayName }}</p>
            <div slot="reference">
              <div class="single-line">{{ scope.row.displayName }}</div>
            </div>
          </el-popover>
        </template>
      </el-table-column>
      <el-table-column :label="$t('columns.status')" align="center" width="100">
        <template slot-scope="scope">
          <el-tag :type="scope.row.status | statusFilter">{{ formatStatus(scope.row.status) }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column :label="$t('sys.tenant.columns.email')" min-width="100px">
        <template slot-scope="scope">
          <el-popover trigger="hover" placement="top">
            <p>{{ scope.row.email }}</p>
            <div slot="reference">
              <div class="single-line">{{ scope.row.email }}</div>
            </div>
          </el-popover>
        </template>
      </el-table-column>
      <el-table-column :label="$t('sys.tenant.columns.mobile')" min-width="100px">
        <template slot-scope="scope">
          <el-popover trigger="hover" placement="top">
            <p>{{ scope.row.mobile }}</p>
            <div slot="reference">
              <div class="single-line">{{ scope.row.mobile }}</div>
            </div>
          </el-popover>
        </template>
      </el-table-column>
      <el-table-column v-if="canUpdate" :label="$t('actions.handle')" align="center" width="230" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button type="primary" @click="update(scope.row)">{{ $t('actions.update') }}</el-button>
          <!--<el-button type="success" @click="openTagDialog(scope.row)">{{ $t('sys.tenant.actions.tag') }}</el-button>-->
        </template>
      </el-table-column>
    </el-table>

    <pagination v-show="page.total>0" :total="page.total" :page.sync="page.currentPage" :limit.sync="page.pageSize" @pagination="search" />

    <el-dialog
      :title="editDialog.title"
      :visible.sync="editDialog.base.visible"
      :center="true"
      :modal="true"
      :close-on-click-modal="false"
      :close-on-press-escape="false"
      :before-close="close"
    >
      <div v-show="editDialog.base.visible">
        <el-form ref="form" :rules="formRules" :model="tenant" label-position="right" label-width="150px" class="form-layout">
          <el-form-item :label="$t('sys.tenant.columns.userName')" prop="userName">
            <el-input v-model="tenant.userName" :placeholder="$t('sys.tenant.placeholder.userName')" />
          </el-form-item>
          <el-form-item :label="$t('columns.displayName')" prop="displayName">
            <el-input v-model="tenant.displayName" :placeholder="$t('placeholders.displayName')" />
          </el-form-item>
          <el-form-item :label="$t('sys.tenant.columns.password')">
            <el-input v-model="tenant.password" :placeholder="$t('sys.tenant.placeholder.password')" type="password" />
          </el-form-item>
          <el-form-item :label="$t('columns.status')">
            <el-select v-model="tenant.status" style="width: 100%;">
              <el-option v-for="item in statuses" :key="item.label" :value="item.value" :label="item.label" />
            </el-select>
          </el-form-item>
          <el-form-item :label="$t('sys.tenant.columns.email')" prop="email">
            <el-input v-model="tenant.email" :placeholder="$t('sys.tenant.placeholder.email')" />
          </el-form-item>
          <el-form-item :label="$t('sys.tenant.columns.mobile')" prop="mobile">
            <el-input v-model="tenant.mobile" :placeholder="$t('sys.tenant.placeholder.mobile')" />
          </el-form-item>
          <el-form-item>
            <el-button type="info" @click="editDialog.base.visible = false">{{ $t('actions.cancel') }}</el-button>
            <el-button type="success" @click="save">{{ $t('actions.save') }}</el-button>
          </el-form-item>
        </el-form>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { listReq, getReq, removeReq, addReq, updateReq } from '@/api/sys/tenant'
import waves from '@/directive/waves' // Waves directive
import { buildMsg } from '@/utils/tools'
import Pagination from '@/components/Pagination' // Secondary package based on el-pagination
import access from '@/directive/access/index.js'
import { canAccess } from '@/utils/tools'
import { load } from '@/constant'
const DEF_OBJ = {
  userName: null,
  displayName: null,
  password: null,
  email: null,
  mobile: null,
  status: 0
}
export default {
  name: 'tenant',
  components: { Pagination },
  directives: { waves, access },
  filters: {
    statusFilter(item) {
      const map = {
        0: 'success',
        1: 'warning',
        2: 'danger'
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
        userName: undefined,
        displayName: undefined,
        sort: 'USER_NAME'
      },
      editDialog: {
        oper: undefined,
        title: undefined,
        base: {
          visible: false
        },
        tag: {
          visible: false
        }
      },
      formRules: {
        userName: [
          { required: true, trigger: 'blur' }
        ],
        displayName: [
          { required: true, trigger: 'blur' }
        ]
      },
      tenant: DEF_OBJ,
      statuses: [],
      downloadLoading: false,
      selections: [],
      canUpdate: canAccess('tenant-update')
    }
  },
  created() {
    this.loadConst()
    this.setFormRules()
    this.search()
  },
  methods: {
    setFormRules() {
      this.formRules.userName[0].message = this.$t('sys.tenant.rules.userName')
      this.formRules.displayName[0].message = this.$t('rules.displayName')
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
      if (prop === 'userName') {
        this.sort(order)
      }
    },
    sort(order) {
      if (order === 'ascending') {
        this.page.sort = 'USER_NAME'
      } else {
        this.page.sort = 'USER_NAME DESC'
      }
      this.search()
    },
    reset() {
      if (!this.selections || !this.selections.length || !this.selections[0].userName) {
        this.tenant = Object.assign({}, DEF_OBJ)
      } else {
        getReq({ userName: this.selections[0].userName }).then(res => {
          this.tenant = res.data.result
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
          const request = (this.editDialog.oper === 'add' ? addReq(this.tenant) : updateReq(this.tenant))
          request.then(res => {
            this.$message.success(res.data.msg)
            this.editDialog.base.visible = false
            this.search()
          })
        }
      })
    },
    remove() {
      const userNames = []
      if (this.selections.length) {
        this.selections.forEach((sel) => {
          userNames.push(sel.userName)
        })
      } else {
        this.$message.warning(this.$t('tip.selectData'))
        return
      }
      const adminTenant = this.selections.find(item => item.admin)
      if (adminTenant) {
        this.$message.warning(this.$t('tip.adminTenantCannotBeRemoved') + ':' + adminTenant.userName)
        return
      }
      this.$confirm(buildMsg(this, userNames), this.$t('tip.confirmMsg'), { type: 'warning' })
        .then(() => {
          removeReq({ userNames: userNames }).then(res => {
            this.$message.success(res.data.msg)
            this.search()
          })
        })
    },
    selectRow(row) {
      this.$refs.tables.clearSelection()
      if (row && row.userName) {
        this.$refs.tables.toggleRowSelection(row, true)
      }
      this.reset() // get the newest or reset to origin
    },
    handleSelectionChange(val) {
      this.selections = val
    },
    formatStatus(item) {
      for (let i = 0; i < this.statuses.length; i++) {
        const option = this.statuses[i]
        if (option.value === item) {
          return option.label
        }
      }
      return item
    },
    close() {
      this.editDialog.base.visible =
        this.editDialog.tag.visible = false
    },
    loadConst() {
      load(`./array/${localStorage.getItem('language')}.js`).then((array) => {
        this.statuses = array.statuses
        this.transferTitles = array.transferTitles
        this.allocateTitles = array.allocateTitles
      })
    }
  }
}
</script>
