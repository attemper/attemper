import DateTimeGenerator from '@/components/DateTimeGenerator'

export default {
  components: { DateTimeGenerator },
  props: {
    jobName: {
      type: String,
      default: null
    }
  },
  data() {
    return {
      visible: true,
      firedSources: []
    }
  },
  created() {
    this.loadConstant()
  },
  methods: {
    loadConstant() {
      import(`@/constant/array/${localStorage.getItem('language')}.js`).then((array) => {
        this.firedSources = array.firedSources
      })
    }
  }
}
