package com.github.attemper.common.constant;

/**
 * constants of string/integer...
 */
public interface CommonConstants {

    int OK = 200;

    String page = "page";

    String list = "list";

    String userName = "userName";

    String tenantId = "tenantId";

    String header = "header";

    String token = "token";

    String result = "result";

    String jobName = "jobName";

    String delayId = "delayId";

    String projectName = "projectName";

    String status = "status";

    String isRetry = "isRetry";

    int DEF_CURRENT_PAGE = 1;

    int DEF_PAGE_SIZE = 10;

    int MAX_PAGE_SIZE = 1000;

    String REGEX_EMAIL = "[\\w!#$%&'*+/=?^_`{|}~-]+(?:\\.[\\w!#$%&'*+/=?^_`{|}~-]+)*@(?:[\\w](?:[\\w-]*[\\w])?\\.)+[\\w](?:[\\w-]*[\\w])?";

    String yyyyMMdd = "yyyyMMdd";

    String REGEX_TRADE_DATE = "^([TWMSHY]{1})((([+-])([0123456789]*))?)(( ((([+-])([0123456789]*))?))?)$";
}
