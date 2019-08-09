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
          <el-form ref="form" :model="project" :rules="formRules" label-width="130px" label-position="right">
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
              <el-switch v-model="project.bindExecutor" active-color="#13ce66" inactive-color="#ff4949" />
            </el-form-item>
            <el-form-item v-show="project.bindExecutor" :label="$t('application.project.label.executorUri')">
              <el-select v-model="executorUris" style="width: 100%;" multiple>
                <el-option v-for="item in executors" :key="item.uri" :value="item.uri" :label="item.uri" />
              </el-select>
            </el-form-item>
            <el-form-item :label="$t('application.project.label.position')">
              <el-input-number v-model="project.position" :precision="0" :min="0" :step="1" controls-position="right" />
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
              <template slot-scope="scope">
                <span>{{ scope.row.uri }}</span>
              </template>
            </el-table-column>
            <el-table-column label="type" prop="type" align="center" min-width="80px">
              <template slot-scope="scope">
                <el-tag :type="scope.row.type | typeFilter">{{ formatType(scope.row.type) }}</el-tag>
              </template>
            </el-table-column>
            <el-table-column :label="$t('actions.handle')" align="center" class-name="small-padding">
              <template slot-scope="scope">
                <el-button type="danger" @click="removeInfo(scope.row)">{{ $t('actions.remove') }}</el-button>
              </template>
            </el-table-column>
          </el-table>
          <div style="margin-top: 10px;">
            <el-row>
              <el-col :span="8">
                <el-input v-model="projectInfo.uri" placeholder="uri" />
              </el-col>
              <el-col :span="6" :offset="1">
                <el-select v-model="projectInfo.type" style="width: 100%;">
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
  type: 0
}
export default {
  name: 'project',
  filters: {
    typeFilter(type) {
      const typeMap = {
        0: 'primary',
        1: 'success',
        2: 'info'
      }
      return typeMap[type]
    }
  },
  data() {
    return {
      treeData: [],
      project: null,
      visible: false,
      formRules: {
        projectName: [
          { required: true, trigger: 'blur' }
        ],
        displayName: [
          { required: true, trigger: 'blur' }
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
      const position = Math.floor(Math.random() * 1000)
      const newChild = {
        parentProjectName: data.projectName,
        projectName: data.projectName + '-' + position,
        displayName: data.projectName + '-' + position,
        position: position,
        bindExecutor: false,
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
      import(`@/constant/array/${localStorage.getItem('language')}.js`).then((array) => {
        this.uriTypes = array.uriTypes
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
