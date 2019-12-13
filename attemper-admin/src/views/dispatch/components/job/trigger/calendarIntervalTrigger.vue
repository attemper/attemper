<template>
  <div>
    <div v-for="(item,index) in triggerArray" :key="index" class="row-bottom">
      <hr v-show="index > 0" class="row-hr">
      <el-row>
        <el-col :span="triggerArray.length < 2 ? 3 : 6">
          <el-button icon="el-icon-plus" type="success" @click="add" />
          <el-button v-show="triggerArray.length>1" icon="el-icon-minus" type="danger" @click="remove(index)" />
          <el-button v-show="item.triggerName" type="primary" style="display: inline; margin: 10px 0 0 0;" @click="test(index)">{{ $t('actions.test') }}</el-button>
        </el-col>
        <el-col :span="triggerArray.length < 2 ? 21 : 18">
          <el-form :model="item" label-width="110px" label-position="left">
            <el-form-item :label="$t('dispatch.trigger.title.triggerName')">
              <el-input v-model="item.triggerName" :placeholder="$t('dispatch.trigger.placeholder.triggerName')">
                <el-button slot="prepend" @click="generateId(item)">
                  <svg-icon icon-class="random" />
                </el-button>
                <el-button slot="append" icon="el-icon-close" @click="removeId(item, index)" />
              </el-input>
            </el-form-item>
            <el-form-item :label="$t('dispatch.trigger.title.timeRange')">
              <el-col :span="11">
                <date-time-generator v-model="item.startTime" style="width: 100%;" :placeholder="$t('columns.startTime')" @update="item.startTime = $event" />
              </el-col>
              <el-col :span="11" :offset="2">
                <date-time-generator v-model="item.endTime" style="width: 100%;" :placeholder="$t('columns.endTime')" @update="item.endTime = $event" />
              </el-col>
            </el-form-item>
            <el-form-item :label="$t('columns.repeatInterval')">
              <el-input-number v-model="item.repeatInterval" :placeholder="$t('columns.repeatInterval')" :precision="0" :min="1" :step="1" controls-position="right" style="width: 100%;" />
            </el-form-item>
            <el-form-item :label="$t('dispatch.trigger.title.timeUnit')">
              <el-select v-model="item.timeUnit" :placeholder="$t('dispatch.trigger.placeholder.timeUnit')" style="width: 100%;">
                <el-option
                  v-for="ele in inDayTimeUnits.concat(dayTimeUnit).concat(overDayTimeUnits)"
                  :key="ele.value"
                  :label="ele.label"
                  :value="ele.value"
                />
              </el-select>
            </el-form-item>
            <el-form-item :label="$t('dispatch.trigger.title.repeatCount')">
              <el-input-number v-model="item.repeatCount" :placeholder="$t('dispatch.trigger.placeholder.repeatCount')" :precision="0" :min="-1" :step="1" controls-position="right" style="width: 100%;" />
            </el-form-item>
            <el-form-item :label="$t('dispatch.trigger.title.calendar')">
              <el-select v-model="item.calendarNames" multiple style="width: 100%;">
                <el-option v-for="cell in calendars" :key="cell.calendarName" :label="cell.displayName" :value="cell.calendarName" />
              </el-select>
            </el-form-item>
            <el-form-item :label="$t('dispatch.trigger.title.misfireInstruction')">
              <el-select v-model="item.misfireInstruction" filterable clearable style="width: 100%;">
                <el-option
                  v-for="ele in misfireInstructions"
                  :key="ele.value"
                  :label="ele.label"
                  :value="ele.value"
                />
              </el-select>
            </el-form-item>
            <el-form-item :label="$t('dispatch.trigger.title.timeZone')">
              <el-select v-model="item.timeZoneId" filterable clearable style="width: 100%;">
                <el-option
                  v-for="ele in timeZones"
                  :key="ele"
                  :label="ele"
                  :value="ele"
                />
              </el-select>
            </el-form-item>
            <el-form-item :label="$t('dispatch.trigger.title.preserveDayLight')">
              <el-switch v-model="item.preserveDayLight" :active-value="1" :inactive-value="0" />
            </el-form-item>
            <el-form-item :label="$t('dispatch.trigger.title.skipDayIfNoHour')">
              <el-switch v-model="item.skipDayIfNoHour" :active-value="1" :inactive-value="0" />
            </el-form-item>
          </el-form>
        </el-col>
      </el-row>
    </div>
  </div>
</template>

<script>
import { isBlank } from '@/utils/tools'
import CommonTrigger from '../mixins/commonTrigger'
import { testCalendarIntervalTriggerReq } from '@/api/dispatch/job'

const DEF_OBJ = {
  triggerName: '',
  startTime: null,
  endTime: null,
  repeatInterval: 1,
  timeUnit: 'DAY',
  repeatCount: -1,
  preserveDayLight: 0,
  skipDayIfNoHour: 0,
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
      this.put(Object.assign({}, DEF_OBJ))
    },
    test(index) {
      this.testByReq(testCalendarIntervalTriggerReq, index)
    },
    validateThenSet(trigger) {
      if (!trigger.calendarIntervalTriggers) {
        trigger.calendarIntervalTriggers = []
      }
      for (let i = 0; i < this.triggerArray.length; i++) {
        const item = this.triggerArray[i]
        if (!isBlank(item.triggerName)) {
          trigger.calendarIntervalTriggers.push(item)
        }
      }
      return true
    }
  }
}
</script>

<style rel="stylesheet/scss" lang="scss">
  @import "../../../styles/jobs";
</style>
