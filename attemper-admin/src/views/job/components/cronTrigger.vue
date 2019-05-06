<template>
  <div>
    <div v-for="(item,index) in triggerArray" :key="index" class="trigger-row-bottom">
      <el-row>
        <el-col :span="4">
          <el-button icon="el-icon-plus" type="success" size="mini" @click="add" />
          <el-button v-show="triggerArray.length>1" icon="el-icon-minus" type="danger" size="mini" @click="remove(index)" />
        </el-col>
        <el-col :span="20">
          <el-form :model="item" label-width="150px" label-position="left" size="mini">
            <el-form-item :label="$t('job.trigger.title.triggerName')">
              <el-input v-model="item.triggerName" :placeholder="$t('job.trigger.placeholder.triggerName')" size="mini">
                <el-button slot="prepend" @click="generateId(item)">
                  <svg-icon icon-class="random" />
                </el-button>
                <el-button slot="append" icon="el-icon-minus" @click="removeId(item, index)" />
              </el-input>
            </el-form-item>
            <el-form-item :label="$t('job.trigger.title.expression')">
              <cron-input v-model="item.expression" @change="change($event, index)" @reset="reset($event, index)" />
            </el-form-item>
            <el-form-item :label="$t('job.trigger.title.timeRange')">
              <el-col :span="11">
                <el-date-picker
                  v-model="item.startTime"
                  :placeholder="$t('job.trigger.placeholder.startTime')"
                  type="datetime"
                  value-format="yyyy-MM-dd HH:mm:ss"
                />
              </el-col>
              <el-col :span="11" :offset="1">
                <el-date-picker
                  v-model="item.endTime"
                  :placeholder="$t('job.trigger.placeholder.endTime')"
                  type="datetime"
                  value-format="yyyy-MM-dd HH:mm:ss"
                />
              </el-col>
            </el-form-item>
            <el-form-item :label="$t('job.trigger.title.timeZone')">
              <el-select v-model="item.timeZoneId" filterable>
                <el-option
                  v-for="ele in timeZones"
                  :key="ele"
                  :label="ele"
                  :value="ele"
                />
              </el-select>
            </el-form-item>
          </el-form>
        </el-col>
      </el-row>
      <hr class="trigger-row-hr">
    </div>
  </div>
</template>

<script>
import CronInput from 'vue-cron-generator/src/components/cron-input'
import { DEFAULT_CRON_EXPRESSION } from 'vue-cron-generator/src/constant/filed'
import { isBlank, startAfterEndTime } from '.././scripts/support'
import CommonTrigger from './mixins/commonTrigger'

const CRON_OBJ = {
  triggerName: '',
  expression: DEFAULT_CRON_EXPRESSION,
  startTime: null,
  endTime: null,
  timeZoneId: null
}

export default {
  name: 'CronTrigger',
  components: {
    CronInput
  },
  mixins: [CommonTrigger],
  props: {
    initTriggerArray: {
      type: Array,
      default: null
    },
    timeZones: {
      type: Array,
      default: null
    }
  },
  data() {
    return {
      triggerArray: this.initTriggerArray
    }
  },
  methods: {
    add() {
      this.put(CRON_OBJ)
    },
    change(newCron, index) {
      this.triggerArray[index].expression = newCron
    },
    reset(oldCron, index) {
      this.triggerArray[index].expression = DEFAULT_CRON_EXPRESSION
    },
    validateThenSet(trigger) {
      for (let i = 0; i < this.triggerArray.length; i++) {
        const item = this.triggerArray[i]
        if (JSON.stringify(item) !== JSON.stringify(CRON_OBJ)) {
          if (isBlank(item.triggerName)) {
            this.$message.error(this.$t('job.trigger.tip.triggerNameNotBlank') + ':' + JSON.stringify(item))
            return false
          }
          if (startAfterEndTime(item.startTime, item.endTime)) {
            this.$message.error(this.$t('job.trigger.tip.startAfterEndTime') + ':' + item.triggerName)
            return false
          }
          if (isBlank(item.expression)) { // TODO need regex validation
            this.$message.error(this.$t('job.trigger.tip.cronExpressionInvalid') + ':' + item.triggerName)
            return false
          }
          if (!trigger.cronTriggers) {
            trigger.cronTriggers = []
          }
          trigger.cronTriggers.push(item)
        }
      }
      return true
    }
  }
}
</script>

<style rel="stylesheet/scss" lang="scss">
  @import ".././styles/jobs.scss";
</style>
