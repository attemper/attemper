package com.thor.config.constant;

/**
 * swagger2需要使用到的静态常量，用于标识Controller及其各方法的说明
 * @author ldang
 */
public final class APIConst {
    public static final class Tag{

        public static final String LOGIN = "登录";

        public static final String TENANT = "多租户";

        public static final String USER = "用户";

        public static final String TAG = "标签";

        public static final String RESOURCE = "资源";
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

            public static final String GET_INFO_BY_TOKEN = "根据token获取用户详细信息";
        }

        public static final class Tenant{

            private static final String _NAME = "租户";

            public static final String ADD = _ADD + _NAME;

            public static final String UPDATE = _UPDATE + _NAME;

            public static final String LIST = _NAME + _LIST;

            public static final String REMOVE = _REMOVE + _NAME;

            public static final String GET = _GET + _NAME;
        }

        public static final class User{

            private static final String _NAME = "用户";

            public static final String ADD = _ADD + _NAME;

            public static final String UPDATE = _UPDATE + _NAME;

            public static final String LIST = _NAME + _LIST;

            public static final String REMOVE = _REMOVE + _NAME;

            public static final String GET = _GET + _NAME;

        }

        public static final class Tag{

            private static final String _NAME = "标签";

            public static final String ADD = _ADD + _NAME;

            public static final String UPDATE = _UPDATE + _NAME;

            public static final String LIST = _NAME + _LIST;

            public static final String REMOVE = _REMOVE + _NAME;

            public static final String GET = _GET + _NAME;

            public static final String TAG_USER_GET = GET + "关联的"+ User._NAME;

            public static final String TAG_USER_UPDATE = UPDATE + "关联的"+ User._NAME;

            public static final String TAG_RESOURCE_GET = GET + "关联的"+ Resource._NAME;

            public static final String TAG_RESOURCE_UPDATE = UPDATE + "关联的"+ Resource._NAME;
        }

        public static final class Resource {
            private static final String _NAME = "资源";

            public static final String TREE_LIST = _GET + _NAME + "树";

            public static final String SAVE = _SAVE + _NAME;

            public static final String REMOVE = _REMOVE + _NAME;
        }
    }
}
