<template>
  <div class="app-container">
    <div class="filter-container">
      <el-input v-model="page.tagName" :placeholder="$t('sys.tag.columns.tagName')" style="width: 100px;" class="filter-item" @keyup.enter.native="search" />
      <el-input v-model="page.displayName" :placeholder="$t('sys.tag.columns.displayName')" style="width: 100px;" class="filter-item" @keyup.enter.native="search" />
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
      <el-table-column :label="$t('sys.tag.columns.tagName')" prop="id" sortable="custom" align="center" min-width="100px">
        <template slot-scope="scope">
          <span>{{ scope.row.tagName }}</span>
        </template>
      </el-table-column>
      <el-table-column :label="$t('sys.tag.columns.displayName')" min-width="150px">
        <template slot-scope="scope">
          <span>{{ scope.row.displayName }}</span>
        </template>
      </el-table-column>
      <el-table-column :label="$t('sys.tag.columns.tagType')" align="center" width="100">
        <template slot-scope="scope">
          <el-tag :type="scope.row.tagType | tagTypeFilter">{{ formatTagType(scope.row.tagType) }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column :label="$t('actions.handle')" align="center" width="260px" class-name="small-padding">
        <template slot-scope="scope">
          <el-button type="primary" size="mini" @click="update(scope.row)">{{ $t('actions.update') }}</el-button>
          <el-button type="success" size="mini" @click="openUserDialog(scope.row)">{{ $t('sys.tag.actions.user') }}</el-button>
          <el-button type="primary" size="mini" @click="openResourceDialog(scope.row)">{{ $t('sys.tag.actions.resource') }}</el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination v-show="page.total>0" :total="page.total" :page.sync="page.currentPage" :limit.sync="page.pageSize" @pagination="search" />

    <el-dialog :title="editDialog.title" :visible.sync="editDialog.base.visible || editDialog.user.visible || editDialog.resource.visible" :center="true" :modal="true" :close-on-click-modal="false" :close-on-press-escape="false" :before-close="close">
      <div v-show="editDialog.base.visible">
        <el-form ref="form" :rules="formRules" :model="tag" label-position="right" label-width="150px" class="form-layout">
          <el-form-item :label="$t('sys.tag.columns.tagName')" prop="tagName">
            <el-input v-model="tag.tagName" :placeholder="$t('sys.tag.placeholder.tagName')" />
          </el-form-item>
          <el-form-item :label="$t('sys.tag.columns.displayName')" prop="displayName">
            <el-input v-model="tag.displayName" :placeholder="$t('sys.tag.placeholder.displayName')" />
          </el-form-item>
          <el-form-item :label="$t('sys.tag.columns.tagType')">
            <el-select v-model="tag.tagType">
              <el-option v-for="item in tagTypes" :key="item.label" :value="item.value" :label="item.label" />
            </el-select>
          </el-form-item>
          <el-form-item :label="$t('sys.tag.columns.remark')">
            <el-input v-model="tag.remark" :autosize="{ minRows: 1, maxRows: 4}" :placeholder="$t('sys.tag.placeholder.remark')" type="textarea" />
          </el-form-item>
          <el-form-item>
            <el-button type="info" @click="editDialog.base.visible = false">{{ $t('actions.cancel') }}</el-button>
            <el-button type="success" @click="save">{{ $t('actions.save') }}</el-button>
          </el-form-item>
        </el-form>
      </div>
      <div v-show="editDialog.user.visible" class="custom-transfer">
        <el-transfer
          v-model="targetKeys"
          :titles="allocateTitles"
          :button-texts="transferTitles"
          :format="{ noChecked: '${total}', hasChecked: '${checked}/${total}' }"
          :props="{ key: 'userName', label: 'displayName' }"
          :data="transferData"
          :filter-placeholder="$t('sys.tag.tip.userFilterTip')"
          :render-content="renderFunc"
          class="transfer-class"
          style="text-align: left; display: inline-block;"
          filterable
          @change="handleChange"
        >
          <el-button slot="left-footer" class="transfer-footer" size="small" icon="el-icon-refresh" @click="generateData">{{ $t('actions.refresh') }}</el-button>
          <el-button slot="right-footer" class="transfer-footer" size="small" icon="el-icon-refresh" @click="generateData">{{ $t('actions.refresh') }}</el-button>
        </el-transfer>
      </div>
      <div v-show="editDialog.resource.visible">
        <div>
          <el-tree
            ref="tree"
            :data="treeData"
            :expand-on-click-node="false"
            :default-checked-keys="checkedKeys"
            :default-expanded-keys="checkedKeys"
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
import { listReq, getReq, removeReq, addReq, updateReq, getUsersReq, updateTagUserReq, getResourcesReq, updateTagResourceReq } from '@/api/sys/tag'
import { treeListReq } from '@/api/sys/resource'
import * as userApi from '@/api/sys/user'
import waves from '@/directive/waves' // Waves directive
import Pagination from '@/components/Pagination' // Secondary package based on el-pagination
import { load } from '@/constant'
// import { parseTime } from '@/utils'
import Cookies from 'js-cookie'

export default {
  name: 'Tag',
  components: { Pagination },
  directives: { waves },
  filters: {
    tagTypeFilter(tagType) {
      const tagTypeMap = {
        0: 'success',
        1: 'warning',
        2: 'danger'
      }
      return tagTypeMap[tagType]
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
        tagName: undefined,
        displayName: undefined,
        sort: 'TAG_NAME'
      },
      editDialog: {
        oper: undefined,
        title: undefined,
        base: {
          visible: false
        },
        user: {
          visible: false
        },
        resource: {
          visible: false
        }
      },
      formRules: {
        tagName: [
          { required: true, trigger: 'blur' }
        ],
        displayName: [
          { required: true, trigger: 'blur' }
        ]
      },
      tag: {
        tagName: null,
        displayName: null,
        status: 0,
        remark: null
      },
      tagTypes: [],
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
      this.formRules.tagName[0].message = this.$t('sys.tag.rules.tagName')
      this.formRules.displayName[0].message = this.$t('sys.tag.rules.displayName')
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
      if (prop === 'tagName') {
        this.sort(order)
      }
    },
    sort(order) {
      if (order === 'ascending') {
        this.page.sort = 'TAG_NAME'
      } else {
        this.page.sort = 'TAG_NAME DESC'
      }
      this.search()
    },
    reset() {
      if (!this.selections || !this.selections.length || !this.selections[0].tagName) {
        this.tag = {
          tagName: null,
          displayName: null,
          tagType: 0,
          remark: null
        }
      } else {
        getReq({ tagName: this.selections[0].tagName, tagType: this.selections[0].tagType }).then(res => {
          this.tag = res.data.result
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
          const request = (this.editDialog.oper === 'add' ? addReq(this.tag) : updateReq(this.tag))
          request.then(res => {
            this.$message.success(res.data.msg)
            this.editDialog.base.visible = false
            this.search()
          })
        }
      })
    },
    remove() {
      const tagNames = []
      if (this.selections.length) {
        this.selections.forEach((sel) => {
          tagNames.push(sel.tagName)
        })
      } else {
        this.$message.warning(this.$t('tip.remove'))
        return
      }
      const tagType = this.selections[0].tagType
      const msg = '<p>' + this.$t('tip.removeConfirm') + ':<br><span style="color: red">' + tagNames.join('<br>') + '</span></p>'
      this.$confirm(msg, this.$t('tip.confirm'), { type: 'warning', dangerouslyUseHTMLString: true })
        .then(() => {
          removeReq({ tagNames: tagNames, tagType: tagType }).then(res => {
            this.$message.success(res.data.msg)
            this.search()
          })
        })
    },
    selectRow(row) {
      this.$refs.tables.clearSelection()
      if (row && row.tagName) {
        this.$refs.tables.toggleRowSelection(row, true)
      }
      this.reset() // get the newest or reset to origin
    },
    handleSelectionChange(val) {
      this.selections = val
    },
    formatTagType(item) {
      for (let i = 0; i < this.tagTypes.length; i++) {
        const option = this.tagTypes[i]
        if (option.value === item) {
          return option.label
        }
      }
      return item
    },
    close() {
      this.editDialog.base.visible =
        this.editDialog.user.visible =
          this.editDialog.resource.visible = false
    },
    openUserDialog(row) {
      this.editDialog.user.visible = true
      this.editDialog.title = this.$t('sys.tag.tip.user')
      this.selectRow(row)
      this.generateData()
    },
    openResourceDialog(row) {
      this.editDialog.resource.visible = true
      this.editDialog.title = this.$t('sys.tag.tip.resource')
      this.selectRow(row)
      this.initTreeData()
    },
    initTreeData() {
      this.treeData =
        this.checkedKeys = []
      treeListReq().then(res => {
        const sourceList = res.data.result
        let root
        for (let i = 0; i < sourceList.length; i++) {
          const resource = sourceList[i]
          if (!resource.parentResourceName) {
            root = resource
            root.expand = true
            break
          }
        }
        this.createTree(sourceList, root)
        getResourcesReq({ tagName: this.selections[0].tagName, tagType: this.selections[0].tagType }).then(res => {
          res.data.result.forEach(resource => {
            this.checkAllocatedResource(resource.resourceName, root.children)
          })
          this.treeData = [root]
        })
      })
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
    createTree(sourceList, cell) {
      for (let i = 0; i < sourceList.length; i++) {
        const resource = sourceList[i]
        if (resource.parentResourceName === cell.resourceName) {
          if (resource.resourceType === 0) {
            resource.expand = true
          }
          if (!cell.children) {
            cell.children = []
          }
          cell.children.push(resource)
          this.createTree(sourceList, resource)
        }
      }
    },
    saveResources() {
      const data = {
        tagName: this.selections[0].tagName,
        tagType: this.selections[0].tagType,
        resourceNames: this.$refs.tree.getCheckedKeys()
      }
      updateTagResourceReq(data).then(res => {
        this.$message.success(res.data.msg)
        this.editDialog.resource.visible = false
      })
    },
    generateData() {
      this.transferData = []
      this.targetKeys = []
      const page = {
        currentPage: 1,
        pageSize: 1000
      }
      userApi.listReq(page).then(res => {
        res.data.result.list.forEach(item => {
          this.transferData.push({
            userName: item.userName,
            displayName: item.displayName,
            disabled: item.status !== 0
          })
        })
      })
      getUsersReq({ tagName: this.selections[0].tagName, tagType: this.selections[0].tagType }).then(res => {
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
        tagName: this.selections[0].tagName,
        tagType: this.selections[0].tagType,
        userNames: this.targetKeys
      }
      updateTagUserReq(data).then(res => {
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
      load(`./array/${Cookies.get('language')}.js`).then((array) => {
        this.tagTypes = array.tagTypes
        this.transferTitles = array.transferTitles
        this.allocateTitles = array.allocateTitles
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
      width: 360px;
      height: 500px;
      -webkit-box-sizing: border-box;
      box-sizing: border-box;
      position: relative;
    }
    .el-transfer-panel__list.is-filterable {
      height: 100%;
    }
  }
  .custom-tree-node {
    flex: 1;
    display: flex;
    align-items: center;
    justify-content: space-between;
    font-size: 14px;
    padding-right: 8px;
  }
</style>
