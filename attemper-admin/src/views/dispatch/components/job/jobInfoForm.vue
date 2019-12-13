<template>
  <el-form
    ref="baseForm"
    :rules="rules"
    :model="job"
    label-position="left"
    label-width="150px"
    class="form-layout"
  >
    <el-form-item :label="$t('dispatch.job.columns.jobName')" prop="jobName">
      <el-input v-model="job.jobName" :placeholder="$t('dispatch.job.placeholder.jobName')" />
    </el-form-item>
    <el-form-item :label="$t('columns.displayName')" prop="displayName">
      <el-input v-model="job.displayName" :placeholder="$t('placeholders.displayName')" />
    </el-form-item>
    <el-form-item :label="$t('columns.status')" prop="status">
      <el-select v-model="job.status" class="filter-item" style="width: 100%;">
        <el-option v-for="item in jobStatuses" :key="item.value" :label="item.label" :value="item.value" />
      </el-select>
    </el-form-item>
    <el-form-item :label="$t('dispatch.job.columns.concurrent')">
      <el-switch v-model="job.concurrent" :active-value="1" :inactive-value="0" />
    </el-form-item>
    <el-form-item :label="$t('dispatch.job.columns.once')">
      <el-switch v-model="job.once" :active-value="1" :inactive-value="0" />
    </el-form-item>
    <el-form-item :label="$t('columns.remark')">
      <el-input
        v-model="job.remark"
        :autosize="{ minRows: 1, maxRows: 5}"
        :placeholder="$t('placeholders.remark')"
        type="textarea"
      />
    </el-form-item>
    <el-form-item>
      <el-button type="success" @click="save">{{ $t('actions.save') }}</el-button>
    </el-form-item>
  </el-form>
</template>

<script>

export default {
  name: 'JobInfoForm',
  props: {
    job: {
      type: Object,
      default: null
    }
  },
  data() {
    return {
      rules: {
        jobName: [{ required: true, trigger: 'blur', range: { max: 64 }, pattern: /^[a-zA-Z0-9_-]+$/ }],
        displayName: [{ required: true, trigger: 'blur', range: { max: 255 }}]
      },
      jobStatuses: []
    }
  },
  created() {
    this.loadConst()
    this.setFormRules()
  },
  methods: {
    setFormRules() {
      this.rules.jobName[0].message = this.$t('dispatch.job.rules.jobName')
      this.rules.displayName[0].message = this.$t('rules.displayName')
    },
    save() {
      this.$refs.baseForm.validate((valid) => {
        if (valid) {
          this.$emit('save', this.job)
        }
      })
    },
    clearValidate() {
      this.$nextTick(() => {
        this.$refs['baseForm'].clearValidate()
      })
    },
    loadConst() {
      import(`@/constant/array/${localStorage.getItem('language')}.js`).then((array) => {
        this.jobStatuses = array.jobStatuses
      })
    }
  }
}
</script>
