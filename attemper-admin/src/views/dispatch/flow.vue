<template>
  <div ref="container" class="content">
    <div ref="canvas" class="canvas" />
    <div ref="propertiesPanel" class="properties-panel-parent" />
    <ul class="buttons">
      <el-tooltip :content="$t('dispatch.flow.title.undo')">
        <el-button icon="el-icon-back" @click="bpmnModeler.get('commandStack').undo()" />
      </el-tooltip>
      <el-tooltip :content="$t('dispatch.flow.title.redo')">
        <el-button icon="el-icon-right" @click="bpmnModeler.get('commandStack').redo()" />
      </el-tooltip>
      <el-tooltip :content="$t('dispatch.flow.title.fit')">
        <el-button icon="el-icon-rank" @click="fitViewport" />
      </el-tooltip>
      <el-tooltip :content="$t('dispatch.flow.title.zoomBig')">
        <el-button icon="el-icon-zoom-in" @click="zoomViewport(true)" />
      </el-tooltip>
      <el-tooltip :content="$t('dispatch.flow.title.zoomSmall')">
        <el-button icon="el-icon-zoom-out" @click="zoomViewport(false)" />
      </el-tooltip>
      <el-tooltip :content="$t('dispatch.flow.title.bpmn')">
        <el-button icon="el-icon-download" @click="exportBPMN(true)" />
      </el-tooltip>
      <el-tooltip :content="$t('dispatch.flow.title.svg')">
        <el-button icon="el-icon-picture" @click="exportSVG(true)" />
      </el-tooltip>
    </ul>
    <div class="custom-area">
      <el-select
        v-model="currentVersion"
        :placeholder="$t('dispatch.job.columns.reversion') + '-' + $t('dispatch.job.columns.version')"
        class="filter-item"
        style="width: 160px"
        @change="changeJob"
      >
        <el-option v-for="item in jobWithVersions" :key="item.version" :label="createVersionLabel(item)" :value="item.version">
          <span style="float: left; font-size: 13px">{{ createVersionLabel(item) }}</span>
          <span style="float: right; color: #8492a6; margin-left: 20px;">{{ item.updateTime | parseTime }}</span>
        </el-option>
      </el-select>
      <span style="margin-left: 40px;">
        <el-popover placement="top" trigger="hover">
          <el-button type="info" @click="save">{{ $t('actions.save') }}</el-button>
          <el-button type="primary" @click="publish">{{ $t('actions.publish') }}</el-button>
          <el-button type="success" @click="saveAndPublish">{{ $t('actions.saveAndPublish') }}</el-button>
          <el-button slot="reference" icon="el-icon-check" type="success" />
        </el-popover>
        <el-tooltip :content="$t('dispatch.flow.tip.exchange')" placement="top">
          <span style="margin-left: 10px;">
            <el-button type="warning" @click="exchange">
              <svg-icon icon-class="exchange" />
            </el-button>
          </span>
        </el-tooltip>
        <el-tooltip :content="$t('dispatch.flow.tip.copy')" placement="top-start">
          <span style="margin-left: 10px;">
            <el-button type="primary" @click="openCopyDialog">
              <svg-icon icon-class="copy" />
            </el-button>
          </span>
        </el-tooltip>
        <el-tooltip :content="$t('actions.manual')" placement="top-start">
          <span style="margin-left: 10px;">
            <el-button :disabled="!job.procDefId && job.version === 0" type="danger" @click="openParam">
              <svg-icon icon-class="hand" />
            </el-button>
          </span>
        </el-tooltip>
      </span>
    </div>

    <!-- dialog -->
    <el-dialog
      :title="editDialog.title"
      :visible.sync="editDialog.copy.visible || editDialog.param.visible "
      :center="true"
      :modal="true"
      :close-on-click-modal="false"
      :close-on-press-escape="false"
      :before-close="close"
    >
      <div v-show="editDialog.copy.visible">
        <job-info-form :job="targetJobParam" @save="copy" @cancel="editDialog.copy.visible = false" />
      </div>
      <div v-show="editDialog.param.visible">
        <code-editor v-model="jsonData" :read-only="false" extension=".json" />
        <div style="text-align: center; margin-top: 10px">
          <el-button type="danger" @click="manual">
            <svg-icon icon-class="hand" />{{ $t('actions.manual') }}
          </el-button>
        </div>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { getContentReq, updateContentReq, versionsReq, copyReq, exchangeReq, publishReq, manualReq, getJsonArgReq } from '@/api/dispatch/job'
import BpmnModeler from 'bpmn-js/lib/Modeler'
import propertiesPanelModule from 'bpmn-js-properties-panel'
import propertiesProviderModule from 'bpmn-js-properties-panel/lib/provider/camunda'
import camundaModdleDescriptor from 'camunda-bpmn-moddle/resources/camunda'
import miniMapModule from 'diagram-js-minimap'
import customTranslate from '@/utils/customTranslate'
import JobInfoForm from './components/job/jobInfoForm'
import { getTimeStr } from '@/utils/tools'
import customElementTemplate from './components/job/element-templates/custom'
import customControlsModule from './components/job/custom'
import CodeEditor from '@/components/CodeEditor'

export default {
  name: 'flow',
  components: {
    JobInfoForm,
    CodeEditor
  },
  data() {
    return {
      job: {},
      jsonData: null,
      currentVersion: -1,
      bpmnModeler: null,
      jobWithVersions: [],
      editDialog: {
        title: undefined,
        copy: {
          visible: false
        },
        param: {
          visible: false
        }
      },
      targetJobParam: {},
      zoom: 1
    }
  },
  created() {
    setTimeout(() => this.bindBpmn(), 20)
    this.initJobWithVersions()
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
          miniMapModule,
          customTranslateModule,
          customControlsModule
        ],
        elementTemplates: customElementTemplate, // camunda:poperty must be String, Hidden or Dropdown
        moddleExtensions: {
          camunda: camundaModdleDescriptor
        },
        keyboard: {
          bindTo: document
        }
      })
      this.initWidget()
    },
    async openDiagram(xml) {
      try {
        const result = await this.bpmnModeler.importXML(xml)
        const { warnings } = result
        this.bpmnModeler.get('canvas').zoom('fit-viewport')
        if (warnings.length > 0) {
          this.$message.warning(warnings)
        }
      } catch (err) {
        this.$message.error(err.message + err.warnings)
      }
    },
    save() {
      this.$confirm(
        this.$t('tip.saveConfirm'),
        this.$t('tip.confirm'),
        { type: 'info' })
        .then(async() => {
          this.process.content = await this.exportBPMN()
          updateContentReq(this.job).then(res => {
            this.$message.success(res.data.msg)
            this.openNewJobPage(this.job.jobName)
          })
        })
    },
    publish() {
      this.$confirm(
        this.$t('tip.publishConfirm'),
        this.$t('tip.confirm'),
        { type: 'info' })
        .then(() => {
          publishReq({ jobNames: [this.job.jobName] }).then(res => {
            this.$message.success(res.data.msg)
            this.openNewJobPage(this.job.jobName)
          })
        })
    },
    saveAndPublish() {
      this.$confirm(
        this.$t('tip.saveAndPublishConfirm'),
        this.$t('tip.confirm'),
        { type: 'info' })
        .then(async() => {
          this.process.content = await this.exportBPMN()
          updateContentReq(this.job).then(res => {
            publishReq({ jobNames: [this.job.jobName] }).then(res => {
              this.$message.success(res.data.msg)
              this.openNewJobPage(this.job.jobName)
            })
          })
        })
    },
    changeJob() {
      const loading = this.getLoading()
      this.renderContent()
      this.closeLoading(loading)
    },
    initJobWithVersions() {
      versionsReq({ jobName: this.$route.params.key }).then(res => {
        this.jobWithVersions = res.data.result.reverse()
        this.currentVersion = this.jobWithVersions[0].version
        this.renderContent()
      })
    },
    renderContent() {
      this.job = Object.assign({}, this.jobWithVersions.find(item => item.version === this.currentVersion))
      getContentReq(this.job).then(res => {
        this.job.content = res.data.result
        this.openDiagram(this.job.content) // open the job content
      })
    },
    openCopyDialog() {
      this.editDialog.title = this.$t('dispatch.flow.tip.copy')
      this.editDialog.copy.visible = true
      this.targetJobParam = Object.assign({}, this.job)
    },
    // copy job with current version to another job(if the target was existent, will add its version)
    copy() {
      if (this.job.jobName === this.targetJobParam.jobName) {
        this.$message.warning(this.$t('dispatch.flow.tip.jobNameNotChanged'))
        return
      }
      this.$confirm(this.$t('dispatch.flow.tip.copyConfirm'), this.$t('tip.confirm'), { type: 'info' })
        .then(() => {
          const data = {
            jobName: this.job.jobName,
            procDefId: this.job.procDefId,
            targetJobParam: this.targetJobParam
          }
          copyReq(data).then(res => {
            this.$message.success(res.data.msg)
            this.editDialog.copy.visible = false
            this.openNewJobPage(this.targetJobParam.jobName)
          })
        })
    },
    // exchange current version to the latest
    exchange() {
      if (this.currentVersion === this.jobWithVersions[0].version) {
        this.$message.warning(this.$t('dispatch.flow.tip.versionIsLatest'))
        return
      }
      this.$confirm(this.$t('dispatch.flow.tip.exchangeConfirm'), this.$t('tip.confirm'), { type: 'info' })
        .then(() => {
          exchangeReq(this.job).then(res => {
            this.$message.success(res.data.msg)
            this.openNewJobPage(this.job.jobName)
          })
        })
    },
    openParam() {
      if (!this.job.version) {
        this.$message.warning(this.$t('tip.manualWithNoVersion'))
        return
      } else if (this.job.status === 1) {
        this.$message.warning(this.$t('tip.disabledJobError'))
        return
      }
      this.jsonData = null
      getJsonArgReq({ jobName: this.job.jobName }).then(res => {
        if (!res.data.result) {
          this.manual()
        } else {
          this.editDialog.title = this.$t('dispatch.job.actions.param')
          this.editDialog.param.visible = true
          this.jsonData = JSON.parse(res.data.result)
        }
      })
    },
    manual() {
      this.$confirm(this.$t('tip.confirm'), this.$t('tip.confirmMsg'), { type: 'warning' })
        .then(() => {
          manualReq({
            jobName: this.job.jobName,
            jsonData: (!this.jsonData ? null : (typeof this.jsonData === 'string' ? JSON.parse(this.jsonData) : this.jsonData))
          }).then(res => {
            this.editDialog.param.visible = false
            this.$message.success(res.data.msg)
            setTimeout(() => {
              this.$router.push({ name: 'total', replace: true })
            }, 600)
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
      let label = item.version
      if (item.procDefId) {
        label += ' - ' + item.version
      }
      return label
    },
    async exportSVG(download = false) {
      try {
        const { svg } = await this.bpmnModeler.saveSVG({ format: true })
        if (download) {
          this.downloadFile(this.getExportFileName(), svg, 'image/svg+xml')
        }
        return svg
      } catch (err) {
        this.$message.error(err)
      }
    },
    async exportBPMN(download = false) {
      try {
        const { xml } = await this.bpmnModeler.saveXML({ format: true })
        if (download) {
          this.downloadFile(`${this.getExportFileName()}.bpmn`, xml, 'application/xml')
        }
        return xml
      } catch (err) {
        this.$message.error(err)
      }
    },
    downloadFile(filename, data, type) {
      const a = document.createElement('a')
      const url = window.URL.createObjectURL(new Blob([data], { type: type }))
      a.href = url
      a.download = filename
      a.click()
      window.URL.revokeObjectURL(url)
    },
    getExportFileName() {
      return (this.job.displayName || this.job.jobName || 'undefined') + '-' + getTimeStr()
    },
    initWidget() {
      const self = this
      function registerFileDrop(container, callback) {
        function handleFileSelect(e) {
          e.stopPropagation()
          e.preventDefault()
          const files = e.dataTransfer.files
          const file = files[0]
          const reader = new FileReader()
          reader.onload = function(e) {
            callback(e.target.result)
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
    fitViewport() {
      this.zoom = this.bpmnModeler.get('canvas').zoom('fit-viewport')
      const bbox = document.querySelector('.flow-containers .viewport').getBBox()
      const currentViewbox = this.bpmnModeler.get('canvas').viewbox()
      const elementMid = {
        x: bbox.x + bbox.width / 2 - 65,
        y: bbox.y + bbox.height / 2
      }
      this.bpmnModeler.get('canvas').viewbox({
        x: elementMid.x - currentViewbox.width / 2,
        y: elementMid.y - currentViewbox.height / 2,
        width: currentViewbox.width,
        height: currentViewbox.height
      })
      this.zoom = bbox.width / currentViewbox.width * 1.8
    },
    zoomViewport(zoomIn = true) {
      this.zoom = this.bpmnModeler.get('canvas').zoom()
      this.zoom += (zoomIn ? 0.1 : -0.1)
      this.bpmnModeler.get('canvas').zoom(this.zoom)
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
    },
    close() {
      this.editDialog.copy.visible =
          this.editDialog.param.visible = false
    }
  }
}
</script>

<style rel="stylesheet/scss" lang="scss">
  @import "~@/styles/bpmn.scss";
</style>
