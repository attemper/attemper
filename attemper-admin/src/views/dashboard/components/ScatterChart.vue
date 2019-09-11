<template>
  <v-chart :options="option" :style="{height:height,width:width}" />
</template>

<script>
import resize from './mixins/resize'

export default {
  mixins: [resize],
  props: {
    width: {
      type: String,
      default: '100%'
    },
    height: {
      type: String,
      default: '100px'
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
          position: 'top'
        },
        title: [{
          textBaseline: 'middle',
          top: '20%',
          text: meta.text
        }],
        singleAxis: [{
          left: 100,
          type: 'category',
          boundaryGap: false,
          data: meta.axisData,
          height: '50',
          axisLabel: {
            interval: 0
          }
        }],
        series: [{
          singleAxisIndex: 0,
          coordinateSystem: 'singleAxis',
          type: 'scatter',
          data: meta.seriesData,
          symbolSize: function(dataItem) {
            return dataItem[1] * 10
          }
        }]
      }
    }
  }
}
</script>

