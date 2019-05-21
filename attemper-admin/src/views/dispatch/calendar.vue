<template>
  <div>
    <el-container>
      <el-aside width="200px">
        <el-select v-model="currentCalendar.calendarName" @change="change">
          <el-option-group v-for="group in calendarGroups" :key="group.label" :label="group.label">
            <el-option v-for="item in group.options" :key="item.calendarName" :label="item.displayName" :value="item.calendarName" />
          </el-option-group>
        </el-select>
        <el-checkbox v-model="excluded" :disabled="!dayObj" class="cell">{{ $t('actions.exclude') }}</el-checkbox>
        <el-input v-model="remark" :disabled="!dayObj" class="cell" :placeholder="$t('placeholders.remark')" />
        <el-button :disabled="!dayObj" class="cell" type="success" @click="save">{{ $t('actions.save') }}</el-button>
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
import { load } from '@/constant'
export default {
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
    save() {
      const operatedDay = this.formatDay(this.dayObj)
      const msg = '<p>' + this.$t('tip.confirmMsg') + ':<br><span style="color: red">' + operatedDay + '</span></p>'
      this.$confirm(msg, this.$t('tip.confirm'), { type: 'warning', dangerouslyUseHTMLString: true })
        .then(() => {
          const data = {
            calendarName: this.currentCalendar.calendarName,
            dayName: operatedDay,
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
            const arr = day.dayName.split('-')
            const attr = {
              day: day.dayName,
              remark: day.remark,
              dates: new Date(parseInt(arr[0]), parseInt(arr[1]) - 1, parseInt(arr[2]))
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
      const target = this.attrs.find(attr => { return attr.day === dayStr })
      this.excluded = target !== undefined && target !== null
      this.remark = target ? target.remark : null
    },
    formatDay(day) {
      return day.year + '-' + this.addZero(day.month) + '-' + this.addZero(day.day)
    },
    addZero(key) {
      return key < 10 ? '0' + key : '' + key
    },
    loadConst() {
      load(`./array/${localStorage.getItem('language')}.js`).then((array) => {
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
