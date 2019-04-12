package com.sse.attemper.common.constant;

/**
 * @author ldang
 */
public class APIConst {
    public static final class APITag {

        public static final String LOGIN = "登录";

        public static final String TENANT = "租户";

        public static final String USER = "用户";

        public static final String TAG = "标签";

        public static final String RESOURCE = "资源";

        public static final String TOKEN = "令牌";

        public static final String JOB = "任务";

        public static final String TRIGGER = "触发器";

        public static final String TOOL = "工具";

        public static final String ARG = "参数";
    }

    public static final class APIOperation {

        public static final String _ADD = "新增";

        public static final String _UPDATE = "更新";

        public static final String _LIST = "列表";

        public static final String _REMOVE = "删除";

        public static final String _GET = "查询";

        public static final String _SAVE = "保存";

        public static final String _REFRESH = "刷新";

        public static final String _PUBLISH = "发布";

        public static final class LoginTitle {
            public static final String LOGIN = "以用户名和密码登录";
        }

        public static final class TenantTitle {

            private static final String _NAME = APITag.TENANT;

            public static final String ADD = _ADD + _NAME;

            public static final String UPDATE = _UPDATE + _NAME;

            public static final String LIST = _NAME + _LIST;

            public static final String REMOVE = _REMOVE + _NAME;

            public static final String GET = _GET + _NAME;

            private static final String INSTANCE_NAME = "应用系统地址";

            public static final String SAVE_INSTANCE = _SAVE + INSTANCE_NAME;

            public static final String LIST_INSTANCES = INSTANCE_NAME + _LIST;

            public static final String REMOVE_INSTANCE = _REMOVE + INSTANCE_NAME;
        }

        public static final class UserTitle {

            private static final String _NAME = APITag.USER;

            public static final String ADD = _ADD + _NAME;

            public static final String UPDATE = _UPDATE + _NAME;

            public static final String LIST = _NAME + _LIST;

            public static final String REMOVE = _REMOVE + _NAME;

            public static final String GET = _GET + _NAME;

            public static final String USER_TAG_GET = GET + "关联的"+ TagTitle._NAME;

            public static final String USER_TAG_UPDATE = UPDATE + "关联的"+ TagTitle._NAME;

            public static final String INFO = "根据token获取用户详细信息";

            public static final String ADMIN_INFO = "根据token获取用户及其管理的租户的信息";
        }

        public static final class TagTitle {

            private static final String _NAME = APITag.TAG;

            public static final String ADD = _ADD + _NAME;

            public static final String UPDATE = _UPDATE + _NAME;

            public static final String LIST = _NAME + _LIST;

            public static final String REMOVE = _REMOVE + _NAME;

            public static final String GET = _GET + _NAME;

            public static final String TAG_USER_GET = GET + "关联的"+ UserTitle._NAME;

            public static final String TAG_USER_UPDATE = UPDATE + "关联的"+ UserTitle._NAME;

            public static final String TAG_RESOURCE_GET = GET + "关联的"+ ResourceTitle._NAME;

            public static final String TAG_RESOURCE_UPDATE = UPDATE + "关联的"+ ResourceTitle._NAME;
        }

        public static final class ResourceTitle {
            private static final String _NAME = APITag.RESOURCE;

            public static final String TREE_LIST = _GET + _NAME + "树";

            public static final String SAVE = _SAVE + _NAME;

            public static final String REMOVE = _REMOVE + _NAME;
        }

        public static final class TokenTitle {
            private static final String _NAME = APITag.TOKEN;

            public static final String REFRESH = "根据header中的租户" + _REFRESH + _NAME;
        }

        public static final class JobTitle {
            private static final String _NAME = APITag.JOB;

            public static final class BaseTitle {
                public static final String LIST = _NAME + _LIST;

                public static final String REMOVE = _REMOVE + _NAME;

                public static final String ADD = _ADD + _NAME;

                public static final String UPDATE = _UPDATE + _NAME;

                public static final String GET = _GET + _NAME;

                public static final String PUBLISH = _PUBLISH + _NAME;

                public static final String VERSIONS = _NAME + "版本列表";

                public static final String COPY = "复制" + _NAME;

                public static final String EXCHANGE = "将当前模型切换为最新的版本";
            }

            public static final class TriggerTitle {

                public static final String UPDATE = _UPDATE + _NAME + APITag.TRIGGER;

                public static final String GET = _GET + _NAME + APITag.TRIGGER;
            }

        }

        public static final class ArgTitle {
            private static final String _NAME = APITag.ARG;

            public static final String LIST = _NAME + _LIST;

            public static final String REMOVE = _REMOVE + _NAME;

            public static final String ADD = _ADD + _NAME;

            public static final String UPDATE = _UPDATE + _NAME;

            public static final String GET = _GET + _NAME;
        }

        public static final class ToolTitle {
            public static final String GET_TIME_ZONE = _GET + "时区";

            public static final String PING = "Ping";
        }
    }
}
