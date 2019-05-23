<template>
  <div>
    <el-date-picker
      v-model="val"
      type="datetime"
      value-format="yyyyMMddHHmmss"
      style="width: 100%;"
    />
  </div>
</template>
<script>
import { isBlank } from '@/utils/tools'
export default {
  name: 'DateTimeInput',
  props: {
    value: {
      type: String,
      default: ''
    }
  },
  computed: {
    val: {
      get() {
        if (isBlank(this.value) || this.value.length !== 14) {
          return null
        }
        return new Date(
          parseInt(this.value.substring(0, 4)),
          parseInt(this.value.substring(4, 6)) - 1,
          parseInt(this.value.substring(6, 8)),
          parseInt(this.value.substring(8, 10)),
          parseInt(this.value.substring(10, 12)),
          parseInt(this.value.substring(12, 14)))
      },
      set(val) {
        this.$emit('input', val)
      }
    }
  }
}
</script>
