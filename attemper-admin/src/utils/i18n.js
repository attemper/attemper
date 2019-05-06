// translate router.meta.title, be used in breadcrumb sidebar tagsview
export function generateTitle(title) {
  if (!title && this.$route.param.title) {
    return this.$route.param.title
  }
  if (typeof title === 'function') {
    const displayTitle = this.$route.meta.title(this.$route)
    return (displayTitle && displayTitle.indexOf('undefined') === -1) ? displayTitle : this.$route.params.key
  }
  return translateByKey(this, 'route.' + title, title)
}

export function translate(langKey) {
  return translateByKey(this, langKey, langKey)
}

export function translateByKey(vm, key, value) {
  const hasKey = vm.$te(key)
  return hasKey ? vm.$t(key) : value
}
