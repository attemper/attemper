package com.thor.common.constant;

/**
 * 全局API路径定义
 * @author ldang
 */
public final class APIPath {

    private static final String API_PATH = "api";

    public static final String SAVE = "";

    public static final String ADD = "";

    public static final String UPDATE = "";

    public static final String LIST = "";

    public static final String REMOVE = "";

    public static final String GET = "get";

    public static final class Sys{
        private static final String SYS = "/sys";

        public static final String LOGIN = API_PATH + SYS + "/login";

        public static final String TENANT = API_PATH + SYS + "/tenant";

        public static final String USER = API_PATH + SYS + "/user";

        public static final String TAG = API_PATH + SYS + "/tag";

        public static final String RESOURCE = API_PATH + SYS + "/resource";

        public static final class Resource{
            public static final String TREE_LIST = "treeList";
        }
    }

}
