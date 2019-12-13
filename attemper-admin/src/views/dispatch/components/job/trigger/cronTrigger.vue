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
          <el-form :model="item" label-width="100px" label-position="left">
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
                <date-time-generator v-model="item.startTime" style="width: 100%;" :placeholder="$t('columns.startTime')" @update="item.startTime = $event" />
              </el-col>
              <el-col :span="11" :offset="2">
                <date-time-generator v-model="item.endTime" style="width: 100%;" :placeholder="$t('columns.endTime')" @update="item.endTime = $event" />
              </el-col>
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
          </el-form>
        </el-col>
      </el-row>
    </div>
  </div>
</template>

<script>
import CronInput from 'vue-cron-generator/src/components/cron-input'
import { DEFAULT_CRON_EXPRESSION } from 'vue-cron-generator/src/constant/filed'
import { isBlank } from '@/utils/tools'
import CommonTrigger from '../mixins/commonTrigger'
import { testCronTriggerReq } from '@/api/dispatch/job'

const DEF_OBJ = {
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
      this.put(Object.assign({}, DEF_OBJ))
    },
    change(newCron, index) {
      this.triggerArray[index].expression = newCron
    },
    reset(oldCron, index) {
      this.triggerArray[index].expression = DEFAULT_CRON_EXPRESSION
    },
    test(index) {
      this.testByReq(testCronTriggerReq, index)
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
  @import "../../../styles/jobs";
</style>
