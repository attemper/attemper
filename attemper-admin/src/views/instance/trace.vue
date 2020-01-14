<template>
  <div class="layout">
    <div ref="canvas" class="canvas-monitor" />
    <div v-show="showRefresh" class="refresh">
      <el-button type="primary" icon="el-icon-refresh" @click="refresh" />
    </div>
    <div class="external">
      <el-tabs v-model="activeName" type="border-card" @tab-click="handleClick">
        <el-tab-pane :label="$t('monitor.label.job')" name="flow">
          <el-timeline>
            <el-timeline-item
              v-for="item in actNodes"
              :key="item.id"
              :timestamp="(item.actName || item.actId)"
              :type="item.status | renderInstanceStatus"
              placement="top"
            >
              <el-card>
                <span><b>{{ item.startTime | parseTime }} - {{ item.endTime | parseTime }}</b></span>&nbsp;&nbsp;
                <span v-show="item.duration">{{ item.duration | parseDuration }}</span>
                <div style="overflow-x: scroll;">
                  <p v-show="item.logText && item.logText.length>0" v-html="item.logText" />
                  <p v-show="item.incidentMsg && item.incidentMsg.length>0" v-html="item.incidentMsg" />
                </div>
              </el-card>
            </el-timeline-item>
          </el-timeline>
        </el-tab-pane>
        <el-tab-pane :label="$t('monitor.label.record')" name="record">
          <div style="margin-bottom: 10px;">
            <el-button class="filter-item" type="success" @click="toggleList">
              {{ showAll ? $t('actions.showCurrent') : $t('actions.showAll') }}
            </el-button>
            <el-popover
              v-model="visible"
              placement="left"
              trigger="click"
            >
              <el-form
                label-position="left"
                label-width="100px"
                style="height: 100%"
              >
                <el-form-item :label="$t('columns.status')">
                  <el-select v-if="showAll" v-model="page.status" :placeholder="$t('columns.status')" multiple clearable collapse-tags class="filter-item" style="width: 150px" @change="search">
                    <el-option v-for="item in instanceStatuses" :key="item.value" :label="item.label" :value="item.value" />
                  </el-select>
                </el-form-item>
                <el-form-item :label="$t('columns.startTime')" style="margin-bottom: 5px;">
                  <date-time-generator v-model="page.lowerStartTime" @update="page.lowerStartTime = $event" @change="search" />
                </el-form-item>
                <el-form-item>
                  <date-time-generator v-model="page.upperStartTime" @update="page.upperStartTime = $event" @change="search" />
                </el-form-item>
                <el-form-item :label="$t('columns.endTime')" style="margin-bottom: 5px;">
                  <date-time-generator v-model="page.lowerEndTime" @update="page.lowerEndTime = $event" @change="search" />
                </el-form-item>
                <el-form-item>
                  <date-time-generator v-model="page.upperEndTime" @update="page.upperEndTime = $event" @change="search" />
                </el-form-item>
              </el-form>
              <el-button v-if="showAll" slot="reference" class="filter-item" style="float: right;" type="primary" @click="visible = !visible">{{ $t('actions.highSearch') }}</el-button>
            </el-popover>
          </div>
          <el-table
            ref="tables"
            v-loading="listLoading"
            :data="list"
            border
            fit
            highlight-current-row
            style="width: 100%;"
            :row-class-name="renderRecordStyle"
            @cell-click="clickCell"
          >
            <el-table-column
              type="selection"
              width="45"
            />
            <el-table-column :label="$t('columns.startTime')" min-width="100px" align="center">
              <template slot-scope="scope">
                <div>{{ scope.row.startTime | parseTime }}</div>
              </template>
            </el-table-column>
            <el-table-column :label="$t('columns.endTime')" min-width="100px" align="center">
              <template slot-scope="scope">
                <span>{{ scope.row.endTime | parseTime }}</span>
              </template>
            </el-table-column>
            <el-table-column :label="$t('monitor.columns.duration')" min-width="80px" align="center">
              <template slot-scope="scope">
                <span>{{ scope.row.duration | parseDuration }}</span>
              </template>
            </el-table-column>
          </el-table>

          <pagination
            v-show="page.total>0"
            :total="page.total"
            :page.sync="page.currentPage"
            :limit.sync="page.pageSize"
            :page-sizes="[10, 20]"
            @pagination="search"
          />
        </el-tab-pane>
        <el-tab-pane :label="$t('monitor.label.task')" name="task">
          <div v-if="singleActNodes && singleActNodes.length > 0" class="operation-area">
            <el-button type="warning" icon="el-icon-s-operation" @click="openParam">{{ $t('actions.retry') }}</el-button>
          </div>
          <el-timeline>
            <el-timeline-item
              v-for="item in singleActNodes"
              :key="item.id"
              :timestamp="(item.actName || item.actId)"
              :type="item.status | renderInstanceStatus"
              placement="top"
            >
              <el-card>
                <span><b>{{ item.startTime | parseTime }} - {{ item.endTime | parseTime }}</b></span>&nbsp;&nbsp;
                <span v-show="item.duration">{{ item.duration | parseDuration }}</span>
                <div style="overflow-x: scroll;">
                  <p v-show="item.logText && item.logText.length>0" v-html="item.logText" />
                  <p v-show="item.incidentMsg && item.incidentMsg.length>0" v-html="item.incidentMsg" />
                </div>
              </el-card>
            </el-timeline-item>
          </el-timeline>
        </el-tab-pane>
      </el-tabs>
    </div>

    <el-dialog
      :title="editDialog.title"
      :visible.sync="editDialog.param.visible "
      :center="true"
      :modal="true"
      :close-on-click-modal="false"
      :close-on-press-escape="false"
      :before-close="close"
    >
      <div v-show="editDialog.param.visible">
        <code-editor v-model="jsonData" :read-only="false" extension=".json" />
        <el-row style="text-align: center; margin-top: 10px">
          <el-col :span="3" :offset="6">
            <el-tooltip :content="$t('tip.beforeActivity')" effect="dark" placement="top-start">
              <el-button type="primary" @click="retry(-1)">
                <svg-icon icon-class="hand-up" />
              </el-button>
            </el-tooltip>
          </el-col>
          <el-col :span="3" :offset="1">
            <el-tooltip :content="$t('tip.afterActivity')" effect="light" placement="top-start">
              <el-button type="primary" @click="retry(1)">
                <svg-icon icon-class="hand-down" />
              </el-button>
            </el-tooltip>
          </el-col>
        </el-row>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { listReq, listActReq, getArgsReq, retryReq } from '@/api/instance/instance'
import { getContentReq } from '@/api/dispatch/job'
import BpmnViewer from 'bpmn-js'
import miniMapModule from 'diagram-js-minimap'
import customTranslate from '@/utils/customTranslate'
import customElementTemplate from '../dispatch/components/job/element-templates/custom'
import Pagination from '@/components/Pagination'
import DateTimeGenerator from '@/components/DateTimeGenerator'
import CodeEditor from '@/components/CodeEditor'

export default {
  name: 'trace',
  components: {
    DateTimeGenerator,
    Pagination,
    CodeEditor
  },
  data() {
    return {
      job: {
        jobName: null,
        procDefId: null,
        content: null
      },
      bpmnViewer: null,
      list: null,
      listLoading: true,
      page: {
        currentPage: 1,
        pageSize: 10,
        total: 0,
        jobName: undefined,
        status: [],
        sort: 'START_TIME DESC',
        lowerStartTime: null,
        upperStartTime: null,
        lowerEndTime: null,
        upperEndTime: null
      },
      procInstId: null,
      nodeOverlay: {},
      instanceStatuses: [],
      showAll: false,
      visible: false,
      activeName: 'flow',
      actNodes: [],
      singleActNodes: [],
      jsonData: null,
      editDialog: {
        title: undefined,
        param: {
          visible: false
        }
      }
    }
  },
  computed: {
    showRefresh() {
      return this.actNodes.length > 0 && this.actNodes.find(item => { return item.status === 0 }) !== undefined
    }
  },
  created() {
    this.loadConst()
  },
  activated() {
    this.initData()
    this.fetchJob()
    this.search()
  },
  methods: {
    openParam() {
      this.jsonData = null
      getArgsReq({ procInstId: this.procInstId }).then(res => {
        this.editDialog.title = this.$t('dispatch.job.actions.param')
        this.editDialog.param.visible = true
        this.jsonData = JSON.parse(res.data.result)
      })
    },
    retry(direction) {
      this.$confirm(this.$t('tip.confirmMsg'), this.$t('tip.confirm'), { type: 'info' })
        .then(() => {
          const data = {
            procInstId: this.procInstId,
            jsonData: (!this.jsonData ? null : (typeof this.jsonData === 'string' ? JSON.parse(this.jsonData) : this.jsonData))
          }
          if (direction > 0) {
            data.afterActIds = [this.singleActNodes[0].actId]
          } else {
            data.beforeActIds = [this.singleActNodes[0].actId]
          }
          retryReq(data)
            .then(res => {
              this.editDialog.param.visible = false
              this.$message.success(res.data.msg)
              setTimeout(() => {
                this.$router.push({ name: 'total', replace: true })
              }, 600)
            })
        })
    },
    initData() {
      this.page.procInstId = this.procInstId = this.$route.params.procInstId
      this.page.jobName = this.$route.params.key
      this.job = {
        jobName: this.$route.params.key,
        procDefId: this.$route.params.procDefId
      }
    },
    bindBpmn() {
      const canvas = this.$refs.canvas
      const customTranslateModule = {
        translate: ['value', customTranslate]
      }
      this.bpmnViewer = new BpmnViewer({
        container: canvas,
        additionalModules: [
          miniMapModule,
          customTranslateModule
        ],
        elementTemplates: customElementTemplate,
        keyboard: {
          bindTo: document
        }
      })
      const self = this
      this.bpmnViewer.on('commandStack.changed', function() {
        self.bindContent(function(err, xml) {
          if (!err) {
            self.job.content = xml
          } else {
            self.$message.error(err)
          }
        })
      })
      var eventBus = this.bpmnViewer.get('eventBus')
      // you may hook into any of the following events
      var events = [
        'element.click',
        'element.out'
      ]
      events.forEach(function(event) {
        eventBus.on(event, function(e) {
          // e.element = the model element
          // e.gfx = the graphical element
          // console.log(event, 'on', e.element.id)
          // console.log(e.element)
          if (e.element.type !== 'bpmn:Process' && !e.element.type.endsWith('Event')) {
            const overlays = self.bpmnViewer.get('overlays')
            if (event === 'element.click') {
              if (self.nodeOverlay[e.element.id]) {
                overlays.remove(self.nodeOverlay[e.element.id])
                self.nodeOverlay[e.element.id] = undefined
              }
              listActReq({ procInstId: self.procInstId, actId: e.element.id }).then(res => {
                self.activeName = 'task'
                self.singleActNodes = res.data.result
                /* if (nodes.length > 0) {
                  let htmlText = '<div style="width: 300px;">'
                  nodes.forEach(node => {
                    htmlText += '<h4>' + node.startTime + ' - ' + (node.endTime || '...') + '</h4><br><br>' + (node.logText || '-----------------')
                  })
                  htmlText += '</div>'
                  const overlayId = overlays.add(e.element.id, 'note', {
                    position: {
                      bottom: -20,
                      right: 70
                    },
                    html: htmlText
                  })
                  self.nodeOverlay[e.element.id] = overlayId
                }*/
              })
            } else if (event === 'element.out') {
              if (self.nodeOverlay[e.element.id]) {
                overlays.remove(self.nodeOverlay[e.element.id])
                self.nodeOverlay[e.element.id] = undefined
              }
            }
          }
        })
      })
    },
    openDiagram(xml) {
      const self = this
      this.bpmnViewer.importXML(xml, function(err) {
        if (!err) {
          self.bpmnViewer.get('canvas').zoom('fit-viewport')
        } else {
          self.$message.error(err)
        }
      })
    },
    search() {
      this.listLoading = true
      listReq(this.page).then(response => {
        this.list = response.data.result.list
        Object.assign(this.page, response.data.result.page)
        setTimeout(() => {
          this.listLoading = false
        }, 200)
      })
    },
    refresh() {
      this.fetchJob()
    },
    toggleList() {
      this.showAll = !this.showAll
      if (this.showAll) { // show all
        this.page.procInstId = undefined
      } else { // show only the current
        this.page.procInstId = this.procInstId
        this.visible = false
      }
      this.search()
    },
    fetchJob() {
      const params = {
        jobName: this.job.jobName,
        procDefId: this.job.procDefId
      }
      getContentReq(params).then(res => {
        this.job.content = res.data.result
        if (!this.bpmnViewer) {
          this.bindBpmn()
        }
        setTimeout(() => {
          this.openDiagram(this.job.content)
        }, 50)
        this.getAct()
      })
    },
    selectRow(row) {
      this.$refs.tables.clearSelection()
      if (row && row.jobName) {
        this.$refs.tables.toggleRowSelection(row, true)
      }
      this.job = {
        jobName: row.jobName,
        procDefId: row.procDefId
      }
      this.procInstId = row.procInstId
      this.fetchJob()
    },
    getAct() {
      listActReq({ procInstId: this.procInstId }).then(res => {
        this.setActNodes(res.data.result)
        const nodes = res.data.result
        if (nodes.length > 0) {
          const overlays = this.bpmnViewer.get('overlays')
          const elementRegistry = this.bpmnViewer.get('elementRegistry')
          nodes.forEach(node => {
            const shape = elementRegistry.get(node.actId)
            overlays.remove({ element: node.actId })
            if (shape.width && shape.height) {
              const overlayHtml = '<div class="status' + node.status + '" style="width:' + (shape.width - 1) + 'px; height: ' + (shape.height - 1) + 'px;">'
              overlays.add(node.actId, {
                position: {
                  top: 1,
                  left: 1
                },
                html: overlayHtml
              })
            }
          })
        }
      })
    },
    clickCell(row, column, cell, event) {
      this.selectRow(row)
    },
    bindContent(done) {
      this.bpmnViewer.saveXML({ format: true }, function(err, xml) {
        done(err, xml)
      })
    },
    renderRecordStyle({ row, rowIndex }) {
      return 'row' + row.status
    },
    handleClick(tab, event) {
      if (tab.name === 'flow') {
        this.renderFlow()
      } else if (tab.name === 'record') {
        this.search()
      }
    },
    renderFlow() {
      listActReq({ procInstId: this.procInstId }).then(res => {
        this.getAct()
      })
    },
    setActNodes(allActNodes) {
      this.actNodes = []
      if (allActNodes) {
        allActNodes.forEach(item => {
          if (!item.actType.endsWith('Event') && !item.actType.endsWith('Gateway')) {
            this.actNodes.push(item)
          }
        })
      }
    },
    close() {
      this.editDialog.param.visible = false
    },
    loadConst() {
      import(`@/constant/array/${localStorage.getItem('language')}.js`).then((array) => {
        this.instanceStatuses = array.instanceStatuses
      })
    }
  }
}
</script>

<style rel="stylesheet/scss" lang="scss">
  @import "~@/styles/bpmn.scss";
</style>
