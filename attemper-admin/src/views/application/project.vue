<template>
  <el-row :gutter="10">
    <el-col :span="10">
      <el-card>
        <div slot="header">
          <span>{{ $t('application.project.title.left') }}</span>
          <span style="float: right;">
            <el-button type="primary" icon="el-icon-refresh" @click="initTreeData" />
            <el-input v-model="searchKey" :placeholder="$t('application.project.tip.searchKey')" style="width: 170px;" />
          </span>
        </div>
        <el-tree
          ref="tree"
          :data="treeData"
          :filter-node-method="filterNode"
          :expand-on-click-node="false"
          node-key="projectName"
          default-expand-all
          @node-click="selectNode"
        >
          <span slot-scope="{ node, data }" class="custom-tree-node">
            <span>
              {{ data.displayName }}
            </span>
            <span>
              <el-button
                :style="{ marginRight: data.parentProjectName ? '3px' : '31px' }"
                class="custom-add"
                type="text"
                icon="el-icon-plus"
                @click="() => append(data)"
              />
              <el-button
                v-show="data.parentProjectName"
                type="text"
                icon="el-icon-delete"
                @click="() => remove(node, data)"
              />
            </span>
          </span>
        </el-tree>
      </el-card>
    </el-col>
    <el-col v-if="visible" :span="14">
      <el-card>
        <div slot="header">
          <span>{{ $t('application.project.title.rightTop') }}</span>
        </div>
        <div>
          <el-form ref="form" :rules="formRules" :model="project" label-width="130px" label-position="right">
            <el-form-item :label="$t('application.project.label.projectName')" prop="projectName">
              <el-input v-model="project.projectName" :disabled="!project || !project.parentProjectName" :placeholder="$t('application.project.placeholder.projectName')" />
            </el-form-item>
            <el-form-item :label="$t('columns.displayName')" prop="displayName">
              <el-input v-model="project.displayName" :placeholder="$t('placeholders.displayName')" />
            </el-form-item>
            <el-form-item :label="$t('application.project.label.contextPath')">
              <el-input v-model="project.contextPath" :placeholder="$t('application.project.placeholder.contextPath')" />
            </el-form-item>
            <el-form-item :label="$t('application.project.label.bindExecutor')">
              <el-switch v-model="project.bindExecutor" active-color="#13ce66" inactive-color="#ff4949" :active-value="1" :inactive-value="0" />
            </el-form-item>
            <el-form-item v-show="project.bindExecutor > 0" :label="$t('application.project.label.executorUri')">
              <el-select v-model="executorUris" style="width: 100%;" multiple>
                <el-option v-for="item in executors" :key="item.uri" :value="item.uri" :label="item.uri" />
              </el-select>
            </el-form-item>
          </el-form>
          <el-row>
            <el-col :span="4" :offset="10">
              <el-button style="width:60%" type="success" icon="el-icon-circle-check-outline" @click="save">
                {{ $t('actions.save') }}
              </el-button>
            </el-col>
          </el-row>
        </div>
      </el-card>
      <el-card style="margin-top: 10px;">
        <div slot="header">
          <span>{{ $t('application.project.title.rightBottom') }}</span>
        </div>
        <div>
          <el-table
            :data="projectInfos"
            border
            fit
            highlight-current-row
            style="width: 100%;"
            @current-change="handleCurrentChange"
          >
            <el-table-column label="uri" prop="uri" align="center" min-width="100px">
              <template v-slot="scope">
                <span>{{ scope.row.uri }}</span>
              </template>
            </el-table-column>
            <el-table-column label="type" prop="uriType" align="center" min-width="80px">
              <template v-slot="scope">
                <el-tag :type="scope.row.uriType | typeFilter">{{ formatType(scope.row.uriType) }}</el-tag>
              </template>
            </el-table-column>
            <el-table-column :label="$t('actions.handle')" align="center" class-name="small-padding">
              <template v-slot="scope">
                <el-button type="danger" icon="el-icon-delete" @click="removeInfo(scope.row)" />
              </template>
            </el-table-column>
          </el-table>
          <div style="margin-top: 10px;">
            <el-row>
              <el-col :span="8">
                <el-input v-model="projectInfo.uri" :placeholder="projectInfo.uriType === 0 ? 'service name' : 'http(s)://'" />
              </el-col>
              <el-col :span="6" :offset="1">
                <el-select v-model="projectInfo.uriType" style="width: 100%;">
                  <el-option v-for="item in uriTypes" :key="item.label" :value="item.value" :label="item.label" />
                </el-select>
              </el-col>
              <el-col :span="6" :offset="2">
                <el-button :disabled="!projectInfo.uri" type="success" @click="saveInfo">{{ $t('actions.save') }}</el-button>
                <el-button :disabled="!projectInfo.uri" type="primary" @click="ping">Ping</el-button>
              </el-col>
            </el-row>
          </div>
        </div>
      </el-card>
    </el-col>
  </el-row>
</template>

<script>
import { treeListReq, saveReq, removeReq, saveInfoReq, listInfoReq, removeInfoReq, saveExecutorReq, listExecutorReq } from '@/api/application/project'
import { listExecutorServiceReq, pingReq } from '@/api/dispatch/tool'

const DEF_INSTANCE = {
  uri: null,
  uriType: 0
}
export default {
  name: 'project',
  filters: {
    typeFilter(uriType) {
      const typeMap = {
        0: 'primary',
        1: 'success',
        2: 'info'
      }
      return typeMap[uriType]
    }
  },
  data() {
    return {
      treeData: [],
      project: null,
      visible: false,
      formRules: {
        projectName: [
          { required: true, trigger: 'blur', range: { max: 255 }, pattern: /^[a-zA-Z0-9_-]+$/ }
        ],
        displayName: [
          { required: true, trigger: 'blur', range: { max: 255 }}
        ]
      },
      uriTypes: [],
      searchKey: '',
      projectInfo: Object.assign({}, DEF_INSTANCE),
      projectInfos: [],
      executorUris: [],
      executors: []
    }
  },
  watch: {
    searchKey(val) {
      this.$refs.tree.filter(val)
    }
  },
  created() {
    this.loadConst()
    this.setFormRules()
    this.initTreeData()
  },
  methods: {
    setFormRules() {
      this.formRules.projectName[0].message = this.$t('application.project.rules.projectName')
      this.formRules.displayName[0].message = this.$t('rules.displayName')
    },
    initTreeData() {
      treeListReq().then(res => {
        const sourceList = res.data.result
        let root
        for (let i = 0; i < sourceList.length; i++) {
          const project = sourceList[i]
          if (!project.parentProjectName) {
            root = project
            break
          }
        }
        this.createTree(sourceList, root)
        this.treeData = [root]
      })
    },
    createTree(sourceList, cell) {
      for (let i = 0; i < sourceList.length; i++) {
        const project = sourceList[i]
        if (project.parentProjectName === cell.projectName) {
          if (!cell.children) {
            cell.children = []
          }
          cell.children.push(project)
          this.createTree(sourceList, project)
        }
      }
    },
    save() {
      this.$refs.form.validate((valid) => {
        if (valid) {
          this.$confirm(this.$t('tip.saveConfirm'), this.$t('tip.confirm'), { type: 'info' })
            .then(() => {
              saveReq(this.project).then(res => {
                saveExecutorReq({
                  projectName: this.project.projectName,
                  executorUris: this.executorUris
                }).then(response => {
                  this.$message.success(response.data.msg)
                  this.initTreeData()
                })
              })
            })
        }
      })
    },
    append(data) {
      const newChild = {
        parentProjectName: data.projectName,
        projectName: data.projectName,
        displayName: data.projectName,
        bindExecutor: 0,
        transition: true
      }
      if (!data.children) {
        this.$set(data, 'children', [])
      }
      data.children.push(newChild)
    },
    remove(node, data) {
      if (data.transition) {
        this.removeNode(node, data)
      } else {
        if (data.children && data.children.length > 0) {
          this.$message.warning(this.$t('application.project.tip.projectRemoveWarning'))
        } else {
          this.$confirm(data.displayName, this.$t('tip.confirmMsg'), { type: 'info' })
            .then(() => {
              removeReq({ projectNames: [data.projectName] }).then(res => {
                this.$message.success(res.data.msg)
                this.removeNode(node, data)
              })
            })
        }
      }
    },
    removeNode(node, data) {
      const parent = node.parent
      const children = parent.data.children
      const index = children.findIndex(d => d.projectName === data.projectName)
      children.splice(index, 1)
      this.visible = false
    },
    selectNode(data) {
      this.visible = true
      this.project = data
      Object.assign(this.projectInfo, DEF_INSTANCE)
      if (this.project.projectName) {
        this.initInfos()
        this.initExecutor()
      }
    },
    initExecutor() {
      listExecutorServiceReq().then(res => {
        this.executors = res.data.result
        listExecutorReq(this.project).then(response => {
          this.executorUris = response.data.result
        })
      })
    },
    filterNode(value, data) {
      if (!value) {
        return true
      }
      return data.projectName.indexOf(value) !== -1 || data.displayName.indexOf(value) !== -1
    },
    formatType(item) {
      for (let i = 0; i < this.uriTypes.length; i++) {
        const option = this.uriTypes[i]
        if (option.value === item) {
          return option.label
        }
      }
      return item
    },
    ping() {
      pingReq(this.projectInfo).then(res => {
        if (res.data.result) {
          this.$message.success(res.data.msg)
        } else {
          this.$message.error(this.$t('tip.pingError'))
        }
      })
    },
    removeInfo(row) {
      this.$confirm(row.uri, this.$t('tip.confirmMsg'), { type: 'warning' })
        .then(() => {
          const data = {
            projectName: this.project.projectName,
            ...this.projectInfo
          }
          removeInfoReq(data).then(res => {
            this.$message.success(res.data.msg)
            this.initInfos()
          })
        })
    },
    saveInfo() {
      const saveInfo = () => {
        const data = {
          projectName: this.project.projectName,
          ...this.projectInfo
        }
        saveInfoReq(data).then(res => {
          this.$message.success(res.data.msg)
          this.initInfos()
          this.projectInfo = Object.assign({}, DEF_INSTANCE)
        })
      }
      pingReq(this.projectInfo).then(res => {
        if (res.data.result) {
          saveInfo()
        } else {
          this.$confirm(this.$t('tip.pingError') + ',' + this.$t('tip.saveConfirm') + '?',
            this.$t('tip.confirm'), { type: 'error' })
            .then(() => {
              saveInfo()
            })
        }
      })
    },
    initInfos() {
      listInfoReq({ projectName: this.project.projectName }).then(res => {
        this.projectInfos = res.data.result
      })
    },
    handleCurrentChange(val) {
      this.projectInfo = Object.assign({}, val) || Object.assign({}, DEF_INSTANCE)
    },
    loadConst() {
      const lang = localStorage.getItem('language')
      import('@/lang/dict.js').then(array => {
        this.uriTypes = array['uriTypes_' + lang]
      })
    }
  }
}
</script>

<style>
  .custom-add {
    margin-right: 5px;
  }
</style>
