import defaultSettings from '@/settings'
import { translateByKey } from './i18n'
const title = defaultSettings.title

export default function getPageTitle(pageTitle, vm) {
  const projectTitle = translateByKey(vm, `${title}`, `${title}`)
  if (pageTitle) {
    return translateByKey(vm, 'route.' + `${pageTitle}`, `${pageTitle}`) + '-' + projectTitle
  }
  return projectTitle
}
