<template>
  <el-form
    ref="baseForm"
    :rules="rules"
    :model="job"
    label-position="left"
    label-width="150px"
    class="form-layout"
  >
    <el-form-item :label="$t('dispatch.columns.jobName')" prop="jobName">
      <el-input v-model="job.jobName" :placeholder="$t('dispatch.placeholder.jobName')" />
    </el-form-item>
    <el-form-item :label="$t('columns.displayName')" prop="displayName">
      <el-input v-model="job.displayName" :placeholder="$t('placeholder.displayName')" />
    </el-form-item>
    <el-form-item :label="$t('dispatch.columns.status')" prop="status">
      <el-select v-model="job.status" :placeholder="$t('dispatch.placeholder.status')" class="filter-item">
        <el-option v-for="item in jobStatuses" :key="item.value" :label="item.label" :value="item.value" />
      </el-select>
    </el-form-item>
    <el-form-item :label="$t('columns.remark')">
      <el-input
        v-model="job.remark"
        :autosize="{ minRows: 2, maxRows: 4}"
        :placeholder="$t('placeholders.remark')"
        type="textarea"
      />
    </el-form-item>
    <el-form-item>
      <el-button type="info" @click="cancel">{{ $t('actions.cancel') }}</el-button>
      <el-button type="success" @click="save">{{ $t('actions.save') }}</el-button>
    </el-form-item>
  </el-form>
</template>

<script>
import { load } from '@/constant'

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
        jobName: [{ required: true, trigger: 'blur' }],
        displayName: [{ required: true, trigger: 'blur' }],
        status: [{ required: true, trigger: 'blur' }]
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
      this.rules.jobName[0].message = this.$t('dispatch.rules.jobName')
      this.rules.displayName[0].message = this.$t('rules.displayName')
      this.rules.status[0].message = this.$t('dispatch.rules.status')
    },
    save() {
      this.$refs.baseForm.validate((valid) => {
        if (valid) {
          this.$emit('save', this.job)
        }
      })
    },
    cancel() {
      this.$emit('cancel')
    },
    clearValidate() {
      this.$nextTick(() => {
        this.$refs['baseForm'].clearValidate()
      })
    },
    loadConst() {
      load(`./array/${localStorage.getItem('language')}.js`).then((array) => {
        this.jobStatuses = array.jobStatuses
      })
    }
  }
}
</script>

<style rel="stylesheet/scss" lang="scss">
  .form {
    &-layout {
    width: 500px;
    min-height: 350px;
    margin-top: 20px;
    margin-left: 50px;
    }
  }
</style>
