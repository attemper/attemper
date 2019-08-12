<template>
  <div class="base-editor">
    <textarea ref="textarea" :disabled="disabled" />
  </div>
</template>

<script>
import CodeMirror from 'codemirror'
import 'codemirror/lib/codemirror.css'
import 'codemirror/addon/lint/lint.css'
import 'codemirror/addon/hint/show-hint.css'
import 'codemirror/theme/rubyblue.css'
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

export default {
  name: 'CodeEditor',
  /* eslint-disable vue/require-prop-types */
  props: {
    value: {
      type: String,
      default: null
    },
    disabled: {
      type: Boolean,
      default: true
    },
    fileExtension: {
      type: String,
      default: '.js'
    }
  },
  data() {
    return {
      editor: false
    }
  },
  watch: {
    value(value) {
      const editorValue = this.editor.getValue()
      if (value !== editorValue && this.fileExtension === '.json') {
        this.editor.setValue(JSON.stringify(this.value, null, 2))
      }
    }
  },
  created() {
    import(`@/constant/common.js`).then((array) => {
      const mode = array.modes.find(item => {
        return item.label === this.fileExtension
      })
      this.editor = CodeMirror.fromTextArea(this.$refs.textarea, {
        lineNumbers: true,
        mode: mode ? mode.value : 'text/javascript',
        gutters: ['CodeMirror-lint-markers'],
        theme: 'rubyblue',
        lint: true
      })

      this.editor.setValue(this.fileExtension === '.json' ? JSON.stringify(this.value, null, 2) : this.value)
      this.editor.on('change', cm => {
        this.$emit('changed', cm.getValue())
        this.$emit('input', cm.getValue())
      })
    })
  },
  methods: {
    getValue() {
      return this.editor.getValue()
    }
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
