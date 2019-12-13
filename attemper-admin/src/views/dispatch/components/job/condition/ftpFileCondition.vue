<template>
  <div>
    <div v-for="(item,index) in conditionArray" :key="index" class="row-bottom">
      <hr v-show="index > 0" class="row-hr">
      <el-row>
        <el-col :span="conditionArray.length < 2 ? 3 : 6">
          <el-button icon="el-icon-plus" type="success" @click="add" />
          <el-button v-show="conditionArray.length>1" icon="el-icon-minus" type="danger" @click="remove(index)" />
        </el-col>
        <el-col :span="conditionArray.length < 2 ? 21 : 18">
          <el-form :model="item" label-width="100px" label-position="left">
            <el-form-item :label="$t('dispatch.condition.title.conditionName')">
              <el-input v-model="item.conditionName" :placeholder="$t('dispatch.condition.placeholder.conditionName')">
                <el-button slot="prepend" @click="generateId(item)">
                  <svg-icon icon-class="random" />
                </el-button>
                <el-button slot="append" icon="el-icon-close" @click="removeId(item, index)" />
              </el-input>
            </el-form-item>
            <el-tooltip :content="$t('tip.ftpPrefix')" effect="dark" placement="top">
              <el-form-item :label="$t('columns.prefix')">
                <el-input v-model="item.prefix" />
              </el-form-item>
            </el-tooltip>
            <el-tooltip :content="$t('tip.fileSeparatorLeft')" effect="dark" placement="top">
              <el-form-item :label="$t('dispatch.condition.title.filePath')">
                <el-input v-model="item.filePath" />
              </el-form-item>
            </el-tooltip>
            <el-form-item :label="$t('dispatch.condition.title.fileName')">
              <el-input v-model="item.fileName" />
            </el-form-item>
          </el-form>
        </el-col>
      </el-row>
    </div>
  </div>
</template>

<script>
import { isBlank } from '@/utils/tools'
import CommonCondition from '../mixins/commonCondition'

const DEF_OBJ = {
  conditionName: '',
  filePath: null,
  fileName: null
}

export default {
  name: 'FtpFileCondition',
  mixins: [CommonCondition],
  computed: {
    size() {
      return localStorage.getItem('size')
    }
  },
  methods: {
    add() {
      this.put(DEF_OBJ)
    },
    validateThenSet(trigger) {
      if (!trigger.ftpFileConditions) {
        trigger.ftpFileConditions = []
      }
      for (let i = 0; i < this.conditionArray.length; i++) {
        const item = this.conditionArray[i]
        if (!isBlank(item.conditionName)) {
          trigger.ftpFileConditions.push(item)
        }
      }
      return true
    }
  }
}
</script>

<style rel="stylesheet/scss" lang="scss">
  @import "../../../styles/jobs";
</style>
