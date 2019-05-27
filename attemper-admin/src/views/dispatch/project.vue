<template>
  <el-row :gutter="10">
    <el-col :span="10">
      <el-card>
        <div slot="header">
          <span>{{ $t('dispatch.project.title.left') }}</span>
          <span style="float: right;">
            <el-input v-model="searchKey" :placeholder="$t('dispatch.project.tip.searchKey')" style="width: 170px;" @keyup.enter.native="search" />
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
    <el-col v-show="project.projectName" :span="14">
      <el-card>
        <div slot="header">
          <span>{{ $t('dispatch.project.title.rightTop') }}</span>
        </div>
        <div v-if="visible">
          <el-form ref="form" :model="project" :rules="formRules" label-width="130px" label-position="right">
            <el-form-item :label="$t('dispatch.project.label.projectName')" prop="projectName">
              <el-input v-model="project.projectName" :placeholder="$t('dispatch.project.placeholder.projectName')" />
            </el-form-item>
            <el-form-item :label="$t('columns.displayName')" prop="displayName">
              <el-input v-model="project.displayName" :placeholder="$t('placeholders.displayName')" />
            </el-form-item>
            <el-form-item :label="$t('dispatch.project.label.contextPath')">
              <el-input v-model="project.contextPath" :placeholder="$t('dispatch.project.placeholder.contextPath')" />
            </el-form-item>
            <el-form-item :label="$t('dispatch.project.label.bindExecutor')">
              <el-switch v-model="project.bindExecutor" active-color="#13ce66" inactive-color="#ff4949" />
            </el-form-item>
            <el-form-item v-show="project.bindExecutor" :label="$t('dispatch.project.label.executorUri')">
              <el-select v-model="executorUris" style="width: 100%;" multiple>
                <el-option v-for="item in executors" :key="item.uri" :value="item.uri" :label="item.uri" />
              </el-select>
            </el-form-item>
            <el-form-item :label="$t('dispatch.project.label.position')">
              <el-input-number v-model="project.position" controls-position="right" />
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
          <span>{{ $t('dispatch.project.title.rightBottom') }}</span>
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
            <el-table-column label="type" prop="type" align="center" min-width="100px">
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
              <el-col :span="8" :offset="1">
                <el-select v-model="projectInfo.type" style="width: 100%;">
                  <el-option v-for="item in uriTypes" :key="item.label" :value="item.value" :label="item.label" />
                </el-select>
              </el-col>
              <el-col :span="4" :offset="2">
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
import { treeListReq, saveReq, removeReq, saveInfoReq, listInfoReq, removeInfoReq, saveExecutorReq, listExecutorReq } from '@/api/dispatch/project'
import { listExecutorServiceReq, pingReq } from '@/api/dispatch/tool'
import { load } from '@/constant'

const DEF_OBJ = {
  projectName: null,
  parentProjectName: null,
  displayName: null,
  contextPath: null,
  bindExecutor: false,
  position: 0
}

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
      project: DEF_OBJ,
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
      this.formRules.projectName[0].message = this.$t('dispatch.project.rules.projectName')
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
          const msg = '<p>' + this.$t('tip.saveConfirm') + '?</p>'
          this.$confirm(msg, this.$t('tip.confirm'), { type: 'info', dangerouslyUseHTMLString: true })
            .then(() => {
              saveReq(this.project).then(res => {
                saveExecutorReq(
                  {
                    projectName: this.project.projectName,
                    executorUris: this.executorUris
                  }
                ).then(response => {
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
      const newChild = Object.assign(
        {
          parentProjectName: data.projectName,
          projectName: data.projectName + '-' + position,
          displayName: data.projectName + '-' + position,
          position: position,
          transition: true
        }, DEF_OBJ)
      if (!data.children) {
        this.$set(data, 'children', [])
      }
      data.children.push(newChild)
      this.$refs.tree.setCurrentNode(newChild)
    },
    remove(node, data) {
      if (data.transition) {
        this.removeNode(node, data)
      } else {
        if (data.children && data.children.length > 0) {
          this.$message.warning(this.$t('dispatch.project.tip.projectRemoveWarning'))
        } else {
          const msg = '<p>' + this.$t('tip.confirmMsg') + ':<br><span style="color: red">' + data.displayName + '</span></p>'
          this.$confirm(msg, this.$t('tip.confirm'), { type: 'info', dangerouslyUseHTMLString: true })
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
      Object.assign(this.project, data)
      this.visible = true
      this.projectInfo.projectName = this.project.projectName
      this.initInfos(this.projectInfo)
      this.initExecutor()
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
        this.$message.success(res.data.msg)
        return true
      })
    },
    removeInfo(row) {
      const msg = '<p>' + this.$t('tip.confirmMsg') + ':<br><span style="color: red">' + row.uri + '</span></p>'
      this.$confirm(msg, this.$t('tip.confirm'), { type: 'warning', dangerouslyUseHTMLString: true })
        .then(() => {
          removeInfoReq(this.projectInfo).then(res => {
            this.$message.success(res.data.msg)
            this.initInfos(this.projectInfo)
          })
        })
    },
    saveInfo() {
      const saveInfo = () => {
        saveInfoReq(this.projectInfo).then(res => {
          this.$message.success(res.data.msg)
          this.initInfos(this.projectInfo)
        })
      }
      pingReq(this.projectInfo).then(res => {
        saveInfo()
      }).catch(err => {
        this.$confirm(err.toString() + '<br><p>' + this.$t('tip.saveConfirm') + '?</p>',
          this.$t('tip.confirm'), { type: 'info', dangerouslyUseHTMLString: true })
          .then(() => {
            saveInfo()
          })
      })
    },
    initInfos(row) {
      listInfoReq({ projectName: row.projectName }).then(res => {
        this.projectInfos = res.data.result
      })
    },
    handleCurrentChange(val) {
      this.projectInfo = Object.assign({}, val) || Object.assign({ projectName: this.project.projectName }, DEF_INSTANCE)
    },
    loadConst() {
      load(`./array/${localStorage.getItem('language')}.js`).then((array) => {
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
  .custom-tree-node {
    flex: 1;
    display: flex;
    align-items: center;
    justify-content: space-between;
    font-size: 14px;
    padding-right: 8px;
  }
</style>
