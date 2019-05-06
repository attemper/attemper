import defaultSettings from '@/settings'
import { generateTitleByVm, translateByVm } from './i18n'
const title = defaultSettings.title

export default function getPageTitle(toMeta, vm) {
  const projectTitle = translateByVm(vm, title, title)
  if (toMeta.title) {
    return generateTitleByVm(vm, toMeta.title) + '-' + projectTitle
  }
  return projectTitle
}
