import zh from '@/lang/bpmn/zh'
import property_zh from 'bpmn-vue-properties-panel-camunda/src/locale/zh'

export default function customTranslate(template, replacements) {
  if (!template || !template.length) {
    return template
  }
  const lang = localStorage.getItem('language') || 'en'
  if (lang.startsWith('zh')) {
    // Translate
    template = zh[template] || property_zh[template] || template
  }
  replacements = replacements || {}

  // Replace
  return template.replace(/{([^}]+)}/g, function(_, key) {
    return replacements[key] || '{' + key + '}'
  })
}
