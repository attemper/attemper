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
            <!--<el-col :span="10" :offset="2">
              <el-select v-model="item.calendar" :placeholder="$t('dispatch.trigger.placeholder.calendar')">
                <el-option
                  v-for="ele in calendars"
                  :key="ele.value"
                  :label="ele.label"
                  :value="ele.value"/>
              </el-select>
            </el-col>-->
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
            <el-form-item :label="$t('dispatch.trigger.title.interval')">
              <el-input-number v-model="item.interval" :placeholder="$t('dispatch.trigger.placeholder.interval')" :precision="0" :min="1" :step="1" controls-position="right" />
            </el-form-item>
            <el-form-item :label="$t('dispatch.trigger.title.timeUnit')">
              <el-select v-model="item.timeUnit" :placeholder="$t('dispatch.trigger.placeholder.timeUnit')">
                <el-option
                  v-for="ele in inDayTimeUnits.concat(dayTimeUnit).concat(overDayTimeUnits)"
                  :key="ele.value"
                  :label="ele.label"
                  :value="ele.value"
                />
              </el-select>
            </el-form-item>
            <el-form-item :label="$t('dispatch.trigger.title.repeatCount')">
              <el-input-number v-model="item.repeatCount" :placeholder="$t('dispatch.trigger.placeholder.repeatCount')" :precision="0" :min="-1" :step="1" controls-position="right" />
            </el-form-item>
            <el-form-item :label="$t('dispatch.trigger.title.preserveDayLight')">
              <el-switch v-model="item.preserveDayLight" />
            </el-form-item>
            <el-form-item :label="$t('dispatch.trigger.title.skipDayIfNoHour')">
              <el-switch v-model="item.skipDayIfNoHour" />
            </el-form-item>
            <el-form-item :label="$t('dispatch.trigger.title.timeZone')">
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
import { isBlank, startAfterEndTime } from '.././scripts/support'
import CommonTrigger from './mixins/commonTrigger'

const CALENDAR_INTERVAL_OBJ = {
  triggerName: '',
  // calendar: null,
  startTime: null,
  endTime: null,
  interval: 1,
  timeUnit: 'DAY',
  repeatCount: -1,
  preserveDayLight: false,
  skipDayIfNoHour: false,
  timeZoneId: null
}

export default {
  name: 'CalendarIntervalTrigger',
  mixins: [CommonTrigger],
  props: {
    dayTimeUnit: {
      type: Array,
      default: null
    },
    inDayTimeUnits: {
      type: Array,
      default: null
    },
    overDayTimeUnits: {
      type: Array,
      default: null
    },
    timeZones: {
      type: Array,
      default: null
    }
  },
  methods: {
    add() {
      this.put(CALENDAR_INTERVAL_OBJ)
    },
    validateThenSet(trigger) {
      for (let i = 0; i < this.triggerArray.length; i++) {
        const item = this.triggerArray[i]
        if (JSON.stringify(item) !== JSON.stringify(CALENDAR_INTERVAL_OBJ)) {
          if (isBlank(item.triggerName)) {
            this.$message.error(this.$t('dispatch.trigger.tip.triggerNameNotBlank') + ':' + JSON.stringify(item))
            return false
          }
          if (startAfterEndTime(item.startTime, item.endTime)) {
            this.$message.error(this.$t('dispatch.trigger.tip.startAfterEndTime') + ':' + item.triggerName)
            return false
          }
          if (!trigger.calendarIntervalTriggers) {
            trigger.calendarIntervalTriggers = []
          }
          trigger.calendarIntervalTriggers.push(item)
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
