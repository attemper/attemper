<template>
  <div class="base-editor">
    <!--
    <el-select v-model="theme" style="width: 200px; margin: 10px 0 10px 10px;" filterable @change="selectTheme">
      <el-option v-for="item in themes" :key="item.value" :value="item.value" :label="item.value" />
    </el-select>
    -->
    <textarea ref="textarea" />
  </div>
</template>

<script>
import CodeMirror from 'codemirror'
import 'codemirror/lib/codemirror.css'
import 'codemirror/addon/lint/lint.css'
import 'codemirror/addon/hint/show-hint.css'
// lint
import 'codemirror/addon/lint/lint'
/* import 'codemirror/addon/lint/css-lint'
import 'codemirror/addon/lint/html-lint'
import 'codemirror/addon/lint/javascript-lint'*/
import 'codemirror/addon/lint/json-lint'
require('script-loader!jsonlint')
// hint
import 'codemirror/addon/hint/anyword-hint'
import 'codemirror/addon/hint/css-hint'
import 'codemirror/addon/hint/html-hint'
import 'codemirror/addon/hint/javascript-hint'
import 'codemirror/addon/hint/show-hint'
import 'codemirror/addon/hint/sql-hint'
import 'codemirror/addon/hint/xml-hint'
// mode
import 'codemirror/mode/clike/clike'
import 'codemirror/mode/css/css'
import 'codemirror/mode/dockerfile/dockerfile'
import 'codemirror/mode/go/go'
import 'codemirror/mode/groovy/groovy'
import 'codemirror/mode/javascript/javascript'
import 'codemirror/mode/markdown/markdown'
import 'codemirror/mode/php/php'
import 'codemirror/mode/perl/perl'
import 'codemirror/mode/python/python'
import 'codemirror/mode/ruby/ruby'
import 'codemirror/mode/shell/shell'
import 'codemirror/mode/sql/sql'
import 'codemirror/mode/xml/xml'
import 'codemirror/mode/vue/vue'
//
import 'codemirror/addon/edit/matchbrackets.js'
import 'codemirror/addon/selection/active-line.js'
import 'codemirror/addon/fold/foldcode.js'
import 'codemirror/addon/search/search.js'
import 'codemirror/addon/search/searchcursor.js'
import 'codemirror/addon/search/jump-to-line.js'
import 'codemirror/addon/dialog/dialog.js'
import 'codemirror/addon/dialog/dialog.css'
import 'codemirror/addon/display/fullscreen.js'
import 'codemirror/addon/display/fullscreen.css'

// const DEF_THEME_NAME = 'idea'
// const THEME_KEY = 'theme'
export default {
  name: 'CodeEditor',
  /* eslint-disable vue/require-prop-types */
  props: {
    value: {
      type: [Object, String],
      default: null
    },
    extension: {
      type: String,
      default: null
    },
    readOnly: {
      type: [Boolean, String],
      default: 'nocursor'
    }
  },
  data() {
    return {
      editor: false // ,
      // theme: DEF_THEME_NAME,
      // themes: []
    }
  },
  watch: {
    value(value) {
      const editorValue = this.editor.getValue()
      if (value !== editorValue && this.extension === '.json') {
        this.editor.setValue(JSON.stringify(this.value, null, 2))
      }
    }
  },
  created() {
    // this.initTheme()
    this.initEditor()
  },
  methods: {
    initEditor() {
      import('@/lang/dict.js').then(array => {
        const mode = array.modes.find(item => {
          return item.label === this.extension
        })
        // this.themes = array.themes
        this.editor = CodeMirror.fromTextArea(this.$refs.textarea, {
          lineNumbers: true,
          mode: mode ? mode.value : 'text/javascript',
          smartIndent: true,
          indentWidthTabs: true,
          gutters: ['CodeMirror-lint-markers', 'CodeMirror-foldgutter', 'CodeMirror-lint-markers'],
          foldGutter: true,
          readOnly: this.readOnly,
          lint: true,
          extraKeys: { 'Ctrl': 'autocomplete' },
          matchBrackets: true,
          autoCloseBrackets: true
        })
        // this.importTheme(this.theme)
        this.editor.setOption('theme', 'idea')
        this.editor.setValue(this.extension === '.json' ? JSON.stringify(this.value, null, 2) : this.value)
        this.editor.on('change', cm => {
          this.$emit('changed', cm.getValue())
          this.$emit('input', cm.getValue())
        })
      })
    },
    getValue() {
      return this.editor.getValue()
    }
    /*,
    initTheme() {
      this.theme = sessionStorage.getItem(THEME_KEY) || DEF_THEME_NAME
    },
    selectTheme(themeName) {
      sessionStorage.setItem(THEME_KEY, themeName)
      this.importTheme(themeName)
    },
    importTheme(themeName) {
      import(`codemirror/theme/${themeName}.css`).then(() => {
        this.editor.setOption(THEME_KEY, themeName)
      })
    }
    */
  }
}
</script>

<style scoped>
  .base-editor{
    height: 100%;
    position: relative;
  }
  .base-editor >>> .CodeMirror {
    height: auto;
    min-height: 300px;
  }
  .base-editor >>> .CodeMirror-scroll{
    min-height: 300px;
  }
  .base-editor >>> .cm-s-rubyblue span.cm-string {
    color: #F08047;
  }
</style>
