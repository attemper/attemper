// translate router.meta.title, be used in breadcrumb sidebar tagsview
export function generateTitle(title) {
  return generateTitleByVm(this, title)
}

export function generateTitleByVm(vm, title) {
  if (!title && vm.$route.param.title) {
    return vm.$route.param.title
  }
  if (typeof title === 'function') {
    if (typeof vm.$route.meta.title === 'function') {
      const displayTitle = vm.$route.meta.title(vm.$route)
      return (displayTitle && displayTitle.indexOf('undefined') === -1) ? displayTitle : vm.$route.params.key
    } else if (typeof vm.$route.meta.title === 'string') {
      return translateByVm(vm, 'route.' + vm.$route.meta.title, vm.$route.meta.title)
    }
  }
  return translateByVm(vm, 'route.' + title, title)
}

export function translate(langKey) {
  return translateByVm(this, langKey, langKey)
}

export function translateByVm(vm, key, value) {
  const hasKey = vm.$te(key)
  return hasKey ? vm.$t(key) : value
}
