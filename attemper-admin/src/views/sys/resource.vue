<template>
  <el-row :gutter="10">
    <el-col :span="10">
      <el-card>
        <div slot="header">
          <span>{{ $t('sys.resource.title.left') }}</span>
          <span style="float: right;">
            <el-input v-model="searchKey" :placeholder="$t('sys.resource.tip.searchKey')" size="mini" style="width: 170px;" @keyup.enter.native="search" />
          </span>
        </div>
        <el-tree
          ref="tree"
          :data="treeData"
          :filter-node-method="filterNode"
          :expand-on-click-node="false"
          node-key="resourceName"
          default-expand-all
          @node-click="selectNode"
        >
          <span slot-scope="{ node, data }" class="custom-tree-node">
            <span>
              <i v-if="data.icon && data.icon.indexOf('el-') === 0" :class="data.icon" />
              <svg-icon v-if="data.icon && data.icon.indexOf('el-') !== 0" :icon-class="data.icon" style="margin-right: 3px;" />
              {{ data.displayName }}
            </span>
            <span>
              <el-button
                v-show="data.resourceType <= 1"
                :style="{ marginRight: data.parentResourceName ? '3px' : '31px' }"
                class="custom-add"
                type="text"
                size="mini"
                icon="el-icon-plus"
                @click="() => append(data)"
              />
              <el-button
                v-show="data.parentResourceName"
                type="text"
                size="mini"
                icon="el-icon-delete"
                @click="() => remove(node, data)"
              />
            </span>
          </span>
        </el-tree>
      </el-card>
    </el-col>
    <el-col :span="14">
      <el-card>
        <div slot="header">
          <span>{{ $t('sys.resource.title.right') }}</span>
        </div>
        <div v-if="visible">
          <el-form ref="form" :model="resource" :rules="formRules" label-width="130px" label-position="right">
            <el-form-item :label="$t('sys.resource.label.resourceName')" prop="resourceName">
              <el-input v-model="resource.resourceName" :placeholder="$t('sys.resource.placeholder.resourceName')" />
            </el-form-item>
            <el-form-item :label="$t('sys.resource.label.displayName')" prop="displayName">
              <el-input v-model="resource.displayName" :placeholder="$t('sys.resource.placeholder.displayName')" />
            </el-form-item>
            <el-form-item :label="$t('sys.resource.label.resourceType')">
              <el-select v-model="resource.resourceType">
                <el-option v-for="item in resourceTypes" :key="item.value" :label="item.label" :value="item.value" />
              </el-select>
            </el-form-item>
            <el-form-item :label="$t('sys.resource.label.uri')">
              <el-input v-model="resource.uri" :placeholder="$t('sys.resource.placeholder.uri')" />
            </el-form-item>
            <el-form-item :label="$t('sys.resource.label.icon')">
              <el-row>
                <el-col :span="18">
                  <el-input v-model="resource.icon" :placeholder="$t('sys.resource.placeholder.icon')" />
                </el-col>
                <el-col v-if="resource.icon" :span="4" :offset="2">
                  <i v-if="resource.icon.indexOf('el-') === 0" :class="resource.icon" />
                  <svg-icon v-else :icon-class="resource.icon" />
                </el-col>
              </el-row>
            </el-form-item>
            <el-form-item :label="$t('sys.resource.label.position')">
              <el-input-number v-model="resource.position" />
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
    </el-col>
  </el-row>
</template>

<script>
import { treeListReq, saveReq, removeReq } from '@/api/sys/resource'
import { load } from '@/constant'
import Cookies from 'js-cookie'

export default {
  name: 'Resource',
  data() {
    return {
      treeData: [],
      resource: {
        resourceName: null,
        parentResourceName: null,
        displayName: null,
        resourceType: 0,
        path: null,
        icon: null,
        position: 0
      },
      resourceTypes: [],
      visible: false,
      formRules: {
        resourceName: [
          { required: true, trigger: 'blur' }
        ],
        displayName: [
          { required: true, trigger: 'blur' }
        ]
      },
      searchKey: ''
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
      this.formRules.resourceName[0].message = this.$t('sys.resource.rules.resourceName')
      this.formRules.displayName[0].message = this.$t('sys.tag.rules.displayName')
    },
    initTreeData() {
      treeListReq().then(res => {
        const sourceList = res.data.result
        let root
        for (let i = 0; i < sourceList.length; i++) {
          const resource = sourceList[i]
          if (!resource.parentResourceName) {
            root = resource
            break
          }
        }
        this.createTree(sourceList, root)
        this.treeData = [root]
      })
    },
    createTree(sourceList, cell) {
      for (let i = 0; i < sourceList.length; i++) {
        const resource = sourceList[i]
        if (resource.parentResourceName === cell.resourceName) {
          if (!cell.children) {
            cell.children = []
          }
          cell.children.push(resource)
          this.createTree(sourceList, resource)
        }
      }
    },
    save() {
      this.$refs.form.validate((valid) => {
        if (valid) {
          const msg = '<p>' + this.$t('tip.saveConfirm') + '?</p>'
          this.$confirm(msg, this.$t('tip.confirm'), { type: 'info', dangerouslyUseHTMLString: true })
            .then(() => {
              saveReq(this.resource).then(res => {
                this.$message.success(res.data.msg)
                this.initTreeData()
              })
            })
        }
      })
    },
    append(data) {
      const position = Math.floor(Math.random() * 1000)
      const newChild = {
        parentResourceName: data.resourceName,
        resourceName: data.resourceName + '-' + position,
        displayName: data.resourceName + '-' + position,
        resourceType: !data.parentResourceName ? 0 : data.resourceType === 0 ? 1 : 2,
        uri: null,
        icon: null,
        position: position,
        transition: true
      }
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
          this.$message.warning(this.$t('sys.resource.tip.resourceRemoveWarning'))
        } else {
          const msg = '<p>' + this.$t('tip.removeConfirm') + ':<br><span style="color: red">' + data.displayName + '</span></p>'
          this.$confirm(msg, this.$t('tip.confirm'), { type: 'info', dangerouslyUseHTMLString: true })
            .then(() => {
              removeReq({ resourceNames: [data.resourceName] }).then(res => {
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
      const index = children.findIndex(d => d.resourceName === data.resourceName)
      children.splice(index, 1)
      this.visible = false
    },
    selectNode(data) {
      Object.assign(this.resource, data)
      this.visible = true
    },
    filterNode(value, data) {
      if (!value) {
        return true
      }
      return data.resourceName.indexOf(value) !== -1 || data.displayName.indexOf(value) !== -1
    },
    loadConst() {
      load(`./array/${Cookies.get('language')}.js`).then((array) => {
        this.resourceTypes = array.resourceTypes
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
