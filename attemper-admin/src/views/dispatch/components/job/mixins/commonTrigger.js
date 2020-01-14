import { next } from '../../../scripts/support'
import DateTimeGenerator from '@/components/DateTimeGenerator'

export default {
  components: { DateTimeGenerator },
  props: {
    calendars: {
      type: Array,
      default: null
    }
  },
  data() {
    return {
      triggerArray: [],
      misfireInstructions: [],
      simpleTriggerMisfireInstructions: []
    }
  },
  created() {
    this.loadConst()
  },
  methods: {
    generateId(item) {
      item.triggerName = next()
    },
    removeId(item, index) {
      this.remove(index)
      if (this.triggerArray.length === 0) {
        this.add()
      }
    },
    put(staticObj) {
      this.triggerArray.push(Object.assign({}, staticObj))
    },
    remove(index) {
      this.triggerArray.splice(index, 1)
    },
    testByReq(req, index) {
      req(this.handle(this.triggerArray[index])).then(res => {
        if (!res.data.result || res.data.result.length === 0) {
          this.$message.warning(this.$t('tip.noLongerTriggered'))
        } else {
          this.$notify.success({
            title: this.$t('tip.testResult'),
            dangerouslyUseHTMLString: true,
            message: res.data.result.join('<br>')
          })
        }
      })
    },
    handle(item) {
      return item
    },
    loadConst() {
      import(`@/constant/array/${localStorage.getItem('language')}.js`).then((array) => {
        this.misfireInstructions = array.misfireInstructions
        this.simpleTriggerMisfireInstructions = array.simpleTriggerMisfireInstructions
      })
    }
  }
}
