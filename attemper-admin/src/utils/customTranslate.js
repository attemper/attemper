import zh from '@/lang/bpmn/zh'

export default function customTranslate(template, replacements) {
  const lang = localStorage.getItem('language') || 'en'
  if (lang.startsWith('zh')) {
    // Translate
    template = zh[template] || template
  }
  replacements = replacements || {}

  // Replace
  return template.replace(/{([^}]+)}/g, function(_, key) {
    return replacements[key] || '{' + key + '}'
  })
}
