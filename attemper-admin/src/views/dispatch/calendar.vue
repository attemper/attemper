<template>
  <div>
    <el-container>
      <el-aside width="200px">
        <el-select v-model="currentCalendar.calendarName" @change="change">
          <el-option v-for="item in calendars" :key="item.calendarName" :label="item.displayName" :value="item.calendarName" />
        </el-select>
        <div v-access="'calendar-saveDate'">
          <el-checkbox v-model="excluded" :disabled="!dayObj" class="cell">{{ $t('actions.exclude') }}</el-checkbox>
          <el-input v-model="remark" :disabled="!dayObj" class="cell" :placeholder="$t('placeholders.remark')" />
          <el-button :disabled="!dayObj" class="cell" type="success" @click="saveDate">{{ $t('actions.save') }}</el-button>
          <el-upload style="margin-left: 5px; display: inline;" action="" accept="text/plain" :on-change="importDate" :auto-upload="false" :show-file-list="false">
            <el-button class="cell" type="primary" icon="el-icon-upload2">{{ $t('actions.import') }}</el-button>
          </el-upload>
        </div>
      </el-aside>
      <el-main>
        <v-calendar
          :min-date="new Date(2021, 0, 1)"
          :attributes="attrs"
          :from-date="new Date(new Date().getFullYear(), 0, 1)"
          :columns="layout.columns"
          :rows="layout.rows"
          :is-expanded="layout.isExpanded"
          @dayclick="click"
        />
      </el-main>
    </el-container>
  </div>
</template>

<script>
import { listReq, saveDayReq, removeDayReq, listDayReq, importDateReq } from '@/api/dispatch/calendar'
import access from '@/directive/access/index.js'
import { addZero } from '@/utils/tools'

export default {
  directives: { access },
  data() {
    return {
      dayObj: null,
      calendars: [],
      excluded: false,
      remark: '',
      attrs: [],
      currentCalendar: {
        calendarName: null
      },
      calendarConfigs: []
    }
  },
  computed: {
    layout() {
      return this.$screens(
        {
          // Default layout for mobile
          default: {
            columns: 1,
            rows: 1,
            isExpanded: true
          },
          // Override for large screens
          lg: {
            columns: 3,
            rows: 4,
            isExpanded: true
          }
        }
      )
    }
  },
  created() {
    this.search()
  },
  methods: {
    search() {
      if (this.currentCalendar && this.currentCalendar.calendarName) {
        this.getCalendarConfigs()
      } else {
        this.calendars = []
        listReq().then(res => {
          this.calendars = res.data.result
          this.currentCalendar = Object.assign({}, res.data.result[0])
          this.getCalendarConfigs()
        })
      }
    },
    saveDate() {
      const operatedDay = this.formatDay(this.dayObj)
      this.$confirm(operatedDay, this.$t('tip.confirmMsg'), { type: 'warning' })
        .then(() => {
          const data = {
            calendarName: this.currentCalendar.calendarName,
            dayNum: operatedDay,
            remark: this.remark
          }
          const request = this.excluded ? saveDayReq(data) : removeDayReq(data)
          request.then(res => {
            this.$message.success(res.data.msg)
            this.getCalendarConfigs()
          })
        })
    },
    importDate(file) {
      this.$confirm(this.$t('tip.confirm') + ' ' + file.name, this.$t('tip.confirmMsg'), { type: 'warning' })
        .then(() => {
          const data = new FormData()
          data.append('calendarName', this.currentCalendar.calendarName)
          data.append('file', file.raw)
          importDateReq(data).then(res => {
            this.$message.success(res.data.msg)
            this.search()
          })
        })
    },
    change() {
      this.getCalendarConfigs()
    },
    getCalendarConfigs() {
      this.initAttrs()
      listDayReq(this.currentCalendar).then(res => {
        this.calendarConfigs = res.data.result
        this.calendarConfigs.forEach(day => {
          const dayNumStr = String(day.dayNum)
          const attr = {
            remark: day.remark,
            dates: new Date(parseInt(dayNumStr.substring(0, 4)), parseInt(dayNumStr.substring(4, 6)) - 1, parseInt(dayNumStr.substring(6, 8)))
          }
          if (attr.dates.getDay() === 0 || attr.dates.getDay() === 6) {
            attr.highlight = 'gray'
          } else {
            attr.highlight = 'red'
          }
          this.attrs.push(attr)
        })
        this.remark = null
      })
    },
    initAttrs() {
      this.attrs = [{
        bar: 'purple',
        dates: [
          new Date()
        ]
      }]
    },
    click(day) {
      this.dayObj = day
      const dayStr = this.formatDay(day)
      const target = this.attrs.find(attr => { return this.formatDate(attr.dates) === dayStr })
      this.excluded = target !== undefined && target !== null
      this.remark = target ? target.remark : null
    },
    formatDate(date) {
      if (date instanceof Array) {
        return null
      }
      return date.getFullYear() + addZero(date.getMonth() + 1) + addZero(date.getDate())
    },
    formatDay(day) {
      return day.year + addZero(day.month) + addZero(day.day)
    }
  }
}
</script>

<style>
  .cell{
    margin-top: 15px;
  }
</style>
