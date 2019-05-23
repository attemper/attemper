<template>
  <div>
    <el-time-picker
      v-model="val"
      value-format="HHmmss"
      style="width: 100%;"
    />
  </div>
</template>
<script>
import { isBlank } from '@/utils/tools'
export default {
  name: 'TimeInput',
  props: {
    value: {
      type: String,
      default: ''
    }
  },
  computed: {
    val: {
      get() {
        if (isBlank(this.value) || this.value.length !== 6) {
          return null
        }
        return new Date(2019, 0, 1,
          parseInt(this.value.substring(0, 2)),
          parseInt(this.value.substring(2, 4)),
          parseInt(this.value.substring(4, 6)))
      },
      set(val) {
        this.$emit('input', val)
      }
    }
  }
}
</script>
