export default {
  route: {
    dashboard: 'Dashboard',
    sys: 'System',
    tenant: 'Tenant',
    user: 'User',
    tag: 'Tag',
    resource: 'Resource',
    job: 'Job',
    jobs: 'Job Design',
    introduction: 'Introduction',
    documentation: 'Documentation',
    guide: 'Guide',
    permission: 'Permission',
    pagePermission: 'Page Permission',
    directivePermission: 'Directive Permission',
    icons: 'Icons',
    components: 'Components',
    componentIndex: 'Introduction',
    tinymce: 'Tinymce',
    markdown: 'Markdown',
    jsonEditor: 'JSON Editor',
    dndList: 'Dnd List',
    splitPane: 'SplitPane',
    avatarUpload: 'Avatar Upload',
    dropzone: 'Dropzone',
    sticky: 'Sticky',
    countTo: 'CountTo',
    componentMixin: 'Mixin',
    backToTop: 'BackToTop',
    dragDialog: 'Drag Dialog',
    dragSelect: 'Drag Select',
    dragKanban: 'Drag Kanban',
    charts: 'Charts',
    keyboardChart: 'Keyboard Chart',
    lineChart: 'Line Chart',
    mixChart: 'Mix Chart',
    example: 'Example',
    nested: 'Nested Routes',
    menu1: 'Menu 1',
    'menu1-1': 'Menu 1-1',
    'menu1-2': 'Menu 1-2',
    'menu1-2-1': 'Menu 1-2-1',
    'menu1-2-2': 'Menu 1-2-2',
    'menu1-3': 'Menu 1-3',
    menu2: 'Menu 2',
    Table: 'Table',
    dynamicTable: 'Dynamic Table',
    dragTable: 'Drag Table',
    inlineEditTable: 'Inline Edit',
    complexTable: 'Complex Table',
    treeTable: 'Tree Table',
    customTreeTable: 'Custom TreeTable',
    tab: 'Tab',
    form: 'Form',
    createArticle: 'Create Article',
    editArticle: 'Edit Article',
    articleList: 'Article List',
    errorPages: 'Error Pages',
    page401: '401',
    page404: '404',
    errorLog: 'Error Log',
    excel: 'Excel',
    exportExcel: 'Export Excel',
    selectExcel: 'Export Selected',
    uploadExcel: 'Upload Excel',
    zip: 'Zip',
    exportZip: 'Export Zip',
    theme: 'Theme',
    clipboardDemo: 'Clipboard',
    i18n: 'I18n',
    externalLink: 'External Link'
  },
  actions: {
    handle: 'Handle',
    add: 'Add',
    update: 'Update',
    save: 'Save',
    search: 'Search',
    refresh: 'Refresh',
    remove: 'Remove',
    cancel: 'Cancel',
    reset: 'Reset',
    transferIn: 'Transfer In',
    transferOut: 'Transfer Out',
    import: 'Import',
    export: 'Export',
    last: 'Last',
    next: 'Next',
    ok: 'OK',
    download: 'Download'
  },
  tip: {
    search: 'please entry something to search',
    batchRemove: 'batch remove',
    confirm: 'please confirm to do',
    remove: 'please select the data to remove',
    removeConfirm: 'are you sure to remove it(them)?',
    saveConfirm: 'are you sure to save it',
    clickToSeeDetail: 'Tip:click the button to see detail'
  },
  sys: {
    tenant: {
      columns: {
        id: 'ID',
        name: 'Name',
        sign: 'Sign',
        admin: 'Administrator'
      },
      rules: {
        id: 'The Tenant ID can not be empty',
        name: 'The Tenant Name can not be empty',
        admin: 'The Administrator can not be empty'
      },
      placeholder: {
        id: 'Please entry Tenant ID',
        name: 'Please entry Tenant Name',
        admin: 'Please Select Administrator'
      }
    },
    user: {
      columns: {
        userName: 'User Name',
        displayName: 'Display Name',
        password: 'Password',
        email: 'Email',
        mobile: 'Mobile',
        status: 'Status'
      },
      rules: {
        userName: 'User Name can not be empty',
        displayName: 'Display Name can not be empty',
        email: 'Email is invalid',
        mobile: 'Mobile must be length of 11'
      },
      placeholder: {
        userName: 'Please entry User Name',
        displayName: 'Please entry Display Name',
        password: 'Please entry Password',
        email: 'Please entry Email',
        mobile: 'Please entry Mobile'
      },
      tip: {
        tag: 'Allocate Tag',
        tagFilterTip: 'Tag or Display Name'
      },
      actions: {
        tag: 'Tag'
      }
    },
    tag: {
      columns: {
        tagName: 'Tag Name',
        displayName: 'Display Name',
        tagType: 'Tag Type',
        remark: 'Remark'
      },
      rules: {
        tagName: 'Tag Name can not be empty',
        displayName: 'Display Name can not be empty',
        tagType: 'Tag Type can not be empty'
      },
      placeholder: {
        tagName: 'Please entry Tag Name',
        displayName: 'Please entry Display Name',
        remark: 'Please entry Remark'
      },
      tip: {
        user: 'Allocate User',
        resource: 'Allocate Resource',
        userFilterTip: 'User or Display Name'
      },
      actions: {
        user: 'User',
        resource: 'Resource'
      },
      label: {
        allocateUser: 'Allocate User',
        allocateResource: 'Allocate Resource'
      }
    },
    resource: {
      title: {
        left: 'Resource Tree',
        right: 'Detail'
      },
      label: {
        resourceName: 'Resource Name',
        displayName: 'Display Name',
        resourceType: 'Resource Type',
        uri: 'URI',
        icon: 'Icon',
        position: 'Position'
      },
      rules: {
        resourceName: 'Resource Name can not be empty',
        displayName: 'Display Name can not be empty'
      },
      placeholder: {
        resourceName: 'Please entry Resource Name',
        displayName: 'Please entry Display Name',
        uri: 'Please entry URI',
        icon: 'Please entry Icon'
      },
      tip: {
        searchKey: 'Resource/Display Name',
        resourceRemoveWarning: 'Please remove children first'
      }
    }
  },
  job: {
    title: 'Job',
    sort: {
      nameAsc: 'jobName Ascending',
      nameDesc: 'jobName Descending'
    },
    rules: {
      jobName: 'The Job Name can not be null',
      displayName: 'The Display Name can not be null',
      status: 'The Status can not be null'
    },
    columns: {
      reversion: 'Reversion',
      version: 'Version',
      jobName: 'Job Name',
      displayName: 'Display Name',
      status: 'Status',
      createTime: 'Created',
      updateTime: 'Last Updated',
      remark: 'Remark'
    },
    placeholder: {
      jobName: 'Please entry Job Name',
      displayName: 'Please entry Job Display Name',
      status: 'Please select Status',
      remark: 'Please entry Remark'
    },
    actions: {
      design: 'Design',
      param: 'Param',
      trigger: 'Trigger'
    },
    flowJob: {
      btn: {
        xml: 'XML',
        svg: 'SVG'
      },
      title: {
        xml: 'download as .xml(.bpmn) file',
        svg: 'download as .svg image'
      }
    },
    trigger: {
      tab: {
        time: {
          title: 'Time',
          cron: 'Cron',
          real: 'Real',
          fixed: 'Fixed'
        },
        event: {
          title: 'Event'
        }
      },
      title: {
        triggerInfo: 'Trigger Info',
        timeUnit: 'Time Unit',
        timeRange: 'Time Range',
        period: 'Period',
        timeRangeOfDay: 'Time Range Of Day'
      },
      placeholder: {
        triggerName: 'Please Entry Trigger Name',
        calendar: 'Select Calendar',
        timeUnit: 'Time Unit',
        startTime: 'Start Time',
        endTime: 'End Time',
        period: 'Please Entry Period',
        startTimeOfDay: 'Start Time Of Day',
        endTimeOfDay: 'End Time Of Day'
      }
    }
  },
  navbar: {
    logOut: 'Log Out',
    dashboard: 'Dashboard',
    github: 'Github',
    screenfull: 'Screenfull',
    theme: 'Theme',
    size: 'Global Size'
  },
  login: {
    title: 'Login Form',
    logIn: 'Log in',
    userName: 'Username',
    password: 'Password',
    any: 'any',
    thirdparty: 'Or connect with',
    thirdpartyTips: 'Can not be simulated on local, so please combine you own business simulation! ! !'
  },
  documentation: {
    documentation: 'Documentation',
    github: 'Github Repository'
  },
  permission: {
    roles: 'Your roles',
    switchRoles: 'Switch roles',
    tips: 'In some cases it is not suitable to use v-permission, such as element Tab component or el-table-column and other asynchronous rendering dom cases which can only be achieved by manually setting the v-if.'
  },
  guide: {
    description: 'The guide page is useful for some people who entered the project for the first time. You can briefly introduce the features of the project. Demo is based on ',
    button: 'Show Guide'
  },
  components: {
    documentation: 'Documentation',
    tinymceTips: 'Rich text editor is a core part of management system, but at the same time is a place with lots of problems. In the process of selecting rich texts, I also walked a lot of detours. The common rich text editors in the market are basically used, and the finally chose Tinymce. See documentation for more detailed rich text editor comparisons and introductions.',
    dropzoneTips: 'Because my business has special needs, and has to upload images to qiniu, so instead of a third party, I chose encapsulate it by myself. It is very simple, you can see the detail code in @/components/Dropzone.',
    stickyTips: 'when the page is scrolled to the preset position will be sticky on the top.',
    backToTopTips1: 'When the page is scrolled to the specified position, the Back to Top button appears in the lower right corner',
    backToTopTips2: 'You can customize the style of the button, show / hide, height of appearance, height of the return. If you need a text prompt, you can use element-ui el-tooltip elements externally',
    imageUploadTips: 'Since I was using only the vue@1 version, and it is not compatible with mockjs at the moment, I modified it myself, and if you are going to use it, it is better to use official version.'
  },
  table: {
    dynamicTips1: 'Fixed header, sorted by header order',
    dynamicTips2: 'Not fixed header, sorted by click order',
    dragTips1: 'The default order',
    dragTips2: 'The after dragging order',
    title: 'Title',
    importance: 'Imp',
    type: 'Type',
    remark: 'Remark',
    search: 'Search',
    add: 'Add',
    export: 'Export',
    reviewer: 'reviewer',
    id: 'ID',
    date: 'Date',
    author: 'Author',
    readings: 'Readings',
    status: 'Status',
    actions: 'Actions',
    edit: 'Edit',
    publish: 'Publish',
    draft: 'Draft',
    delete: 'Delete',
    cancel: 'Cancel',
    confirm: 'Confirm'
  },
  errorLog: {
    tips: 'Please click the bug icon in the upper right corner',
    description: 'Now the management system are basically the form of the spa, it enhances the user experience, but it also increases the possibility of page problems, a small negligence may lead to the entire page deadlock. Fortunately Vue provides a way to catch handling exceptions, where you can handle errors or report exceptions.',
    documentation: 'Document introduction'
  },
  excel: {
    export: 'Export',
    selectedExport: 'Export Selected Items',
    placeholder: 'Please enter the file name(default excel-list)'
  },
  zip: {
    export: 'Export',
    placeholder: 'Please enter the file name(default file)'
  },
  pdf: {
    tips: 'Here we use window.print() to implement the feature of downloading pdf.'
  },
  theme: {
    change: 'Change Theme',
    documentation: 'Theme documentation',
    tips: 'Tips: It is different from the theme-pick on the navbar is two different skinning methods, each with different application scenarios. Refer to the documentation for details.'
  },
  tagsView: {
    refresh: 'Refresh',
    close: 'Close',
    closeOthers: 'Close Others',
    closeAll: 'Close All'
  }
}
