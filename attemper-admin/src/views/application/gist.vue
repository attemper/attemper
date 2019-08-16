<template>
  <div>
    <el-row :gutter="10">
      <el-col :span="7">
        <el-card>
          <div slot="header">
            <span>{{ $t('application.gist.title.gistList') }}</span>
          </div>
          <div class="filter-container">
            <el-input v-model="page.gistName" :placeholder="$t('application.gist.columns.gistName')" class="filter-item search-input" @keyup.enter.native="search" />
            <el-button v-waves class="filter-item" type="primary" icon="el-icon-search" @click="search" />
            <el-button class="filter-item" style="margin-left: 10px;" type="success" icon="el-icon-plus" @click="add" />
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
                    <span>{{ props.row.createTime }}</span>
                  </el-form-item>
                </el-form>
              </template>
            </el-table-column>
            <el-table-column
              type="selection"
              width="40"
            />
            <el-table-column :label="$t('application.gist.columns.gistName')" align="center" min-width="100px">
              <template slot-scope="scope">
                <span>{{ scope.row.gistName }}</span>
              </template>
            </el-table-column>
          </el-table>

          <pagination v-show="page.total>0" :total="page.total" :page.sync="page.currentPage" :limit.sync="page.pageSize" @pagination="search" />

        </el-card>
      </el-col>
      <el-col v-if="selections && selections.length === 1" :span="17">
        <el-card>
          <div slot="header">
            <span style="margin-right: 10px;">{{ gist.gistName }}</span>
            <el-select v-model="currentId" @change="getContent">
              <el-option v-for="item in gistList" :key="item.id" :label="item.version" :value="item.id">
                <span style="float: left; font-size: 13px">{{ item.version }}</span>
                <span style="float: right; color: #8492a6; margin-left: 20px;">{{ item.updateTime }}</span>
              </el-option>
            </el-select>
            <el-button v-if="currentId" style="margin-left: 5px;" type="danger" icon="el-icon-delete" @click="removeInfo" />
            <el-button style="margin-left: 10px;" type="success" icon="el-icon-plus" @click="addInfo" />
            <el-button v-if="canSave" type="primary" @click="updateContent">{{ $t('actions.save') }}</el-button>
          </div>
          <code-editor v-if="visible" v-model="code" :extension="extension" :read-only="readOnly" />
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import { listReq, removeReq, addReq, addInfoReq, listInfoReq, removeInfoReq, getContentReq, updateContentReq } from '@/api/application/gist'
import waves from '@/directive/waves'
import CodeEditor from '@/components/CodeEditor'
import Pagination from '@/components/Pagination'
import { buildMsg } from '@/utils/tools'

const DEF_OBJ = {
  gistName: null
}
export default {
  name: 'gist',
  components: { CodeEditor, Pagination },
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
        gistName: undefined
      },
      packagePage: {
        currentPage: 1,
        pageSize: 10,
        total: 0,
        gistName: null
      },
      gist: DEF_OBJ,
      selections: [],
      visible: false,
      extension: '.js',
      readOnly: false,
      code: '',
      bakCode: '',
      gistList: [],
      currentId: null
    }
  },
  computed: {
    canSave() {
      return this.currentId && this.gistList.length > 0 && this.gistList[0].id === this.currentId && this.bakCode !== this.code
    }
  },
  created() {
    this.search()
  },
  methods: {
    search() {
      this.listLoading = true
      listReq(this.page).then(response => {
        this.list = response.data.result.list
        Object.assign(this.page, response.data.result.page)
        this.listLoading = false
      })
    },
    add() {
      this.$prompt(this.$t('application.gist.placeholder.gistName'), this.$t('actions.add'), {
        closeOnClickModal: false,
        inputPattern: /([^\\/]+)\.([^\\/]+)/,
        inputErrorMessage: this.$t('application.gist.rules.gistName')
      }).then(({ value }) => {
        this.gist.gistName = value
        addReq(this.gist).then(res => {
          this.$message.success(res.data.msg)
          this.search()
        })
      })
    },
    remove() {
      const gistNames = []
      if (this.selections.length) {
        this.selections.forEach((sel) => {
          gistNames.push(sel.gistName)
        })
      } else {
        this.$message.warning(this.$t('tip.selectData'))
        return
      }
      this.$confirm(buildMsg(this, gistNames), this.$t('tip.confirmMsg'), { type: 'warning' })
        .then(() => {
          removeReq({ gistNames: gistNames }).then(res => {
            this.$message.success(res.data.msg)
            this.search()
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
      if (row && row.gistName) {
        this.$refs.tables.toggleRowSelection(row, true)
      }
      this.reset() // get the newest or reset to origin
    },
    reset() {
      this.gist = Object.assign({}, this.selections[0])
      this.extension = this.gist.gistName.substring(this.gist.gistName.lastIndexOf('.'))
      this.resetInfo()
    },
    resetInfo() {
      this.currentId = null
      this.code = ''
      this.getListInfo()
    },
    getListInfo() {
      listInfoReq({ gistName: this.gist.gistName }).then(res => {
        this.gistList = res.data.result
        if (this.gistList.length > 0) {
          this.currentId = this.gistList[0].id
          this.getContent(this.gistList[0].id)
        } else {
          this.visible = false
        }
      })
    },
    getContent(id) {
      this.visible = false
      getContentReq({ id: id }).then(res => {
        this.bakCode = this.code = res.data.result || ''
        this.readOnly = !(this.currentId && this.gistList.length > 0 && this.gistList[0].id === this.currentId)
        this.visible = true
      })
    },
    updateContent() {
      this.$confirm(this.$t('tip.confirm'), this.$t('tip.confirmMsg'), { type: 'warning' })
        .then(() => {
          updateContentReq({ id: this.currentId, content: this.code }).then(res => {
            this.$message.success(res.data.msg)
          })
        })
    },
    addInfo() {
      this.$prompt(this.$t('tip.entryVersion'), this.$t('actions.add'), {
        closeOnClickModal: false,
        inputPattern: /^[\s\S]*.*[^\s][\s\S]*$/,
        inputErrorMessage: this.$t('tip.versionNotBlank')
      }).then(({ value }) => {
        addInfoReq({ gistName: this.gist.gistName, version: value }).then(res => {
          this.$message.success(res.data.msg)
          this.resetInfo()
        })
      })
    },
    removeInfo() {
      this.$confirm(this.$t('tip.confirm'), this.$t('tip.confirmMsg'), { type: 'warning' })
        .then(() => {
          removeInfoReq({ id: this.currentId }).then(res => {
            this.$message.success(res.data.msg)
            this.resetInfo()
          })
        })
    },
    close() {
      this.editDialog.base.visible = false
    }
  }
}
</script>
