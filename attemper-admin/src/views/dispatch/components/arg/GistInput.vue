<template>
  <div>
    <el-select v-model="val" style="width: 100%;" filterable @change="change">
      <el-option v-for="item in gistNames" :key="item" :value="item" :label="item" />
    </el-select>
  </div>
</template>
<script>

export default {
  name: 'GistInput',
  props: {
    value: {
      type: String,
      default: ''
    },
    gistNames: {
      type: Array,
      default: null
    }
  },
  data() {
    return {
      val: this.initValue(this.value)
    }
  },
  methods: {
    initValue(value) {
      if (value && value.length >= 1) {
        this.val = value
      }
    },
    change(val) {
      this.$emit('change', this.val)
    }
  }
}
</script>
