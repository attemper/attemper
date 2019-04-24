<template>
  <div class="app-container">
    <div class="filter-container">
      <el-input v-model="page.id" :placeholder="$t('sys.tenant.columns.id')" style="width: 100px;" class="filter-item" @keyup.enter.native="search" />
      <el-input v-model="page.name" :placeholder="$t('sys.tenant.columns.name')" style="width: 100px;" class="filter-item" @keyup.enter.native="search" />
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
      <el-table-column :label="$t('sys.tenant.columns.id')" prop="id" sortable="custom" align="center" min-width="100px">
        <template slot-scope="scope">
          <span>{{ scope.row.id }}</span>
        </template>
      </el-table-column>
      <el-table-column :label="$t('sys.tenant.columns.name')" min-width="150px">
        <template slot-scope="scope">
          <span>{{ scope.row.name }}</span>
        </template>
      </el-table-column>
      <el-table-column :label="$t('sys.tenant.columns.sign')" min-width="200px">
        <template slot-scope="scope">
          <span>{{ scope.row.sign }}</span>
        </template>
      </el-table-column>
      <el-table-column :label="$t('sys.tenant.columns.admin')" min-width="130px">
        <template slot-scope="scope">
          <span>{{ scope.row.admin }}</span>
        </template>
      </el-table-column>
      <el-table-column :label="$t('actions.handle')" align="center" width="230" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button v-if="canUpdate" type="primary" size="mini" @click="update(scope.row)">{{ $t('actions.update') }}</el-button>
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
          <el-form-item :label="$t('sys.tenant.columns.id')" prop="id">
            <el-input v-model="tenant.id" :placeholder="$t('sys.tenant.placeholder.id')" />
          </el-form-item>
          <el-form-item :label="$t('sys.tenant.columns.name')" prop="name">
            <el-input v-model="tenant.name" :placeholder="$t('sys.tenant.placeholder.name')" />
          </el-form-item>
          <el-form-item :label="$t('sys.tenant.columns.admin')" prop="admin">
            <el-select v-model="tenant.admin" :placeholder="$t('sys.tenant.placeholder.admin')" class="filter-item">
              <el-option v-for="item in tenantAdmins" :key="item.userName" :value="item.userName" :disabled="item.status !== 0" :label="item.displayName + '-' + item.userName">
                <span>{{ item.displayName }} - {{ item.userName }}</span>
              </el-option>
            </el-select>
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
import { listReq, removeReq, addReq, updateReq } from '@/api/sys/tenant'
import * as userApi from '@/api/sys/user'
import waves from '@/directive/waves' // Waves directive
// import { parseTime } from '@/utils'
import Pagination from '@/components/Pagination' // Secondary package based on el-pagination
import access from '@/directive/access/index.js'
import { canAccess } from '@/utils/tools'

export default {
  name: 'tenant',
  components: { Pagination },
  directives: { waves, access },
  data() {
    return {
      tableKey: 0,
      list: null,
      listLoading: true,
      page: {
        currentPage: 1,
        pageSize: 10,
        total: 0,
        id: undefined,
        name: undefined,
        sort: 'ID'
      },
      editDialog: {
        oper: undefined,
        title: undefined,
        base: {
          visible: false
        }
      },
      formRules: {
        id: [
          { required: true, trigger: 'blur' }
        ],
        name: [
          { required: true, trigger: 'blur' }
        ],
        admin: [
          { required: true, trigger: 'blur' }
        ]
      },
      tenant: {
        id: null,
        name: null,
        admin: null
      },
      tenantAdmins: [],
      downloadLoading: false,
      selections: [],
      canUpdate: canAccess('tenant-update')
    }
  },
  created() {
    this.setFormRules()
    this.search()
  },
  methods: {
    setFormRules() {
      this.formRules.id[0].message = this.$t('sys.tenant.rules.id')
      this.formRules.name[0].message = this.$t('sys.tenant.rules.name')
      this.formRules.admin[0].message = this.$t('sys.tenant.rules.admin')
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
      if (prop === 'id') {
        this.sort(order)
      }
    },
    sort(order) {
      if (order === 'ascending') {
        this.page.sort = 'ID'
      } else {
        this.page.sort = 'ID DESC'
      }
      this.search()
    },
    reset() {
      if (!this.selections || !this.selections.length || !this.selections[0].id) {
        this.tenant = {
          id: null,
          name: null,
          admin: null
        }
      } else {
        this.tenant = this.selections[0]
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
      this.loadTenantAdmins()
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
      const ids = []
      if (this.selections.length) {
        this.selections.forEach((sel) => {
          ids.push(sel.id)
        })
      } else {
        this.$message.warning(this.$t('tip.selectData'))
        return
      }
      const msg = '<p>' + this.$t('tip.confirmMsg') + ':<br><span style="color: red">' + ids.join('<br>') + '</span></p>'
      this.$confirm(msg, this.$t('tip.confirm'), { type: 'warning', dangerouslyUseHTMLString: true })
        .then(() => {
          removeReq({ ids: ids }).then(res => {
            this.$message.success(res.data.msg)
            this.search()
          })
        })
    },
    selectRow(row) {
      this.$refs.tables.clearSelection()
      if (row && row.id) {
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
    loadTenantAdmins() {
      userApi.listReq({ currentPage: 1, pageSize: 1000 }).then(res => {
        this.tenantAdmins = res.data.result.list
      })
    }
  }
}
</script>

<style lang="scss">
  .form{
    &-layout{
      width: 400px;
      min-height: 350px;
      margin-top: 20px;
      margin-left:50px;
    }
  }
</style>
