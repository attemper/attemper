<template>
  <div ref="container" class="content">
    <div ref="canvas" class="canvas"/>
    <div ref="propertiesPanel" class="properties-panel-parent"/>
    <ul class="buttons">
      <li>{{ $t('actions.download') }}</li>
      <li>
        <a ref="exportBPMN" :title="$t('job.flowJob.title.xml')" href="javascript:">{{ $t('job.flowJob.btn.xml') }}</a>
      </li>
      <li>
        <a ref="exportSvg" :title="$t('job.flowJob.title.svg')" href="javascript:">{{ $t('job.flowJob.btn.svg') }}</a>
      </li>
    </ul>
    <div class="custom-buttons">
      <el-button type="info" size="mini" @click="refresh">
        {{ $t('actions.refresh') }}
      </el-button>
      <el-button type="success" size="mini" @click="save">
        {{ $t('actions.save') }}
      </el-button>
    </div>
  </div>
</template>

<script>
import { getReq, updateReq } from '@/api/job/baseJob'
import BpmnModeler from 'bpmn-js/lib/Modeler'
import propertiesPanelModule from 'bpmn-js-properties-panel'
import propertiesProviderModule from 'bpmn-js-properties-panel/lib/provider/camunda'
import camundaModdleDescriptor from 'camunda-bpmn-moddle/resources/camunda'
import minimapModule from 'diagram-js-minimap'
// import { diff } from 'bpmn-js-differ' // https://github.com/bpmn-io/bpmn-js-differ
import prioritiesModule from 'bpmn-js-task-priorities/lib/priorities'
import customTranslate from '@/utils/customTranslate'
import {
  getTimeStr
} from './scripts/support'

export default {
  data() {
    return {
      job: null,
      bpmnModeler: null
    }
  },
  mounted() {
    this.bindBpmn()
  },
  methods: {
    refresh() {
      // TODO
    },
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
      const self = this
      const downloadLink = this.$refs.exportBPMN
      const downloadSvgLink = this.$refs.exportSvg
      this.bpmnModeler.on('commandStack.changed', function() {
        self.exportSvg(function(err, svg) {
          self.setEncoded(downloadSvgLink, self.getExportFileName() + '.svg', err ? null : svg)
        })

        self.exportBPMN(function(err, xml) {
          self.setEncoded(downloadLink, self.getExportFileName() + '.bpmn', err ? null : xml)
        })

        self.bindJobContent(function(err, xml) {
          if (!err) {
            self.job.jobContent = xml
          } else {
            console.error(err)
          }
        })
      })

      this.initWidget()
      this.initData()
    },
    openDiagram(xml) {
      this.bpmnModeler.importXML(xml, function(err) {
        if (err) {
          console.error(err)
        }
      })
    },
    save() {
      updateReq(this.job).then(res => {
        this.$message.success(res.data.msg)
      })
    },
    exportSvg(done) {
      this.bpmnModeler.saveSVG(done)
    },
    exportBPMN(done) {
      this.bpmnModeler.saveXML({ format: true }, function(err, xml) {
        done(err, xml)
      })
    },
    setEncoded(link, name, data) {
      const encodedData = encodeURIComponent(data)
      if (data) {
        link.className = 'active'
        link.href = 'data:application/bpmn20-xml;charset=UTF-8,' + encodedData
        link.download = name
      }
    },
    getExportFileName() {
      return (this.job.displayName || this.job.jobName || 'undefined') + '-' + getTimeStr()
    },
    bindJobContent(done) {
      this.bpmnModeler.saveXML({ format: true }, function(err, xml) {
        done(err, xml)
      })
    },
    initData() {
      getReq({ jobName: this.$route.params.id }).then(res => {
        this.job = res.data.result
        this.openDiagram(this.job.jobContent) // open the job content
      })
    },
    initWidget() {
      const self = this
      //
      function registerFileDrop(container, callback) {
        function handleFileSelect(e) {
          e.stopPropagation()
          e.preventDefault()

          var files = e.dataTransfer.files

          var file = files[0]

          var reader = new FileReader()

          reader.onload = function(e) {
            var xml = e.target.result

            callback(xml)
          }

          reader.readAsText(file)
        }

        function handleDragOver(e) {
          e.stopPropagation()
          e.preventDefault()

          e.dataTransfer.dropEffect = 'copy' // Explicitly show this is a copy.
        }
        container.ondragover = handleDragOver
        container.ondrop = handleFileSelect
      }
      // file drag / drop ///////////////////////
      // check file api availability
      if (!window.FileList || !window.FileReader) {
        window.alert(
          'Looks like you use an older browser that does not support drag and drop. ' +
          'Try using Chrome, Firefox or the Internet Explorer > 10.')
      } else {
        registerFileDrop(self.$refs.container, this.openDiagram)
      }
    }
  }
}
</script>

<style rel="stylesheet/scss" lang="scss">
  @import "./styles/flow.scss";
</style>
