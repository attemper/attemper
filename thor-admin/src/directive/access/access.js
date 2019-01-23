import { canAccess } from '@/utils/tools'

export default {
  bind: function(el, binding) {
    if (!canAccess(binding.value)) {
      el.parentNode.removeChild(el)
    }
  }
}
