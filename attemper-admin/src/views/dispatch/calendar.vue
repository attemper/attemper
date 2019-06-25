<template>
  <div>
    <el-container>
      <el-aside width="200px">
        <el-select v-model="currentCalendar.calendarName" @change="change">
          <el-option-group v-for="group in calendarGroups" :key="group.label" :label="group.label">
            <el-option v-for="item in group.options" :key="item.calendarName" :label="item.displayName" :value="item.calendarName" />
          </el-option-group>
        </el-select>
        <div v-access="'calendar-saveDate'">
          <el-checkbox v-model="excluded" :disabled="!dayObj" class="cell">{{ $t('actions.exclude') }}</el-checkbox>
          <el-input v-model="remark" :disabled="!dayObj" class="cell" :placeholder="$t('placeholders.remark')" />
          <el-button :disabled="!dayObj" class="cell" type="success" @click="saveDate">{{ $t('actions.save') }}</el-button>
        </div>
      </el-aside>
      <el-main>
        <v-calendar
          :min-date="new Date(2019, 0, 1)"
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
import { listReq, saveDayReq, removeDayReq, listDayReq } from '@/api/dispatch/calendar'
import access from '@/directive/access/index.js'

export default {
  directives: { access },
  data() {
    return {
      dayObj: null,
      calendarGroups: [],
      calendarTypes: [],
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
            columns: 4,
            rows: 3,
            isExpanded: true
          }
        }
      )
    }
  },
  created() {
    this.loadConst()
    this.search()
  },
  methods: {
    search() {
      this.calendarGroups = []
      listReq().then(res => {
        this.calendarTypes.forEach(type => {
          const options = res.data.result.filter(item => item.type === type.value)
          this.calendarGroups.push({
            label: type.label,
            options: options && options.length > 0 ? options : []
          })
        })
        this.currentCalendar = Object.assign({}, res.data.result[0])
        this.getCalendarConfigs()
      })
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
    change() {
      this.getCalendarConfigs()
    },
    getCalendarConfigs() {
      this.initAttrs()
      if (this.currentCalendar.type === 0) {
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
      }
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
      return date.getFullYear() + this.addZero(date.getMonth() + 1) + this.addZero(date.getDate())
    },
    formatDay(day) {
      return day.year + this.addZero(day.month) + this.addZero(day.day)
    },
    addZero(key) {
      return (key < 10 ? '0' : '') + key
    },
    loadConst() {
      import(`@/constant/array/${localStorage.getItem('language')}.js`).then((array) => {
        this.calendarTypes = array.calendarTypes
      })
    }
  }
}
</script>

<style>
  .cell{
    margin-top: 15px;
  }
</style>
