<template>
  <v-chart :options="option" :style="{height:height,width:width}" />
</template>

<script>
import resize from './mixins/resize'

export default {
  name: 'LineChart',
  mixins: [resize],
  props: {
    width: {
      type: String,
      default: '100%'
    },
    height: {
      type: String,
      default: '300px'
    }
  },
  data() {
    return {
      option: {}
    }
  },
  methods: {
    initOption(meta) {
      this.option = {
        tooltip: {
          trigger: 'axis'
        },
        legend: {
          data: [meta.chartName]
        },
        xAxis: {
          type: 'category',
          data: meta.xAxisData
        },
        yAxis: {
          type: 'value'
        },
        series: [
          {
            name: meta.chartName,
            type: 'line',
            stack: meta.chartName,
            data: meta.seriesData
          }
        ]
      }
    }
  }
}
</script>

