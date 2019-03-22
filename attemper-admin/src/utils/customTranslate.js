import store from '@/store'
import zh from '@/lang/bpmn/zh'
import de from '@/lang/bpmn/de'

export default function customTranslate(template, replacements) {
  const lang = store.state.app.language || 'en'
  if (lang.startsWith('zh')) {
    // Translate
    template = zh[template] || template
  } else if (lang.startsWith('de')) {
    // Translate
    template = de[template] || template
  }
  replacements = replacements || {}

  // Replace
  return template.replace(/{([^}]+)}/g, function(_, key) {
    return replacements[key] || '{' + key + '}'
  })
}
