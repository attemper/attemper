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
          trigger: 'item',
          formatter: '{a} <br/>{b} : {c} ({d}%)'
        },
        legend: {
          left: 'center',
          bottom: '10',
          data: meta.legendData
        },
        series: [
          {
            name: meta.chartName,
            type: 'pie',
            // roseType: 'radius',
            // radius: [15, 95],
            // center: ['50%', '38%'],
            data: meta.seriesData,
            animationEasing: 'cubicInOut',
            animationDuration: 2600,
            itemStyle: {
              normal: {
                color: function(params) {
                  return meta.colorList[params.data.status]
                }
              }
            }
          }
        ]
      }
    }
  }
}
</script>

