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
   * @description tenant id
   */
  tenantId: 'attemper',

  /**
   * @description 认证签名
   */
  sign: '1e8c0c1cdc283425a2027bf3dbf9bfe5',

  /**
   * API path
   */
  APIPath: {
    SYS: 'sys', // 系统管理
    DISPATCH: 'dispatch', // 调度
    LIST: '', // 查询列表
    ADD: '', // 新增
    UPDATE: '', // 更新
    REMOVE: '', // 删除
    GET: '/get' // 查询对象
  }
}
