<template>
  <div>
    <el-select v-model="val" style="width: 100%;" filterable @change="change">
      <el-option v-for="item in gistNames" :key="item" :value="item" :label="item" />
    </el-select>
  </div>
</template>
<script>

import { listReq } from '@/api/application/gist'
export default {
  name: 'GistInput',
  props: {
    value: {
      type: String,
      default: ''
    }
  },
  data() {
    return {
      val: this.initValue(this.value),
      gistNames: []
    }
  },
  methods: {
    initValue(value) {
      this.initGistNames()
      if (value && value.length >= 1 && this.gistNames.find(item => { return item === value }) !== undefined) {
        this.val = value
      }
    },
    initGistNames() {
      this.gistNames = []
      listReq({ pageSize: -1 }).then(res => {
        this.gistNames = res.data.result.list.map(item => item.gistName)
      })
    },
    change(val) {
      this.$emit('change', this.val)
    }
  }
}
</script>
