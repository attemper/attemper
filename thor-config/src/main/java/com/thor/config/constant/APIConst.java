package com.thor.config.constant;

/**
 * swagger2需要使用到的静态常量，用于标识Controller及其各方法的说明
 * @author ldang
 */
public final class APIConst {
    public static final class Tag{

        public static final String LOGIN = "登录";
    }

    public static final class Operation{

        private static final String _ADD = "新增";

        private static final String _UPDATE = "更新";

        private static final String _LIST = "列表";

        private static final String _REMOVE = "删除";

        private static final String _GET = "查询";

        private static final String _SAVE = "保存";

        public static final class Login{
            public static final String LOGIN = "以用户名和密码登录";
        }

    }
}
