<template>
  <div>
    <el-row v-for="(item, index) in options" :key="index" style="margin-bottom: 2px;">
      <el-col :span="8">
        <el-button icon="el-icon-plus" type="success" @click="add" />
        <el-button v-show="options.length>1" icon="el-icon-minus" type="danger" @click="remove(index)" />
      </el-col>
      <el-col :span="16">
        <string-input v-if="genericType === 0" v-model="item.val" :placeholder="$t('dispatch.arg.placeholder.argValue')" />
        <boolean-input v-else-if="genericType === 1" ref="booleanInput" v-model="item.val" @change="item.val = $event" />
        <number-input v-else-if="genericType === 2" v-model="item.val" :min="-2147483648" :max="2147483647" :step="1" :precision="0" />
        <number-input v-else-if="genericType === 3" v-model="item.val" :min="4.9000000e-324" :max="1.797693e+308" :step="1" :precision="5" />
        <number-input v-else-if="genericType === 4" v-model="item.val" :min="-9223372036854774808" :max="9223372036854774807" :step="1" :precision="0" />
        <date-input v-else-if="genericType === 10" v-model="item.val" />
        <time-input v-else-if="genericType === 11" v-model="item.val" />
        <date-time-input v-else-if="genericType === 12" v-model="item.val" />
        <string-input v-else v-model="item.val" :placeholder="$t('dispatch.arg.placeholder.argValue')" />
      </el-col>
    </el-row>
  </div>
</template>
<script>
import { alias } from '@/settings'
import { isBlank } from '@/utils/tools'
import StringInput from './StringInput'
import NumberInput from './NumberInput'
import BooleanInput from './BooleanInput'
import DateInput from './DateInput'
import DateTimeInput from './DateTimeInput'
import TimeInput from './TimeInput'

const DEF_OBJ = { val: '' }
export default {
  name: 'ListInput',
  components: { BooleanInput, NumberInput, StringInput, DateInput, DateTimeInput, TimeInput },
  props: {
    value: {
      type: String,
      default: ''
    },
    genericType: {
      type: Number,
      default: null
    }
  },
  data() {
    return {
      options: []
    }
  },
  watch: {
    options: {
      deep: true,
      handler(opts) {
        this.change(opts)
      }
    }
  },
  created() {
    this.initValue(this.value)
  },
  methods: {
    initValue(value) {
      const arr = []
      if (value) {
        const list = value.split(alias.comma)
        list.forEach(item => arr.push({ val: item }))
      }
      this.options = arr.length === 0 ? [Object.assign({}, DEF_OBJ)] : arr
      if (this.genericType === 1 && this.$refs.booleanInput) {
        for (let i = 0; i < this.$refs.booleanInput.length; i++) {
          this.$refs.booleanInput[i].initValue(this.options[i].val)
        }
      }
    },
    add() {
      this.options.push(Object.assign({}, DEF_OBJ))
    },
    remove(index) {
      this.options.splice(index, 1)
    },
    change(opts) {
      let result = ''
      if (opts) {
        opts.forEach(item => {
          if (!isBlank(item.val)) {
            result += item.val + alias.comma
          }
        })
      }
      this.$emit('change', result.endsWith(alias.comma) ? result.substring(0, result.length - alias.comma.length) : result)
    }
  }
}
</script>
