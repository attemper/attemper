package com.sse.attemper.common.constant;

/**
 * @author ldang
 */
public class APIPath {

    public static final String API_PATH = "/api";

    private static final String SYS = "/sys";

    private static final String DISPATCH = "/dispatch";

    public static final String SCHEDULER = "/scheduler";

    public static final String _SAVE = "";

    public static final String _ADD = "";

    public static final String _UPDATE = "";

    public static final String _LIST = "";

    public static final String _REMOVE = "";

    public static final String _GET = "/get";

    public static final String _PUBLISH = "/publish";
    
    public static final class LoginPath {
        public static final String LOGIN = "/login";

        public static final String LOGIN_BY_USERNAME_PWD = API_PATH + SYS + LOGIN;
    }

    public static final class TenantPath {
        public static final String TENANT = "/tenant";

        public static final String LIST = API_PATH + SYS + TENANT + _LIST;

        public static final String ADD = API_PATH + SYS + TENANT + _ADD;

        public static final String UPDATE = API_PATH + SYS + TENANT + _UPDATE;

        public static final String REMOVE = API_PATH + SYS + TENANT + _REMOVE;

        public static final String GET = API_PATH + SYS + TENANT + _GET;

    }

    public static final class UserPath {
        public static final String USER = "/user";

        public static final String LIST = API_PATH + SYS + USER + _LIST;

        public static final String ADD = API_PATH + SYS + USER + _ADD;

        public static final String UPDATE = API_PATH + SYS + USER + _UPDATE;

        public static final String REMOVE = API_PATH + SYS + USER + _REMOVE;

        public static final String GET = API_PATH + SYS + USER + _GET;

        public static final String TAG_LIST = API_PATH + SYS + USER + TagPath.TAG + _LIST;

        public static final String TAG_UPDATE = API_PATH + SYS + USER + TagPath.TAG + _UPDATE;

        public static final String INFO = API_PATH + SYS + USER + "/info";

        //public static final String ADMIN_INFO = API_PATH + SYS + USER + "/adminInfo";
    }

    public static final class TagPath {
        public static final String TAG = "/tag";

        public static final String LIST = API_PATH + SYS + TAG + _LIST;

        public static final String ADD = API_PATH + SYS + TAG + _ADD;

        public static final String UPDATE = API_PATH + SYS + TAG + _UPDATE;

        public static final String REMOVE = API_PATH + SYS + TAG + _REMOVE;

        public static final String GET = API_PATH + SYS + TAG + _GET;

        public static final String USER_LIST = API_PATH + SYS + TAG + UserPath.USER + _LIST;

        public static final String RESOURCE_LIST = API_PATH + SYS + TAG + ResourcePath.RESOURCE + _LIST;

        public static final String USER_UPDATE = API_PATH + SYS + TAG + UserPath.USER + _UPDATE;

        public static final String RESOURCE_UPDATE = API_PATH + SYS + TAG + ResourcePath.RESOURCE + _UPDATE;
    }

    public static final class ResourcePath {
        public static final String RESOURCE = "/resource";

        /*public static final String LIST = API_PATH + SYS + RESOURCE + _LIST;

        public static final String ADD = API_PATH + SYS + RESOURCE + _ADD;*/

        public static final String UPDATE = API_PATH + SYS + RESOURCE + _UPDATE;

        public static final String SAVE = API_PATH + SYS + RESOURCE + _SAVE;

        public static final String REMOVE = API_PATH + SYS + RESOURCE + _REMOVE;

        //public static final String GET = API_PATH + SYS + RESOURCE + _GET;

        public static final String TREE_LIST = API_PATH + SYS + RESOURCE + "/treeList";
    }

    public static final class TokenPath {
        public static final String TOKEN = "/token";

        public static final String REFRESH = API_PATH + SYS + TOKEN + "/refresh";
    }

    public static final class JobPath {

        public static final String JOB = "/job";

        public static final class BasePath {
            public static final String BASE = "/base";

            public static final String ADD = API_PATH + DISPATCH + JOB + BASE + _ADD;

            public static final String UPDATE = API_PATH + DISPATCH + JOB + BASE + _UPDATE;

            public static final String LIST = API_PATH + DISPATCH + JOB + BASE + _LIST;

            public static final String GET = API_PATH + DISPATCH + JOB + BASE + _GET;

            public static final String REMOVE = API_PATH + DISPATCH + JOB + BASE + _REMOVE;

            public static final String PUBLISH = API_PATH + DISPATCH + JOB + BASE + _PUBLISH;

            public static final String VERSIONS = API_PATH + DISPATCH + JOB + BASE + "/versions";

            public static final String COPY = API_PATH + DISPATCH + JOB + BASE + "/copy";

            public static final String EXCHANGE = API_PATH + DISPATCH + JOB + BASE + "/exchange";
        }

        public static final class TriggerPath {
            public static final String TRIGGER = "/trigger";

            public static final String UPDATE = API_PATH + DISPATCH + JOB + TRIGGER + _UPDATE;

            public static final String GET = API_PATH + DISPATCH + JOB + TRIGGER + _GET;
        }
    }

    public static final class ArgPath {
        public static final String ARG = "/arg";

        public static final String ADD = API_PATH + DISPATCH + ARG + _ADD;

        public static final String UPDATE = API_PATH + DISPATCH + ARG + _UPDATE;

        public static final String LIST = API_PATH + DISPATCH + ARG + _LIST;

        public static final String GET = API_PATH + DISPATCH + ARG + _GET;

        public static final String REMOVE = API_PATH + DISPATCH + ARG + _REMOVE;

    }

    public static final class ProjectPath {
        public static final String PROJECT = "/project";

        public static final String UPDATE = API_PATH + SYS + PROJECT + _UPDATE;

        public static final String SAVE = API_PATH + SYS + PROJECT + _SAVE;

        public static final String REMOVE = API_PATH + SYS + PROJECT + _REMOVE;

        public static final String TREE_LIST = API_PATH + SYS + PROJECT + "/treeList";

        private static final String INFO = "/info";

        public static final String LIST_INFOS = API_PATH + SYS + PROJECT + INFO + _LIST;

        public static final String SAVE_INFO = API_PATH + SYS + PROJECT + INFO + _ADD;

        public static final String REMOVE_INFO = API_PATH + SYS + PROJECT + INFO + _REMOVE;
    }

    public static final class ToolPath {
        public static final String TOOL = "/tool";

        public static final String GET_TIME_ZONE = API_PATH + SYS + TOOL + "/timeZone" + _LIST;

        public static final String PING = API_PATH + SYS + TOOL + "/ping";
    }

    public static final class SchedulerPath {

        public static final String TRIGGER = API_PATH + SCHEDULER + "/trigger";
    }
}
