<template>
  <div>
    <el-row>
      <el-col :span="12">
        <el-select v-model="unit" @change="change">
          <el-option v-for="item in tradeDateUnits" :key="item" :value="item" :label="item" />
        </el-select>
      </el-col>
      <el-col :span="8" :offset="2">
        <el-input-number v-model="num" :step="1" :precision="0" controls-position="right" @change="change" />
      </el-col>
    </el-row>
  </div>
</template>
<script>
export default {
  name: 'TradeDateInput',
  props: {
    value: {
      type: String,
      default: ''
    },
    placeholder: {
      type: String,
      default: ''
    },
    tradeDateUnits: {
      type: Array,
      default: null
    }
  },
  data() {
    return {
      unit: this.initUnit(this.value),
      num: this.initNum(this.value)
    }
  },
  methods: {
    initUnit(value) {
      if (value && value.length >= 1 && this.tradeDateUnits.find(item => { return item === value.substring(0, 1) !== undefined })) {
        this.unit = value.substring(0, 1)
      }
    },
    initNum(value) {
      if (value && value.length >= 3) {
        this.num = Number.parseInt(value.substring(1))
      }
    },
    change(val) {
      this.$emit('change', this.unit + '' + (!this.num || this.num === 0 ? '' : (this.num > 0 ? '+' + this.num : this.num)))
    }
  }
}
</script>
