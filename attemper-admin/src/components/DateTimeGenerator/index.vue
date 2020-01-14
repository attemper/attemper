<template>
  <el-date-picker
    v-model="value_"
    type="datetime"
    :placeholder="placeholder"
    :picker-options="pickerOptions"
    value-format="timestamp"
    @change="change"
  />
</template>

<script>

export default {
  name: 'DateTimeGenerator',
  props: {
    value: {
      type: Number,
      default: 0
    },
    placeholder: {
      type: String,
      default: null
    }
  },
  data() {
    return {
      value_: null
    }
  },
  computed: {
    pickerOptions() {
      const now = new Date()
      return {
        shortcuts: [{
          text: this.$t('tip.today'),
          onClick(picker) {
            picker.$emit('pick', now)
          }
        }, {
          text: this.$t('tip.yesterday'),
          onClick(picker) {
            picker.$emit('pick', new Date(now.getTime() - 3600 * 1000 * 24))
          }
        }, {
          text: this.$t('tip.lastWeek'),
          onClick(picker) {
            picker.$emit('pick', new Date(now.getTime() - 3600 * 1000 * 24 * 7))
          }
        }, {
          text: this.$t('tip.lastMonth'),
          onClick(picker) {
            picker.$emit('pick', new Date(now.getMonth() === 0 ? now.getFullYear() - 1 : now.getFullYear(), now.getMonth() === 0 ? 11 : now.getMonth() - 1, now.getDate()))
          }
        }, {
          text: this.$t('tip.lastYear'),
          onClick(picker) {
            picker.$emit('pick', new Date(now.getFullYear() - 1, now.getMonth(), now.getDate()))
          }
        }]
      }
    }
  },
  watch: {
    value(val) {
      this.setValue(val)
    }
  },
  created() {
    this.setValue(this.value)
  },
  methods: {
    setValue(val) {
      this.value_ = val
    },
    change(val) {
      this.value_ = val
      this.$emit('update', this.value_)
      this.$emit('change', this.value_)
    }
  }
}
</script>
