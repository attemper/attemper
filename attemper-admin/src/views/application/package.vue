<template>
  <div>
    <el-row>
      <el-col :span="6">
        <el-input v-model="searchKey" style="margin: 10px 0 10px 10px; width: 200px;">
          <i v-show="searchKey.length > 0" slot="suffix" class="el-input__icon el-icon-close" @click="searchKey = ''" />
        </el-input>
        <el-tree
          ref="tree"
          :data="treeData"
          :filter-node-method="filterNode"
          node-key="fileName"
          default-expand-all
          @node-click="selectNode"
        >
          <span slot-scope="{ node, data }" class="custom-tree-node">
            <span>
              <i :class="data.dir ? (node.expanded ? 'el-icon-folder-opened' : 'el-icon-folder') : (node.isCurrent ? 'el-icon-notebook-2' : 'el-icon-notebook-1')" />
              {{ data.fileName }}
              <span v-show="node.isCurrent && !data.dir" style="margin-left: 20px;">
                <i style="margin-left: 10px;" class="el-icon-download" @click="downloadFile(data)" />
              </span>
            </span>
          </span>
        </el-tree>
      </el-col>
      <el-col :span="18">
        <code-editor v-if="fileInfo" ref="codeEditor" v-model="fileContent" :file-extension="fileExtension" :disabled="disabled" />
      </el-col>
    </el-row>
  </div>
</template>

<script>
import { listPackageCategoryReq, viewFileReq, downloadFileReq } from '@/api/application/program'
import CodeEditor from '@/components/CodeEditor'
import waves from '@/directive/waves'
import { download } from '@/utils/tools'

export default {
  name: 'package',
  components: { CodeEditor },
  directives: { waves },
  data() {
    return {
      packagePage: {
        currentPage: 1,
        pageSize: 10,
        total: 0,
        programName: null
      },
      editDialog: {
        oper: undefined,
        title: undefined,
        base: {
          visible: false
        },
        category: {
          visible: false
        }
      },
      treeData: [],
      searchKey: '',
      fileInfo: null,
      fileContent: null,
      fileExtension: '.js',
      disabled: true
    }
  },
  watch: {
    searchKey(val) {
      this.$refs.tree.filter(val)
    }
  },
  created() {
    this.initPackageCategory()
  },
  methods: {
    selectNode(data) {
      this.fileInfo = null
      if (!data.dir) {
        viewFileReq({ filePath: data.filePath }).then((res) => {
          this.fileExtension = data.fileName.indexOf('.') !== -1 ? data.fileName.substring(data.fileName.lastIndexOf('.')) : data.fileName
          this.fileContent = res.data.result
          this.fileInfo = data
        })
      }
    },
    downloadFile(fileInfo) {
      this.$confirm(this.$t('tip.confirm'), this.$t('tip.confirmMsg'), { type: 'warning' })
        .then(() => {
          downloadFileReq({ filePath: fileInfo.filePath }).then((res) => {
            download(res.data, fileInfo.fileName)
          })
        })
    },
    initPackageCategory() {
      this.searchKey = ''
      listPackageCategoryReq({ id: this.$route.params.key }).then(res => {
        const sourceList = res.data.result
        let root
        for (let i = 0; i < sourceList.length; i++) {
          const item = sourceList[i]
          if (!item.parentFileName) {
            root = item
            break
          }
        }
        this.createTree(sourceList, root)
        this.treeData = [root]
      })
    },
    createTree(sourceList, cell) {
      for (let i = 0; i < sourceList.length; i++) {
        const item = sourceList[i]
        if (item.parentFileName === cell.fileName) {
          if (!cell.children) {
            cell.children = []
          }
          cell.children.push(item)
          this.createTree(sourceList, item)
        }
      }
    },
    filterNode(value, data) {
      if (!value) {
        return true
      }
      return data.fileName.indexOf(value) !== -1
    },
    closeCodeEditor() {
      this.fileContent = null
      this.fileExtension = '.js'
    }
  }
}
</script>
