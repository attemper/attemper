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
            <el-form-item :label="$t('dispatch.trigger.title.startTimeOfDay')">
              <el-time-picker
                v-model="item.startTimeOfDay"
                :placeholder="$t('dispatch.trigger.placeholder.startTimeOfDay')"
                value-format="HH:mm:ss"
              />
            </el-form-item>
            <el-form-item :label="$t('dispatch.trigger.title.timeUnit')">
              <el-select v-model="item.timeUnit" :placeholder="$t('dispatch.trigger.placeholder.timeUnit')" filterable>
                <el-option
                  v-for="ele in overDayTimeUnits"
                  :key="ele.value"
                  :label="ele.label"
                  :value="ele.value"
                />
              </el-select>
            </el-form-item>
            <el-form-item :label="$t('dispatch.trigger.title.repeatCount')">
              <el-input-number v-model="item.repeatCount" :placeholder="$t('dispatch.trigger.placeholder.repeatCount')" :precision="0" :min="-1" :step="1" controls-position="right" />
            </el-form-item>
            <el-form-item :label="$t('dispatch.trigger.title.reversed')">
              <el-switch v-model="item.reversed" />
            </el-form-item>
            <el-form-item :label="$t('dispatch.trigger.title.innerOffset')">
              <el-input-number v-model="item.innerOffset" :placeholder="$t('dispatch.trigger.placeholder.innerOffset')" :precision="0" :min="0" :step="1" controls-position="right" />
            </el-form-item>
            <el-form-item :label="$t('dispatch.trigger.title.outerOffset')">
              <el-input-number v-model="item.outerOffset" :placeholder="$t('dispatch.trigger.placeholder.outerOffset')" :precision="0" :min="0" :step="1" controls-position="right" />
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
import { isBlank } from '@/utils/tools'
import CommonTrigger from './mixins/commonTrigger'

const CALENDAR_OFFSET_OBJ = {
  triggerName: '',
  startTime: null,
  endTime: null,
  startTimeOfDay: '08:00:00',
  timeUnit: 'WEEK',
  repeatCount: -1,
  reversed: false,
  innerOffset: 0,
  outerOffset: 0
}

export default {
  name: 'CalendarOffsetTrigger',
  mixins: [CommonTrigger],
  props: {
    overDayTimeUnits: {
      type: Array,
      default: null
    }
  },
  methods: {
    add() {
      this.put(CALENDAR_OFFSET_OBJ)
    },
    validateThenSet(trigger) {
      if (!trigger.calendarOffsetTriggers) {
        trigger.calendarOffsetTriggers = []
      }
      for (let i = 0; i < this.triggerArray.length; i++) {
        const item = this.triggerArray[i]
        if (!isBlank(item.triggerName)) {
          trigger.calendarOffsetTriggers.push(item)
        }
      }
      return true
    }
  }
}
</script>

<style rel="stylesheet/scss" lang="scss">
  @import "../../styles/jobs";
</style>
