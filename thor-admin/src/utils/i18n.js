// translate router.meta.title, be used in breadcrumb sidebar tagsview
export function generateTitle(title) {
  if (!title && this.$route.param.title) {
    return this.$route.param.title
  }
  if (typeof title === 'function') {
    const displayTitle = this.$route.meta.title(this.$route)
    return displayTitle !== 'undefined' ? displayTitle : this.$route.meta.displayTitle
  }
  const hasKey = this.$te('route.' + title)

  if (hasKey) {
    // $t :this method from vue-i18n, inject in @/lang/index.js
    const translatedTitle = this.$t('route.' + title)

    return translatedTitle
  }
  return title
}
