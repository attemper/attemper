<template>
  <div>
    <el-row :gutter="12">
      <el-col :span="12">
        <info-card :title="title.cpu" :data="info.cpu" />
        <info-card :title="$t('serverInfo.base.service')" :data="info.service" />
        <info-card :title="$t('serverInfo.base.file')" :data="info.file" />
        <info-card :title="$t('serverInfo.base.machine')" :data="info.machine" />
      </el-col>
      <el-col :span="12">
        <info-card :title="$t('serverInfo.base.memory')" :data="info.memory" />
        <info-card :title="title.jvm" :data="info.jvm" />
        <info-card :title="$t('serverInfo.base.resource')" :data="info.resource" />
      </el-col>
    </el-row>
  </div>
</template>

<script>
import InfoCard from './InfoCard'

export default {
  name: 'ServerInfo',
  components: {
    InfoCard
  },
  props: {
    info: {
      type: Object,
      default: null
    }
  },
  data() {
    return {
      title: {
        cpu: 'CPU',
        jvm: 'JVM'
      }
    }
  },
  created() {
  },
  methods: {
  }
}
</script>
