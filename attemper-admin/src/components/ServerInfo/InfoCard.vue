<template>
  <div>
    <el-card shadow="hover" style="margin-bottom: 20px;">
      <div slot="header">
        <b>{{ title }}</b>
      </div>
      <div>
        <el-table :data="data" :show-header="showHeader">
          <el-table-column prop="displayName" min-width="70px">
            <template v-slot="scope">
              <span>{{ scope.row.displayName }}</span>
            </template>
          </el-table-column>
          <el-table-column prop="value" min-width="130px">
            <template v-slot="scope">
              <span>{{ scope.row.value }}</span>
            </template>
          </el-table-column>
        </el-table>
      </div>
    </el-card>
  </div>
</template>

<script>
export default {
  name: 'InfoCard',
  props: {
    title: {
      type: String,
      default: null
    },
    data: {
      type: Array,
      default: null
    }
  },
  data() {
    return {
      showHeader: false
    }
  }
}
</script>
