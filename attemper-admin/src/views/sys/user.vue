<template>
  <div class="app-container">
    <div class="filter-container">
      <el-input :placeholder="$t('sys.user.columns.userName')" v-model="page.userName" style="width: 100px;" class="filter-item" @keyup.enter.native="search"/>
      <el-input :placeholder="$t('sys.user.columns.displayName')" v-model="page.displayName" style="width: 100px;" class="filter-item" @keyup.enter.native="search"/>
      <el-button v-waves class="filter-item" type="primary" icon="el-icon-search" @click="search">{{ $t('actions.search') }}</el-button>
      <el-button class="filter-item" style="margin-left: 10px;" type="success" icon="el-icon-plus" @click="update(null)">{{ $t('actions.add') }}</el-button>
      <el-button :disabled="!selections || !selections.length" class="filter-item" style="margin-left: 10px;" type="danger" icon="el-icon-delete" @click="remove">{{ $t('actions.remove') }}</el-button>
      <!--<el-button v-waves :loading="downloadLoading" class="filter-item" type="primary" icon="el-icon-download" @click="handleDownload">{{ $t('actions.export') }}</el-button>-->
    </div>

    <el-table
      v-loading="listLoading"
      ref="tables"
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
      <el-table-column :label="$t('sys.user.columns.userName')" prop="id" sortable="custom" align="center" min-width="100px">
        <template slot-scope="scope">
          <span>{{ scope.row.userName }}</span>
        </template>
      </el-table-column>
      <el-table-column :label="$t('sys.user.columns.displayName')" min-width="150px">
        <template slot-scope="scope">
          <span>{{ scope.row.displayName }}</span>
        </template>
      </el-table-column>
      <el-table-column :label="$t('sys.user.columns.email')" min-width="150px">
        <template slot-scope="scope">
          <span>{{ scope.row.email }}</span>
        </template>
      </el-table-column>
      <el-table-column :label="$t('sys.user.columns.mobile')" min-width="130px">
        <template slot-scope="scope">
          <span>{{ scope.row.mobile }}</span>
        </template>
      </el-table-column>
      <el-table-column :label="$t('sys.user.columns.status')" align="center" width="100">
        <template slot-scope="scope">
          <el-tag :type="scope.row.status | statusFilter">{{ formatStatus(scope.row.status) }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column :label="$t('actions.handle')" align="center" width="230" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button type="primary" size="mini" @click="update(scope.row)">{{ $t('actions.update') }}</el-button>
          <el-button type="success" size="mini" @click="openTagDialog(scope.row)">{{ $t('sys.user.actions.tag') }}</el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination v-show="page.total>0" :total="page.total" :page.sync="page.currentPage" :limit.sync="page.pageSize" @pagination="search" />

    <el-dialog :title="editDialog.title" :visible.sync="editDialog.base.visible || editDialog.tag.visible" :center="true" :modal="true" :close-on-click-modal="false" :close-on-press-escape="false" :before-close="close">
      <div v-show="editDialog.base.visible">
        <el-form ref="form" :rules="formRules" :model="user" label-position="right" label-width="150px" class="form-layout">
          <el-form-item :label="$t('sys.user.columns.userName')" prop="userName">
            <el-input v-model="user.userName" :placeholder="$t('sys.user.placeholder.userName')"/>
          </el-form-item>
          <el-form-item :label="$t('sys.user.columns.displayName')" prop="displayName">
            <el-input v-model="user.displayName" :placeholder="$t('sys.user.placeholder.displayName')"/>
          </el-form-item>
          <el-form-item :label="$t('sys.user.columns.password')">
            <el-input v-model="user.password" :placeholder="$t('sys.user.placeholder.password')" type="password"/>
          </el-form-item>
          <el-form-item :label="$t('sys.user.columns.email')" prop="email">
            <el-input v-model="user.email" :placeholder="$t('sys.user.placeholder.email')"/>
          </el-form-item>
          <el-form-item :label="$t('sys.user.columns.mobile')" prop="mobile">
            <el-input v-model="user.mobile" :placeholder="$t('sys.user.placeholder.mobile')"/>
          </el-form-item>
          <el-form-item :label="$t('sys.user.columns.status')">
            <el-select v-model="user.status">
              <el-option v-for="item in statuses" :value="item.value" :key="item.label" :label="item.label"/>
            </el-select>
          </el-form-item>
          <el-form-item>
            <el-button type="info" @click="editDialog.base.visible = false">{{ $t('actions.cancel') }}</el-button>
            <el-button type="success" @click="save">{{ $t('actions.save') }}</el-button>
          </el-form-item>
        </el-form>
      </div>
      <div v-show="editDialog.tag.visible" class="custom-transfer">
        <el-transfer
          v-model="targetKeys"
          :titles="allocateTitles"
          :button-texts="transferTitles"
          :format="{ noChecked: '${total}', hasChecked: '${checked}/${total}' }"
          :props="{ key: 'tagName', label: 'displayName' }"
          :data="transferData"
          :filter-placeholder="$t('sys.user.tip.tagFilterTip')"
          :render-content="renderFunc"
          class="transfer-class"
          style="text-align: left; display: inline-block;"
          filterable
          @change="handleChange">
          <el-button slot="left-footer" class="transfer-footer" size="small" icon="el-icon-refresh" @click="generateData">{{ $t('actions.refresh') }}</el-button>
          <el-button slot="right-footer" class="transfer-footer" size="small" icon="el-icon-refresh" @click="generateData">{{ $t('actions.refresh') }}</el-button>
        </el-transfer>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { listReq, getReq, removeReq, addReq, updateReq, getTagsReq, updateUserTagReq } from '@/api/sys/user'
import * as tagApi from '@/api/sys/tag'
import waves from '@/directive/waves' // Waves directive
import Pagination from '@/components/Pagination' // Secondary package based on el-pagination
import { load } from '@/constant'
// import { parseTime } from '@/utils'

export default {
  name: 'User',
  components: { Pagination },
  directives: { waves },
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
        ],
        email: [
          { required: false, trigger: 'blur', type: 'email' }
        ],
        mobile: [
          { required: false, trigger: 'blur', len: 11 }
        ]
      },
      user: {
        userName: null,
        displayName: null,
        password: null,
        email: null,
        mobile: null,
        status: 0
      },
      statuses: [],
      transferData: [],
      targetKeys: [],
      transferTitles: [],
      allocateTitles: [],
      downloadLoading: false,
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
      this.formRules.userName[0].message = this.$t('sys.user.rules.userName')
      this.formRules.displayName[0].message = this.$t('sys.user.rules.displayName')
      this.formRules.email[0].message = this.$t('sys.user.rules.email')
      this.formRules.mobile[0].message = this.$t('sys.user.rules.mobile')
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
        this.user = {
          userName: null,
          displayName: null,
          password: null,
          email: null,
          mobile: null,
          status: 0
        }
      } else {
        getReq({ userName: this.selections[0].userName }).then(res => {
          this.user = res.data.result
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
          const request = (this.editDialog.oper === 'add' ? addReq(this.user) : updateReq(this.user))
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
        this.$message.warning(this.$t('tip.remove'))
        return
      }
      const msg = '<p>' + this.$t('tip.removeConfirm') + ':<br><span style="color: red">' + userNames.join('<br>') + '</span></p>'
      this.$confirm(msg, this.$t('tip.confirm'), { type: 'warning', dangerouslyUseHTMLString: true })
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
    openTagDialog(row) {
      this.editDialog.tag.visible = true
      this.editDialog.title = this.$t('sys.user.tip.tag')
      this.selectRow(row)
      this.generateData()
    },
    generateData() {
      this.transferData = []
      this.targetKeys = []
      const page = {
        currentPage: 1,
        pageSize: 1000
      }
      tagApi.listReq(page).then(res => {
        res.data.result.list.forEach(item => {
          this.transferData.push({
            tagName: item.tagName,
            displayName: item.displayName,
            tagType: item.tagType
          })
        })
      })
      getTagsReq({ userName: this.selections[0].userName }).then(res => {
        res.data.result.forEach(item => {
          this.targetKeys.push(item.tagName)
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
        userName: this.selections[0].userName,
        tagNames: this.targetKeys,
        tagType: 0 // TODO应当拼接tagType到targetKeys中
      }
      updateUserTagReq(data).then(res => {
        this.$message.success(res.data.msg)
      })
    },
    renderFunc(h, option) {
      if (option.tagType === 0) {
        return h(
          'span',
          { style: 'color: #67C23A;' },
          [option.tagName, ' - ', option.displayName]
        )
      } else if (option.tagType === 1) {
        return h(
          'span',
          { style: 'color: #E6A23C;' },
          [option.tagName, ' - ', option.displayName]
        )
      } else if (option.tagType === 2) {
        return h(
          'span',
          { style: 'color: #F56C6C;' },
          [option.tagName, ' - ', option.displayName]
        )
      }
      return h(
        'span',
        [option.tagName, ' - ', option.displayName]
      )
    },
    loadConst() {
      load(`./array/${this.$store.state.app.language}.js`).then((array) => {
        this.statuses = array.statuses
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
</style>
