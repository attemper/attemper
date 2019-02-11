<template>
  <div ref="content" class="content">
    <div ref="canvas" class="canvas"/>
    <div ref="propertiesPanel" class="properties-panel-parent"/>
    <ul class="buttons">
      <li>{{ $t('actions.download') }}</li>
      <li>
        <a ref="saveDiagram" href="javascript:" title="download BPMN diagram">{{ $t('job.flowJob.btn.bpmn') }}</a>
      </li>
      <li>
        <a ref="saveSvg" href="javascript:" title="download as SVG image">{{ $t('job.flowJob.btn.svg') }}</a>
      </li>
    </ul>
  </div>
</template>

<script>
import BpmnModeler from 'bpmn-js/lib/Modeler'
import propertiesPanelModule from 'bpmn-js-properties-panel'
import propertiesProviderModule from 'bpmn-js-properties-panel/lib/provider/camunda'
import camundaModdleDescriptor from 'camunda-bpmn-moddle/resources/camunda'
import minimapModule from 'diagram-js-minimap'
// import { diff } from 'bpmn-js-differ' // https://github.com/bpmn-io/bpmn-js-differ
import prioritiesModule from 'bpmn-js-task-priorities/lib/priorities'
import customTranslate from '@/utils/customTranslate'

export default {
  data() {
    return {
      bpmnModeler: null,
      xmlStr: null,
      processName: ''
    }
  },
  mounted() {
    this.bindBpmn()
  },
  methods: {
    bindBpmn() {
      const canvas = this.$refs.canvas
      const customTranslateModule = {
        translate: ['value', customTranslate]
      }
      this.bpmnModeler = new BpmnModeler({
        container: canvas,
        propertiesPanel: {
          parent: this.$refs.propertiesPanel
        },
        additionalModules: [
          propertiesProviderModule,
          propertiesPanelModule,
          minimapModule,
          prioritiesModule,
          customTranslateModule
        ],
        moddleExtensions: {
          camunda: camundaModdleDescriptor
        },
        keyboard: {
          bindTo: document
        }
      })
      const _this = this
      const downloadLink = this.$refs.saveDiagram
      const downloadSvgLink = this.$refs.saveSvg
      this.bpmnModeler.on('commandStack.changed', function() {
        _this.saveSVG(function(err, svg) {
          _this.setEncoded(downloadSvgLink, 'diagram.svg', err ? null : svg)
        })

        _this.saveDiagram(function(err, xml) {
          _this.setEncoded(downloadLink, 'diagram.bpmn', err ? null : xml)
        })
      })

      this.createNewDiagram()
    },
    createNewDiagram() {
      const bpmnXmlStr = '<?xml version="1.0" encoding="UTF-8"?>\n' +
          '<bpmn2:definitions xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xmlns:bpmn2="http://www.omg.org/spec/BPMN/20100524/MODEL" xmlns:bpmndi="http://www.omg.org/spec/BPMN/20100524/DI" xmlns:dc="http://www.omg.org/spec/DD/20100524/DC" xmlns:di="http://www.omg.org/spec/DD/20100524/DI" xsi:schemaLocation="http://www.omg.org/spec/BPMN/20100524/MODEL BPMN20.xsd" id="sample-diagram" targetNamespace="http://bpmn.io/schema/bpmn">\n' +
          '  <bpmn2:process id="Process_1" isExecutable="false">\n' +
          '    <bpmn2:startEvent id="StartEvent_1"/>\n' +
          '  </bpmn2:process>\n' +
          '  <bpmndi:BPMNDiagram id="BPMNDiagram_1">\n' +
          '    <bpmndi:BPMNPlane id="BPMNPlane_1" bpmnElement="Process_1">\n' +
          '      <bpmndi:BPMNShape id="_BPMNShape_StartEvent_2" bpmnElement="StartEvent_1">\n' +
          '        <dc:Bounds height="36.0" width="36.0" x="412.0" y="240.0"/>\n' +
          '      </bpmndi:BPMNShape>\n' +
          '    </bpmndi:BPMNPlane>\n' +
          '  </bpmndi:BPMNDiagram>\n' +
          '</bpmn2:definitions>'
      this.bpmnModeler.importXML(bpmnXmlStr, function(err) {
        if (err) {
          console.error(err)
        }
      })
    },
    saveSVG(done) {
      this.bpmnModeler.saveSVG(done)
    },
    saveDiagram(done) {
      this.bpmnModeler.saveXML({ format: true }, function(err, xml) {
        done(err, xml)
      })
    },
    setEncoded(link, name, data) {
      const encodedData = encodeURIComponent(data)
      this.xmlStr = data
      if (data) {
        link.className = 'active'
        link.href = 'data:application/bpmn20-xml;charset=UTF-8,' + encodedData
        link.download = name
      }
    }
  }
}
</script>

<style lang="scss">
  @import 'bpmn-js/dist/assets/diagram-js.css';
  @import 'bpmn-js/dist/assets/bpmn-font/css/bpmn.css';
  @import 'bpmn-js/dist/assets/bpmn-font/css/bpmn-codes.css';
  @import 'bpmn-js/dist/assets/bpmn-font/css/bpmn-embedded.css';
  @import 'bpmn-js-properties-panel/dist/assets/bpmn-js-properties-panel.css';
  @import 'diagram-js-minimap/assets/diagram-js-minimap.css';

  .content{
    position: absolute;
    background-color: #ffffff;
    width: 100%;
    height: 100%;
    font-size: 12px;
  }
  .canvas{
    position: absolute;
    top: 0;
    left: 0;
    right: 0;
    bottom: 0;
  }
  .properties-panel-parent{
    position: absolute;
    top: 0;
    bottom: 0;
    right: 0;
    width: 260px;
    z-index: 10;
    border-left: 1px solid #ccc;
    overflow: auto;
  }
  .djs-minimap{
    top: 10px;
    right: 270px;
  }
/*  .toggle{
    position: absolute;
    top: 20px;
    right: 280px;
  }*/
  .buttons{
    position: absolute;
    left: 20px;
    bottom: 20px;
    &>li{
      display:inline-block;margin: 5px;
      &>a{
        color: #999;
        background: #eee;
        cursor: not-allowed;
        padding: 8px;
        border: 1px solid #ccc;
        &.active{
          color: #333;
          background: #fff;
          cursor: pointer;
        }
      }
    }
  }
</style>
