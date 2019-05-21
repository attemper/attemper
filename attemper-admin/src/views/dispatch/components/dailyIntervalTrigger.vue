<template>
  <div>
    <div v-for="(item,index) in triggerArray" :key="index" class="trigger-row-bottom">
      <el-row>
        <el-col :span="4">
          <el-button icon="el-icon-plus" type="success" @click="add" />
          <el-button v-show="triggerArray.length>1" icon="el-icon-minus" type="danger" @click="remove(index)" />
        </el-col>
        <el-col :span="20">
          <el-form :model="item" label-width="150px" label-position="left">
            <el-form-item :label="$t('dispatch.trigger.title.triggerName')">
              <el-input v-model="item.triggerName" :placeholder="$t('dispatch.trigger.placeholder.triggerName')">
                <el-button slot="prepend" @click="generateId(item)">
                  <svg-icon icon-class="random" />
                </el-button>
                <el-button slot="append" icon="el-icon-minus" @click="removeId(item, index)" />
              </el-input>
            </el-form-item>
            <el-form-item :label="$t('dispatch.trigger.title.timeRange')">
              <el-col :span="11">
                <el-date-picker
                  v-model="item.startTime"
                  :placeholder="$t('dispatch.trigger.placeholder.startTime')"
                  type="datetime"
                  value-format="yyyy-MM-dd HH:mm:ss"
                />
              </el-col>
              <el-col :span="11" :offset="1">
                <el-date-picker
                  v-model="item.endTime"
                  :placeholder="$t('dispatch.trigger.placeholder.endTime')"
                  type="datetime"
                  value-format="yyyy-MM-dd HH:mm:ss"
                />
              </el-col>
            </el-form-item>
            <el-form-item :label="$t('dispatch.trigger.title.timeRangeOfDay')">
              <el-col :span="11">
                <el-time-picker
                  v-model="item.startTimeOfDay"
                  :placeholder="$t('dispatch.trigger.placeholder.startTimeOfDay')"
                  value-format="HH:mm:ss"
                />
              </el-col>
              <el-col :span="11" :offset="1">
                <el-time-picker
                  v-model="item.endTimeOfDay"
                  :placeholder="$t('dispatch.trigger.placeholder.endTimeOfDay')"
                  value-format="HH:mm:ss"
                />
              </el-col>
            </el-form-item>
            <el-form-item :label="$t('dispatch.trigger.title.interval')">
              <el-input-number v-model="item.interval" :placeholder="$t('dispatch.trigger.placeholder.interval')" :precision="0" :min="1" :step="1" controls-position="right" />
            </el-form-item>
            <el-form-item :label="$t('dispatch.trigger.title.timeUnit')">
              <el-select v-model="item.timeUnit" :placeholder="$t('dispatch.trigger.placeholder.timeUnit')" filterable>
                <el-option
                  v-for="ele in milliSecondTimeUnits.concat(inDayTimeUnits)"
                  :key="ele.value"
                  :label="ele.label"
                  :value="ele.value"
                />
              </el-select>
            </el-form-item>
            <el-form-item :label="$t('dispatch.trigger.title.repeatCount')">
              <el-input-number v-model="item.repeatCount" :placeholder="$t('dispatch.trigger.placeholder.repeatCount')" :precision="0" :min="-1" :step="1" controls-position="right" />
            </el-form-item>
            <el-form-item :label="$t('dispatch.trigger.title.daysOfWeek')">
              <el-select v-model="item.daysOfWeekArr" :placeholder="$t('dispatch.trigger.placeholder.daysOfWeek')" multiple filterable collapse-tags style="width: 160px;">
                <el-option
                  v-for="ele in daysOfWeek"
                  :key="ele.value"
                  :label="ele.label"
                  :value="ele.value"
                />
              </el-select>
            </el-form-item>
            <el-form-item :label="$t('dispatch.trigger.title.calendar')">
              <el-select v-model="item.calendarNames" multiple>
                <el-option-group v-for="group in calendarGroups" :key="group.label" :label="group.label">
                  <el-option v-for="cell in group.options" :key="cell.calendarName" :label="cell.displayName" :value="cell.calendarName" />
                </el-option-group>
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
import CommonTrigger from './mixins/commonTrigger'

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
  mixins: [CommonTrigger],
  props: {
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
  methods: {
    add() {
      this.put(DAILY_INTERVAL_OBJ)
    },
    validateThenSet(trigger) {
      for (let i = 0; i < this.triggerArray.length; i++) {
        const item = this.triggerArray[i]
        if (JSON.stringify(item) !== JSON.stringify(DAILY_INTERVAL_OBJ)) {
          if (isBlank(item.triggerName)) {
            this.$message.error(this.$t('dispatch.trigger.tip.triggerNameNotBlank') + ':' + JSON.stringify(item))
            return false
          }
          if (startAfterEndTime(item.startTime, item.endTime)) {
            this.$message.error(this.$t('dispatch.trigger.tip.startAfterEndTime') + ':' + item.triggerName)
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
