package com.github.attemper.common.constant;

/**
 * 常用字符串/整型/长整型等默认数据类型的常量
 */
public interface CommonConstants {

    int OK = 200;

    int INTERNAL_SERVER_ERROR = 500;

    String page = "page";

    String list = "list";

    String userName = "userName";

    String tenantId = "tenantId";

    String header = "header";

    String token = "token";

    String duration = "duration";

    String result = "result";

    String jobName = "jobName";

    String projectName = "projectName";

    int DEF_CURRENT_PAGE = 1;

    int DEF_PAGE_SIZE = 10;

    int MAX_PAGE_SIZE = 1000;

    String REGEX_EMAIL = "[\\w!#$%&'*+/=?^_`{|}~-]+(?:\\.[\\w!#$%&'*+/=?^_`{|}~-]+)*@(?:[\\w](?:[\\w-]*[\\w])?\\.)+[\\w](?:[\\w-]*[\\w])?";

    String yyyyMMdd = "yyyyMMdd";

    String REGEX_TRADE_DATE = "^([TWMSHY]{1})((([+-])([0123456789]*))?)(( ((([+-])([0123456789]*))?))?)$";
}
