package com.github.attemper.common.constant;

import com.github.attemper.java.sdk.common.constant.SdkAPIPath;

/**
 * @author ldang
 */
public class APIPath {

    public static final String API_PATH = SdkAPIPath.API_PATH;

    public static final String SYS = SdkAPIPath.SYS;

    private static final String DISPATCH = "/dispatch";

    private static final String STATISTICS = "/statistics";

    public static final String SCHEDULER = "/scheduler";

    public static final String EXECUTOR = "/executor";

    public static final String _SAVE = SdkAPIPath._SAVE;

    public static final String _ADD = SdkAPIPath._ADD;

    public static final String _UPDATE = SdkAPIPath._UPDATE;

    public static final String _LIST = SdkAPIPath._LIST;

    public static final String _REMOVE = SdkAPIPath._REMOVE;

    public static final String _GET = SdkAPIPath._GET;

    public static final String _PUBLISH = "/publish";
    
    public static final class LoginPath {

        public static final String INFO = API_PATH + SYS + SdkAPIPath.LoginPath.SUB_PATH + "/info";
    }

    public static final class TenantPath {
        public static final String SUB_PATH = "/tenant";

        public static final String LIST = API_PATH + SYS + SUB_PATH + _LIST;

        public static final String ADD = API_PATH + SYS + SUB_PATH + _ADD;

        public static final String UPDATE = API_PATH + SYS + SUB_PATH + _UPDATE;

        public static final String REMOVE = API_PATH + SYS + SUB_PATH + _REMOVE;

        public static final String GET = API_PATH + SYS + SUB_PATH + _GET;

        public static final String LIST_TAG = API_PATH + SYS + SUB_PATH + TagPath.SUB_PATH + _LIST;

        public static final String SAVE_TAGS = API_PATH + SYS + SUB_PATH + TagPath.SUB_PATH + _SAVE;

    }

    public static final class TagPath {
        public static final String SUB_PATH = "/tag";

        public static final String LIST = API_PATH + SYS + SUB_PATH + _LIST;

        public static final String ADD = API_PATH + SYS + SUB_PATH + _ADD;

        public static final String UPDATE = API_PATH + SYS + SUB_PATH + _UPDATE;

        public static final String REMOVE = API_PATH + SYS + SUB_PATH + _REMOVE;

        public static final String GET = API_PATH + SYS + SUB_PATH + _GET;

        public static final String RESOURCE = "/resource";

        public static final String LIST_TENANT = API_PATH + SYS + SUB_PATH + TenantPath.SUB_PATH + _LIST;

        public static final String LIST_RESOURCE = API_PATH + SYS + SUB_PATH + RESOURCE + _LIST;

        public static final String SAVE_TENANTS = API_PATH + SYS + SUB_PATH + TenantPath.SUB_PATH + _SAVE;

        public static final String SAVE_RESOURCES = API_PATH + SYS + SUB_PATH + RESOURCE + _SAVE;
    }

    public static final class JobPath {

        public static final String SUB_PATH = "/job";

        public static final String ADD = API_PATH + DISPATCH + SUB_PATH + _ADD;

        public static final String UPDATE = API_PATH + DISPATCH + SUB_PATH + _UPDATE;

        public static final String LIST = API_PATH + DISPATCH + SUB_PATH + _LIST;

        public static final String GET = API_PATH + DISPATCH + SUB_PATH + _GET;

        public static final String REMOVE = API_PATH + DISPATCH + SUB_PATH + _REMOVE;

        public static final String PUBLISH = API_PATH + DISPATCH + SUB_PATH + _PUBLISH;

        public static final String VERSIONS = API_PATH + DISPATCH + SUB_PATH + "/versions";

        public static final String COPY = API_PATH + DISPATCH + SUB_PATH + "/copy";

        public static final String EXCHANGE = API_PATH + DISPATCH + SUB_PATH + "/exchange";

        public static final String MANUAL = API_PATH + DISPATCH + SUB_PATH + "/manual";

        public static final String GET_PROJECT = API_PATH + DISPATCH + SUB_PATH + "/project";

        public static final String SAVE_PROJECT = API_PATH + DISPATCH + SUB_PATH + "/project";

        public static final String LIST_ARG = API_PATH + DISPATCH + SUB_PATH + ArgPath.SUB_PATH + _LIST;

        public static final String ADD_ARG = API_PATH + DISPATCH + SUB_PATH + ArgPath.SUB_PATH + _ADD;

        public static final String REMOVE_ARG = API_PATH + DISPATCH + SUB_PATH + ArgPath.SUB_PATH + _REMOVE;
    }

    public static final class TriggerPath {
        public static final String SUB_PATH = "/trigger";

        public static final String UPDATE = API_PATH + DISPATCH + SUB_PATH + _UPDATE;

        public static final String GET = API_PATH + DISPATCH + SUB_PATH + _GET;

        public static final String GET_CALENDAR = API_PATH + DISPATCH + SUB_PATH + CalendarPath.SUB_PATH + _GET;

    }

    public static final class ArgPath {
        public static final String SUB_PATH = "/arg";

        public static final String ADD = API_PATH + DISPATCH + SUB_PATH + _ADD;

        public static final String UPDATE = API_PATH + DISPATCH + SUB_PATH + _UPDATE;

        public static final String LIST = API_PATH + DISPATCH + SUB_PATH + _LIST;

        public static final String GET = API_PATH + DISPATCH + SUB_PATH + _GET;

        public static final String REMOVE = API_PATH + DISPATCH + SUB_PATH + _REMOVE;

        public static final String TEST_SQL = API_PATH + DISPATCH + SUB_PATH + "/testSql";

        public static final String TEST_TRADE_DATE = API_PATH + DISPATCH + SUB_PATH + "/testTradeDate";
    }

    public static final class DataSourcePath {
        public static final String SUB_PATH = "/datasource";

        public static final String ADD = API_PATH + DISPATCH + SUB_PATH + _ADD;

        public static final String UPDATE = API_PATH + DISPATCH + SUB_PATH + _UPDATE;

        public static final String LIST = API_PATH + DISPATCH + SUB_PATH + _LIST;

        public static final String GET = API_PATH + DISPATCH + SUB_PATH + _GET;

        public static final String REMOVE = API_PATH + DISPATCH + SUB_PATH + _REMOVE;

        public static final String TEST_CONNECTION = API_PATH + DISPATCH + SUB_PATH + "/test";

    }

    public static final class CalendarPath {
        public static final String SUB_PATH = "/calendar";

        private static final String DAY = "/day";

        private static final String TIME = "/time";

        public static final String LIST = API_PATH + DISPATCH + SUB_PATH + _LIST;

        public static final String SAVE_DAY = API_PATH + DISPATCH + SUB_PATH + DAY + _SAVE;

        public static final String SAVE_TIME = API_PATH + DISPATCH + SUB_PATH + TIME + _SAVE;

        public static final String LIST_DAY = API_PATH + DISPATCH + SUB_PATH + DAY + _LIST;

        public static final String LIST_TIME = API_PATH + DISPATCH + SUB_PATH + TIME + _LIST;

        public static final String REMOVE_DAY = API_PATH + DISPATCH + SUB_PATH + DAY + _REMOVE;

        public static final String REMOVE_TIME = API_PATH + DISPATCH + SUB_PATH + TIME + _REMOVE;
    }

    public static final class ProjectPath {
        public static final String SUB_PATH = "/project";

        public static final String UPDATE = API_PATH + DISPATCH + SUB_PATH + _UPDATE;

        public static final String SAVE = API_PATH + DISPATCH + SUB_PATH + _SAVE;

        public static final String REMOVE = API_PATH + DISPATCH + SUB_PATH + _REMOVE;

        public static final String LIST_TREE = API_PATH + DISPATCH + SUB_PATH + "/treeList";

        private static final String INFO = "/info";

        public static final String LIST_INFO = API_PATH + DISPATCH + SUB_PATH + INFO + _LIST;

        public static final String SAVE_INFO = API_PATH + DISPATCH + SUB_PATH + INFO + _SAVE;

        public static final String REMOVE_INFO = API_PATH + DISPATCH + SUB_PATH + INFO + _REMOVE;

        private static final String EXECUTOR = "/executor";

        public static final String LIST_EXECUTOR = API_PATH + DISPATCH + SUB_PATH + EXECUTOR + _LIST;

        public static final String SAVE_EXECUTOR = API_PATH + DISPATCH + SUB_PATH + EXECUTOR + _SAVE;
    }

    public static final class InstancePath {
        private static final String SUB_PATH = "/instance";

        public static final String LIST = API_PATH + DISPATCH + SUB_PATH + _LIST;

        private static final String ACT = "/act";

        public static final String LIST_ACT = API_PATH + DISPATCH + SUB_PATH + _LIST + ACT;
    }

    public static final class CountPath {
        private static final String SUB_PATH = "/count";

        public static final String TENANT = API_PATH + STATISTICS + SUB_PATH + TenantPath.SUB_PATH;

        public static final String JOB = API_PATH + STATISTICS + SUB_PATH + JobPath.SUB_PATH;

        public static final String INSTANCE = API_PATH + STATISTICS + SUB_PATH + "/instance";
    }

    public static final class ToolPath {
        public static final String SUB_PATH = "/tool";

        public static final String GET_TIME_ZONE = API_PATH + DISPATCH + SUB_PATH + "/timeZone" + _LIST;

        public static final String LIST_EXECUTOR_SERVICE = API_PATH + DISPATCH + SUB_PATH + "/executor" + _LIST;

        public static final String PING = API_PATH + DISPATCH + SUB_PATH + "/ping";
    }

    public static final class SchedulerPath {

        public static final String TRIGGER = API_PATH + SCHEDULER + "/trigger";
    }

    public static final class ExecutorPath {

        public static final String JOB_INVOKING = API_PATH + EXECUTOR + "/invoke";
    }
}
