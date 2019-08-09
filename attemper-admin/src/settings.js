module.exports = {
  title: 'title',

  /**
   * @type {boolean} true | false
   * @description Whether show the settings right-panel
   */
  showSettings: true,

  /**
   * @type {boolean} true | false
   * @description Whether need tagsView
   */
  tagsView: true,

  /**
   * @type {boolean} true | false
   * @description Whether fix the header
   */
  fixedHeader: false,

  /**
   * @type {boolean} true | false
   * @description Whether show the logo in sidebar
   */
  sidebarLogo: false,

  /**
   * @type {string | array} 'production' | ['production', 'development']
   * @description Need show err logs component.
   * The default is only used in the production env
   * If you want to also use it in dev, you can pass ['production', 'development']
   */
  errorLog: 'production',

  /**
   * API path
   */
  APIPath: {
    SYS: 'sys',
    DISPATCH: 'dispatch',
    APPLICATION: 'app',
    STATISTICS: 'statistics',
    LOGIN: '/login',
    TENANT: '/tenant',
    TAG: '/tag',
    RESOURCE: '/resource',
    JOB: '/job',
    TRIGGER: '/trigger',
    DELAY: '/delay',
    ARG: '/arg',
    DATASOURCE: '/datasource',
    CALENDAR: '/calendar',
    PROJECT: '/project',
    PROGRAM: '/program',
    INSTANCE: '/instance',
    COUNT: '/count',
    TOOL: '/tool',
    GET: '/get'
  },
  /**
   * app config
   */
  alias: {
    comma: ',,',
    colon: '::'
  }
}
