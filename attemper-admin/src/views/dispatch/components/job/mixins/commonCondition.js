import { next } from '../../../scripts/support'
export default {
  data() {
    return {
      conditionArray: []
    }
  },
  methods: {
    generateId(item) {
      item.conditionName = next()
    },
    removeId(item, index) {
      this.remove(index)
      if (this.conditionArray.length === 0) {
        this.add()
      }
    },
    put(staticObj) {
      this.conditionArray.push(Object.assign({}, staticObj))
    },
    remove(index) {
      this.conditionArray.splice(index, 1)
    }
  }
}
