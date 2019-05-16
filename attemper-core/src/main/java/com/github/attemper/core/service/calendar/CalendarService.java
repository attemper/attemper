package com.github.attemper.core.service.calendar;

import com.github.attemper.common.constant.CommonConstants;
import com.github.attemper.common.param.dispatch.calendar.CalendarGetParam;
import com.github.attemper.common.param.dispatch.calendar.DayCalendarConfigRemoveParam;
import com.github.attemper.common.param.dispatch.calendar.DayCalendarConfigSaveParam;
import com.github.attemper.common.result.dispatch.calendar.CalendarInfo;
import com.github.attemper.common.result.dispatch.calendar.DayCalendarConfig;
import com.github.attemper.config.base.util.BeanUtil;
import com.github.attemper.core.dao.mapper.calendar.CalendarMapper;
import com.github.attemper.sys.service.BaseServiceAdapter;
import com.github.attemper.sys.service.TenantService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class CalendarService extends BaseServiceAdapter {

    @Autowired
    private TenantService tenantService;

    @Autowired
    private CalendarMapper mapper;

    public List<CalendarInfo> list() {
        Map<String, Object> paramMap = injectTenantIdToMap(null);
        return mapper.list(paramMap);
    }

    public Void saveDay(DayCalendarConfigSaveParam param) {
        mapper.saveDay(objJoinTenantToMap(param));
        return null;
    }

    public Void removeDay(DayCalendarConfigRemoveParam param) {
        mapper.deleteDay(objJoinTenantToMap(param));
        return null;
    }

    public List<DayCalendarConfig> listDay(CalendarGetParam param) {
        return mapper.listDay(objJoinTenantToMap(param));
    }

    private Map<String, Object> objJoinTenantToMap(Object obj) {
        Map<String, Object> paramMap = BeanUtil.bean2Map(obj);
        if (StringUtils.equals(tenantService.getAdmin().getUserName(), injectTenantId())) {
            paramMap.put(CommonConstants.tenantId, "");
        } else {
            paramMap.put(CommonConstants.tenantId, injectTenantId());
        }
        return paramMap;
    }

}
