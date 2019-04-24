import Pagination from '@/components/Pagination'
import waves from '@/directive/waves'
import DateTimeGenerator from '@/components/DateTimeGenerator'

export default {
  components: {
    DateTimeGenerator,
    Pagination
  },
  directives: { waves },
  data() {
    return {
      list: null,
      listLoading: true,
      page: {
        currentPage: 1,
        pageSize: 10,
        total: 0,
        jobName: undefined,
        displayName: undefined,
        status: [],
        sort: 'START_TIME DESC'
      },
      jobInstanceStatuses: [],
      jobInstance: {
        id: undefined,
        jobName: null,
        displayName: '',
        status: 0,
        startTime: null,
        duration: 0
      },
      downloadLoading: false,
      selections: []
    }
  },
  created() {
    this.loadConst()
  },
  methods: {
    sortChange(data) {
      const { prop, order } = data
      if (prop === 'startTime') {
        this.sortByName(order)
      }
    },
    sortByName(order) {
      if (order === 'ascending') {
        this.page.sort = 'START_TIME'
      } else {
        this.page.sort = 'START_TIME DESC'
      }
      this.search()
    },
    formatStatus(item) {
      for (let i = 0; i < this.jobInstanceStatuses.length; i++) {
        const option = this.jobInstanceStatuses[i]
        if (option.value === item) {
          return option.label
        }
      }
      return item
    },
    selectRow(row) {
      this.$refs.tables.clearSelection()
      if (row && row.jobName) {
        this.$refs.tables.toggleRowSelection(row, true)
      }
      this.reset() // get the newest or reset to origin
    },
    handleSelectionChange(val) {
      this.selections = val
    },
    initPageStatus() {
      if (this.page.status || this.page.status.length === 0) {
        this.page.status = this.jobInstanceStatuses.map(item => item.value)
      }
    },
    openTrace(row) {
      const route = {
        name: 'trace',
        params: {
          key: row.jobName,
          rootProcInstId: row.rootProcInstId,
          procDefId: row.procDefId
        }
      }
      this.$router.push(route)
    }
  }
}
