<template>
  <div class="dashboard-editor-container">
    <github-corner class="github-corner" />

    <panel-group :cell-number="cellNumber" :admin="admin" />

    <chart-group :cell-number="cellNumber" />

    <scatter-chart-group />
  </div>
</template>

<script>
import GithubCorner from '@/components/GithubCorner'
import PanelGroup from './components/PanelGroup'
import ScatterChartGroup from './components/ScatterChartGroup'
import ChartGroup from './components/ChartGroup'
import { mapGetters } from 'vuex'

export default {
  name: 'Dashboard',
  components: {
    ChartGroup,
    GithubCorner,
    PanelGroup,
    ScatterChartGroup
  },
  computed: {
    ...mapGetters([
      'admin'
    ]),
    cellNumber() {
      return this.admin ? 8 : 12
    }
  }
}
</script>

<style lang="scss" scoped>
.dashboard-editor-container {
  padding: 10px;
  background-color: rgb(240, 242, 245);
  position: relative;

  .github-corner {
    position: absolute;
    top: 0px;
    border: 0;
    right: 0;
  }

  .chart-wrapper {
    background: #fff;
    padding: 16px 16px 0;
    margin-bottom: 32px;
  }
}

@media (max-width:1024px) {
  .chart-wrapper {
    padding: 8px;
  }
}
</style>
