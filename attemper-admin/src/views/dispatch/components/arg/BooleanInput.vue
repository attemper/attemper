<template>
  <div>
    <el-switch
      v-model="val"
      active-color="#13ce66"
      inactive-color="#ff4949"
      active-text="true"
      inactive-text="false"
      @change="change"
    />
  </div>
</template>
<script>

export default {
  name: 'BooleanInput',
  props: {
    value: {
      type: String,
      default: ''
    }
  },
  data() {
    return {
      val: this.initValue(this.value)
    }
  },
  methods: {
    initValue(value) {
      this.val = value === 'true'
    },
    change(val) {
      this.$emit('change', String(val))
    }
  }
}
</script>
