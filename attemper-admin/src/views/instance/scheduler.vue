<template>
  <div class="app-container">
    <div class="filter-container">
      <el-select v-model="serverUri" :placeholder="$t('monitor.columns.schedulerUri')" class="filter-item" style="width: 260px;" @change="change">
        <el-option v-for="item in instanceInfos" :key="item.homePageUrl" :label="item.homePageUrl" :value="item.homePageUrl" />
      </el-select>
      <el-button class="filter-item" type="primary" icon="el-icon-refresh" @click="change(serverUri)" />
    </div>
    <server-info v-if="instanceInfos.length > 0" :info="info" />
  </div>
</template>

<script>
import { listSchedulerServiceReq, getServerInfoReq } from '@/api/dispatch/tool'
import ServerInfo from '@/components/ServerInfo'
import { formatDate } from '@/utils/tools'
import { dateFormatPattern } from '@/settings'

export default {
  name: 'Scheduler',
  components: {
    ServerInfo
  },
  data() {
    return {
      serverUri: null,
      instanceInfos: [],
      info: {}
    }
  },
  created() {
    this.loadService()
  },
  methods: {
    loadService() {
      listSchedulerServiceReq().then(res => {
        if (!res.data.result || res.data.result.length === 0) {
          this.$message.warning(this.$t('tip.noneScheduler'))
        } else {
          this.instanceInfos = res.data.result.map(item => item.instanceInfo)
          this.serverUri = this.instanceInfos[0].homePageUrl
          this.change(this.serverUri)
        }
      })
    },
    change(uri) {
      const serviceArray = this.executeApp(uri)
      getServerInfoReq({
        address: uri,
        source: 'scheduler'
      }).then(res => {
        this.info = res.data.result
        this.info.service = serviceArray
        for (const key in this.info) {
          this.setDisplayName(this.info[key], key)
        }
      })
    },
    executeApp(uri) {
      const serviceArray = []
      const instanceInfo = this.instanceInfos.find(item => {
        return uri === item.homePageUrl
      })
      for (const key in instanceInfo) {
        const displayName = this.$t('serverInfo.service.' + key)
        if (displayName && !displayName.startsWith('serverInfo.')) {
          const serviceInfo = {
            name: key,
            displayName: displayName,
            value: key.endsWith('Timestamp') ? formatDate(new Date(instanceInfo[key]), dateFormatPattern) : instanceInfo[key]
          }
          serviceArray.push(serviceInfo)
        }
      }
      return serviceArray
    },
    setDisplayName(array, key) {
      for (let i = array.length - 1; i >= 0; i--) {
        const item = array[i]
        item.displayName = this.$t('serverInfo.' + key + '.' + item.name)
        if (!item.displayName || item.displayName.startsWith('serverInfo.')) {
          console.warn('undefined key:' + item.name)
          item.displayName = item.name
        }
      }
    }
  }
}
</script>
