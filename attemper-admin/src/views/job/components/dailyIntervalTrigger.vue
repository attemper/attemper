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
                <el-button slot="append" @click="generateId(item)">
                  <svg-icon icon-class="random" />
                </el-button>
              </el-input>
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
            <el-form-item :label="$t('job.trigger.title.timeRangeOfDay')">
              <el-col :span="11">
                <el-time-picker
                  v-model="item.startTimeOfDay"
                  :placeholder="$t('job.trigger.placeholder.startTimeOfDay')"
                  value-format="HH:mm:ss"
                />
              </el-col>
              <el-col :span="11" :offset="1">
                <el-time-picker
                  v-model="item.endTimeOfDay"
                  :placeholder="$t('job.trigger.placeholder.endTimeOfDay')"
                  value-format="HH:mm:ss"
                />
              </el-col>
            </el-form-item>
            <el-form-item :label="$t('job.trigger.title.interval')">
              <el-input-number v-model="item.interval" :placeholder="$t('job.trigger.placeholder.interval')" :precision="0" :min="1" :step="1" controls-position="right" />
            </el-form-item>
            <el-form-item :label="$t('job.trigger.title.timeUnit')">
              <el-select v-model="item.timeUnit" :placeholder="$t('job.trigger.placeholder.timeUnit')" filterable>
                <el-option
                  v-for="ele in milliSecondTimeUnits.concat(inDayTimeUnits)"
                  :key="ele.value"
                  :label="ele.label"
                  :value="ele.value"
                />
              </el-select>
            </el-form-item>
            <el-form-item :label="$t('job.trigger.title.repeatCount')">
              <el-input-number v-model="item.repeatCount" :placeholder="$t('job.trigger.placeholder.repeatCount')" :precision="0" :min="-1" :step="1" controls-position="right" />
            </el-form-item>
            <el-form-item :label="$t('job.trigger.title.daysOfWeek')">
              <el-select v-model="item.daysOfWeekArr" :placeholder="$t('job.trigger.placeholder.daysOfWeek')" multiple filterable collapse-tags style="width: 160px;">
                <el-option
                  v-for="ele in daysOfWeek"
                  :key="ele.value"
                  :label="ele.label"
                  :value="ele.value"
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
import { next, isBlank, startAfterEndTime } from '.././scripts/support'

const DAILY_INTERVAL_OBJ = {
  triggerName: '',
  startTime: null,
  endTime: null,
  startTimeOfDay: '00:00:00',
  endTimeOfDay: '23:59:59',
  interval: 1,
  timeUnit: 'MINUTE',
  repeatCount: -1,
  daysOfWeekArr: [1, 2, 3, 4, 5, 6, 7]
}

export default {
  name: 'DailyIntervalTrigger',
  props: {
    initTriggerArray: {
      type: Array,
      default: null
    },
    milliSecondTimeUnits: {
      type: Array,
      default: null
    },
    inDayTimeUnits: {
      type: Array,
      default: null
    },
    daysOfWeek: {
      type: Array,
      default: null
    }
  },
  data() {
    return {
      triggerArray: this.initTriggerArray
    }
  },
  created() {

  },
  methods: {
    generateId(item) {
      item.triggerName = next()
    },
    add() {
      this.triggerArray.push(Object.assign({}, DAILY_INTERVAL_OBJ))
    },
    remove(index) {
      this.triggerArray.splice(index, 1)
    },
    validateThenSet(trigger) {
      for (let i = 0; i < this.triggerArray.length; i++) {
        const item = this.triggerArray[i]
        if (JSON.stringify(item) !== JSON.stringify(DAILY_INTERVAL_OBJ)) {
          if (isBlank(item.triggerName)) {
            this.$message.error(this.$t('job.trigger.tip.triggerNameNotBlank') + ':' + JSON.stringify(item))
            return false
          }
          if (startAfterEndTime(item.startTime, item.endTime)) {
            this.$message.error(this.$t('job.trigger.tip.startAfterEndTime') + ':' + item.triggerName)
            return false
          }
          if (isBlank(item.triggerName)) {
            item.triggerName = next()
          }
          if (!trigger.dailyIntervalTriggers) {
            trigger.dailyIntervalTriggers = []
          }
          item.daysOfWeek = item.daysOfWeekArr.join(',')
          trigger.dailyIntervalTriggers.push(item)
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
