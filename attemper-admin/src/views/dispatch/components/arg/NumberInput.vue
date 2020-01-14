<template>
  <div>
    <el-input-number v-model="val" style="width: 100%;" :min="min" :max="max" :step="step" :precision="precision" controls-position="right" />
  </div>
</template>
<script>

export default {
  name: 'NumberInput',
  props: {
    value: {
      type: String,
      default: ''
    },
    min: {
      type: Number,
      default: 0
    },
    max: {
      type: Number,
      default: 0
    },
    step: {
      type: Number,
      default: 0
    },
    precision: {
      type: Number,
      default: 1
    }
  },
  computed: {
    val: {
      get() {
        return Number(this.value)
      },
      set(val) {
        this.$emit('input', String(val))
      }
    }
  }
}
</script>
