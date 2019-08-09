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
                <el-button slot="append" icon="el-icon-close" @click="removeId(item, index)" />
              </el-input>
            </el-form-item>
            <el-form-item :label="$t('dispatch.trigger.title.expression')">
              <cron-input v-model="item.expression" :size="size" @change="change($event, index)" @reset="reset($event, index)" />
            </el-form-item>
            <el-form-item :label="$t('dispatch.trigger.title.timeRange')">
              <el-col :span="11">
                <el-date-picker
                  v-model="item.startTime"
                  :placeholder="$t('columns.startTime')"
                  type="datetime"
                  value-format="yyyy-MM-dd HH:mm:ss"
                  style="width: 100%;"
                />
              </el-col>
              <el-col :span="11" :offset="2">
                <el-date-picker
                  v-model="item.endTime"
                  :placeholder="$t('columns.endTime')"
                  type="datetime"
                  value-format="yyyy-MM-dd HH:mm:ss"
                  style="width: 100%;"
                />
              </el-col>
            </el-form-item>
            <el-form-item :label="$t('dispatch.trigger.title.calendar')">
              <el-select v-model="item.calendarNames" multiple style="width: 100%;">
                <el-option-group v-for="group in calendarGroups" :key="group.label" :label="group.label">
                  <el-option v-for="cell in group.options" :key="cell.calendarName" :label="cell.displayName" :value="cell.calendarName" />
                </el-option-group>
              </el-select>
            </el-form-item>
            <el-form-item :label="$t('dispatch.trigger.title.misfireInstruction')">
              <el-select v-model="item.misfireInstruction" filterable style="width: 100%;">
                <el-option
                  v-for="ele in misfireInstructions"
                  :key="ele.value"
                  :label="ele.label"
                  :value="ele.value"
                />
              </el-select>
            </el-form-item>
            <el-form-item :label="$t('dispatch.trigger.title.timeZone')">
              <el-select v-model="item.timeZoneId" filterable style="width: 100%;">
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
import { isBlank } from '@/utils/tools'
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
    timeZones: {
      type: Array,
      default: null
    }
  },
  computed: {
    size() {
      return localStorage.getItem('size')
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
      if (!trigger.cronTriggers) {
        trigger.cronTriggers = []
      }
      for (let i = 0; i < this.triggerArray.length; i++) {
        const item = this.triggerArray[i]
        if (!isBlank(item.triggerName)) {
          trigger.cronTriggers.push(item)
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
