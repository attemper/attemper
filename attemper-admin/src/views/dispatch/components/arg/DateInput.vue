<template>
  <div>
    <el-date-picker
      v-model="val"
      type="date"
      value-format="yyyyMMdd"
      style="width: 100%;"
    />
  </div>
</template>
<script>
import { isBlank } from '@/utils/tools'
export default {
  name: 'DateInput',
  props: {
    value: {
      type: String,
      default: ''
    }
  },
  computed: {
    val: {
      get() {
        if (isBlank(this.value) || this.value.length !== 8) {
          return null
        }
        return new Date(parseInt(this.value.substring(0, 4)), parseInt(this.value.substring(4, 6)) - 1, parseInt(this.value.substring(6, 8)))
      },
      set(val) {
        this.$emit('input', val)
      }
    }
  }
}
</script>
