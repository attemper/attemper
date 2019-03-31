import { canAccess } from '@/utils/tools'

export default {
  inserted: function(el, binding) {
    if (!canAccess(binding.value)) {
      el.parentNode.removeChild(el)
    }
  }
}
