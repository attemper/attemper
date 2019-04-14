<template>
  <div>
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
        node-key="projectName"
        @check="check"
      >
        <span slot-scope="{ node, data }" class="custom-tree-node">
          <span>
            {{ data.displayName }}
          </span>
        </span>
      </el-tree>
    </div>
    <div style="margin-top: 20px; text-align: center">
      <el-button type="success" icon="el-icon-circle-check-outline" @click="saveProject">
        {{ $t('actions.save') }}
      </el-button>
    </div>
  </div>
</template>

<script>
import { getProjectReq, saveProjectReq } from '@/api/job/baseJob'
import { treeListReq } from '@/api/job/project'

export default {
  name: 'ProjectTree',
  props: {
    jobName: {
      type: String,
      default: null
    }
  },
  data() {
    return {
      treeData: [],
      checkedKeys: []
    }
  },
  created() {
    this.initTreeData()
  },
  methods: {
    initTreeData() {
      this.treeData = this.checkedKeys = []
      treeListReq().then(res => {
        const sourceList = res.data.result
        let root
        for (let i = 0; i < sourceList.length; i++) {
          const project = sourceList[i]
          if (!project.parentProjectName) {
            root = project
            root.expand = true
            break
          }
        }
        this.createTree(sourceList, root)
        getProjectReq({ jobName: this.jobName }).then(res0 => {
          const result = res0.data.result
          if (result) {
            if (result.projectName === root.projectName) {
              this.checkedKeys.push(result.projectName)
            } else {
              this.checkAllocatedProject(res0.data.result.projectName, root.children)
            }
          }
          this.treeData = [root]
        })
      })
    },
    checkAllocatedProject(key, children) {
      for (let i = 0; i < children.length; i++) {
        const child = children[i]
        if (key === child.projectName) {
          this.checkedKeys.push(child.projectName)
        } else if (child.children) {
          this.checkAllocatedProject(key, child.children)
        }
      }
    },
    createTree(sourceList, cell) {
      for (let i = 0; i < sourceList.length; i++) {
        const project = sourceList[i]
        if (project.parentProjectName === cell.projectName) {
          project.expand = true
          if (!cell.children) {
            cell.children = []
          }
          cell.children.push(project)
          this.createTree(sourceList, project)
        }
      }
    },
    check(data, node) {
      this.checkedKeys = []
      this.checkedKeys.push(data.projectName)
      this.$refs.tree.setCheckedNodes(this.checkedKeys)
    },
    saveProject() {
      const data = {
        jobName: this.jobName,
        projectName: this.checkedKeys[0]
      }
      saveProjectReq(data).then(res => {
        this.$message.success(res.data.msg)
        this.$emit('cancel')
      })
    }
  }
}
</script>

