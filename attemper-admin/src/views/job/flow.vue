<template>
  <div ref="container" class="content">
    <div ref="canvas" class="canvas" />
    <div ref="propertiesPanel" class="properties-panel-parent" />
    <ul class="buttons">
      <li>{{ $t('actions.download') }}</li>
      <li>
        <a ref="exportBPMN" :title="$t('job.flowJob.title.xml')" href="javascript:">{{ $t('job.flowJob.btn.xml') }}</a>
      </li>
      <li>
        <a ref="exportSvg" :title="$t('job.flowJob.title.svg')" href="javascript:">{{ $t('job.flowJob.btn.svg') }}</a>
      </li>
    </ul>
    <div class="custom-bottom">
      <el-select
        v-model="currentReversion"
        :placeholder="$t('job.columns.reversion') + '-' + $t('job.columns.version')"
        class="filter-item"
        style="width: 160px"
        @change="changeJob"
      >
        <el-option v-for="item in jobWithVersions" :key="item.reversion" :label="createVersionLabel(item)" :value="item.reversion">
          <span style="float: left; font-size: 13px">{{ createVersionLabel(item) }}</span>
          <span style="float: right; color: #8492a6; margin-left: 20px;">{{ item.updateTime }}</span>
        </el-option>
      </el-select>
      <span style="margin-left: 40px;">
        <el-tooltip :content="$t('actions.save')" effect="dark" placement="top-start">
          <span>
            <el-button :disabled="job.maxReversion !== currentReversion" icon="el-icon-check" type="success" @click="save" />
          </span>
        </el-tooltip>
        <el-tooltip :content="$t('job.flowJob.tip.exchange')" effect="dark" placement="top">
          <span style="margin-left: 10px;">
            <el-button :disabled="job.maxReversion === currentReversion" type="warning" @click="exchange">
              <svg-icon icon-class="exchange" />
            </el-button>
          </span>
        </el-tooltip>
        <el-tooltip :content="$t('job.flowJob.tip.copy')" effect="dark" placement="top-start">
          <span style="margin-left: 10px;">
            <el-button type="primary" @click="openCopyDialog">
              <svg-icon icon-class="copy" />
            </el-button>
          </span>
        </el-tooltip>
      </span>
    </div>

    <!-- dialog -->
    <el-dialog
      :title="editDialog.title"
      :visible.sync="editDialog.visible"
      :center="true"
      :modal="true"
      :close-on-click-modal="false"
      :close-on-press-escape="false"
    >
      <job-info-form :job="targetJobParam" @save="copy" @cancel="editDialog.visible = false" />
    </el-dialog>
  </div>
</template>

<script>
import { getReq, updateReq, versionsReq, copyReq, exchangeReq } from '@/api/job/flowJob'
import BpmnModeler from 'bpmn-js/lib/Modeler'
import propertiesPanelModule from 'bpmn-js-properties-panel'
import propertiesProviderModule from 'bpmn-js-properties-panel/lib/provider/camunda'
import camundaModdleDescriptor from 'camunda-bpmn-moddle/resources/camunda'
import minimapModule from 'diagram-js-minimap'
// import { diff } from 'bpmn-js-differ' // https://github.com/bpmn-io/bpmn-js-differ
import prioritiesModule from 'bpmn-js-task-priorities/lib/priorities'
import customTranslate from '@/utils/customTranslate'
import JobInfoForm from './components/jobInfoForm'
import {
  getTimeStr
} from './scripts/support'
import customElementTemplate from './element-templates/custom'

export default {
  name: 'flow',
  components: {
    JobInfoForm
  },
  data() {
    return {
      job: {},
      currentReversion: 1,
      bpmnModeler: null,
      jobWithVersions: [],
      editDialog: {
        title: undefined,
        visible: false
      },
      targetJobParam: {}
    }
  },
  created() {
    this.initJobWithVersions()
    setTimeout(() => this.bindBpmn(), 100)
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
        elementTemplates: customElementTemplate,
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
    },
    openDiagram(xml) {
      const self = this
      this.bpmnModeler.importXML(xml, function(err) {
        if (err) {
          console.error(err)
        } else {
          self.bpmnModeler.get('canvas').zoom('fit-viewport')
        }
      })
    },
    save() {
      const msg = '<p>' + this.$t('tip.saveConfirm') + '?</p>'
      this.$confirm(msg, this.$t('tip.confirm'), { type: 'info', dangerouslyUseHTMLString: true })
        .then(() => {
          updateReq(this.job).then(res => {
            this.$message.success(res.data.msg)
            this.openNewJobPage(this.job.jobName)
          })
        })
    },
    changeJob() {
      const loading = this.getLoading()
      this.resolveJob({ jobName: this.job.jobName, reversion: this.currentReversion })
      this.closeLoading(loading)
    },
    initJobWithVersions() {
      versionsReq({ jobName: this.$route.params.key }).then(res => {
        this.jobWithVersions = res.data.result
        for (let i = 0; i < this.jobWithVersions.length; i++) {
          const item = this.jobWithVersions[i]
          this.currentReversion = item.maxReversion
          this.resolveJob(item)
          break
        }
      })
    },
    resolveJob(item) {
      getReq({ jobName: item.jobName, reversion: item.reversion }).then(res => {
        this.job = res.data.result
        this.openDiagram(this.job.jobContent) // open the job content
      })
    },
    openCopyDialog() {
      this.editDialog.title = this.$t('job.flowJob.tip.copy')
      this.editDialog.visible = true
      this.targetJobParam = Object.assign({}, this.job)
    },
    // copy job with current reversion to another job(if the target was existent, will add its reversion)
    copy() {
      if (this.job.jobName === this.targetJobParam.jobName) {
        this.$message.error(this.$t('job.flowJob.tip.jobNameNotChanged'))
        return
      }
      const msg = '<p>' + this.$t('job.flowJob.tip.copyConfirm') + '?</p>'
      this.$confirm(msg, this.$t('tip.confirm'), { type: 'info', dangerouslyUseHTMLString: true })
        .then(() => {
          const data = {
            jobName: this.job.jobName,
            reversion: this.currentReversion,
            targetJobParam: this.targetJobParam
          }
          copyReq(data).then(res => {
            this.$message.success(res.data.msg)
            this.editDialog.visible = false
            this.openNewJobPage(this.targetJobParam.jobName)
          })
        })
    },
    // exchange current reversion to the latest
    exchange() {
      const msg = '<p>' + this.$t('job.flowJob.tip.exchangeConfirm') + '?</p>'
      this.$confirm(msg, this.$t('tip.confirm'), { type: 'info', dangerouslyUseHTMLString: true })
        .then(() => {
          exchangeReq(this.job).then(res => {
            this.$message.success(res.data.msg)
            this.openNewJobPage(this.job.jobName)
            this.currentReversion = res.data.result.maxReversion
          })
        })
    },
    // open new route
    openNewJobPage(key) {
      const loading = this.getLoading()
      setTimeout(() => {
        const route = {
          name: 'flow',
          params: {
            key: key
          }
        }
        this.$router.push(route)
      }, 100)
      this.initJobWithVersions()
      this.closeLoading(loading)
    },
    createVersionLabel(item) {
      let label = item.reversion
      if (item.version) {
        label += ' - ' + item.version
      }
      return label
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
    initWidget() {
      const self = this
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
    },
    getLoading() {
      return this.$loading({
        lock: true,
        text: 'Loading',
        spinner: 'el-icon-loading',
        background: 'rgba(0, 0, 0, 0.7)'
      })
    },
    closeLoading(loading) {
      setTimeout(() => {
        loading.close()
      }, 200)
    }
  }
}
</script>

<style rel="stylesheet/scss" lang="scss">
  @import "./styles/flow.scss";
</style>
