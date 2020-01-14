<template>
  <el-timeline>
    <el-timeline-item
      v-for="item in value"
      :key="item.nextFireTime"
      :timestamp="item.nextFireTime"
      placement="top"
    >
      <div v-for="plan in item.plans" :key="plan.jobName">{{ plan.jobName }}</div>
    </el-timeline-item>
  </el-timeline>
</template>

<script>
export default {
  name: 'JobPlan',
  props: {
    value: {
      type: Array,
      default: null
    }
  }
}
</script>
