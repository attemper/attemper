package com.github.attemper.web.service.dispatch;

import com.github.attemper.common.constant.CommonConstants;
import com.github.attemper.common.exception.RTException;
import com.github.attemper.common.param.dispatch.calendar.DayCalendarRemoveParam;
import com.github.attemper.common.param.dispatch.calendar.DayCalendarSaveParam;
import com.github.attemper.common.util.DateTimeUtil;
import com.github.attemper.config.base.util.BeanUtil;
import com.github.attemper.core.dao.dispatch.CalendarMapper;
import com.github.attemper.java.sdk.common.util.DateUtil;
import com.github.attemper.sys.service.BaseServiceAdapter;
import org.apache.commons.lang.StringUtils;
import org.camunda.commons.utils.IoUtil;
import org.quartz.Calendar;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.impl.calendar.HolidayCalendar;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;

@Service
@Transactional
public class CalendarOperatedService extends BaseServiceAdapter {

    @Autowired
    private CalendarMapper mapper;

    @Autowired
    private Scheduler scheduler;

    public Void saveDay(DayCalendarSaveParam param) {
        mapper.saveDay(BeanUtil.bean2Map(param));
        saveHolidayCalendar(param.getCalendarName(), false, param.getDayNum());
        return null;
    }

    public Void removeDay(DayCalendarRemoveParam param) {
        mapper.deleteDay(param);
        saveHolidayCalendar(param.getCalendarName(), true, param.getDayNum());
        return null;
    }

    public Void importDate(String calendarName, MultipartFile file) {
        try (InputStream is = file.getInputStream()) {
            InputStreamReader isr = new InputStreamReader(is, "GB18030");
            String lines = IoUtil.readerAsString(isr);
            String[] lineArray;
            if (lines.contains("\r\n")) {
                lineArray = lines.split("\r\n");
            } else if (lines.contains("\n")) {
                lineArray = lines.split("\n");
            } else {
                lineArray = new String[]{ lines };
            }
            if (lineArray.length == 0) {
                return null;
            }
            Integer[] days = new Integer[lineArray.length];
            List<Map<String, Object>> paramList = new ArrayList<>(lineArray.length);
            for (int i = 0; i < lineArray.length; i++) {
                String line = lineArray[i];
                if (StringUtils.isBlank(line)) {
                    throw new RTException(6706, i+1);
                }
                Map<String, Object> dayParamMap = new HashMap<>();
                dayParamMap.put(KEY_CALENDAR_NAME, calendarName);
                String dateStr;
                if (line.contains("|")) {
                    String[] columns = line.split("\\|");
                    if (columns.length > 2) {
                        throw new RTException(6711, line);
                    }
                    dateStr = columns[0].trim();
                    dayParamMap.put(KEY_REMARK, columns[1].trim());
                } else {
                    dateStr = line.trim();
                }
                if (dateStr.length() != 8 || DateUtil.parse(dateStr, CommonConstants.yyyyMMdd) == null) {
                    throw new RTException(6705, dateStr);
                }
                int day = Integer.parseInt(dateStr);
                days[i] = day;
                dayParamMap.put(KEY_DAY_NUM, day);
                paramList.add(dayParamMap);
            }
            // delete existed days
            Map<String, Object> paramMap = new HashMap<>(2);
            paramMap.put(KEY_CALENDAR_NAME, calendarName);
            paramMap.put(KEY_DAY_NUMS, days);
            mapper.deleteDays(paramMap);
            mapper.addDays(paramList);
            saveHolidayCalendar(calendarName, false, days);
        } catch (IOException e) {
            throw new RTException(6710, e);
        }
        return null;
    }

    private void saveHolidayCalendar(String calendarName, boolean toRemove, Integer... dayNums) {
        try {
            Calendar calendar = scheduler.getCalendar(calendarName);
            HolidayCalendar holidayCalendar;
            if (calendar == null) {
                if (toRemove) {
                    throw new RTException(3006, calendarName);
                }
                holidayCalendar = new HolidayCalendar();
            } else {
                holidayCalendar = (HolidayCalendar) calendar;
            }
            for (Integer dayNum : dayNums) {
                Date date = DateTimeUtil.parseDateStr(String.valueOf(dayNum), CommonConstants.yyyyMMdd);
                if (toRemove) {
                    holidayCalendar.removeExcludedDate(date);
                } else {
                    holidayCalendar.addExcludedDate(date);
                }
            }
            scheduler.addCalendar(calendarName, holidayCalendar, true, true);
        } catch (SchedulerException e) {
            throw new RTException(e.getMessage());
        }
    }

    private static final String KEY_CALENDAR_NAME = "calendarName";

    private static final String KEY_DAY_NUMS = "dayNums";

    private static final String KEY_DAY_NUM = "dayNum";

    private static final String KEY_REMARK = "remark";


}
