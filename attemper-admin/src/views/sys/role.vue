<template>
  <div class="app-container">
    <div class="filter-container">
      <el-input v-model="page.roleName" :placeholder="$t('sys.role.columns.roleName')" class="filter-item search-input" @keyup.enter.native="search" />
      <el-input v-model="page.displayName" :placeholder="$t('columns.displayName')" class="filter-item search-input" @keyup.enter.native="search" />
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
      <el-table-column
        type="selection"
        width="45"
      />
      <el-table-column :label="$t('sys.role.columns.roleName')" prop="id" align="center" min-width="100px">
        <template v-slot="scope">
          <span>{{ scope.row.roleName }}</span>
        </template>
      </el-table-column>
      <el-table-column :label="$t('columns.displayName')" min-width="150px">
        <template v-slot="scope">
          <span>{{ scope.row.displayName }}</span>
        </template>
      </el-table-column>
      <el-table-column :label="$t('actions.handle')" align="center" width="260px" class-name="small-padding">
        <template v-slot="scope">
          <el-button type="primary" @click="update(scope.row)">{{ $t('actions.update') }}</el-button>
          <el-button type="success" @click="openTenantDialog(scope.row)">{{ $t('sys.role.actions.tenant') }}</el-button>
          <el-button type="primary" @click="openResourceDialog(scope.row)">{{ $t('sys.role.actions.resource') }}</el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination v-show="page.total>0" :total="page.total" :page.sync="page.currentPage" :limit.sync="page.pageSize" @pagination="search" />

    <el-dialog :title="editDialog.title" :visible.sync="editDialog.base.visible || editDialog.tenant.visible || editDialog.resource.visible" :center="true" :modal="true" :close-on-click-modal="false" :close-on-press-escape="false" :before-close="close">
      <div v-show="editDialog.base.visible">
        <el-form ref="form" :rules="formRules" :model="role" label-position="right" label-width="150px" class="form-layout">
          <el-form-item :label="$t('sys.role.columns.roleName')" prop="roleName">
            <el-input v-model="role.roleName" :placeholder="$t('sys.role.placeholder.roleName')" />
          </el-form-item>
          <el-form-item :label="$t('columns.displayName')" prop="displayName">
            <el-input v-model="role.displayName" :placeholder="$t('placeholders.displayName')" />
          </el-form-item>
          <el-form-item :label="$t('columns.remark')">
            <el-input v-model="role.remark" :autosize="{ minRows: 1, maxRows: 5}" :placeholder="$t('placeholders.remark')" type="textarea" />
          </el-form-item>
          <el-form-item>
            <el-button type="info" @click="editDialog.base.visible = false">{{ $t('actions.cancel') }}</el-button>
            <el-button type="success" @click="save">{{ $t('actions.save') }}</el-button>
          </el-form-item>
        </el-form>
      </div>
      <div v-show="editDialog.tenant.visible" class="custom-transfer">
        <el-transfer
          v-model="targetKeys"
          :titles="allocateTitles"
          :button-texts="transferTitles"
          :format="{ noChecked: '${total}', hasChecked: '${checked}/${total}' }"
          :props="{ key: 'userName', label: 'displayName' }"
          :data="transferData"
          :filter-placeholder="$t('sys.role.tip.tenantFilterTip')"
          :render-content="renderFunc"
          class="transfer-class"
          style="text-align: left; display: inline-block;"
          filterable
          @change="handleChange"
        >
          <el-button slot="left-footer" class="transfer-footer" icon="el-icon-refresh" @click="generateData">{{ $t('actions.refresh') }}</el-button>
          <el-button slot="right-footer" class="transfer-footer" icon="el-icon-refresh" @click="generateData">{{ $t('actions.refresh') }}</el-button>
        </el-transfer>
      </div>
      <div v-show="editDialog.resource.visible">
        <div>
          <el-tree
            ref="tree"
            :data="treeData"
            :expand-on-click-node="false"
            :default-checked-keys="checkedKeys"
            default-expand-all
            show-checkbox
            check-on-click-node
            check-strictly
            node-key="resourceName"
          >
            <span slot-scope="{ node, data }" class="custom-tree-node">
              <span>
                <i v-if="data.icon && data.icon.indexOf('el-') === 0" :class="data.icon" />
                <svg-icon v-if="data.icon && data.icon.indexOf('el-') !== 0" :icon-class="data.icon" style="margin-right: 3px;" />
                {{ data.displayName }}
              </span>
            </span>
          </el-tree>
        </div>
        <div style="margin-top: 20px; text-align: center">
          <el-button type="success" icon="el-icon-circle-check-outline" @click="saveResources">
            {{ $t('actions.save') }}
          </el-button>
        </div>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { listReq, getReq, removeReq, addReq, updateReq, getTenantsReq, updateRoleTenantReq, getResourcesReq, updateRoleResourceReq } from '@/api/sys/role'
import { asyncRouterMap } from '@/router'
import * as tenantApi from '@/api/sys/tenant'
import Pagination from '@/components/Pagination'
import { generateTitleByVm, translateByVm } from '@/utils/i18n'
import { injectIcon, buildMsg } from '@/utils/tools'
const DEF_OBJ = {
  roleName: null,
  displayName: null,
  remark: null
}
export default {
  name: 'role',
  components: { Pagination },
  data() {
    return {
      tableKey: 0,
      list: null,
      listLoading: true,
      page: {
        currentPage: 1,
        pageSize: 10,
        total: 0,
        roleName: undefined,
        displayName: undefined
      },
      editDialog: {
        oper: undefined,
        title: undefined,
        base: {
          visible: false
        },
        tenant: {
          visible: false
        },
        resource: {
          visible: false
        }
      },
      formRules: {
        roleName: [
          { required: true, trigger: 'blur', range: { max: 64 }, pattern: /^[a-zA-Z0-9_-]+$/ }
        ],
        displayName: [
          { required: true, trigger: 'blur', range: { max: 255 }}
        ]
      },
      role: DEF_OBJ,
      transferData: [],
      targetKeys: [],
      transferTitles: [],
      allocateTitles: [],
      downloadLoading: false,
      selections: [],
      treeData: [],
      checkedKeys: []
    }
  },
  created() {
    this.loadConst()
    this.setFormRules()
    this.search()
  },
  methods: {
    setFormRules() {
      this.formRules.roleName[0].message = this.$t('sys.role.rules.roleName')
      this.formRules.displayName[0].message = this.$t('rules.displayName')
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
    reset() {
      if (!this.selections || !this.selections.length || !this.selections[0].roleName) {
        this.role = Object.assign({}, DEF_OBJ)
      } else {
        getReq({ roleName: this.selections[0].roleName }).then(res => {
          this.role = res.data.result
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
          const request = (this.editDialog.oper === 'add' ? addReq(this.role) : updateReq(this.role))
          request.then(res => {
            this.$message.success(res.data.msg)
            this.editDialog.base.visible = false
            this.search()
          })
        }
      })
    },
    remove() {
      const roleNames = []
      if (this.selections.length) {
        this.selections.forEach((sel) => {
          roleNames.push(sel.roleName)
        })
      } else {
        this.$message.warning(this.$t('tip.selectData'))
        return
      }
      this.$confirm(buildMsg(this, roleNames), this.$t('tip.confirmMsg'), { type: 'warning' })
        .then(() => {
          removeReq({ roleNames: roleNames }).then(res => {
            this.$message.success(res.data.msg)
            this.search()
          })
        })
    },
    selectRow(row) {
      this.$refs.tables.clearSelection()
      if (row && row.roleName) {
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
    close() {
      this.editDialog.base.visible =
        this.editDialog.tenant.visible =
          this.editDialog.resource.visible = false
    },
    openTenantDialog(row) {
      this.editDialog.tenant.visible = true
      this.editDialog.title = this.$t('sys.role.tip.tenant')
      this.selectRow(row)
      this.generateData()
    },
    openResourceDialog(row) {
      this.editDialog.resource.visible = true
      this.editDialog.title = this.$t('sys.role.tip.resource')
      this.selectRow(row)
      this.initTreeData()
    },
    initTreeData() {
      this.treeData =
        this.checkedKeys = []
      const rootResource = {
        resourceName: 'root',
        parentResourceName: null,
        displayName: this.$t('sys.role.tip.root'),
        icon: 'tree'
      }
      this.createResourceTree(asyncRouterMap, rootResource)
      getResourcesReq({ roleName: this.selections[0].roleName }).then(res => {
        res.data.result.forEach(resourceName => {
          this.checkAllocatedResource(resourceName, rootResource.children)
        })
        this.treeData = [rootResource]
      })
    },
    createResourceTree(routerList, currentResource) {
      for (let i = 0; i < routerList.length; i++) {
        if (routerList[i].path !== '*' && routerList[i].path.indexOf(':') === -1) {
          const resource = {
            resourceName: routerList[i].name,
            parentResourceName: currentResource.resourceName,
            displayName: generateTitleByVm(this, routerList[i].meta.title),
            icon: injectIcon(routerList[i].meta.icon)
          }
          if (routerList[i].meta && routerList[i].meta.buttons && routerList[i].meta.buttons.length > 0) {
            if (!resource.children) {
              resource.children = []
            }
            routerList[i].meta.buttons.forEach(btn => {
              const btnResource = {
                resourceName: resource.resourceName + '-' + btn,
                parentResourceName: resource.resourceName,
                displayName: translateByVm(this, 'actions.' + btn)
              }
              resource.children.push(btnResource)
            })
          }
          if (!currentResource.children) {
            currentResource.children = []
          }
          currentResource.children.push(resource)
          if (routerList[i].children) {
            this.createResourceTree(routerList[i].children, resource)
          }
        }
      }
    },
    checkAllocatedResource(key, children) {
      for (let i = 0; i < children.length; i++) {
        const child = children[i]
        if (key === child.resourceName) {
          this.checkedKeys.push(child.resourceName)
        } else if (child.children) {
          this.checkAllocatedResource(key, child.children)
        }
      }
    },
    saveResources() {
      const data = {
        roleName: this.selections[0].roleName,
        resourceNames: this.$refs.tree.getCheckedKeys()
      }
      updateRoleResourceReq(data).then(res => {
        this.$message.success(res.data.msg + '(' + this.$t('tip.refreshPage') + ')')
        setTimeout(() => this.$router.go(0), 800)
      })
    },
    generateData() {
      this.transferData = []
      this.targetKeys = []
      const page = {
        currentPage: 1,
        pageSize: -1
      }
      tenantApi.listReq(page).then(res => {
        res.data.result.list.forEach(item => {
          this.transferData.push({
            userName: item.userName,
            displayName: item.displayName,
            disabled: item.status !== 0
          })
        })
      })
      getTenantsReq({ roleName: this.selections[0].roleName }).then(res => {
        res.data.result.forEach(item => {
          this.targetKeys.push(item.userName)
        })
      })
    },
    handleChange(value, direction, movedKeys) {
      if (direction === 'right') {
        movedKeys.forEach(key => {
          if (!this.targetKeys.includes(key)) {
            this.targetKeys.push(key)
          }
        })
      } else if (direction === 'left') {
        const newTargetKeys = []
        this.targetKeys.forEach(key => {
          if (!movedKeys.includes(key)) {
            newTargetKeys.push(key)
          }
        })
        this.targetKeys = newTargetKeys
      }
      const data = {
        roleName: this.selections[0].roleName,
        userNames: this.targetKeys
      }
      updateRoleTenantReq(data).then(res => {
        this.$message.success(res.data.msg)
      })
    },
    renderFunc(h, option) {
      return h(
        'span',
        [option.userName, ' - ', option.displayName]
      )
    },
    loadConst() {
      const lang = localStorage.getItem('language')
      import('@/lang/dict.js').then(array => {
        this.transferTitles = array['transferTitles_' + lang]
        this.allocateTitles = array['allocateTitles_' + lang]
      })
    }
  }
}
</script>

<style lang="scss">
  .custom-transfer {
     height: 500px;
     text-align: center;
     margin-top: 20px;
   }
  .transfer-footer {
    margin-left: 20px;
    padding: 5px 5px;
  }
  .transfer-class {
    .el-transfer-panel {
      border: 1px solid #ebeef5;
      border-radius: 4px;
      overflow: hidden;
      background: #fff;
      display: inline-block;
      vertical-align: middle;
      width: 200px;
      height: 500px;
      -webkit-box-sizing: border-box;
      box-sizing: border-box;
      position: relative;
    }
    .el-transfer-panel__body {
      height: 400px;
    }
    .el-transfer-panel__list.is-filterable {
      height: 100%;
    }
  }
</style>
