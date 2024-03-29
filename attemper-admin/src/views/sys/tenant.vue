<template>
  <div class="app-container">
    <div class="filter-container">
      <el-input v-model="page.userName" :placeholder="$t('columns.userName')" class="filter-item search-input" @keyup.enter.native="search" />
      <el-input v-model="page.displayName" :placeholder="$t('columns.displayName')" class="filter-item search-input" @keyup.enter.native="search" />
      <el-button class="filter-item" type="primary" icon="el-icon-search" @click="search" />
      <el-button v-access="'tenant-add'" class="filter-item" style="margin-left: 10px;" type="success" icon="el-icon-plus" @click="update(null)" />
      <el-button v-access="'tenant-remove'" :disabled="!selections || !selections.length" class="filter-item" style="margin-left: 10px;" type="danger" icon="el-icon-delete" @click="remove" />
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
      <el-table-column :label="$t('columns.userName')" prop="userName" sortable="custom" align="center" min-width="100px">
        <template v-slot="scope">
          <span>{{ scope.row.userName }}</span>
        </template>
      </el-table-column>
      <el-table-column :label="$t('columns.displayName')" min-width="150px">
        <template v-slot="scope">
          <el-popover trigger="hover" placement="top">
            <p>{{ scope.row.displayName }}</p>
            <div slot="reference">
              <div class="single-line">{{ scope.row.displayName }}</div>
            </div>
          </el-popover>
        </template>
      </el-table-column>
      <el-table-column :label="$t('columns.status')" align="center" width="100">
        <template v-slot="scope">
          <el-tag :type="scope.row.status | statusFilter">{{ formatStatus(scope.row.status) }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column :label="$t('sys.tenant.columns.email')" min-width="100px">
        <template v-slot="scope">
          <el-popover trigger="hover" placement="top">
            <p>{{ scope.row.email }}</p>
            <div slot="reference">
              <div class="single-line">{{ scope.row.email }}</div>
            </div>
          </el-popover>
        </template>
      </el-table-column>
      <el-table-column :label="$t('sys.tenant.columns.mobile')" min-width="100px">
        <template v-slot="scope">
          <el-popover trigger="hover" placement="top">
            <p>{{ scope.row.mobile }}</p>
            <div slot="reference">
              <div class="single-line">{{ scope.row.mobile }}</div>
            </div>
          </el-popover>
        </template>
      </el-table-column>
      <el-table-column v-if="canUpdate" :label="$t('actions.handle')" align="center" width="230" class-name="small-padding fixed-width">
        <template v-slot="scope">
          <el-button type="warning" @click="changePassword(scope.row)">{{ $t('actions.changePassword') }}</el-button>
          <el-button type="primary" @click="update(scope.row)">{{ $t('actions.update') }}</el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination v-show="page.total>0" :total="page.total" :page.sync="page.currentPage" :limit.sync="page.pageSize" @pagination="search" />

    <el-dialog
      :title="editDialog.title"
      :visible.sync="editDialog.base.visible || editDialog.password.visible"
      :center="true"
      :modal="true"
      :close-on-click-modal="false"
      :close-on-press-escape="false"
      :before-close="close"
    >
      <div v-show="editDialog.base.visible">
        <el-form ref="form" :rules="formRules" :model="tenant" label-position="right" label-width="150px" class="form-layout">
          <el-form-item v-show="editDialog.oper === 'add'" :label="$t('columns.userName')" prop="userName">
            <el-input v-model="tenant.userName" :placeholder="$t('placeholders.userName')" />
          </el-form-item>
          <el-form-item :label="$t('columns.displayName')" prop="displayName">
            <el-input v-model="tenant.displayName" :placeholder="$t('placeholders.displayName')" />
          </el-form-item>
          <el-form-item :label="$t('columns.password')" prop="password">
            <el-input v-model="tenant.password" :placeholder="$t('placeholders.password')" :type="passwordType"><svg-icon slot="suffix" :icon-class="passwordType === 'password' ? 'eye' : 'eye-open'" @click="passwordType = (passwordType === 'password' ? '' : 'password')" /></el-input>
          </el-form-item>
          <el-form-item :label="$t('columns.status')">
            <el-select v-model="tenant.status" style="width: 100%;">
              <el-option v-for="item in tenantStatuses" :key="item.label" :value="item.value" :label="item.label" />
            </el-select>
            <el-alert v-if="tenant.status !== 0" style="padding: 0;" :type="tenant.status === 1 ? 'warning' : 'error'" :title="tenant.status === 1 ? $t('tip.tenantFrozen') : $t('tip.tenantDisabled')" />
          </el-form-item>
          <el-form-item :label="$t('sys.tenant.columns.email')" prop="email">
            <el-input v-model="tenant.email" :placeholder="$t('sys.tenant.placeholder.email')" />
          </el-form-item>
          <el-form-item :label="$t('sys.tenant.columns.mobile')" prop="mobile">
            <el-input v-model="tenant.mobile" :placeholder="$t('sys.tenant.placeholder.mobile')" />
          </el-form-item>
          <el-form-item :label="$t('sys.tenant.columns.sendConfig')">
            <el-checkbox-group v-model="configs">
              <el-checkbox-button v-for="item in sendConfigs" :key="item" :label="item">{{ item }}</el-checkbox-button>
            </el-checkbox-group>
          </el-form-item>
          <el-form-item>
            <el-button type="info" @click="editDialog.base.visible = false">{{ $t('actions.cancel') }}</el-button>
            <el-button type="success" @click="save">{{ $t('actions.save') }}</el-button>
          </el-form-item>
        </el-form>
      </div>
      <div v-show="editDialog.password.visible">
        <el-form ref="passwordForm" :rules="passwordFormRules" :model="passwordModel" label-position="left" label-width="130px" class="form-layout">
          <el-form-item :label="$t('tip.oldPassword')" prop="oldPassword">
            <el-input v-model="passwordModel.oldPassword" :type="passwordType"><svg-icon slot="suffix" :icon-class="passwordType === 'password' ? 'eye' : 'eye-open'" @click="passwordType = (passwordType === 'password' ? '' : 'password')" /></el-input>
          </el-form-item>
          <el-form-item :label="$t('tip.newPassword')" prop="newPassword">
            <el-input v-model="passwordModel.newPassword" :type="passwordType"><svg-icon slot="suffix" :icon-class="passwordType === 'password' ? 'eye' : 'eye-open'" @click="passwordType = (passwordType === 'password' ? '' : 'password')" /></el-input>
          </el-form-item>
          <el-form-item :label="$t('tip.doubleNewPassword')" prop="doubleNewPassword">
            <el-input v-model="passwordModel.doubleNewPassword" :type="passwordType"><svg-icon slot="suffix" :icon-class="passwordType === 'password' ? 'eye' : 'eye-open'" @click="passwordType = (passwordType === 'password' ? '' : 'password')" /></el-input>
          </el-form-item>
          <el-form-item>
            <el-button type="info" @click="editDialog.password.visible = false">{{ $t('actions.cancel') }}</el-button>
            <el-button type="success" @click="savePassword">{{ $t('actions.save') }}</el-button>
          </el-form-item>
          <el-alert v-if="passwordModel.newPassword && passwordModel.newPassword === passwordModel.oldPassword" type="error" :title="$t('tip.differentOldPassword')" />
          <el-alert v-if="passwordModel.newPassword !== passwordModel.doubleNewPassword" type="error" :title="$t('tip.differentNewPassword')" />
        </el-form>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { listReq, getReq, removeReq, addReq, updateReq, updatePasswordReq } from '@/api/sys/tenant'
import { buildMsg, canAccess } from '@/utils/tools'
import Pagination from '@/components/Pagination'
import access from '@/directive/access/index.js'

const DEF_OBJ = {
  userName: null,
  displayName: null,
  password: null,
  email: null,
  mobile: null,
  status: 0,
  sendConfig: '0'
}

const DEF_CHANGE_PASSWORD_OBJ = {
  oldPassword: '',
  newPassword: '',
  doubleNewPassword: ''
}

export default {
  name: 'tenant',
  components: { Pagination },
  directives: { access },
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
        password: {
          visible: false
        }
      },
      formRules: {
        userName: [
          { required: true, trigger: 'blur', range: { max: 64 }, pattern: /^[a-zA-Z0-9_-]+$/ }
        ],
        displayName: [
          { required: true, trigger: 'blur', range: { max: 255 }}
        ],
        password: [
          { required: true, trigger: 'blur' }
        ]
      },
      tenant: DEF_OBJ,
      passwordModel: DEF_CHANGE_PASSWORD_OBJ,
      passwordFormRules: {
        oldPassword: [
          { required: true, trigger: 'blur', range: { max: 64 }}
        ],
        newPassword: [
          { required: true, trigger: 'blur', range: { max: 64 }}
        ],
        doubleNewPassword: [
          { required: true, trigger: 'blur', range: { max: 64 }}
        ]
      },
      passwordType: 'password',
      tenantStatuses: [],
      sendConfigs: [],
      downloadLoading: false,
      selections: [],
      configs: [],
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
      this.formRules.userName[0].message = this.$t('rules.userName')
      this.formRules.displayName[0].message = this.$t('rules.displayName')
      this.formRules.password[0].message = this.$t('rules.password')
      this.passwordFormRules.oldPassword[0].message = this.$t('sys.tenant.rules.oldPassword')
      this.passwordFormRules.newPassword[0].message = this.$t('sys.tenant.rules.newPassword')
      this.passwordFormRules.doubleNewPassword[0].message = this.$t('sys.tenant.rules.doubleNewPassword')
    },
    search() {
      this.listLoading = true
      listReq(this.page).then(response => {
        this.list = response.data.result.list
        Object.assign(this.page, response.data.result.page)
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
          this.configs = []
          if (this.tenant.sendConfig) {
            for (let i = 0; i < this.sendConfigs.length; i++) {
              if (this.tenant.sendConfig.length > i && this.tenant.sendConfig[i] !== '0') {
                this.configs.push(this.sendConfigs[i])
              }
            }
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
          this.tenant.sendConfig = ''
          for (let i = 0; i < this.sendConfigs.length; i++) {
            if (this.configs.find(item => { return item === this.sendConfigs[i] })) {
              this.tenant.sendConfig += '1'
            } else {
              this.tenant.sendConfig += '0'
            }
          }
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
      const adminTenant = this.selections.find(item => item.superAdmin > 0)
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
    changePassword(row) {
      this.editDialog.password.visible = true
      this.passwordModel = Object.assign({ userName: row.userName }, DEF_CHANGE_PASSWORD_OBJ)
    },
    savePassword() {
      updatePasswordReq(this.passwordModel).then(res => {
        this.$message.success(res.data.msg)
        setTimeout(() => { this.logout() }, 500)
      })
    },
    async logout() {
      await this.$store.dispatch('user/logout')
      /* this.$router.push(`/login?redirect=${this.$route.fullPath}`)*/
      this.$router.push(`/login`)
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
    clickCell(row, column, cell, event) {
      this.selectRow(row)
    },
    formatStatus(item) {
      for (let i = 0; i < this.tenantStatuses.length; i++) {
        const option = this.tenantStatuses[i]
        if (option.value === item) {
          return option.label
        }
      }
      return item
    },
    close() {
      this.editDialog.base.visible =
        this.editDialog.password.visible = false
    },
    loadConst() {
      const lang = localStorage.getItem('language')
      import('@/lang/dict.js').then(array => {
        this.tenantStatuses = array['tenantStatuses_' + lang]
        this.sendConfigs = array['sendConfigs_' + lang]
      })
    }
  }
}
</script>
