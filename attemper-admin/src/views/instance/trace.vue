<template>
  <div class="layout">
    <div ref="canvas" class="canvas" />
    <div class="external">
      <el-tabs v-model="activeName" type="border-card" @tab-click="handleClick">
        <el-tab-pane :label="$t('monitor.label.log')" name="log">
          <el-timeline>
            <el-timeline-item
              v-for="item in actNodes"
              :key="item.id"
              :timestamp="item.actName + '——' + item.startTime + '  -  ' + (item.endTime || '...')"
              :type="item.status | renderJobInstanceStatus"
              placement="top"
            >
              <el-card>
                <p v-if="item.code && item.code.length > 0">item.code</p>
                <p>{{ item.logText }}</p>
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
                    <el-option v-for="item in jobInstanceStatuses" :key="item.value" :label="item.label" :value="item.value" />
                  </el-select>
                </el-form-item>
                <el-form-item :label="$t('monitor.columns.startTime')" style="margin-bottom: 5px;">
                  <date-time-generator @update="page.lowerStartTime = $event" @change="search" />
                </el-form-item>
                <el-form-item>
                  <date-time-generator @update="page.upperStartTime = $event" @change="search" />
                </el-form-item>
                <el-form-item :label="$t('monitor.columns.endTime')" style="margin-bottom: 5px;">
                  <date-time-generator @update="page.lowerEndTime = $event" @change="search" />
                </el-form-item>
                <el-form-item>
                  <date-time-generator @update="page.upperEndTime = $event" @change="search" />
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
              width="35"
            />
            <el-table-column :label="$t('monitor.columns.startTime')" min-width="100px" align="center">
              <template slot-scope="scope">
                <div>{{ scope.row.startTime }}</div>
              </template>
            </el-table-column>
            <el-table-column :label="$t('monitor.columns.endTime')" min-width="100px" align="center">
              <template slot-scope="scope">
                <span>{{ scope.row.endTime }}</span>
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
      </el-tabs>
    </div>
  </div>
</template>

<script>
import { listReq, listActReq } from '@/api/dispatch/instance'
import { getReq } from '@/api/dispatch/job'
import BpmnViewer from 'bpmn-js'
import miniMapModule from 'diagram-js-minimap'
import customTranslate from '@/utils/customTranslate'
import customElementTemplate from '../dispatch/components/job/element-templates/custom'
import Pagination from '@/components/Pagination'
import { getVersionByDefinition } from './scripts/support'
import { load } from '@/constant'
import DateTimeGenerator from '@/components/DateTimeGenerator'

export default {
  name: 'trace',
  components: {
    DateTimeGenerator,
    Pagination
  },
  data() {
    return {
      job: {
        jobName: null,
        reversion: 0
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
      rootProcInstId: null,
      nodeOverlay: {},
      jobInstanceStatuses: [],
      showAll: false,
      visible: false,
      activeName: 'log',
      actNodes: []
    }
  },
  created() {
    this.loadConst()
    this.initData()
    this.fetchJob()
    this.search()
  },
  methods: {
    initData() {
      this.page.rootProcInstId = this.rootProcInstId = this.$route.params.rootProcInstId
      this.page.jobName = this.$route.params.key
      this.job = {
        jobName: this.$route.params.key,
        reversion: getVersionByDefinition(this.$route.params.procDefId)
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
        self.bindJobContent(function(err, xml) {
          if (!err) {
            self.job.jobContent = xml
          } else {
            console.error(err)
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
              listActReq({ rootProcInstId: self.rootProcInstId, actId: e.element.id }).then(res => {
                const nodes = res.data.result
                if (nodes.length > 0) {
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
                }
              })
            } else if (event === 'element.out' && self.nodeOverlay[e.element.id]) {
              overlays.remove(self.nodeOverlay[e.element.id])
              self.nodeOverlay[e.element.id] = undefined
            }
          }
        })
      })
    },
    openDiagram(xml) {
      const self = this
      this.bpmnViewer.importXML(xml, function(err) {
        if (err) {
          console.error(err)
        } else {
          self.bpmnViewer.get('canvas').zoom('fit-viewport')
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
    toggleList() {
      this.showAll = !this.showAll
      if (this.showAll) { // show all
        this.page.rootProcInstId = undefined
      } else { // show only the current
        this.page.rootProcInstId = this.rootProcInstId
        this.visible = false
      }
      this.search()
    },
    // fetch job by jobName and reversion(from procDefId which format was [jobName:version:randomId])
    fetchJob() {
      getReq({ jobName: this.job.jobName, reversion: this.job.reversion }).then(res => {
        this.job = res.data.result
        if (!this.bpmnViewer) {
          this.bindBpmn()
        }
        setTimeout(() => {
          this.openDiagram(this.job.jobContent)
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
        reversion: getVersionByDefinition(row.procDefId)
      }
      this.rootProcInstId = row.rootProcInstId
      this.fetchJob()
    },
    getAct() {
      listActReq({ rootProcInstId: this.rootProcInstId }).then(res => {
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
    bindJobContent(done) {
      this.bpmnViewer.saveXML({ format: true }, function(err, xml) {
        done(err, xml)
      })
    },
    renderRecordStyle({ row, rowIndex }) {
      return 'row' + row.status
    },
    handleClick(tab, event) {
      if (tab.name === 'log') {
        this.renderLog()
      } else if (tab.name === 'record') {
        this.search()
      }
    },
    renderLog() {
      listActReq({ rootProcInstId: this.rootProcInstId }).then(res => {
        this.getAct()
      })
    },
    setActNodes(allActNodes) {
      this.actNodes = []
      if (allActNodes) {
        allActNodes.forEach(item => {
          if (!item.actType.endsWith('Event')) {
            this.actNodes.push(item)
          }
        })
      }
    },
    loadConst() {
      load(`./array/${localStorage.getItem('language')}.js`).then((array) => {
        this.jobInstanceStatuses = array.jobInstanceStatuses
      })
    }
  }
}
</script>

<style rel="stylesheet/scss" lang="scss">
  @import "./styles/flow.scss";
</style>
