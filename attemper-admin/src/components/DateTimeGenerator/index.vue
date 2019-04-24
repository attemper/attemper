<template>
  <div>
    <el-date-picker
      v-model="value"
      :size="size"
      type="datetime"
      :placeholder="placeholder"
      :picker-options="pickerOptions"
      value-format="yyyy-MM-dd HH:mm:ss"
      @change="change"
    />
  </div>
</template>

<script>

export default {
  name: 'DateTimeGenerator',
  props: {
    placeholder: {
      type: String,
      default: null
    },
    size: {
      type: String,
      default: ''
    }
  },
  data() {
    return {
      value: null,
      pickerOptions: {
        shortcuts: [{
          text: 'today',
          onClick(picker) {
            picker.$emit('pick', new Date())
          }
        }, {
          text: 'yesterday',
          onClick(picker) {
            const date = new Date()
            date.setTime(date.getTime() - 3600 * 1000 * 24)
            picker.$emit('pick', date)
          }
        }, {
          text: '1 week ago',
          onClick(picker) {
            const date = new Date()
            date.setTime(date.getTime() - 3600 * 1000 * 24 * 7)
            picker.$emit('pick', date)
          }
        }]
      }
    }
  },
  created() {},
  methods: {
    change() {
      this.$emit('update', this.value)
      this.$emit('change')
    }
  }
}
</script>

<style rel="stylesheet/scss" lang="scss">

</style>
