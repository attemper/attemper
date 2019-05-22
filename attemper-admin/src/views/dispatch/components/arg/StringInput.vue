<template>
  <div>
    <el-input v-model="val" :placeholder="$t('dispatch.arg.placeholder.argValue')" />
  </div>
</template>
<script>

export default {
  name: 'StringInput',
  props: {
    value: {
      type: String,
      default: ''
    }
  },
  computed: {
    val: {
      get() {
        return this.value
      },
      set(val) {
        this.$emit('input', val)
      }
    }
  }
}
</script>
